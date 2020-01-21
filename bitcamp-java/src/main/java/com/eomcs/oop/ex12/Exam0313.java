// 람다(lambda)
package com.eomcs.oop.ex12;

public class Exam0313 {

  static interface Player {
    void play();
  }

  static void testPlayer(Player p) {
    p.play();
  }

  public static void main(String[] args) {
    // 람다
    testPlayer(() -> System.out.println("람다연습"));
  }
}


