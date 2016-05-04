package net.egordmitriev.watchall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.adapters.CardsAdapter;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.data.Section;
import net.egordmitriev.watchall.ui.modelviews.SectionView;
import net.egordmitriev.watchall.ui.widgets.cards.SectionCard;

import java.util.List;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SectionedCardsAdapter extends CardsAdapter<MediaCard> {

    public static final int TYPE_SECTION = 2;

    public SectionedCardsAdapter(Context context, List<MediaCard> data) {
        super(context, data);
    }

    public boolean isSection(int position) {
        if(mDataList != null && position < mDataList.size()) {
            return mDataList.get(position).type == Section.TYPE;
        }
        return false;
    }

    @Override
    protected int getItemViewTypeNormal(int position) {
        if(mDataList.get(position).type == Section.TYPE) {
            return TYPE_SECTION;
        } else {
            return super.getItemViewTypeNormal(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_SECTION) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_section_layout, parent, false);
            return new SectionView(view);
        } else {
            return super.onCreateNormalViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof SectionView) {
            ((SectionView)viewHolder).bind(((SectionCard)mDataList.get(position)).section);
        } else {
            super.onBindNormalViewHolder(viewHolder, position);
        }
    }
}
