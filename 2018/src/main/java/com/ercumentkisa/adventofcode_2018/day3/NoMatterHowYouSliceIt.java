package com.ercumentkisa.adventofcode_2018.day3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NoMatterHowYouSliceIt {

  private int rowCount = 0;
  private int columnCount = 0;
  private int[][] matrix = null;

  public static void main(String[] args) throws FileNotFoundException {
    NoMatterHowYouSliceIt sliceIt = new NoMatterHowYouSliceIt();
    List<Claim> claims = sliceIt.loadFile();
    sliceIt.calculateClashes(claims);

    sliceIt.partOne();
    sliceIt.partTwo(claims);
  }

  private void calculateClashes(List<Claim> claims) {
    matrix = new int[rowCount + 1][columnCount + 1];

    for (Claim claim : claims) {
      int rowIndex = claim.getTopDistance();
      int colIndex = claim.getLeftDistance();
      for (int i = 0; i < claim.getWidth() * claim.getHeight(); i++) {
        matrix[rowIndex][colIndex] = matrix[rowIndex][colIndex] + 1;
        colIndex++;
        if (colIndex >= claim.getLeftDistance() + claim.getWidth()) {
          colIndex = claim.getLeftDistance();
          rowIndex++;
        }
      }
    }
  }

  private void partOne() {
    int count = 0;
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < columnCount; j++) {
        if (matrix[i][j] > 1) {
          count++;
        }
      }
    }

    System.out.println("Result: " + count);
  }

  private void partTwo(List<Claim> claims) {
    for (Claim claim : claims) {
      int rowIndex = claim.getTopDistance();
      int colIndex = claim.getLeftDistance();
      boolean isIntact = true;
      for (int i = 0; i < claim.getWidth() * claim.getHeight(); i++) {
        if (matrix[rowIndex][colIndex] != 1) {
          isIntact = false;
          break;
        }
        colIndex++;
        if (colIndex >= claim.getLeftDistance() + claim.getWidth()) {
          colIndex = claim.getLeftDistance();
          rowIndex++;
        }
      }

      if (isIntact) {
        System.out.println("Intact item with no clashes: " + claim.getId());
        break;
      }
    }
  }

  private List<Claim> loadFile() throws FileNotFoundException {
    List<Claim> lines = new ArrayList<>();
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day3-input.txt").getFile());

    try (Scanner scanner = new Scanner(inputFile)) {
      while (scanner.hasNext()) {

        final String line = scanner.nextLine();

        int id = Integer.parseInt(line.substring(line.indexOf("#") + 1, line.indexOf("@")).trim());
        String[] distances =
            line.substring(line.indexOf("@") + 1, line.indexOf(":")).trim().split(",");
        int leftDistance = Integer.parseInt(distances[0]);
        int topDistance = Integer.parseInt(distances[1]);
        String[] size = line.substring(line.indexOf(":") + 1).trim().split("x");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);

        if (rowCount < leftDistance + width) {
          rowCount = leftDistance + width;
        }

        if (columnCount < topDistance + height) {
          columnCount = topDistance + height;
        }

        lines.add(new Claim(id, leftDistance, topDistance, width, height));
      }
    }

    return lines;
  }
}
