package com.ercumentkisa.adventofcode_2018.day6;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChronalCoordinates {

  private static final int SAFE_REGION_DISTANCE_LIMIT = 10000;

  private List<Coordinate> coordinates = new ArrayList<>();
  private ChronalPoint[][] matrix;

  public static void main(String[] args) throws FileNotFoundException {
    ChronalCoordinates chronalCoordinates = new ChronalCoordinates();
    chronalCoordinates.loadFile();

    chronalCoordinates.partOne();
    chronalCoordinates.partTwo();
  }

  private void partOne() {
    // find the greatest X and Y coordinates to use in the matrix
    int maxRowIndex = Integer.MIN_VALUE;
    int maxColumnIndex = Integer.MIN_VALUE;
    for (Coordinate coordinate : coordinates) {
      maxRowIndex = Math.max(maxRowIndex, coordinate.rowIndex);
      maxColumnIndex = Math.max(maxColumnIndex, coordinate.columnIndex);
    }

    matrix = new ChronalPoint[maxRowIndex + 1][maxColumnIndex + 1];
    for (Coordinate coordinate : coordinates) {
      matrix[coordinate.rowIndex][coordinate.columnIndex] =
          new ChronalPoint(coordinate.rowIndex, coordinate.columnIndex, coordinate, 0);
    }

    // for each coordinate
    for (Coordinate coordinate : coordinates) {
      // calculate the distances and overwrite if it is a smaller distanceToOwner
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          ChronalPoint chronalPoint = matrix[i][j];
          int currentDistance =
              chronalPoint == null ? Integer.MAX_VALUE : chronalPoint.distanceToOwner;
          int distance = manhattanDistance(i, j, coordinate.rowIndex, coordinate.columnIndex);

          if (!containsCoordinate(chronalPoint)) {
            if (distance < currentDistance) {
              matrix[i][j] = new ChronalPoint(i, j, coordinate, distance);
            } else if (distance == currentDistance) {
              matrix[i][j].owner = null;
            }
          }
        }
      }
    }

    // find the infinite coordinates
    Set<Coordinate> infiniteCoordinates = new HashSet<>();
    for (int columnIndex = 0; columnIndex < matrix[0].length; columnIndex++) {
      Coordinate owner = matrix[0][columnIndex].owner;
      if (owner != null && !coordinates.contains(owner)) {
        infiniteCoordinates.add(owner);
      }
    }
    for (int columnIndex = 0; columnIndex < matrix[0].length; columnIndex++) {
      Coordinate owner = matrix[matrix.length - 1][columnIndex].owner;
      if (owner != null && !coordinates.contains(owner)) {
        infiniteCoordinates.add(owner);
      }
    }
    for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
      Coordinate owner = matrix[rowIndex][0].owner;
      if (owner != null && !coordinates.contains(owner)) {
        infiniteCoordinates.add(owner);
      }
    }
    for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
      Coordinate owner = matrix[rowIndex][matrix[0].length - 1].owner;
      if (owner != null && !coordinates.contains(owner)) {
        infiniteCoordinates.add(owner);
      }
    }

    // all - infinite = finite
    List<Coordinate> finiteCoordinates = new ArrayList<>(coordinates);
    finiteCoordinates.removeAll(infiniteCoordinates);

    System.out.println("Number of finite coordinates: " + finiteCoordinates.size());

    // find the finite coordinate with max area
    int maxArea = Integer.MIN_VALUE;
    for (Coordinate coordinate : finiteCoordinates) {
      int coordinateArea = 0;
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          if (matrix[i][j].owner == coordinate) {
            coordinateArea++;
          }
        }
      }

      maxArea = Math.max(maxArea, coordinateArea);
    }

    System.out.println("The finite coordinate with max area has are size: " + maxArea);
  }

  private void partTwo() {

    int areaOfSafeRegion = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        // find the distance of each point to each coordinate
        int totalDistanceToAllCoordinates = 0;
        for (Coordinate coordinate : coordinates) {
          int distance = manhattanDistance(i, j, coordinate.rowIndex, coordinate.columnIndex);
          totalDistanceToAllCoordinates += distance;
        }

        if (totalDistanceToAllCoordinates < SAFE_REGION_DISTANCE_LIMIT) {
          areaOfSafeRegion++;
        }
      }
    }

    System.out.println("Area of the safe region: " + areaOfSafeRegion);
  }

  private void loadFile() throws FileNotFoundException {
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day6-input.txt").getFile());
    try (Scanner scanner = new Scanner(inputFile)) {
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        List<Integer> points =
            Stream.of(line.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
        coordinates.add(new Coordinate(points.get(1), points.get(0)));
      }
    }
  }

  private int manhattanDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x2 - x1) + Math.abs(y2 - y1);
  }

  static class Coordinate {
    int rowIndex;
    int columnIndex;

    Coordinate(int rowIndex, int columnIndex) {
      this.rowIndex = rowIndex;
      this.columnIndex = columnIndex;
    }

    @Override
    public int hashCode() {
      return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
    }
  }

  private boolean containsCoordinate(ChronalPoint chronalPoint) {
    if (chronalPoint == null) {
      return false;
    }
    for (Coordinate coordinate : coordinates) {
      if (coordinate.rowIndex == chronalPoint.rowIndex
          && coordinate.columnIndex == chronalPoint.columnIndex) {
        return true;
      }
    }
    return false;
  }

  static class ChronalPoint {
    int rowIndex;
    int columnIndex;
    Coordinate owner;
    int distanceToOwner;

    ChronalPoint(int rowIndex, int columnIndex, Coordinate owner, int distanceToOwner) {
      this.rowIndex = rowIndex;
      this.columnIndex = columnIndex;
      this.owner = owner;
      this.distanceToOwner = distanceToOwner;
    }
  }
}
