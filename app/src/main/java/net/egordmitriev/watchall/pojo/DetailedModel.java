package net.egordmitriev.watchall.pojo;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.JsonObject;

import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.utils.ErrorUtils;
import net.egordmitriev.watchall.utils.TypeRunnable;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by EgorDm on 4/2/2016.
 */
public abstract class DetailedModel<B extends BaseModel, D extends BaseModel> extends BaseModel {

    public static class DetailCallback {
        private ArrayList<TypeRunnable<Boolean>> callbacks;

        public DetailCallback(TypeRunnable<Boolean> callback) {
            this.callbacks = new ArrayList<>();
            callbacks.add(callback);
        }

        public void success() {
            for (TypeRunnable<Boolean> callback : callbacks) {
                callback.run(true);
            }
        }

        public void failure(APIError error) {
            //Mayby print error later
            for (TypeRunnable<Boolean> callback : callbacks) {
                callback.run(false);
            }
        }

        public void addCallback(TypeRunnable<Boolean> callback) {
            callbacks.add(callback);
        }
    }

    public B base;
    public D detail;
    private DetailCallback mCallback;

    public DetailedModel(int type) {
        super(type);
    }

    public DetailedModel(int type, B base, D detail) {
        super(type);
        this.id = base.id;
        this.base = base;
        this.detail = detail;
    }

    public abstract void requestDetail(DetailCallback callback);

    protected abstract void populateModel(JsonObject data);

    protected Callback<JsonObject> getDetailCallback(DetailCallback callback) {
        mCallback = callback;
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if (error != null) {
                    mCallback.failure(error);
                }
                populateModel(response.body());
                mCallback.success();
                mCallback = null;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.failure(new APIError(1337, t.getMessage()));
                mCallback = null;
            }
        };
    }

    public DetailCallback isRequestingDetail() {
        return mCallback;
    }

    public String getPoster(boolean small) {
        return null;
    }

    public abstract MediaCard onCreateCard(Context context, String prefix, boolean small);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.base, flags);
        dest.writeParcelable(this.detail, flags);
    }

    protected DetailedModel(Parcel in) {
        super(in);
        this.base = in.readParcelable(getBaseLoader());
        this.detail = in.readParcelable(getDetailLoader());
    }

    protected abstract ClassLoader getBaseLoader();

    protected abstract ClassLoader getDetailLoader();
}
