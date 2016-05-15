package net.egordmitriev.watchall.ui.modelviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.PersonModel;
import net.egordmitriev.watchall.pojo.tmdb.SeasonModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;
import net.egordmitriev.watchall.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.watchall.ui.modelviews.base.ALargeModelView;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.MediaUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class SerieView extends ALargeModelView<SerieModel> {
    public static MediaCard onCreateCard(Context context, SerieModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type, (prefix != null) ? prefix + item.getTitle() : item.getTitle(), getSubtitle(context, item),
                    item.getPoster(true), getRating(item), null);
        } else {
            return new MediaCardLarge(context, item.type, item.getTitle(),
                    getSubtitle(context, item),
                    item.getPoster(false), null);
        }
    }

    public static String getSubtitle(Context context, SerieModel item) {
        if (item.base.genre_ids != null) {
            return MediaUtils.convertGenreString(context, item.base.genre_ids);
        }
        return (item.base.first_air_date != null) ? APIUtils.sFriendlyDateFormat.format(item.base.first_air_date) : "";
    }

    public static float getRating(SerieModel model) {
        if (model.base.vote_average == -1f) {
            return -1f;
        } else {
            return MediaUtils.convertMediaVote(model.base.vote_average);
        }
    }

    private static SerieView sInstance;

    public static SerieView getInstance() {
        if (sInstance == null) {
            sInstance = new SerieView();
        }
        return sInstance;
    }

    @Override
    protected String getBackdrop(SerieModel item) {
        return APIUtils.getTMDBImageUrl(item.base.backdrop_path, APIUtils.Queries.Image.SIZE_MEDIUM_BACKDROP);
    }

    @Override
    protected void setupMainBase(final BaseActivity activity, final SerieModel item, final ViewGroup view) {
        super.setupMainBase(activity, item, view);
        ViewGroup badgeGroup = (ViewGroup) view.findViewById(R.id.detail_badge_container);
        addInformationBadge(((MediaDetailActivity) activity), badgeGroup);

        view.findViewById(R.id.detail_content_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MediaDetailActivity) activity).openPage(MediaDetailActivity.PAGE_ABOUT);
            }
        });
    }

    @Override
    public void setupLiteMain(BaseActivity activity, SerieModel item, ViewGroup view, boolean hasDetail) {
        super.setupLiteMain(activity, item, view, hasDetail);

        TextView summaryView = (TextView) view.findViewById(R.id.detail_content_main);
        summaryView.setText(item.getDescription());

        ViewGroup badgeGroup = (ViewGroup) view.findViewById(R.id.detail_badge_container);
        addRatingBadge(activity.getLayoutInflater(), badgeGroup, item.base.vote_average, item.base.vote_count + " votes", 0);
        if (item.base.genre_ids != null && item.base.genre_ids.length > 0)
            addGenreBadge(activity.getLayoutInflater(), badgeGroup, MediaUtils.getGenreName(activity, item.base.genre_ids[0]), MediaUtils.getGenreIconId(item.base.genre_ids[0]), 0);
    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, final SerieModel item, final ViewGroup view, boolean hasDetail) {
        super.setupMainDetail(activity, item, view, hasDetail);
        if (!hasDetail) {
            return;
        }

        ((TextView) view.findViewById(R.id.detail_subtitle)).setText(getSubtitle(activity, item));

        ViewGroup extraContainer = (ViewGroup) view.findViewById(R.id.detail_footer_data);

        SeasonModel[] seasons = SeasonModel.createArray(item.detail.seasons);
        addDataCardRow(activity, extraContainer, seasons, R.string.seasons, MediaDetailActivity.PAGE_SEASONS, true);
        PersonModel[] persons = PersonModel.createArray(item.detail.credits.cast);
        addDataCardRow(activity, extraContainer, persons, R.string.cast, MediaDetailActivity.PAGE_CAST, true);
        SerieModel[] series = SerieModel.createArray(item.detail.similar.results);
        addDataCardRow(activity, extraContainer, series, R.string.similar, MediaDetailActivity.PAGE_SIMILAR, true);
    }

    @Override
    protected String getRuntime(SerieModel item) {
        if (item.detail != null && item.detail.runtime != null && item.detail.runtime.length > 0) {
            return (item.detail.runtime.length == 1)
                    ? item.detail.runtime[0] + " - " + item.detail.runtime[item.detail.runtime.length - 1]
                    : Integer.toString(item.detail.runtime[0]);
        } else {
            return null;
        }
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, final SerieModel item, final ViewGroup view) {
        if (item.base.genre_ids != null && item.base.genre_ids.length > 0) {
            ((ViewStub) view.findViewById(R.id.detail_about_genre_stub)).inflate();
        }
        ViewStub infoStub = (ViewStub) view.findViewById(R.id.detail_about_info_stub);
        infoStub.setLayoutResource(R.layout.view_detail_about_info_serie);
        infoStub.inflate();
    }

    @Override
    protected void setupAboutBase(final BaseActivity activity, final SerieModel item, final ViewGroup view) {
        super.setupAboutBase(activity, item, view);

        ((TextView) view.findViewById(R.id.detail_about_info_first_aired))
                .setText((item.base.first_air_date != null) ? APIUtils.sFriendlyDateFormat.format(item.base.first_air_date) : "Unknown");

        ViewGroup group = (ViewGroup) view.findViewById(R.id.detail_about_genre_container);
        if (group != null) {
            createGenreList(
                    activity,
                    MediaUtils.getPredefinedCategoryList(activity, MovieModel.TYPE, item.base.genre_ids),
                    group);
        }
    }

    @Override
    protected void setupAboutDetail(final BaseActivity activity, final SerieModel item, final ViewGroup view, boolean hasDetail) {
        super.setupAboutDetail(activity, item, view, hasDetail);
        if (!hasDetail) {
            return;
        }
        View crewView = ((ViewStub) view.findViewById(R.id.detail_about_crew_stub)).inflate();
        displayTextContainer((TextView) crewView.findViewById(R.id.detail_about_crew_actors_content),
                item.detail.credits.getActors(5));
        displayTextContainer((TextView) crewView.findViewById(R.id.detail_about_crew_directors_content),
                item.detail.credits.getCrew(MediaUtils.JOB_DIRECTOR, 3));
        displayTextContainer((TextView) crewView.findViewById(R.id.detail_about_crew_producer_content),
                item.detail.credits.getCrew(MediaUtils.JOB_PRODUCER, 3));
        displayTextContainer((TextView) crewView.findViewById(R.id.detail_about_crew_editors_content),
                item.detail.credits.getCrew(MediaUtils.JOB_EDITOR, 3));
        displayTextContainer((TextView) crewView.findViewById(R.id.detail_about_crew_homepage_content),
                item.detail.homepage);

        ((TextView) view.findViewById(R.id.detail_about_info_last_aired))
                .setText((item.detail.last_air_date != null) ? APIUtils.sFriendlyDateFormat.format(item.detail.last_air_date) : "Unknown");
        ((TextView) view.findViewById(R.id.detail_about_info_episode_runtime))
                .setText((getRuntime(item) != null) ? getRuntime(item) + " minutes" : "Unknown");
        ((TextView) view.findViewById(R.id.detail_about_info_origin_country))
                .setText((item.detail.origin_country != null && item.detail.origin_country.length > 0) ? item.detail.origin_country[0] : "Unknown");
    }

    @Override
    protected String getDescriptionTitle() {
        return null;
    }
}
