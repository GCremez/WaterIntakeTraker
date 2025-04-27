package service;

import model.WaterEntry;
import repository.WaterRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * WaterTrackerService is a service class that manages water intake entries and tracks progress.
 * It allows adding new entries, setting daily goals, and checking if the goal is met.
 */

public class WaterTrackerService {

    private List<WaterEntry> entries;
    private int dailyGoal;



    public WaterTrackerService() {
        this.entries = new ArrayList<>();
        this.dailyGoal = 2000; // Default daily goal in milliliters
    }

    public void addEntry(WaterEntry entry) {
        entries.add(entry);
        // Save the entry to the repository
        WaterRepository.saveEntries(entries);
    }

    public int getTotalWaterIntake() {
        int total = 0;

        for (WaterEntry entry : entries) {
            total += entry.amount();
        }

        return total;
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

    public boolean isGoalMet() {
        return getTotalWaterIntake()
                >= dailyGoal;
    }

    public List<WaterEntry> getHistory() {
        return WaterRepository.loadAllEntries();
    }



    public void clearHistory() {
        entries.clear();
        WaterRepository.clearEntries();
    }
}
