package com.ercumentkisa.adventofcode_2018.day3;

class Claim {

  private final int id;
  private final int leftDistance;
  private final int topDistance;
  private final int width;
  private final int height;

  Claim(int id, int leftDistance, int topDistance, int width, int height) {
    this.id = id;
    this.leftDistance = leftDistance;
    this.topDistance = topDistance;
    this.width = width;
    this.height = height;
  }

  int getId() {
    return id;
  }

  int getLeftDistance() {
    return leftDistance;
  }

  int getTopDistance() {
    return topDistance;
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }
}
