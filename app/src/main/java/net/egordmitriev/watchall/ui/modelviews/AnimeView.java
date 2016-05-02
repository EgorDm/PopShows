package net.egordmitriev.watchall.ui.modelviews;

import android.content.Context;

import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.ui.modelviews.base.AModelView;
import net.egordmitriev.watchall.utils.MediaUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class AnimeView extends AModelView {
    public static MediaCard onCreateCard(Context context, AnimeModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type, (prefix != null) ? prefix + item.base.title_english : item.base.title_english, getSubtitle(item),
                    item.getPoster(true), getRating(item), null);
        } else {
            return new MediaCardLarge(context, item.type, item.base.title_english,
                    getSubtitle( item),
                    item.getPoster(false), null);
        }
    }

    public static String getSubtitle(AnimeModel item) {
        StringBuilder ret = new StringBuilder();
        if (item.base.total_episodes > 0) {
            ret.append("Episodes: ").append(item.base.total_episodes).append(", ");
        }
        ret.append("Type: ").append(item.base.type_anime);
        return ret.toString();
    }

    public static float getRating(AnimeModel item) {
        if (item.base.average_score == -1f) {
            return -1f;
        } else {
            return MediaUtils.convertAnimeVote(item.base.average_score);
        }
    }
}
