package com.eomcs.oop.ex02.study;

public class Car {
  public static final int OIL = 1;
  public static final int GAS = 2;
  
  String name;
  int type;
  String color;
  int speed;
  int energy;
  int power;
  int cleanLevel;
  
  void speedUp() {
    if (this.speed >= 100) {
      return;
    }
    this.speed += 10;
  }
  
  int compare(Car c) {
    if (this.power > c.power) {
      return -1;
    } else if (this.power == c.power) {
      return 0;
    } else
      return 1;
  }
}
