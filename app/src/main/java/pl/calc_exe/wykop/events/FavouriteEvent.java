package pl.calc_exe.wykop.events;

import pl.calc_exe.wykop.model.domain.Link;

/**
 * Created by Mateusz on 2016-09-27.
 */
public class FavouriteEvent {

    private Link link;
    public FavouriteEvent(Link link) {
        this.link = link;
    }

    public Link getLink() {
        return link;
    }
}
