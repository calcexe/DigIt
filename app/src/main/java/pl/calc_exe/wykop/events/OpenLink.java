package pl.calc_exe.wykop.events;

import pl.calc_exe.wykop.model.domain.Link;

/**
 * Created by Mateusz on 2016-09-08.
 */
public class OpenLink {
    private final Link link;

    public OpenLink(Link link) {
        this.link = link;
    }

    public Link getLink() {
        return link;
    }
}
