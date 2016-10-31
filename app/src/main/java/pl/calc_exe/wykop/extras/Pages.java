package pl.calc_exe.wykop.extras;

public class Pages {
    public static final String PAGE = "page";

    public static final int INDEX = 0;
    public static final int STREAM = 1;
    // TODO: 2016-10-31 Login and settings number is temporally changed.
    public static final int SETTINGS = 5;
    public static final int LOGIN = 2;
    public static final int NONE = 4;

    public class Index{
        public static final int PROMOTED = 0;
        public static final int UPCOMING = 1;
        public static final int FAVORITE = 2;
    };

    //TODO: Complete this class:
    public class Stream{
        public static final int INDEX = 0;
        public static final int HOT = 1;
        public static final int FAVORITE = 2;
    };

    //TODO: Change hardcoding string to resources.
    public static String getTitle(int page) {
        switch (page){
            case INDEX:
                return "Wykop";
            case STREAM:
                return "Mikroblog";
            case SETTINGS:
                return "Ustawienia";
            case LOGIN:
                return "Logowanie";
            default:
                return "Wykop";
        }
    }
}
