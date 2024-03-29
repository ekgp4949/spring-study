// 메서드 레퍼런스 - 생성자 레퍼런스
package com.eomcs.oop.ex12;

public class Exam0740 {

  static class Message {
    String name;

    public Message() {
      this.name = "이름없음";
    }

    public Message(String name) {
      this.name = name;
    }

    public void print() {
      System.out.printf("%s님 반갑습니다\n", name);
    }

  }

  static interface Factory1 {
    Message get();
  }

  static interface Factory2 {
    Message get(String name);
  }

  static interface Factory3 {
    Message get(String name, int age);
  }

  public static void main(String[] args) {
    Factory1 f1 = Message::new;
    Factory2 f2 = Message::new;
    Factory3 f3 = Message::new; // 컴파일 오류!/

    Message msg = f1.get();
    msg.print();

    Message msg2 = f2.get("응가");
    msg2.print();
  }
}


