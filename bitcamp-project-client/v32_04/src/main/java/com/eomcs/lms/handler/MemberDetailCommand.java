package com.eomcs.lms.handler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.eomcs.lms.domain.Member;
import com.eomcs.lms.util.Prompt;

public class MemberDetailCommand implements Command {
  ObjectOutputStream out;
  ObjectInputStream in;
  Prompt prompt;

  public MemberDetailCommand(ObjectOutputStream out, ObjectInputStream in, Prompt prompt) {
    this.out = out;
    this.in = in;
    this.prompt = prompt;
  }

  @Override
  public void execute() {
    try {
      out.writeUTF("/member/detail");
      out.writeInt(prompt.inputInt("번호? "));
      out.flush();

      if (in.readUTF().equals("FAIL")) {
        System.out.println(in.readUTF());
      }

      Member member = (Member) in.readObject();
      System.out.printf("번호: %d\n", member.getNo());
      System.out.printf("이름: %s\n", member.getName());
      System.out.printf("이메일: %s\n", member.getEmail());
      System.out.printf("암호: %s\n", member.getPassword());
      System.out.printf("사진: %s\n", member.getPhoto());
      System.out.printf("전화: %s\n", member.getTel());
      System.out.printf("등록일: %s\n", member.getRegisteredDate());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
