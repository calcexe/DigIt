package pl.calc_exe.wykop.extras;

public class Extras {
    public static final String SECRET;
    public static final String APP_KEY;
    public static final String LOGIN_URL;
    public static final String BASE_HOST = "a.wykop.pl";
    public static final String BASE_URL = "http://a.wykop.pl/";
    public static final String API_SIGN = "apisign";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String IS_LOGGED = "islogged";
    public static final String START_PAGE = "startPage";
    public static final String ENTRY_ID = "entryId";

    static {
        SECRET = Secrets.SECRET;
        APP_KEY = Secrets.APP_KEY;
        LOGIN_URL = "http://a.wykop.pl/user/connect/appkey/" + APP_KEY;
    }
}