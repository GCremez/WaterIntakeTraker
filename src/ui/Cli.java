package ui;

import model.WaterEntry;
import service.WaterTrackerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;



public class Cli {

    private static final Scanner scanner = new Scanner((System.in));
    private static WaterTrackerService waterTrackerService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public Cli (WaterTrackerService service) {
        waterTrackerService = service;
    }

    /**
     * Main method to start the CLI application.
     * It initializes the water tracker service and handles user input.
     */

    public static void start() {
        System.out.println("Welcome to the Water Tracker!");

        setDailyGoal();



        // Entering water intake
        while (true) {
            printMenu();

            String input = scanner.nextLine().trim();
           switch (input) {
               case "1":
                   handleWaterIntake();
                        break;
               case "2":
                   showTodaySummary();
                        break;
               case "3":
                   showMonthlySummary();
                        break;
               case "4":
                   showHistory();
                        break;
               case "5":
                     exportDataToCSV();
                        break;
               case "6":
                   showStat();
                        break;
               case "7":
                   clearHistory();
                        break;
               case "0":
                     System.out.println("Exiting...");
                     return;
                default:
                    System.out.println("⚠️ Invalid choice. Try again!");
           }
        }

    }


    // Set daily goal
    private static void setDailyGoal() {
        if (waterTrackerService.getDailyGoal() == 0) {
            System.out.println("Enter your daily water intake goal (ml): ");
            try {
                int goal = Integer.parseInt(scanner.nextLine().trim());
                waterTrackerService.setDailyGoal(goal);
                System.out.println("✅ Daily goal set to " + goal + " ml.");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid input, using default goal (2000 ml).");
            }
        }
    }


    // Print menu
    private static void printMenu() {
        System.out.println("\n========= MENU =========");
        System.out.println("1. Log Water Intake");
        System.out.println("2. Show Today's Total Intake");
        System.out.println("3. Show This Month's Total Intake");
        System.out.println("4. Show Water Intake History");
        System.out.println("5. Export Data to CSV");
        System.out.println("6. Show Progress Toward Goal");
        System.out.println("7. Clear History");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }


    private static void handleWaterIntake() {

        // Handle water intake entry
        try {
            int amount = Integer.parseInt(scanner.nextLine());

            if (amount <= 0) {
                System.out.println("❌ Invalid amount. Please enter a positive number.");
                return;
            }


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

    private static void showTodaySummary() {
        int totalIntakeToday = waterTrackerService.getTotalWaterIntake();
        System.out.println("💧 Total water intake today: " + totalIntakeToday + " ml");
    }

    private static void showMonthlySummary() {
        int totalMonth = waterTrackerService.getTotalWaterIntakeThisMonth();
        System.out.println("📅 Total water intake this month: " + totalMonth + " ml");
    }

    public static void clearHistory() {
        waterTrackerService.clearHistory();
        waterTrackerService.setDailyGoal(0);
        System.out.println("✅ History cleared.");

        System.out.println("🚰 Please set a new daily water intake goal (in ml):");
        int goal = Integer.parseInt(scanner.nextLine());
        waterTrackerService.setDailyGoal(goal);
        System.out.println("🎯 New Goal set: " + goal + " ml per day!");
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
            return;
        } else {
            System.out.println("📜 Water Intake History:");
            for (WaterEntry entry : entries) {
                System.out.println("💧 " + entry.amount() + " ml at " + entry.timestamp());
            }
        }

        int total = waterTrackerService.getTotalWaterIntake();
        double average = total / (double) entries.size();
        WaterEntry lastEntry = entries.getLast();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String lastTime = lastEntry.timestamp().format(formatter);

        System.out.println("📊 Stats:" );
        drawProgressBar(waterTrackerService.getProgress()); // Progress bar
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

    private static void exportDataToCSV() {
        System.out.print("Enter filename to export (example: water_log.csv): ");
        String filename = scanner.nextLine();
        waterTrackerService.exportToCsv(filename);
    }

}
