package com.ercumentkisa.adventofcode_2018.day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class ReposeRecord {

  static class Timeline {
    int[] minutes = new int[60];
  }

  private Map<Integer, Timeline> guardTimeline = new HashMap<>();

  public static void main(String[] args) throws FileNotFoundException {
    ReposeRecord reposeRecord = new ReposeRecord();
    Map<LocalDateTime, String> stats = reposeRecord.loadFile();
    reposeRecord.partOne(stats);
    reposeRecord.partTwo();
  }

  private void partOne(Map<LocalDateTime, String> stats) {

    // Create a mapping of each guard and its corresponding sleep statistics
    int guardId;
    LocalDateTime sleepStartTime = null;
    Timeline sleepTimeline = null;
    for (Map.Entry<LocalDateTime, String> entry : stats.entrySet()) {
      String statsString = entry.getValue();
      if (statsString.contains("begins shift")) {
        guardId =
            Integer.parseInt(
                statsString
                    .substring(statsString.indexOf("#") + 1, statsString.indexOf("begins shift"))
                    .trim());
        sleepTimeline = guardTimeline.get(guardId);
        if (sleepTimeline == null) {
          sleepTimeline = new Timeline();
          guardTimeline.put(guardId, sleepTimeline);
        }
      } else if (statsString.contains("falls asleep")) {
        sleepStartTime = entry.getKey();
      } else if (statsString.contains("wakes up")) {
        // calculate the timeline for this sleep
        int sleepStartMin = sleepStartTime.getMinute();
        int sleptMinutes = entry.getKey().getMinute() - sleepStartMin;
        for (int i = sleepStartMin; i < sleepStartMin + sleptMinutes; i++) {
          sleepTimeline.minutes[i]++;
        }
      }
    }

    // Find the guard with most minutes asleep.
    int guardWithMostSleepId = -1;
    int maxSleepMinutes = Integer.MIN_VALUE;
    for (Map.Entry<Integer, Timeline> guardEntry : guardTimeline.entrySet()) {
      int totalSleepMinutes = IntStream.of(guardEntry.getValue().minutes).sum();
      if (totalSleepMinutes > maxSleepMinutes) {
        maxSleepMinutes = totalSleepMinutes;
        guardWithMostSleepId = guardEntry.getKey();
      }
    }

    // Find the minute this guard sleeps most
    int[] sleepyGuardsTimeline = guardTimeline.get(guardWithMostSleepId).minutes;
    int mostSleepyMinuteIndex = getIndexOfMaxItem(sleepyGuardsTimeline);

    System.out.println(
        "Most sleepy guard is #"
            + guardWithMostSleepId
            + " with "
            + maxSleepMinutes
            + " minutes and the most sleepy minute is "
            + mostSleepyMinuteIndex);
    System.out.println("Result is: " + guardWithMostSleepId * mostSleepyMinuteIndex);
  }

  private void partTwo() {
    int mostSleepyMinuteOverall = Integer.MIN_VALUE;
    int guardId = -1;

    // Find the guard which is most frequently asleep on the same minute
    for (Map.Entry<Integer, Timeline> entry : guardTimeline.entrySet()) {
      int mostSleepyMinuteForGuard = getIndexOfMaxItem(entry.getValue().minutes);
      if (mostSleepyMinuteForGuard > mostSleepyMinuteOverall) {
        mostSleepyMinuteOverall = mostSleepyMinuteForGuard;
        guardId = entry.getKey();
      }
    }

    System.out.println("Result is: " + (guardId * mostSleepyMinuteOverall));
  }

  /** Creates a sorted map of events. */
  private Map<LocalDateTime, String> loadFile() throws FileNotFoundException {
    Map<LocalDateTime, String> stats = new TreeMap<>();
    final ClassLoader classLoader = getClass().getClassLoader();
    final File inputFile = new File(classLoader.getResource("day4-input.txt").getFile());
    try (Scanner scanner = new Scanner(inputFile)) {
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        LocalDateTime timestamp = parseTimestamp(line.substring(1, line.indexOf(']')));
        String statsString = line.substring(line.indexOf("]") + 2);
        stats.put(timestamp, statsString);
      }
    }
    return stats;
  }

  private LocalDateTime parseTimestamp(String timestamp) {
    return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  /** Finds the index having the greatest value in an array */
  private int getIndexOfMaxItem(int[] items) {
    int maxIndex = 0;
    for (int i = 0; i < items.length; i++) {
      maxIndex = items[i] > items[maxIndex] ? i : maxIndex;
    }
    return maxIndex;
  }
}
