package service;

import model.WaterEntry;
import repository.WaterRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * WaterTrackerService is a service class that manages water intake entries and tracks progress.
 * It allows adding new entries, setting daily goals, and checking if the goal is met.
 */

public class WaterTrackerService {

    private List<WaterEntry> entries;
    private int dailyGoal = 2000; // Default daily goal in milliliters


    public WaterTrackerService(List<WaterEntry> entries) {
        this.entries = entries;
    }


    public void addEntry(WaterEntry entry) {
        entries.add(entry);
    }

    public int getTotalWaterIntake() {
        LocalDate today = LocalDate.now();
        return entries.stream()
                .filter(entry -> entry.timestamp().toLocalDate().equals(today))
                .mapToInt(WaterEntry::amount)
                .sum();
    }

    public int getTotalWaterIntakeThisMonth() {
        LocalDate today = LocalDate.now();
        return entries.stream()
                .filter(entry ->
                        entry.timestamp().getMonth() == today.getMonth() &&
                                entry.timestamp().getYear() == today.getYear())
                .mapToInt(WaterEntry::amount)
                .sum();
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public double getProgress() {
        if (dailyGoal == 0) {
            return 0.0; // Avoid division by zero
        }
        return (getTotalWaterIntake() * 100.0) / dailyGoal;

    }

    public void exportToCsv(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Amount,Date,Time\n");
            for (WaterEntry entry : entries) {
                writer.write(entry.amount() + "," +
                        entry.timestamp().toLocalDate() + "," +
                        entry.timestamp().toLocalTime() + "\n");
            }
            System.out.println("✅ Data exported to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Failed to export: " + e.getMessage());
        }
    }

    public boolean isGoalMet() {
        return getTotalWaterIntake()
                >= dailyGoal;
    }

    public List<WaterEntry> getHistory() {
        return entries;
    }



    public void clearHistory() {
        entries.clear();
        WaterRepository.clearEntries();
    }
}
