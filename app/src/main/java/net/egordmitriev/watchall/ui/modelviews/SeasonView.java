package net.egordmitriev.watchall.ui.modelviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.watchall.pojo.tmdb.EpisodeModel;
import net.egordmitriev.watchall.pojo.tmdb.SeasonModel;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;
import net.egordmitriev.watchall.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.watchall.ui.modelviews.base.ASmallModelView;
import net.egordmitriev.watchall.utils.APIUtils;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SeasonView extends ASmallModelView<SeasonModel> {
    public static MediaCard onCreateCard(Context context, SeasonModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type,
                    item.getTitle(),
                    (item.base.air_date != null) ? APIUtils.sFriendlyDateFormat.format(item.base.air_date) : "",
                    item.getPoster(true),
                    -1f, null);
        } else {
            return new MediaCardLarge(context, item.type,
                    item.getTitle(),
                    (item.base.air_date != null) ? APIUtils.sFriendlyDateFormat.format(item.base.air_date) : "",
                    item.getPoster(false), null);
        }
    }

    private static SeasonView sInstance;

    public static SeasonView getInstance() {
        if (sInstance == null) {
            sInstance = new SeasonView();
        }
        return sInstance;
    }

    @Override
    public void inflateMain(LayoutInflater inflater, SeasonModel item, ViewGroup view) {
        inflater.inflate(R.layout.view_detail_header_small, view, true);
        inflater.inflate(R.layout.view_detail_footer, view, true);
    }

    @Override
    protected void setupMainBase(final BaseActivity activity, SeasonModel item, ViewGroup view) {
        super.setupMainBase(activity, item, view);

        ((TextView) view.findViewById(R.id.detail_subtitle))
                .setText("Air date: " + ((item.base.air_date != null) ? APIUtils.sFriendlyDateFormat.format(item.base.air_date) : "Unknown"));

        ((TextView) view.findViewById(R.id.detail_tretitle))
                .setText("Episodes: " + item.base.episode_count);


    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, SeasonModel item, ViewGroup view, boolean hasDetail) {
        super.setupMainDetail(activity, item, view, hasDetail);
        if (!hasDetail) return;

        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            View summaryContainer = activity.getLayoutInflater().inflate(R.layout.view_detail_content, view, false);
            TextView summaryView = (TextView) summaryContainer.findViewById(R.id.detail_content_main);
            summaryView.setText(item.getDescription());
            summaryContainer.findViewById(R.id.detail_content_summary).findViewById(R.id.detail_content_summary).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MediaDetailActivity) activity).openPage(MediaDetailActivity.PAGE_ABOUT);
                }
            });
            view.addView(summaryContainer, 1);
        }

        ViewGroup extraContainer = (ViewGroup) view.findViewById(R.id.detail_footer_data);
        if (item.detail.episodes != null && item.detail.episodes.length > 0) {
            LayoutInflater inflater = activity.getLayoutInflater();
            TextView episodesTitle = (TextView) inflater.inflate(R.layout.view_detail_content_title, extraContainer, false);
            extraContainer.addView(episodesTitle);
            episodesTitle.setText(R.string.episodes);
            inflater.inflate(R.layout.view_extra_alert_spoilers, extraContainer, true);

            int episodeNum = 1;
            for (EpisodeModel episode : item.detail.episodes) {
                View episodeView = inflater.inflate(R.layout.view_detail_footer_episode_item_layout, extraContainer, false);
                ((TextView) episodeView.findViewById(R.id.episode_title))
                        .setText((episode.name != null && !episode.name.isEmpty()) ? episode.name : "Episode " + episodeNum);
                ((TextView) episodeView.findViewById(R.id.episode_subtitle))
                        .setText((episode.air_date != null) ? "Air date: " + APIUtils.sFriendlyDateFormat.format(episode.air_date) : "Air date: Unknown");
                ((TextView) episodeView.findViewById(R.id.episode_content))
                        .setText((episode.sypnose != null && !episode.sypnose.isEmpty()) ? episode.sypnose : "No summary available.");
                extraContainer.addView(episodeView);
                episodeNum++;
            }
        }
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, SeasonModel item, ViewGroup view) {

    }

    @Override
    protected void setupAboutDetail(BaseActivity activity, SeasonModel item, ViewGroup view, boolean hasDetail) {
        //super.setupAboutDetail(activity, item, view, hasDetail);
    }

    @Override
    protected String getDescriptionTitle() {
        return "Sypnose";
    }

    @Override
    protected void setupAboutBase(BaseActivity activity, SeasonModel item, ViewGroup view) {
        //super.setupAboutBase(activity, item, view);
    }
}
