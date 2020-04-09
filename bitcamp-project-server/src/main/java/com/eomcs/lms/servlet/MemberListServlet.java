package com.eomcs.lms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import com.eomcs.lms.domain.Member;
import com.eomcs.lms.service.MemberService;

@WebServlet("/member/list")
public class MemberListServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
      ServletContext servletContext = request.getServletContext();
      ApplicationContext iocContainer =
          (ApplicationContext) servletContext.getAttribute("iocContainer");
      MemberService memberService = iocContainer.getBean(MemberService.class);
      request.getRequestDispatcher("/header").include(request, response);
      out.println("  <h1>회원</h1>");
      out.println("  <a href='add'>새 회원</a><br>");
      out.println("  <table border='1'>");
      out.println("  <tr>");
      out.println("    <th>번호</th>");
      out.println("    <th>이름</th>");
      out.println("    <th>이메일</th>");
      out.println("    <th>전화</th>");
      out.println("    <th>등록일</th>");
      out.println("  </tr>");

      List<Member> members = memberService.list();
      for (Member m : members) {
        out.printf("  <tr>"//
            + "<td>%d</td> "//
            + "<td><a href='detail?no=%d'>%s</a></td> "//
            + "<td>%s</td> "//
            + "<td>%s</td>"//
            + "<td>%s</td>"//
            + "</tr>\n", //
            m.getNo(), //
            m.getNo(), //
            m.getName(), //
            m.getEmail(), //
            m.getTel(), //
            m.getRegisteredDate() //
        );
      }
      out.println("</table>");

      out.println("<hr>");

      out.println("<form action='search'>");
      out.println("검색어: <input name='keyword' type='text'>");
      out.println("<button>검색</button>");
      request.getRequestDispatcher("/footer").include(request, response);
    } catch (Exception e) {
      request.setAttribute("error", e);
      request.setAttribute("url", "list");
      request.getRequestDispatcher("/error").forward(request, response);
    }
  }
}
