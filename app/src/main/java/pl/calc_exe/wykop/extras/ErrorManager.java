package pl.calc_exe.wykop.extras;

import pl.calc_exe.wykop.model.domain.Error;

public class ErrorManager {

    public enum Types {NONE, USERKEY, OTHER}

    public static Types parseError(Error error) {

        switch (error.getCode()) {
            case 0:
                return Types.NONE;
            //Invalid user key
            case 11:
                return Types.USERKEY;
            default:
                return Types.OTHER;
        }

    }

    public static Error throwableToError(Throwable throwable) {
        int code = 666;
        String message;
        if (throwable.getMessage() == null) {
            message = "Wystąpił nieznany błąd.";
        } else {
            message = throwable.getMessage();
        }
        return new Error(code, message);
    }
}
