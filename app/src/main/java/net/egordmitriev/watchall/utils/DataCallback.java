package net.egordmitriev.watchall.utils;

import net.egordmitriev.watchall.api.base.APIError;

/**
 * Created by EgorDm on 4/3/2016.
 */
public interface DataCallback<T> {
    void success(T data);
    void failure(APIError error);
}
