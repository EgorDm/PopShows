package net.egordmitriev.popshows.ui.modelviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.anilist.CharacterModel;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.popshows.ui.modelviews.base.ASmallModelView;
import net.egordmitriev.popshows.ui.widgets.cards.MediaCardSplit;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class CharacterView extends ASmallModelView<CharacterModel> {
    public static MediaCard onCreateCard(Context context, CharacterModel item, String prefix, boolean small) {
        if (item.base.actor != null && item.base.actor.length > 0) {
            return new MediaCardSplit(context, item.type,
                    item.getTitle(), item.base.role, item.getPoster(false),
                    ((item.base.actor[0].name_last != null) ? item.base.actor[0].name_first + " " +item.base.actor[0].name_last : item.base.actor[0].name_first),
                    item.base.actor[0].language, item.base.actor[0].poster);
        } else {
            return new MediaCardSplit(context, item.type,
                    item.getTitle(), item.base.role, item.getPoster(true),
                    "Unknown", "Unknown", null);
        }
    }

    private static CharacterView sInstance;

    public static CharacterView getInstance() {
        if(sInstance == null) {
            sInstance = new CharacterView();
        }
        return sInstance;
    }

    @Override
    public void inflateMain(LayoutInflater inflater, final CharacterModel item, final ViewGroup view) {
        inflater.inflate(R.layout.view_detail_header_small, view, true);
        inflater.inflate(R.layout.view_detail_content, view, true);
    }

    @Override
    protected void setupMainBase(BaseActivity activity, CharacterModel item, ViewGroup view) {
        super.setupMainBase(activity, item, view);
    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, CharacterModel item, ViewGroup view, boolean hasDetail) {
        if (item.detail.name_japanese != null && !item.detail.name_japanese.isEmpty()) {
            ((TextView) view.findViewById(R.id.detail_subtitle))
                    .setText("Native name: " + item.detail.name_japanese);
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
    protected void inflateAbout(LayoutInflater inflater, CharacterModel item, ViewGroup view) {

    }

    @Override
    protected void setupAboutBase(BaseActivity activity, CharacterModel item, ViewGroup view) {
        ((TextView) view.findViewById(R.id.detail_about_summary_title)).setText(getDescriptionTitle());
    }

    @Override
    protected void setupAboutDetail(BaseActivity activity, CharacterModel item, ViewGroup view, boolean hasDetail) {
        super.setupAboutDetail(activity, item, view, hasDetail);
        ((TextView) view.findViewById(R.id.detail_about_summary)).setText(item.getDescription());
    }

    @Override
    protected String getDescriptionTitle() {
        return "Biography";
    }
}
