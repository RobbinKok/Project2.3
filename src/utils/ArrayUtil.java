package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayUtil {

    public static String[] removeFromStringArray(String[] array, int index) {
        String[] newArray = new String[array.length - 1];

        for (int i = 0, j = 0; i < array.length; i++)
            if (i != index)
                newArray[j++] = array[i];

        return newArray;
    }

    public static String stringFromStringArray(String[] array) {
        StringBuilder buffer = new StringBuilder();

        for (String s : array)
            buffer.append(s).append(" ");

        return buffer.toString().trim();
    }

    public static ArrayList<String> stringToArray(String string) {
        ArrayList<String> results = new ArrayList<>();

        Pattern pattern = Pattern.compile("([\\w-]+)");
        Matcher matcher = pattern.matcher(string);

        while(matcher.find()) {
            results.add(matcher.group());
        }

        return results;
    }

    public static HashMap<String, String> stringToHashMap(String string) {
        HashMap<String, String> results = new HashMap<>();

        Pattern pattern = Pattern.compile("(\\w+)\\:\\s\\\"([\\w-]+)\\\"");
        Matcher matcher = pattern.matcher(string);

        while(matcher.find())
            results.put(matcher.group(1), matcher.group(2));

        return results;
    }
}
