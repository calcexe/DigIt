package pl.calc_exe.wykop.events;

/**
 * Event for changing ActionBar title and to show/hide spinner.
 */
public class ActionBarEvent {
    private int page;
    private int pageType;

    public ActionBarEvent(int page, int pageType) {
        this.page = page;
        this.pageType = pageType;
    }

    public int getPage() {
        return page;
    }

    public int getPageType() {
        return pageType;
    }
}
