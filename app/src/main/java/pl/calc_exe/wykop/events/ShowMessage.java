package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-09-04.
 */
public class ShowMessage {
    private final String message;

    public ShowMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
