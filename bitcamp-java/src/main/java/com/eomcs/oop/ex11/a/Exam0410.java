// anonymous class - 사용 전 : 로컬 클래스와 비교
package com.eomcs.oop.ex11.a;

public class Exam0410 {
  // 인터페이스의 경우 static으로 선언하지 않아고 스태틱 멤버에서 사용할 수 있다.
  interface A {
    void print();
  }

  public static void main(final String[] args) {
    // 로컬 클래스로 인터페이스 구현하기
    class My implements A {
      @Override
      public void print() {
        System.out.println("Hello");
      }
    }
    final My a = new My();
    a.print();

    // 익명클래스로 인터페이스 구현하기
  }

}
