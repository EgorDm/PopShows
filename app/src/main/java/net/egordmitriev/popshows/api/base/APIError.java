package net.egordmitriev.popshows.api.base;

import com.orhanobut.logger.Logger;

/**
 * Created by EgorDm on 4/1/2016.
 */
public class APIError {
    private int errorCode;
    private String message;

    public APIError() {
    }

    public APIError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        Logger.e("An error occurred with code: "+errorCode+"\nMessage: "+message);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
