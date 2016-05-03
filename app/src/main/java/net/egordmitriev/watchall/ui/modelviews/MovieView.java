package net.egordmitriev.watchall.ui.modelviews;

import android.content.Context;

import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.ui.modelviews.base.AModelView;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.MediaUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class MovieView extends AModelView {
    public static MediaCard onCreateCard(Context context, MovieModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type, (prefix != null) ? prefix + item.base.title : item.base.title, getGenre(context, item),
                    item.getPoster(true), getRating(item), null);
        } else {
            return new MediaCardLarge(context, item.type, item.base.title,
                    getGenre(context, item),
                    item.getPoster(false), null);
        }
    }

    public static String getGenre(Context context, MovieModel item) {
        if (item.base.genre_ids != null) {
            return MediaUtils.convertGenreString(context, item.base.genre_ids);
        }
        return getSubtitle(item);
    }

    public static String getSubtitle(MovieModel item) {
        return (item.base.release_date != null)
                ? APIUtils.sFriendlyDateFormat.format(item.base.release_date) : "Unknown release date";
    }

    public static float getRating(MovieModel model) {
        if (model.base.vote_average == -1f) {
            return -1f;
        } else {
            return MediaUtils.convertMediaVote(model.base.vote_average);
        }
    }
}
