package net.egordmitriev.popshows.pojo;

import android.content.Context;
import android.os.Parcel;

import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;

/**
 * Created by EgorDm on 5/4/2016.
 */
public abstract class CardedModel extends BaseModel {

    public CardedModel(int type) {
        super(type);
    }

    public CardedModel(Parcel in) {
        super(in);
    }

    public abstract MediaCard onCreateCard(Context context, String prefix, boolean small);
}
