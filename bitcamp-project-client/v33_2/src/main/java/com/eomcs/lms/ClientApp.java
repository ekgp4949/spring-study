// LMS 클라이언트
package com.eomcs.lms;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import com.eomcs.lms.dao.BoardDao;
import com.eomcs.lms.dao.LessonDao;
import com.eomcs.lms.dao.MemberDao;
import com.eomcs.lms.dao.proxy.BoardDaoProxy;
import com.eomcs.lms.dao.proxy.LessonDaoProxy;
import com.eomcs.lms.dao.proxy.MemberDaoProxy;
import com.eomcs.lms.handler.BoardAddCommand;
import com.eomcs.lms.handler.BoardDeleteCommand;
import com.eomcs.lms.handler.BoardDetailCommand;
import com.eomcs.lms.handler.BoardListCommand;
import com.eomcs.lms.handler.BoardUpdateCommand;
import com.eomcs.lms.handler.Command;
import com.eomcs.lms.handler.LessonAddCommand;
import com.eomcs.lms.handler.LessonDeleteCommand;
import com.eomcs.lms.handler.LessonDetailCommand;
import com.eomcs.lms.handler.LessonListCommand;
import com.eomcs.lms.handler.LessonUpdateCommand;
import com.eomcs.lms.handler.MemberAddCommand;
import com.eomcs.lms.handler.MemberDeleteCommand;
import com.eomcs.lms.handler.MemberDetailCommand;
import com.eomcs.lms.handler.MemberListCommand;
import com.eomcs.lms.handler.MemberUpdateCommand;
import com.eomcs.lms.util.Prompt;

public class ClientApp {

  Scanner keyboard = new Scanner(System.in);
  Prompt prompt = new Prompt(keyboard);

  Deque<String> commandStack;
  Queue<String> commandQueue;

  BoardDao boardDao;
  LessonDao lessonDao;

  String host;
  int port;

  HashMap<String, Command> commandMap = new HashMap<>();

  public ClientApp() {
    commandStack = new ArrayDeque<>();
    commandQueue = new LinkedList<>();

    try {
      host = prompt.inputString("서버? ");
      port = prompt.inputInt("포트? ");
    } catch (Exception e) {
      System.out.println("서버주소 또는 포트 번호가 유효하지 않습니다.");
      keyboard.close();
      return;
    }

    // DaoProxy 객체 준비
    boardDao = new BoardDaoProxy(host, port);
    commandMap.put("/board/list", new BoardListCommand(boardDao));
    commandMap.put("/board/add", new BoardAddCommand(boardDao, prompt));
    commandMap.put("/board/detail", new BoardDetailCommand(boardDao, prompt));
    commandMap.put("/board/update", new BoardUpdateCommand(boardDao, prompt));
    commandMap.put("/board/delete", new BoardDeleteCommand(boardDao, prompt));

    lessonDao = new LessonDaoProxy(host, port);
    commandMap.put("/lesson/list", new LessonListCommand(lessonDao));
    commandMap.put("/lesson/add", new LessonAddCommand(lessonDao, prompt));
    commandMap.put("/lesson/detail", new LessonDetailCommand(lessonDao, prompt));
    commandMap.put("/lesson/update", new LessonUpdateCommand(lessonDao, prompt));
    commandMap.put("/lesson/delete", new LessonDeleteCommand(lessonDao, prompt));

    MemberDao memberDao = new MemberDaoProxy(host, port);
    commandMap.put("/member/list", new MemberListCommand(memberDao));
    commandMap.put("/member/add", new MemberAddCommand(memberDao, prompt));
    commandMap.put("/member/detail", new MemberDetailCommand(memberDao, prompt));
    commandMap.put("/member/update", new MemberUpdateCommand(memberDao, prompt));
    commandMap.put("/member/delete", new MemberDeleteCommand(memberDao, prompt));

    commandMap.put("/server/stop", () -> {
      try {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

          out.writeUTF("/server/stop");
          out.flush();
          System.out.println("서버:" + in.readUTF());
          System.out.println("안녕~~");
        }
      } catch (Exception e) {
        //
      }
    });

  }

  public void service() {

    while (true) {
      String command;
      command = prompt.inputString("\n명령> ");
      if (command.length() == 0) {
        continue;
      }

      if (command.equals("history")) {
        printCommandHistory(commandStack.iterator());
        continue;
      } else if (command.equals("history2")) {
        printCommandHistory(commandQueue.iterator());
        continue;
      } else if (command.equalsIgnoreCase("quit")) {
        break;
      }

      commandStack.push(command);
      commandQueue.offer(command);

      processCommand(command);
    }
    keyboard.close();
  }

  private void processCommand(String command) {
    Command commandHandler = commandMap.get(command);
    if (commandHandler == null) {
      System.out.println("실행할 수 없는 입력입니다.");
      return;
    }
    commandHandler.execute();
    System.out.println("서버와 연결을 끊었음");
  }

  private void printCommandHistory(Iterator<String> command) {
    int count = 0;
    while (command.hasNext()) {
      System.out.println(command.next());
      if (++count % 5 == 0 && command.hasNext()) {
        String str = prompt.inputString(":");
        if (str.equalsIgnoreCase("q")) {
          break;
        }
      }
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println("클라이언트 수업 관리 시스템 입니다.");

    ClientApp app = new ClientApp();
    app.service();
  }
}
