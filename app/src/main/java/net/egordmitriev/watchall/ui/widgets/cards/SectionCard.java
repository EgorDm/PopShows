package net.egordmitriev.watchall.ui.widgets.cards;

import android.content.Context;

import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.data.Section;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class SectionCard extends MediaCard {

    public Section section;


    public SectionCard(Context context, Section section) {
        super(context, -1, Section.TYPE, null, null, null);
        this.section = section;
    }

    @Override
    protected void init() {
        //Do nothing!
    }
}
