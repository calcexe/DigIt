package pl.calc_exe.wykop.model.rest.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.calc_exe.wykop.model.domain.Link;

/**
 * Created by Mateusz on 2016-09-01.
 */
public class ListLinkAdapter implements JsonDeserializer<List<Link>> {

    private Gson gson = new Gson();

    @Override
    public List<Link> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List<Link> links = new ArrayList<Link>();
        if (json.isJsonArray())
            for (JsonElement e : json.getAsJsonArray())
                links.add((Link) context.deserialize(e, Link.class));
        else if (json.isJsonObject())
            links.add((Link) context.deserialize(json, Link.class));
        else
            throw new RuntimeException("Unexpected JSON type: " + json.getClass());

        return links;

    }

}
