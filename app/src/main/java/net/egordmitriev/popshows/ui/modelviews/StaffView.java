package net.egordmitriev.popshows.ui.modelviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.popshows.pojo.anilist.StaffModel;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.popshows.ui.modelviews.base.ASmallModelView;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class StaffView extends ASmallModelView<StaffModel> {
    public static MediaCard onCreateCard(Context context, StaffModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type,
                    item.getTitle(),
                    item.base.role,
                    item.getPoster(true),
                    -1f, null);
        } else {
            return new MediaCardLarge(context, item.type,
                    item.getTitle(),
                    item.base.role,
                    item.getPoster(false), null);
        }
    }

    private static StaffView sInstance;

    public static StaffView getInstance() {
        if (sInstance == null) {
            sInstance = new StaffView();
        }
        return sInstance;
    }

    @Override
    public void inflateMain(LayoutInflater inflater, final StaffModel item, final ViewGroup view) {
        inflater.inflate(R.layout.view_detail_header_small, view, true);
        inflater.inflate(R.layout.view_detail_content, view, true);
    }

    @Override
    protected void setupMainBase(BaseActivity activity, StaffModel item, ViewGroup view) {
        super.setupMainBase(activity, item, view);

        ((TextView) view.findViewById(R.id.detail_subtitle))
                .setText("Language: " + ((item.base.language != null && !item.base.language.isEmpty()) ? item.base.language : "Unknown"));
    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, StaffModel item, ViewGroup view, boolean hasDetail) {
        if (item.detail.website != null && !item.detail.website.isEmpty()) {
            ((TextView) view.findViewById(R.id.detail_subtitle))
                    .setText("Website: " + item.detail.website);
        }

        TextView summaryView = (TextView) view.findViewById(R.id.detail_content_main);
        summaryView.setText(item.getDescription());
        view.findViewById(R.id.detail_content_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MediaDetailActivity) activity).openPage(MediaDetailActivity.PAGE_ABOUT);
            }
        });
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, StaffModel item, ViewGroup view) {

    }

    @Override
    protected void setupAboutBase(BaseActivity activity, StaffModel item, ViewGroup view) {
        ((TextView) view.findViewById(R.id.detail_about_summary_title)).setText(getDescriptionTitle());
    }

    @Override
    protected void setupAboutDetail(BaseActivity activity, StaffModel item, ViewGroup view, boolean hasDetail) {
        super.setupAboutDetail(activity, item, view, hasDetail);
        ((TextView) view.findViewById(R.id.detail_about_summary)).setText(item.getDescription());
    }

    @Override
    protected String getDescriptionTitle() {
        return "Biography";
    }
}
