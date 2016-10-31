package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-09-26.
 */
public class ShareEvent {
    private String url;

    public ShareEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
