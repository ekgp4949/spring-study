// 람다(lambda) - 리턴값이 있는 메서드
package com.eomcs.oop.ex12;

public class Exam0140 {

  static interface Calculator {
    int compute(int a, int b);
  }

  public static void main(String[] args) {
    Calculator c1 = (int a, int b) -> {
      return a + b;
    };
    System.out.println(c1.compute(200, 100));


    Calculator c2 = (a, b) -> {
      return a + b;
    };
    System.out.println(c2.compute(200, 100));

    // 리턴 값이 있는 경우 중괄호를 생략할 때 return 키워드도 함께 생략해야 한다.
    Calculator c3 = (a, b) -> a + b;
    System.out.println(c3.compute(200, 100));

    // 리턴 값이 있는 경우 반드시 표현식(expression)이 와야 한다.
    // 표현식? 실행한 후 결과가 리턴되는 명령
    // Calculator c4 = (a, b) -> System.out.println("assa");

  }


}


