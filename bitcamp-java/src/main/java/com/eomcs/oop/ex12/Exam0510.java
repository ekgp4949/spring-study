// 메서드 레퍼런스 - 스태틱 메서드 레퍼런스
package com.eomcs.oop.ex12;

class MyCalculator {
  public static int plus(int a, int b) {
    return a + b;
  }

  public static int minus(int a, int b) {
    return a - b;
  }

  public static int multiple(int a, int b) {
    return a * b;
  }

  public static int divide(int a, int b) {
    return a / b;
  }
}


public class Exam0510 {

  static interface Calculator {
    int compute(int a, int b);
  }

  public static void main(String[] args) {
    // 메서드 한개짜리 인터페이스의 구현체를 만들 때
    // 기존 스태틱 메서드를 람다 구현체로 사용할 수 있다.
    // => 단, 인터페이스에 선언된 메서드의 규격이 일치해야 한다
    // => 규격? 파라미터 타입 및 개수, 리턴 타입
    // => 문법
    // 클래스명::메서드명
    Calculator c1 = MyCalculator::plus;
    Calculator c2 = MyCalculator::minus;
    Calculator c3 = MyCalculator::multiple;
    Calculator c4 = MyCalculator::divide;

    System.out.println(c1.compute(200, 17));
    System.out.println(c2.compute(200, 17));
    System.out.println(c3.compute(200, 17));
    System.out.println(c4.compute(200, 17));
  }
}


