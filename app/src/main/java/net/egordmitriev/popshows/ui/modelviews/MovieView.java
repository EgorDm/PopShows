package net.egordmitriev.popshows.ui.modelviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import net.egordmitriev.popshows.MainApplication;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.pojo.tmdb.PersonModel;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.popshows.ui.modelviews.base.ALargeModelView;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.MediaUtils;

import java.text.NumberFormat;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class MovieView extends ALargeModelView<MovieModel> {
    public static MediaCard onCreateCard(Context context, MovieModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type, (prefix != null) ? prefix + item.getTitle() : item.getTitle(), getGenre(context, item),
                    item.getPoster(true), getRating(item), null);
        } else {
            return new MediaCardLarge(context, item.type, item.getTitle(),
                    getGenre(context, item),
                    item.getPoster(false), null);
        }
    }

    public static String getGenre(Context context, MovieModel item) {
        if (item.base.genre_ids != null) {
            return MediaUtils.convertGenreString(context, item.base.genre_ids);
        }
        return getSubtitle(item);
    }

    public static String getSubtitle(MovieModel item) {
        return (item.base.release_date != null)
                ? APIUtils.sFriendlyDateFormat.format(item.base.release_date) : "Unknown release date";
    }

    public static float getRating(MovieModel model) {
        if (model.base.vote_average == -1f) {
            return -1f;
        } else {
            return MediaUtils.convertMediaVote(model.base.vote_average);
        }
    }

    private static MovieView sInstance;

    public static MovieView getInstance() {
        if (sInstance == null) {
            sInstance = new MovieView();
        }
        return sInstance;
    }

    @Override
    protected String getBackdrop(MovieModel item) {
        return APIUtils.getTMDBImageUrl(item.base.backdrop_path, APIUtils.Queries.Image.SIZE_MEDIUM_BACKDROP);
    }

    @Override
    protected void setupMainBase(final BaseActivity activity, final MovieModel item, final ViewGroup view) {
        super.setupMainBase(activity, item, view);
        ViewGroup badgeGroup = (ViewGroup) view.findViewById(R.id.detail_badge_container);
        addInformationBadge(((MediaDetailActivity) activity), badgeGroup);

        view.findViewById(R.id.detail_content_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MediaDetailActivity) activity).openPage(MediaDetailActivity.PAGE_ABOUT);
            }
        });
        ((TextView) view.findViewById(R.id.detail_subtitle)).setText(getSubtitle(item));
    }

    @Override
    public void setupLiteMain(BaseActivity activity, MovieModel item, ViewGroup view, boolean hasDetail) {
        super.setupLiteMain(activity, item, view, hasDetail);

        TextView summaryView = (TextView) view.findViewById(R.id.detail_content_main);
        summaryView.setText(item.getDescription());
        ViewGroup badgeGroup = (ViewGroup) view.findViewById(R.id.detail_badge_container);

        addRatingBadge(activity.getLayoutInflater(), badgeGroup, item.base.vote_average,
                String.format(activity.getString(R.string.rating_votes), item.base.vote_count), 0);
        if (item.base.genre_ids != null && item.base.genre_ids.length > 0)
            addGenreBadge(activity.getLayoutInflater(), badgeGroup, MediaUtils.getGenreName(activity, item.base.genre_ids[0]), MediaUtils.getGenreIconId(item.base.genre_ids[0]), 0);
    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, final MovieModel item, final ViewGroup view, boolean hasDetail) {
        super.setupMainDetail(activity, item, view, hasDetail);
        if (!hasDetail) {
            return;
        }

        ViewGroup extraContainer = (ViewGroup) view.findViewById(R.id.detail_footer_data);

        PersonModel[] persons = PersonModel.createArray(item.detail.credits.cast);
        addDataCardRow(activity, extraContainer, persons, R.string.cast, MediaDetailActivity.PAGE_CAST, true);
        MovieModel[] movies = MovieModel.createArray(item.detail.similar.results);
        addDataCardRow(activity, extraContainer, movies, R.string.similar, MediaDetailActivity.PAGE_SIMILAR, true);
    }

    @Override
    protected String getRuntime(MovieModel item) {
        return String.format(MainApplication.getAppContext().getString(R.string.duration_minutes), item.detail.runtime);
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, final MovieModel item, final ViewGroup view) {
        if (item.base.genre_ids != null && item.base.genre_ids.length > 0) {
            ((ViewStub) view.findViewById(R.id.detail_about_genre_stub)).inflate();
        }
        ViewStub infoStub = (ViewStub) view.findViewById(R.id.detail_about_info_stub);
        infoStub.setLayoutResource(R.layout.view_detail_about_info_movie);
        infoStub.inflate();
    }

    @Override
    protected void setupAboutBase(final BaseActivity activity, final MovieModel item, final ViewGroup view) {
        super.setupAboutBase(activity, item, view);

        ((TextView) view.findViewById(R.id.detail_about_info_release_date))
                .setText((item.base.release_date != null) ? APIUtils.sFriendlyDateFormat.format(item.base.release_date) : "Unknown");
        ((TextView) view.findViewById(R.id.detail_about_info_classification))
                .setText("Unclassified");

        ViewGroup group = (ViewGroup) view.findViewById(R.id.detail_about_genre_container);
        if (group != null) {
            createGenreList(
                    activity,
                    MediaUtils.getPredefinedCategoryList(MainApplication.getAppContext(), MovieModel.TYPE, item.base.genre_ids),
                    group);
        }
    }

    @Override
    protected void setupAboutDetail(final BaseActivity activity, final MovieModel item, final ViewGroup view, boolean hasDetail) {
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

        ((TextView) view.findViewById(R.id.detail_about_info_revenue))
                .setText((item.detail.revenue != 0) ? NumberFormat.getIntegerInstance().format(item.detail.revenue) + "$" : "Unknown");
        ((TextView) view.findViewById(R.id.detail_about_info_budget))
                .setText((item.detail.budget != 0) ? NumberFormat.getIntegerInstance().format(item.detail.budget) + "$" : "Unknown");
    }

    @Override
    protected String getDescriptionTitle() {
        return "Sypnose";
    }
}
