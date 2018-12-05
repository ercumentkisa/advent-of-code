package com.ercumentkisa.adventofcode_2018.day5;

import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;
import static java.lang.String.valueOf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AlchemicalReduction {

  public static void main(String[] args) throws FileNotFoundException {
    AlchemicalReduction alchemicalReduction = new AlchemicalReduction();
    String polymer = alchemicalReduction.loadFile();

    alchemicalReduction.partOne(polymer);
    alchemicalReduction.partTwo(polymer);
  }

  private void partOne(String polymer) {
    int outputLength = reactPolymer(polymer);
    System.out.println("Remaining units: " + outputLength);
  }

  private void partTwo(String polymer) {
    Set<Character> unitSet = asSet(polymer.toLowerCase());
    int shortestPolymerLength = Integer.MAX_VALUE;

    for (char unit : unitSet) {
      String filteredPolymer =
          polymer
              .replaceAll(valueOf(toLowerCase(unit)), "")
              .replaceAll(valueOf(toUpperCase(unit)), "");

      int reactedLength = reactPolymer(filteredPolymer);
      shortestPolymerLength = Math.min(shortestPolymerLength, reactedLength);
    }

    System.out.println("Shortest polymer length: " + shortestPolymerLength);
  }

  private int reactPolymer(String polymer) {
    Deque<Character> stack = new ArrayDeque<>();

    for (int index = 0; index < polymer.length(); index++) {
      char unit = polymer.charAt(index);
      if (stack.isEmpty()) {
        stack.push(unit);
      } else {
        char lastUnit = stack.peek();
        boolean isSameLetter = toLowerCase(unit) == toLowerCase(lastUnit);

        if (unit != lastUnit && isSameLetter) {
          stack.remove();
        } else {
          stack.push(unit);
        }
      }
    }

    return stack.size();
  }

  private Set<Character> asSet(String input) {
    Set<Character> set = new HashSet<>();
    for (char c : input.toCharArray()) {
      set.add(c);
    }
    return set;
  }

  private String loadFile() throws FileNotFoundException {
    String polymer;
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day5-input.txt").getFile());
    try (Scanner scanner = new Scanner(inputFile)) {
      polymer = scanner.nextLine();
    }
    return polymer;
  }
}
