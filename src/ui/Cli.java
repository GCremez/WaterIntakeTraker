package ui;

import model.WaterEntry;
import service.WaterTrackerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;



public class Cli {

    private static Scanner scanner = new Scanner((System.in));
    private static WaterTrackerService waterTrackerService = new WaterTrackerService();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    /**
     * Main method to start the CLI application.
     * It initializes the water tracker service and handles user input.
     */

    public static void start() {
        System.out.println("Welcome to the Water Tracker!");

        // set daily goal
        System.out.println("Please enter your daily water intake goal (in ml): ");
        int dailyGoal = Integer.parseInt(scanner.nextLine());
        waterTrackerService.setDailyGoal(dailyGoal);

        System.out.println("Your daily goal is set to " + dailyGoal + " ml.");



        // Entering water intake
        while (true) {
            System.out.println("Enter amount (ml), 'history', 'stats', 'clear', or 'exit': ");
            String input = scanner.nextLine();

            // Exit condition
            if (input.equalsIgnoreCase("exit"))
                break;

            switch (input.toLowerCase()) {
                case "history" -> showHistory();
                case "stats" -> showStat();
                case "clear" -> clearHistory();
                default -> handleWaterIntake(input);
            }
        }


        System.out.println("Thank you for using the Water Tracker!");
    }

    private static void handleWaterIntake(String input) {

        // Handle water intake entry
        try {
            int amount = Integer.parseInt(input);
            // Call the service to add the entry
            WaterEntry newEntry = new WaterEntry(amount, LocalDateTime.now());
            waterTrackerService.addEntry(newEntry);

            // Format the timestamp
            String formattedTime = newEntry.timestamp().format(formatter);

            int totalIntake = waterTrackerService.getTotalWaterIntake();
            double progress = waterTrackerService.getProgress();

            System.out.println("✅ Added " + amount + " ml of water at " + formattedTime);
            System.out.printf("💧 Total today: %d ml (%.1f%% of goal)\n", totalIntake, progress);
            drawProgressBar(progress);

            if (waterTrackerService.isGoalMet()) {
                System.out.println("🎉 Congratulations! You've met your daily goal of " +
                        waterTrackerService.getDailyGoal() + " ml!");
            }
        }

        catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Enter a number, 'history', 'stats', or 'exit'.\n");
        }
    }

    public static void clearHistory() {
        waterTrackerService.clearHistory();
        System.out.println("✅ History cleared.");
    }

    private static void showHistory() {
        List<WaterEntry> entries = waterTrackerService.getHistory();

        if (entries.isEmpty()) {
            System.out.println("No entries found.");
            return;
        } else {
            System.out.println("📜 Water Intake History:");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            for (WaterEntry entry : entries) {
                String time = entry.timestamp().format(formatter);
                System.out.println("💧 " + entry.amount() + " ml at " + time);
            }
            System.out.println();
        }
    }

    private static void showStat() {
        List<WaterEntry> entries = waterTrackerService.getHistory();
        if (entries.isEmpty()) {
            System.out.println("No entries found.");
        } else {
            System.out.println("📜 Water Intake History:");
            for (WaterEntry entry : entries) {
                System.out.println("💧 " + entry.amount() + " ml at " + entry.timestamp());
            }
        }

        int total = waterTrackerService.getTotalWaterIntake();
        double average = total / (double) entries.size();
        WaterEntry lastEntry = entries.get(entries.size() - 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String lastTime = lastEntry.timestamp().format(formatter);

        System.out.println("📊 Stats:");
        System.out.println("🧃 Total Entries: " + entries.size());
        System.out.printf("🧮 Average Intake: %.1f ml\n", average);
        System.out.println("⏰ Last Entry: " + lastTime);
        System.out.println();
    }

    private static void drawProgressBar(double percent) {
        int bars = (int) (percent / 10); // total 10 bars
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            bar.append(i < bars ? "▰" : "▱");
        }
        System.out.println(bar + " " + (int) percent + "%\n");
    }


}
