package vip.phantom.api.utils;

import java.time.LocalDate;

public class Methods {

    public static String coverString(String text, int startIndex, int endIndex) {
        if (text == null || text.equalsIgnoreCase("")) {
            return "";
        }
        return text.substring(0, startIndex) + "*".repeat(Math.max(0, endIndex - startIndex));
    }

    public static float clamp(float num, float min, float max) {
        return Math.min(max, Math.max(min, num));
    }

    public static LocalDate getDateFromString(String string) {
        String[] parts = string.split("\\.");
        return parts.length == 3 ? LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0])) : null;
    }

}
