package pl.calc_exe.wykop.model.domain;

import java.util.HashMap;
import java.util.Map;

public class Sexes {

    private static Map<String, String> colors;

    static {
        colors = new HashMap<String, String>();
        colors.put("female", "#f246d0");
        colors.put("male", "#46abf2");
        colors.put("", "#e5e7e9");
    };

    public static String getMaleColor(String male) {
        return colors.get(male);
    }
}
