package com.ercumentkisa.adventofcode_2018.day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChronalCalibration {

  public static void main(String[] args) throws FileNotFoundException {
    ChronalCalibration chronalCalibration = new ChronalCalibration();
    chronalCalibration.partOne();
    chronalCalibration.partTwo();
  }

  private void partOne() throws FileNotFoundException {
    List<Integer> frequencies = loadFile();
    int result = 0;
    for (Integer frequency : frequencies) {
      result += frequency;
    }
    System.out.println("Result: " + result);
  }

  private void partTwo() throws FileNotFoundException {
    boolean isFound = false;
    int result = 0;
    List<Integer> resultSet = new ArrayList<>();
    resultSet.add(result);

    while (!isFound) {
      List<Integer> frequencies = loadFile();

      for (Integer frequency : frequencies) {
        result += frequency;
        if (resultSet.contains(result)) {
          System.out.println("First duplicate frequency is on: " + result);
          isFound = true;
          break;
        }
        resultSet.add(result);
      }
    }
  }

  private List<Integer> loadFile() throws FileNotFoundException {
    List<Integer> frequencies = new ArrayList<>();

    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day1-input.txt").getFile());

    try (Scanner scanner = new Scanner(inputFile)) {
      while (scanner.hasNext()) {
        String freq = scanner.nextLine();
        int frequency = Integer.parseInt(freq.substring(1));
        if (freq.charAt(0) == '-') {
          frequency = -1 * frequency;
        }
        frequencies.add(frequency);
      }
    }

    return frequencies;
  }
}
