package net.egordmitriev.watchall.pojo.watchall;

import android.os.Parcel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class WatchlistModel extends DetailedModel<WatchlistModel.Base, WatchlistModel.Detail> {
    public static final int TYPE = 15;

    public WatchlistModel() {
        super(TYPE);
    }

    public WatchlistModel(WatchlistModel.Base base, WatchlistModel.Detail detail) {
        super(TYPE, base, detail);
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        if (base.is_local) {
            //TODO: Grab from the db the response will also be different because its local
        } else {
            WatchAllServiceHelper.sService.viewWatchlist(this.id).enqueue(getDetailCallback(callback));
        }
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            detail = new Detail();
            detail.id = base.id;
            detail.description = data.get("description").getAsString();
            detail.list_contents = new ArrayList<>();
            JsonArray movies = data.get("movies").getAsJsonArray();
            JsonArray series = data.get("series").getAsJsonArray();
            JsonArray animes = data.get("animes").getAsJsonArray();
            for (JsonElement element : movies) {
                JsonObject object = element.getAsJsonObject();
                object.addProperty("model_type", MovieModel.TYPE);
                detail.list_contents.add(object);
            }
            for (JsonElement element : series) {
                JsonObject object = element.getAsJsonObject();
                object.addProperty("model_type", SerieModel.TYPE);
                detail.list_contents.add(object);
            }
            for (JsonElement element : animes) {
                JsonObject object = element.getAsJsonObject();
                object.addProperty("model_type", AnimeModel.TYPE);
                detail.list_contents.add(object);
            }
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a movie model.\n"+data.toString());
        }
    }

    public static class Base extends BaseModel {
        @SerializedName("title")
        public String title;

        @SerializedName("creator")
        public int creator;

        @SerializedName("icon")
        public String custom_icon;
        @SerializedName("color")
        public int icon_color;

        @SerializedName("modified")
        public Date modified;
        @SerializedName("created")
        public Date created;
        @SerializedName("is_public")
        public boolean is_public;

        public boolean is_local = false;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.title);
            dest.writeInt(this.creator);
            dest.writeString(this.custom_icon);
            dest.writeInt(this.icon_color);
            dest.writeLong(modified != null ? modified.getTime() : -1);
            dest.writeLong(created != null ? created.getTime() : -1);
            dest.writeByte(is_public ? (byte) 1 : (byte) 0);
            dest.writeByte(is_local ? (byte) 1 : (byte) 0);
        }

        public Base() {
            super(WatchlistModel.TYPE);
        }

        protected Base(Parcel in) {
            super(in);
            this.title = in.readString();
            this.creator = in.readInt();
            this.custom_icon = in.readString();
            this.icon_color = in.readInt();
            long tmpModified = in.readLong();
            this.modified = tmpModified == -1 ? null : new Date(tmpModified);
            long tmpCreated = in.readLong();
            this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
            this.is_public = in.readByte() != 0;
            this.is_local = in.readByte() != 0;
        }

        public static final Creator<Base> CREATOR = new Creator<Base>() {
            @Override
            public Base createFromParcel(Parcel source) {
                return new Base(source);
            }

            @Override
            public Base[] newArray(int size) {
                return new Base[size];
            }
        };
    }

    public static class Detail extends BaseModel {
        @SerializedName("description")
        public String description;

        @SerializedName("list_contents")
        public List<JsonObject> list_contents;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.description);
            if (list_contents == null) {
                dest.writeInt(-1);
            } else {
                String[] tempList_contents = new String[list_contents.size()];
                for (int i = 0; i < list_contents.size(); i++) {
                    tempList_contents[i] = list_contents.get(i).toString();
                }
                dest.writeInt(list_contents.size());
                dest.writeStringArray(tempList_contents);
            }
        }

        public Detail() {
            super(WatchlistModel.TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.description = in.readString();
            if (in.readInt() != -1) {
                String[] tempList_contents = in.createStringArray();
                list_contents = new ArrayList<>(tempList_contents.length);
                JsonParser parser = new JsonParser();
                for (String tempList_content : tempList_contents) {
                    list_contents.add(parser.parse(tempList_content).getAsJsonObject());
                }
            }
        }

        public static final Creator<Detail> CREATOR = new Creator<Detail>() {
            @Override
            public Detail createFromParcel(Parcel source) {
                return new Detail(source);
            }

            @Override
            public Detail[] newArray(int size) {
                return new Detail[size];
            }
        };
    }

    @Override
    protected ClassLoader getBaseLoader() {
        return WatchlistModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return WatchlistModel.Detail.class.getClassLoader();
    }

    protected WatchlistModel(Parcel in) {
        super(in);
    }

    public static final Creator<WatchlistModel> CREATOR = new Creator<WatchlistModel>() {
        @Override
        public WatchlistModel createFromParcel(Parcel source) {
            return new WatchlistModel(source);
        }

        @Override
        public WatchlistModel[] newArray(int size) {
            return new WatchlistModel[size];
        }
    };
}
