package pl.calc_exe.wykop.events;

import android.view.MenuItem;

/**
 * Created by Mateusz on 2016-09-04.
 */
public class MainMenuEvent {

    int item;

    public MainMenuEvent(int item) {
        this.item = item;
    }

    public int getItem() {
        return item;
    }
}
