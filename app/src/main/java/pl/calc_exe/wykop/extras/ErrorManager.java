package pl.calc_exe.wykop.extras;

import pl.calc_exe.wykop.model.domain.Error;

public class ErrorManager {

    public enum Types{NONE, USERKEY, OTHER}

    public static Types parseError(Error error){

        switch (error.getCode()){
            case 0:
                return Types.NONE;
            //Invalid user key
            case 11:
                return Types.USERKEY;
            default:
                return Types.OTHER;
        }

    }
}
