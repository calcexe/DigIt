package pl.calc_exe.wykop.model.rest.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

import pl.calc_exe.wykop.extras.Extras;

public class DateTimeAdapter implements JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        DateTimeFormatter formatter = DateTimeFormat.forPattern(Extras.DATE_FORMAT);

        DateTime dateTime = formatter.parseDateTime(json.getAsString());

        return new DateTime(dateTime);
    }
}
