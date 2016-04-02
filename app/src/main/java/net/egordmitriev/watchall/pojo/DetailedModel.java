package net.egordmitriev.watchall.pojo;

import android.os.Parcel;

import com.google.gson.JsonObject;

import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by EgorDm on 4/2/2016.
 */
public abstract class DetailedModel<B extends BaseModel, D extends BaseModel> extends BaseModel {

    public interface DetailCallback {
        void success();
        void failure(APIError error);
    }

    public B base;
    public D detail;

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
    protected Callback<JsonObject> getDetailCallback(final DetailCallback callback) {
        return new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if(error != null) {
                    callback.failure(error);
                }
                populateModel(response.body());
                callback.success();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        };
    }


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
