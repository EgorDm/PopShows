package net.egordmitriev.popshows.pojo.data;

import android.content.Context;
import android.os.Parcel;

import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.CardedModel;
import net.egordmitriev.popshows.ui.widgets.cards.SectionCard;
import net.egordmitriev.popshows.utils.Utils;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class Section extends CardedModel {
    public static final int TYPE = 17;

    public String sectionTitle;

    public Section() {
        super(TYPE);
    }
    public Section(String sectionTitle) {
        super(TYPE);
        this.sectionTitle = Utils.capitalize(sectionTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.sectionTitle);
    }

    protected Section(Parcel in) {
        super(in);
        this.sectionTitle = in.readString();
    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel source) {
            return new Section(source);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        return new SectionCard(context, this);
    }
}