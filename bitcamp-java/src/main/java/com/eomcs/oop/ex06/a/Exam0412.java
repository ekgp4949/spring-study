// 다형성 - 다형적 변수의 활용
package com.eomcs.oop.ex06.a;

public class Exam0412 {

  public static void printSedan(Sedan sedan) {
    System.out.printf("모델명: %s\n", sedan.model);
    System.out.printf("cc: %d\n", sedan.cc);
    System.out.println("-------------------------");
  }
  public static void printTruck(Truck truck) {
    System.out.printf("모델명: %s\n", truck.model);
    System.out.printf("cc: %d\n", truck.cc);
    System.out.println("-------------------------");
  }

  public static void main(String[] args) {
    Sedan car1 = new Sedan();
    car1.model = "티코";
    car1.cc = 800;


    Truck car2 = new Truck();
    car2.model = "타이탄II";
    car2.cc = 10000;

    // Sedan 객체에서 model과 cc를 뽑아 출력할 때는 해당 메서드를 호출하고
    printSedan(car1);

    // Truck은 다음과 같이 그에 대한 메서드를 호출한다.
    printTruck(car2);

    // Sedan의 모델명과 cc를 출력하는 메서드와 
    // Truck의 모델명과 cc를 출력하는 메서드를
    // 모두 만들어야 하는 번거로움이 있다.
    // 해결책?
    // => 두개의 클래스가 같은 조상을 가질 때는 다형적 변수를 사용하라
  }

}




