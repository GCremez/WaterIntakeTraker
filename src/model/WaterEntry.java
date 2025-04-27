package model;

import java.time.LocalDateTime;


/**
 * WaterEntry is a class that represents a single entry of water intake.
 * It contains the amount of water in milliliters and the timestamp of when the entry was made.
 */


public record WaterEntry(int amount, LocalDateTime timestamp) {
    /**
     * Constructor for WaterEntry
     *
     * @param amount the amount of water in milliliters
     */


    public WaterEntry(int amount, LocalDateTime timestamp) {
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WaterEntry{" +
                "date='" + timestamp + '\'' +
                ", amount=" + amount +
                '}';
    }


}
