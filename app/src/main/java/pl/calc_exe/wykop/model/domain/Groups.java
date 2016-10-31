package pl.calc_exe.wykop.model.domain;

import android.util.SparseArray;

public class Groups {

    private static SparseArray<String> colors;

    static {
        colors = new SparseArray<>();
        colors.put(0, "#339933");
        colors.put(1, "#FF5917");
        colors.put(2, "#BB0000");
        colors.put(5, "#000000");
        colors.put(1001, "#999999");
        colors.put(1002, "#999999");
        colors.put(2001, "#3F6FA0");
    }

    public static String getColor(int group) {
        return colors.get(group);
    }
}