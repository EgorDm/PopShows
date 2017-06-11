package net.egordmitriev.popshows.ui.modelviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.pojo.tmdb.SerieModel;
import net.egordmitriev.popshows.pojo.watchall.WatchlistModel;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.modelviews.base.AModelView;
import net.egordmitriev.popshows.ui.widgets.cards.MediaCardGeneratedThumbnail;
import net.egordmitriev.popshows.utils.APIUtils;

import java.util.List;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class WatchlistView extends AModelView<WatchlistModel> {
    public static MediaCard onCreateCard(Context context, WatchlistModel item, String prefix, boolean small) {
        return new MediaCardGeneratedThumbnail(context, item.type, item.base.title, getSubtitle(item), item.base.custom_icon, item.base.color);
    }

    public static String getSubtitle(WatchlistModel item) {
        return "";
    }

    private static WatchlistView sInstance;

    public static WatchlistView getInstance() {
        if (sInstance == null) {
            sInstance = new WatchlistView();
        }
        return sInstance;
    }

    @Override
    public int getActivityLayout() {
        return 0;
    }

    @Override
    protected void inflateMain(LayoutInflater inflater, WatchlistModel item, ViewGroup view) {
    }

    @Override
    public void setupMain(BaseActivity activity, WatchlistModel item, ViewGroup view) {
    }

    @Override
    protected void setupMainBase(BaseActivity activity, WatchlistModel item, ViewGroup view) {
    }

    @Override
    protected void setupMainDetail(BaseActivity activity, WatchlistModel item, ViewGroup view, boolean hasDetail) {
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, WatchlistModel item, ViewGroup view) {
    }

    @Override
    protected String getDescriptionTitle() {
        return "Description";
    }

    public static DetailedModel[] parseListContents(List<JsonObject> contents) {
        if (contents == null || contents.size() <= 0) return new DetailedModel[0];
        DetailedModel[] ret = new DetailedModel[contents.size()];
        for (int i = 0; i < contents.size(); i++) {
            switch (contents.get(i).get("model_type").getAsInt()) {
                case MovieModel.TYPE:
                    ret[i] = new MovieModel(APIUtils.sGlobalParser.fromJson(contents.get(i), MovieModel.Base.class), null);
                    break;
                case SerieModel.TYPE:
                    ret[i] = new SerieModel(APIUtils.sGlobalParser.fromJson(contents.get(i), SerieModel.Base.class), null);
                    break;
                case AnimeModel.TYPE:
                    try {
                        ret[i] = new AnimeModel(APIUtils.sGlobalParser.fromJson(contents.get(i), AnimeModel.Base.class), null);
                    } catch (JsonSyntaxException e) {
                        Logger.e(e.getMessage());
                        Logger.json(APIUtils.sGlobalParser.toJson(contents.get(i)));
                    }
                    break;
                default:
                    break;
            }
        }
        return ret;
    }
}
