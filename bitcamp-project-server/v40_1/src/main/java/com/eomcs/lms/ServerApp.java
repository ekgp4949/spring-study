package com.eomcs.lms;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.eomcs.lms.context.ApplicationContextListener;
import com.eomcs.lms.dao.BoardDao;
import com.eomcs.lms.dao.LessonDao;
import com.eomcs.lms.dao.MemberDao;
import com.eomcs.lms.dao.PhotoBoardDao;
import com.eomcs.lms.dao.PhotoFileDao;
import com.eomcs.lms.servlet.BoardAddServlet;
import com.eomcs.lms.servlet.BoardDeleteServlet;
import com.eomcs.lms.servlet.BoardDetailServlet;
import com.eomcs.lms.servlet.BoardListServlet;
import com.eomcs.lms.servlet.BoardUpdateServlet;
import com.eomcs.lms.servlet.LessonAddServlet;
import com.eomcs.lms.servlet.LessonDeleteServlet;
import com.eomcs.lms.servlet.LessonDetailServlet;
import com.eomcs.lms.servlet.LessonListServlet;
import com.eomcs.lms.servlet.LessonUpdateServlet;
import com.eomcs.lms.servlet.MemberAddServlet;
import com.eomcs.lms.servlet.MemberDeleteServlet;
import com.eomcs.lms.servlet.MemberDetailServlet;
import com.eomcs.lms.servlet.MemberListServlet;
import com.eomcs.lms.servlet.MemberSearchServlet;
import com.eomcs.lms.servlet.MemberUpdateServlet;
import com.eomcs.lms.servlet.PhotoBoardAddServlet;
import com.eomcs.lms.servlet.PhotoBoardDeleteServlet;
import com.eomcs.lms.servlet.PhotoBoardDetailServlet;
import com.eomcs.lms.servlet.PhotoBoardListServlet;
import com.eomcs.lms.servlet.PhotoBoardUpdateServlet;
import com.eomcs.lms.servlet.Servlet;
import com.eomcs.util.ConnectionFactory;

public class ServerApp {

  Set<ApplicationContextListener> listeners = new HashSet<>();
  Map<String, Object> context = new HashMap<>();

  Map<String, Servlet> servletMap = new HashMap<>();

  ExecutorService executorService = Executors.newCachedThreadPool();

  // 서버 멈충 여부 설정 변수
  boolean serverStop = false;

  public void addApplicationContextListener(ApplicationContextListener listener) {
    listeners.add(listener);
  }

  public void removeApplicationContextListener(ApplicationContextListener listener) {
    listeners.remove(listener);
  }

  private void notifyApplicationInitailized() {
    for (ApplicationContextListener listener : listeners) {
      listener.contextInitialized(context);
    }
  }

  private void notifyApplicationDestroyed() {
    for (ApplicationContextListener listener : listeners) {
      listener.contextDestroyed(context);
    }
  }

