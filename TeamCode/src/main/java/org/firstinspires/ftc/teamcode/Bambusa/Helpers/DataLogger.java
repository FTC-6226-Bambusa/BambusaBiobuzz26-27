package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataLogger {
    private static final String mainPath = "/FIRST/Data/";

    public static String getFullPath() {
        return Environment.getExternalStorageDirectory().getPath() + mainPath;
    }

    // Logs array of numbers
    public static void storeNumbersCSV(String fileName, double... numbers) {
        File directory = new File(getFullPath());

        if (!directory.exists()) directory.mkdirs();
        if (!fileName.endsWith(".csv")) fileName += ".csv";

        File file = new File(directory, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < numbers.length; i++) {
                sb.append((float) numbers[i]);
                if (i < numbers.length - 1) sb.append(",");
            }

            writer.write(sb.toString());
            writer.newLine();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    // Returns the names of all files in the data directory
    public static String[] getFileNames() {
        File directory = new File(getFullPath());
        if (!directory.exists() || !directory.isDirectory()) {
            return new String[0];
        }
        return directory.list();
    }

    // Reads the contents of a specific CSV file
    public static String readCSVFile(String fileName) {
        if (!fileName.endsWith(".csv")) fileName += ".csv";
        File file = new File(getFullPath(), fileName);

        if (!file.exists()) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        return content.toString();
    }
}