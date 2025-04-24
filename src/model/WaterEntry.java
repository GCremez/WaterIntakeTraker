package model;

import java.time.LocalDateTime;

public class WaterEntry {
    private LocalDateTime timestamp;
    private int amount;


    /**
     * Constructor for WaterEntry
     *
     * @param amount the amount of water in milliliters
     */



    public WaterEntry(int amount) {
        this.timestamp = LocalDateTime.now();
        this.amount = amount;
    }



    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "WaterEntry{" +
                "date='" + timestamp + '\'' +
                ", amount=" + amount +
                '}';
    }


}
