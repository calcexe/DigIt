package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-09-08.
 */
public class ToolbarSpinnerClick {
    private String value;

    public ToolbarSpinnerClick(String value) {
        this.value = value;
    }

    public String getValue() {
        return value.toLowerCase();
    }
}
