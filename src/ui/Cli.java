package ui;

import model.WaterEntry;
import service.WaterTrackerService;
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
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int amount = Integer.parseInt(input);
                // Call the service to add the entry
                WaterEntry newEntry = new WaterEntry(amount);
                waterTrackerService.addEntry(newEntry);

                // Format the timestamp
                String formattedTime = newEntry.getTimestamp().format(formatter);

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


}
