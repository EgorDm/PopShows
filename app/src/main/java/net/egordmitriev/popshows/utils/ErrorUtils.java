package net.egordmitriev.popshows.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.api.base.APIError;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by EgorDm on 4/1/2016.
 */
public class ErrorUtils {

    public static APIError checkError(Response<?> response) {
        if (response.isSuccessful()) {
            return null;
        }
        APIError error = new APIError();
        try {
            try {
                //TODO: fix serie season crash and serie time
                JsonObject object = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                if (object.has("status_code")) {
                    return new APIError(object.get("status_code").getAsInt(), object.get("status_message").getAsString());
                } else if (object.has("success")) {
                    JsonObject data = object.get("data").getAsJsonObject();
                    return new APIError(data.get("code").getAsInt(), data.get("message").getAsString());
                } else if(object.has("status")) {
                    return new APIError(object.get("status").getAsInt(), object.get("error_message").getAsString());
                }
            } catch (JsonParseException | IllegalStateException e) {
                Logger.e(response.errorBody().string());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }

}
