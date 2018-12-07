package com.ercumentkisa.adventofcode_2018.day7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class TheSumOfItsParts {

  private Set<Character> vertices = new HashSet<>();
  private Map<Character, List<Character>> dependencies = new TreeMap<>();
  private Map<Character, Integer> dependencyCounts = new HashMap<>();

  public static void main(String[] args) throws FileNotFoundException {
    TheSumOfItsParts theSumOfItsParts = new TheSumOfItsParts();
    theSumOfItsParts.loadFile();
    theSumOfItsParts.partOne();
    theSumOfItsParts.partTwo();
  }

  private void partOne() {
    Map<Character, Integer> remainingDependencies = new HashMap<>(dependencyCounts);
    List<Character> queue = new LinkedList<>();

    // start with the parts with no dependency
    for (Character vertex : vertices) {
      if (remainingDependencies.get(vertex) == null) {
        queue.add(vertex);
      }
    }

    StringBuilder sb = new StringBuilder();
    while (!queue.isEmpty()) {
      Collections.sort(queue);
      char vertex = queue.remove(0);
      sb.append(vertex);

      if (dependencies.get(vertex) != null) {
        for (Character dep : dependencies.get(vertex)) {
          remainingDependencies.put(dep, remainingDependencies.get(dep) - 1);
          if (remainingDependencies.get(dep) == 0) {
            queue.add(dep);
          }
        }
      }
    }

    System.out.println("The correct order is " + sb.toString());
  }

  private void partTwo() {

    List<Character> workLoadQueue = new LinkedList<>();
    List<WorkItem> processingQueue = new LinkedList<>();
    Set<Character> visited = new HashSet<>();

    // start with the parts with no dependency
    for (Character vertex : vertices) {
      if (dependencyCounts.get(vertex) == null) {
        workLoadQueue.add(vertex);
        visited.add(vertex);
      }
    }

    Collections.sort(workLoadQueue);
    int t = 0;

    List<Character> finishedWork = new ArrayList<>();
    while (!workLoadQueue.isEmpty() || !processingQueue.isEmpty()) {

      checkFinishedWork(workLoadQueue, processingQueue, visited, finishedWork, t);
      Collections.sort(workLoadQueue);

      while (!workLoadQueue.isEmpty() && processingQueue.size() < 5) {
        char workPiece = workLoadQueue.remove(0);
        processingQueue.add(new WorkItem(workPiece, findRequiredTime(workPiece), t));
      }

      t++;
    }

    System.out.println("It takes " + (t - 1) + " seconds to complete all steps.");
  }

  private void checkFinishedWork(
      List<Character> workLoadQueue,
      List<WorkItem> processingQueue,
      Set<Character> visited,
      List<Character> finishedWork,
      int time) {
    List<WorkItem> finished = new ArrayList<>();
    for (WorkItem workItem : processingQueue) {
      if (time - workItem.startTime == workItem.requiredTime) {
        finished.add(workItem);
      }
    }

    finished.sort(new WorkItemComparator());
    if (!finished.isEmpty()) {
      processingQueue.removeAll(finished);
      for (WorkItem finishedWorkItem : finished) {
        finishedWork.add(finishedWorkItem.name);

        if (dependencies.get(finishedWorkItem.name) != null) {
          for (Character dep : dependencies.get(finishedWorkItem.name)) {
            dependencyCounts.put(dep, dependencyCounts.get(dep) - 1);
            if (dependencyCounts.get(dep) == 0 && !visited.contains(dep)) {
              workLoadQueue.add(dep);
              visited.add(dep);
            }
          }
        }
      }
    }
  }

  private int findRequiredTime(char letter) {
    return 61 + ((int) letter) - ((int) 'A');
  }

  private void loadFile() throws FileNotFoundException {
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day7-input.txt").getFile());
    try (Scanner scanner = new Scanner(inputFile)) {
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        final Character key = line.charAt(36);
        final Character dependency = line.charAt(5);

        vertices.add(dependency);
        vertices.add(key);
        dependencies.computeIfAbsent(dependency, k -> new ArrayList<>()).add(key);
        dependencyCounts.merge(key, 1, (a, b) -> a + b);
      }
    }
  }

  private class WorkItemComparator implements Comparator<WorkItem> {

    @Override
    public int compare(WorkItem o1, WorkItem o2) {
      return Character.compare(o1.name, o2.name);
    }
  }

  static class WorkItem {
    char name;
    int requiredTime;
    int startTime;

    WorkItem(char name, int requiredTime, int startTime) {
      this.name = name;
      this.requiredTime = requiredTime;
      this.startTime = startTime;
    }
  }
}
