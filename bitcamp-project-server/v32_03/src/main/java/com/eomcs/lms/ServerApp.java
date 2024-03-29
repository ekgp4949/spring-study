/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.eomcs.lms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.eomcs.lms.context.ApplicationContextListener;
import com.eomcs.lms.domain.Board;
import com.eomcs.lms.domain.Lesson;
import com.eomcs.lms.domain.Member;

public class ServerApp {

  // 옵저버 관련 코드
  Set<ApplicationContextListener> listeners = new HashSet<>();
  Map<String, Object> context = new HashMap<>();

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

  @SuppressWarnings("unchecked")
  public void service() {
    notifyApplicationInitailized();

    List<Lesson> lessonList = (List<Lesson>) context.get("lessonList");
    List<Member> memberList = (List<Member>) context.get("memberList");
    List<Board> boardList = (List<Board>) context.get("boardList");

    notifyApplicationDestroyed();
  }

  public static void main(String[] args) {
    System.out.println("서버 수업 관리 시스템 입니다.");

    ServerApp app = new ServerApp();
    app.addApplicationContextListener(new DataLoaderListener());
    app.service();

    /*
     * try ( // 서버쪽 연결 준비 // 클라이언트의 연결을 9999번 포트에서 기다린다. ServerSocket serverSocket = new
     * ServerSocket(9999)) { System.out.println("클라이언트 연결 대기중..."); while (true) { // 서버에 대기하고 있는
     * 클라이언트와 연결 // => 대기하고 있는 클라이언트와 연결될 때까지 리턴하지 않는다. Socket socket = serverSocket.accept();
     * System.out.println("클라이언트와 연결되었음"); // 클라이언트의 요청 처리 processRequest(socket);
     * System.out.println("------------------------------------------------"); } } catch (Exception
     * e) { System.out.println("서버 준비 중 오류 발생"); return; } }
     *
     * static void processRequest(Socket clientSocket) { try (Socket socket = clientSocket; //
     * 클라이언트의 메시지를 수신하고 클라이언트로 전송할 입출력도구 준비 Scanner in = new Scanner(socket.getInputStream());
     * PrintStream out = new PrintStream(socket.getOutputStream())) {
     * System.out.println("클라이언트와 연결되었음!");
     *
     * // 클라이언트가 보낸 메시지를 수신한다 // => 한 줄의 메시지를 읽을 때까지 리턴하지 않는다 String message = in.nextLine();
     * System.out.println("클라이언트가 보낸 메시지를 수신하였음!: " + message);
     *
     * // 클라이언트에게 메시지를 전송한다. // => 클라이언트가 메시지를 모두 읽을 때까지 리턴하지 않는다. out.println("들어올땐 마음대로지만...");
     * System.out.println("클라이언트에 메시지를 전송하였음!");
     *
     * } catch (Exception e) { System.out.println("예외발생: "); e.printStackTrace(); }
     */
  }
}
