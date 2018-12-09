package com.ercumentkisa.adventofcode_2018.day8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MemoryManeuver {

  static class Node {
    Header header;
    List<Node> children = new ArrayList<>();
    List<Integer> metadataEntries = new ArrayList<>();
    int value;
  }

  static class Header {
    int childrenCount;
    int metadataCount;

    Header(int childrenCount, int metadataCount) {
      this.childrenCount = childrenCount;
      this.metadataCount = metadataCount;
    }
  }

  private Node root = null;
  private Integer index = 0;

  public static void main(String[] args) throws FileNotFoundException {
    MemoryManeuver memoryManeuver = new MemoryManeuver();
    List<Integer> tokens = memoryManeuver.loadFile();
    memoryManeuver.partOne(tokens);
    memoryManeuver.partTwo();
  }

  private void partOne(List<Integer> tokens) {
    root = addNode(tokens);
    List<Integer> metadataVals = new ArrayList<>();
    Deque<Node> nodes = new ArrayDeque<>();
    nodes.add(root);
    while (!nodes.isEmpty()) {
      Node item = nodes.remove();
      metadataVals.addAll(item.metadataEntries);
      nodes.addAll(item.children);
    }

    int sumOfMetadatas = metadataVals.stream().mapToInt(Integer::intValue).sum();

    System.out.println("The sum of all metadata entries: " + sumOfMetadatas);
  }

  private Node addNode(List<Integer> tokens) {
    Node node = new Node();
    node.header = new Header(tokens.get(index), tokens.get(++index));
    for (int i = 0; i < node.header.childrenCount; i++) {
      ++index;
      node.children.add(addNode(tokens));
    }
    for (int i = 0; i < node.header.metadataCount; i++) {
      node.metadataEntries.add(tokens.get(++index));
    }
    return node;
  }

  private void partTwo() {
    findValues(root);
    System.out.println("The value of root node is: " + root.value);
  }

  private int findValues(Node node) {
    if (node.header.childrenCount == 0 && node.value == 0) {
      node.value += node.metadataEntries.stream().mapToInt(Integer::intValue).sum();
      return node.value;
    } else if (node.header.childrenCount > 0) {
      int val = 0;
      for (int metadataIndex : node.metadataEntries) {
        int size = node.children.size();
        metadataIndex = metadataIndex - 1;
        if (metadataIndex < size && metadataIndex >= 0) {
          val += findValues(node.children.get(metadataIndex));
        }
      }
      node.value = val;
    }
    return node.value;
  }

  private List<Integer> loadFile() throws FileNotFoundException {
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day8-input.txt").getFile());
    try (Scanner scanner = new Scanner(inputFile)) {
      String input = scanner.nextLine();
      String[] tokenStrings = input.split(" ");
      return Stream.of(tokenStrings)
          .mapToInt(Integer::parseInt)
          .boxed()
          .collect(Collectors.toList());
    }
  }
}
