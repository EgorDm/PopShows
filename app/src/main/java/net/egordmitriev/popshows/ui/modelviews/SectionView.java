package net.egordmitriev.popshows.ui.modelviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.pojo.data.Section;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SectionView extends RecyclerView.ViewHolder {

    private TextView mTitleView;

    public SectionView(View itemView) {
        super(itemView);
        mTitleView = (TextView) itemView.findViewById(R.id.media_section_title);
    }

    public void bind(Section data) {
        mTitleView.setText(data.sectionTitle);
    }
}