package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-10-16.
 */
public class MainSpinnerEvent {

    private int position;

    public MainSpinnerEvent(int position){
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
