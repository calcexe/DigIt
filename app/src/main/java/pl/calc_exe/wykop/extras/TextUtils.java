package pl.calc_exe.wykop.extras;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class TextUtils {

    public static String getCommentsText(int count) {
        StringBuilder sb = new StringBuilder();
        sb.append(count);
        if (count == 1)
            sb.append(" komentarz");
        else {
            if (count % 100 > 10 && count % 100 < 15)
                sb.append(" komentarzy");
            else {
                count %= 10;
                if (count > 1 && count < 5)
                    sb.append(" komentarze");
                else
                    sb.append(" komentarzy");
            }
        }

        return sb.toString();
    }

    public static String getDateText(DateTime date) {

        Period period = new Period(date, org.joda.time.DateTime.now(date.getZone()));

        if (period.getYears() > 0)
            return period.getYears() + " lat temu";

        if (period.getMonths() > 0)
            return period.getMonths() + " miesięcy temu";

        if (period.getDays() > 0)
            return period.getDays() + " dni temu";

        if (period.getHours() > 0)
            return period.getHours() + " godzin temu";

        if (period.getMinutes() > 0)
            return period.getMinutes() + " minut temu";

        if (period.getSeconds() > 30)
            return period.getSeconds() + " sekund temu";

        if (period.toStandardSeconds().getSeconds() < 0){
            return "w przyszłości ;)";
        }

        return "przed chwilą";
    }
}
