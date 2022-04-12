package vip.phantom.api.utils;

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

}
