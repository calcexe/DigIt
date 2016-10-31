package pl.calc_exe.wykop.model.rest.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import pl.calc_exe.wykop.model.domain.UserVote;

/**
 * Created by Mateusz on 2016-09-21.
 */

//Date example: 2016-09-21 09:50:59
public class UserVoteAdapter implements JsonDeserializer<UserVote> {
    @Override
    public UserVote deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String value = json.getAsString();
        UserVote userVote;

        if (value.equals("dig"))
            userVote = UserVote.DIG;
        else if (value.equals("bury"))
            userVote = UserVote.BURY;
        else
            userVote = UserVote.NONE;

        return userVote;
    }
}
