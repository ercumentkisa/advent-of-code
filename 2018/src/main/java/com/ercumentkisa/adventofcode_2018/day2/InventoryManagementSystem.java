package com.ercumentkisa.adventofcode_2018.day2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InventoryManagementSystem {

  public static void main(String[] args) throws FileNotFoundException {
    InventoryManagementSystem ims = new InventoryManagementSystem();
    List<String> boxIds = ims.loadFile();
    ims.partOne(boxIds);
    ims.partTwo(boxIds);
  }

  private void partOne(List<String> boxIds) {
    int twos = 0;
    int threes = 0;

    for (String boxId : boxIds) {
      Map<Character, Integer> characterMap = new HashMap<>();
      for (Character c : boxId.toCharArray()) {
        characterMap.merge(c, 1, (i1, i2) -> i1 + i2);
      }

      boolean hasTwo = false;
      boolean hasThree = false;

      for (Map.Entry<Character, Integer> charEntry : characterMap.entrySet()) {
        if (charEntry.getValue() == 2) {
          hasTwo = true;
        }
        if (charEntry.getValue() == 3) {
          hasThree = true;
        }
      }

      if (hasTwo) twos++;
      if (hasThree) threes++;
    }

    System.out.println("Resulting checksum: " + twos * threes);
  }

  private void partTwo(List<String> boxIds) {
    for (String idFirst : boxIds) {
      for (String idSecond : boxIds) {
        int diff = 0;
        for (int i = 0; i < idFirst.length(); i++) {
          if (idFirst.charAt(i) != idSecond.charAt(i)) {
            diff++;
          }
        }

        if (diff == 1) {
          StringBuilder answer = new StringBuilder();
          for (int i = 0; i < idFirst.length(); i++) {
            if (idFirst.charAt(i) == idSecond.charAt(i)) {
              answer.append(idFirst.charAt(i));
            }
          }

          System.out.println("Common letters: " + answer.toString());
          return;
        }
      }
    }
  }

  private List<String> loadFile() throws FileNotFoundException {
    List<String> lines = new ArrayList<>();
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day2-input.txt").getFile());
    try (Scanner scanner = new Scanner(inputFile)) {
      while (scanner.hasNext()) {
        lines.add(scanner.nextLine());
      }
    }
    return lines;
  }
}
