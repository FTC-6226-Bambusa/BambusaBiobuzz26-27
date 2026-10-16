package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class TelemetryPlus {
    // Returns a separator
    public static String separator() {
        return Settings.DEBUG.STYLE.separatorChar.repeat(
                Settings.DEBUG.STYLE.separatorLength
        );
    }

    // Returns separator with a title at the center
    public static String title(String title) {
        int len = Settings.DEBUG.STYLE.separatorLength;
        String ch = Settings.DEBUG.STYLE.separatorChar;

        int padding = len - title.length() - 2;
        if (padding < 0) return title.substring(0, len);

        int left = padding / 2;
        int right = padding - left;

        return ch.repeat(left) + " " + title + " " + ch.repeat(right);
    }
}
