package pl.calc_exe.wykop.model.rest.adapters;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.domain.Link;

/**
 * Created by Mateusz on 2016-10-06.
 */

public class ListEntryAdapter  implements JsonDeserializer<List<Entry>> {

    private Gson gson = new Gson();

    @Override
    public List<Entry> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List<Entry> links = new ArrayList<Entry>();
        if (json.isJsonArray())
            for (JsonElement e : json.getAsJsonArray())
                links.add((Entry) context.deserialize(e, Entry.class));
        else if (json.isJsonObject())
            links.add((Entry) context.deserialize(json, Entry.class));
        else
            throw new RuntimeException("Unexpected JSON type: " + json.getClass());

        return links;

    }
}
