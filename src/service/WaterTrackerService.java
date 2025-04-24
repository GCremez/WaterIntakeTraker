package service;

import model.WaterEntry;

import java.util.ArrayList;
import java.util.List;

public class WaterTrackerService {

    private List<WaterEntry> entries;
    private int dailyGoal;



    public WaterTrackerService() {
        this.entries = new ArrayList<>();
        this.dailyGoal = 2000; // Default daily goal in milliliters
    }

    public void addEntry(WaterEntry entry) {
        entries.add(entry);
    }

    public int getTotalWaterIntake() {
        int total = 0;

        for (WaterEntry entry : entries) {
            total += entry.getAmount();
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
        return (getTotalWaterIntake() * 100.0) / dailyGoal;
    }

    public boolean isGoalMet() {
        return getTotalWaterIntake()
                >= dailyGoal;
    }

}
