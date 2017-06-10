package net.egordmitriev.popshows.utils;

import net.egordmitriev.popshows.api.base.APIError;

/**
 * Created by EgorDm on 4/3/2016.
 */
public interface DataCallback<T> {
    void success(T data);
    void failure(APIError error);
}