  public void service() {

    notifyApplicationInitailized();

    BoardDao boardDao = (BoardDao) context.get("boardDao");
    LessonDao lessonDao = (LessonDao) context.get("lessonDao");
    MemberDao memberDao = (MemberDao) context.get("memberDao");
    PhotoBoardDao photoBoardDao = (PhotoBoardDao) context.get("photoBoardDao");
    PhotoFileDao photoFileDao = (PhotoFileDao) context.get("photoFileDao");
    ConnectionFactory conFactory = (ConnectionFactory) context.get("connectionFactory");

    servletMap.put("/board/list", new BoardListServlet(boardDao));
    servletMap.put("/board/add", new BoardAddServlet(boardDao));
    servletMap.put("/board/detail", new BoardDetailServlet(boardDao));
    servletMap.put("/board/update", new BoardUpdateServlet(boardDao));
    servletMap.put("/board/delete", new BoardDeleteServlet(boardDao));
    //
    servletMap.put("/lesson/list", new LessonListServlet(lessonDao));
    servletMap.put("/lesson/add", new LessonAddServlet(lessonDao));
    servletMap.put("/lesson/detail", new LessonDetailServlet(lessonDao));
    servletMap.put("/lesson/update", new LessonUpdateServlet(lessonDao));
    servletMap.put("/lesson/delete", new LessonDeleteServlet(lessonDao));
    //
    servletMap.put("/member/list", new MemberListServlet(memberDao));
    servletMap.put("/member/add", new MemberAddServlet(memberDao));
    servletMap.put("/member/detail", new MemberDetailServlet(memberDao));
    servletMap.put("/member/update", new MemberUpdateServlet(memberDao));
    servletMap.put("/member/delete", new MemberDeleteServlet(memberDao));
    servletMap.put("/member/search", new MemberSearchServlet(memberDao));

    servletMap.put("/photoboard/list", new PhotoBoardListServlet(photoBoardDao, lessonDao));
    servletMap.put("/photoboard/detail", new PhotoBoardDetailServlet(photoBoardDao, photoFileDao));
    servletMap.put("/photoboard/add",
        new PhotoBoardAddServlet(photoBoardDao, lessonDao, photoFileDao));
    servletMap.put("/photoboard/update", new PhotoBoardUpdateServlet(photoBoardDao, photoFileDao));
    servletMap.put("/photoboard/delete", new PhotoBoardDeleteServlet(photoBoardDao, photoFileDao));

    try (ServerSocket serverSocket = new ServerSocket(9999)) {
      System.out.println("클라이언트 연결 대기중...");
      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("클라이언트와 연결되었음");

        executorService.submit(() -> {
          processRequest(socket);
          conFactory.removeConnection();
          System.out.println("------------------------------------------------");
        });

        if (serverStop == true) {
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("서버 준비 중 오류 발생");
    }
    executorService.shutdown();

    // 모든 스레드가 끝날 때 까지 DB 커넥션을 종료하고 싶지 않다면,
    // 스레드가 끝났는지 검사하며 기다려야 한다.
    while (true) {
      if (executorService.isTerminated()) {
        break;
      }
      try {
        // 0.5초마다 잠들었다가 다시 일어나서 while문 돌림
        Thread.sleep(500);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // 스레드가 모두 종료된 후에 con.close()호출
    notifyApplicationDestroyed();

    System.out.println("서버 종료");
  }

  void processRequest(Socket clientSocket) {
    try (Socket socket = clientSocket;
        Scanner in = new Scanner(socket.getInputStream());
        PrintStream out = new PrintStream(socket.getOutputStream())) {

      System.out.println("통신을 위한 입출력 스트림을 준비하였음!");

      String request = in.nextLine();
      System.out.println("클라이언트가 보낸 메시지를 수신하였음!");
      System.out.printf("=> %s\n", request);

      if (request.equalsIgnoreCase("/server/stop")) {
        quit(out);
        return;
      }

      Servlet servlet = servletMap.get(request);
      if (servlet != null) {
        try {
          servlet.service(in, out);
        } catch (Exception e) {
          out.println("요청 처리 중 오류 발생");
          out.println(e.getMessage());
          System.out.println("클라이언트 요청 처리 중 오류발생");
          e.printStackTrace();
        }
      } else {
        notFount(out);
      }

      out.println("!end!");
      out.flush();
      System.out.println("클라이언트에게 응답하였음");
    } catch (Exception e) {
      System.out.println("예외발생: ");
      e.printStackTrace();
    }
  }

  private void notFount(PrintStream out) throws IOException {
    out.println("요청한 명령을 처리할 수 없습니다");
  }

  private void quit(PrintStream out) throws IOException {
    serverStop = true;
    out.println("OK");
    out.println("!end!");
    out.flush();
  }

  public static void main(String[] args) {
    System.out.println("서버 수업 관리 시스템 입니다.");

    ServerApp app = new ServerApp();
    app.addApplicationContextListener(new DataLoaderListener());
    app.service();
  }
}
