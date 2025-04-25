package repository;

import model.WaterEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class WaterRepository {
    private static final String FILE_NAME = "water_entries.txt";

    public static void saveEntries (List<WaterEntry> entries) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME,  true))) {
            for (WaterEntry entry : entries) {
                writer.write(entry.amount() + "," + entry.timestamp());
                writer.newLine();
            }
        } catch (IOException e) {
                System.out.println("❌ Failed to save entry " + e.getMessage());
            }

    }

    // clear the file
    public static void clearFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("❌ Failed to clear file: " + e.getMessage());
        }
    }

    public static List<WaterEntry> loadAllEntries() {
        List<WaterEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String [] parts = line.split(",");

                if (parts.length == 2) {
                    int amount = Integer.parseInt(parts[0]);
                    LocalDateTime timestamp = LocalDateTime.parse(parts[1]); // Adjust the format if necessary
                    entries.add(new WaterEntry(amount, timestamp)); //
                }
            }
        } catch (FileNotFoundException e) {
            // Handle the case where the file does not exist
            System.out.println("❌ File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ Failed to load entries: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid data format: " + e.getMessage());
        }

        return entries;
    }
    public static void clearEntries() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("❌ Failed to clear entries: " + e.getMessage());
        }
    }
}
