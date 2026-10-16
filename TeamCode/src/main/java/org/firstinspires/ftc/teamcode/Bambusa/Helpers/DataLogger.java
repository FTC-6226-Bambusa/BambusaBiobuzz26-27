package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
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

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < numbers.length; i++) {
                sb.append((float) numbers[i]);
                if (i < numbers.length - 1) sb.append(",");
            }

            writer.write(sb.toString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}