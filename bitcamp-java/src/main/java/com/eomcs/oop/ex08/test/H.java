package com.eomcs.oop.ex08.test;

import com.eomcs.oop.ex08.test.sub.H2;

public class H extends Object{
  private int a;
  int b;
  protected int c;
  public int d;
}

class H3 extends H2{
  void m1() {
    H2 obj2 = new H2();
    //    obj2.a = 100;
    //    obj2.b = 100; // import 하던말던 같은 패키지가 아니므로 못씀
    //    obj2.c = 100;
    // protected 멤버는 서브클래스의 멤버인 경우만 접근할 수 있다.
    obj2.d = 100; // H2인스턴스이기 때문에 H3에게 사용권이 없다

    H3 obj3 = new H3();

//    obj3.a = 100;
//    obj3.b = 100;
    obj3.c = 100; // H3인스턴스이기 때문의 H2의 c 사용권이 있다.
    obj3.d = 100;
  }
}

class TestH {
  public static void main(String[] args) {
    H obj = new H();
    //    obj.a = 100;
    obj.b = 100;
    obj.c = 100;
    obj.d = 100;

    H2 obj2 = new H2();
    //    obj2.a = 100;
    //    obj2.b = 100;
    //    obj2.c = 100;
    obj2.d = 100;


  }
}