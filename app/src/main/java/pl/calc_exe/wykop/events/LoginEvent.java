package pl.calc_exe.wykop.events;

/**
 * Created by Mateusz on 2016-09-22.
 */
public class LoginEvent {
    private boolean logged;

    public LoginEvent(boolean logged) {
        this.logged = logged;
    }

    public boolean isLogged() {
        return logged;
    }
}
