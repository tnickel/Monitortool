package hiflsklasse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SelectorFile {
    private Set<Integer> numbers;

    public SelectorFile(String filename) {
        numbers = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    try {
                        numbers.add(Integer.parseInt(part.trim()));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format: " + part);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public boolean checkSelected(int index) {
        return numbers.contains(index);
    }
}

