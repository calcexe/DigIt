package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-09-26.
 */
public class ChangePageEvent {
    private final int page;

    public ChangePageEvent(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
