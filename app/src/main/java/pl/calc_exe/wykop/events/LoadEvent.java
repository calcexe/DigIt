package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-09-30.
 */
public class LoadEvent {
    private int page;

    public LoadEvent(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
