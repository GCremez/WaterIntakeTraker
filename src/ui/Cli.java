package ui;

import model.WaterEntry;
import repository.WaterRepository;
import service.WaterTrackerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Cli {

    private static Scanner scanner = new Scanner((System.in));
    private static WaterTrackerService waterTrackerService = new WaterTrackerService();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    public static void start() {
        System.out.println("Welcome to the Water Tracker!");

        // set daily goal
        System.out.println("Please enter your daily water intake goal (in ml): ");
        int dailyGoal = Integer.parseInt(scanner.nextLine());
        waterTrackerService.setDailyGoal(dailyGoal);

        System.out.println("Your daily goal is set to " + dailyGoal + " ml.");



        // Entering water intake
        while (true) {
            System.out.println("Please enter the amount of water you drank (in ml): ");
            String input = scanner.nextLine();

            // Exit condition
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.equalsIgnoreCase("history")) {
                showHistory();
                continue;
            }

            if (input.equalsIgnoreCase("clear")) {
                clearHistory();
                continue;
            }

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

                if (waterTrackerService.isGoalMet()) {
                    System.out.println("🎉 Congratulations! You've met your daily goal of " +
                            waterTrackerService.getDailyGoal() + " ml!");
                }
            }

            catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number or 'exit'.\n");
            }
        }

        System.out.println("Thank you for using the Water Tracker!");
    }

    public static void clearHistory() {
        waterTrackerService.clearHistory();
        System.out.println("✅ History cleared.");
    }

    private static void showHistory() {
        System.out.println("📜 Water Intake History:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (WaterEntry entry : waterTrackerService.getHistory()) {
            String time = entry.timestamp().format(formatter);
            System.out.println("💧 " + entry.amount() + " ml at " + time);
        }
        System.out.println();
    }



}
