package model;

import java.time.LocalDateTime;

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
