package com.eomcs.lms.servlet;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import com.eomcs.lms.domain.Member;
import com.eomcs.lms.service.MemberService;
import com.eomcs.util.Component;
import com.eomcs.util.Prompt;

@Component("/auth/login")
public class LoginServlet implements Servlet {

  MemberService memberService;

  public LoginServlet(MemberService memberService) {
    this.memberService = memberService;
  }

  @Override
  public void service(Scanner in, PrintStream out) throws Exception {
    String email = Prompt.getString(in, out, "이메일? ");
    String password = Prompt.getString(in, out, "암호? ");

    HashMap<String, Object> params = new HashMap<>();
    params.put("email", email);
    params.put("password", password);

    Member member = memberService.get(params);

    if (member != null) {
      out.printf("'%s'님 환영합니다.\n", member.getName());
    } else {
      out.println("사용자 정보가 유효하지 않습니다.");
    }
  }
}
