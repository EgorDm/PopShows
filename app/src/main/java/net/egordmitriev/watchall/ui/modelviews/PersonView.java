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
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.PersonModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;
import net.egordmitriev.watchall.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.watchall.ui.modelviews.base.ASmallModelView;
import net.egordmitriev.watchall.utils.APIUtils;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class PersonView extends ASmallModelView<PersonModel> {
    public static MediaCard onCreateCard(Context context, PersonModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type,
                    item.getTitle(),
                    item.base.character,
                    item.getPoster(true),
                    -1f, null);
        } else {
            return new MediaCardLarge(context, item.type,
                    item.getTitle(),
                    item.base.character,
                    item.getPoster(false), null);
        }
    }

    private static PersonView sInstance;

    public static PersonView getInstance() {
        if (sInstance == null) {
            sInstance = new PersonView();
        }
        return sInstance;
    }

    @Override
    protected void setupMainBase(BaseActivity activity, PersonModel item, ViewGroup view) {
        super.setupMainBase(activity, item, view);
    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, PersonModel item, ViewGroup view, boolean hasDetail) {
        super.setupMainDetail(activity, item, view, hasDetail);
        if (!hasDetail) return;

        ((TextView) view.findViewById(R.id.detail_subtitle))
                .setText("Birthday: " + ((item.detail.birthday != null) ? APIUtils.sFriendlyDateFormat.format(item.detail.birthday) : "Unknown"));

        TextView pobView = (TextView) view.findViewById(R.id.detail_tretitle);
        if (item.detail.place_of_birth != null && !item.detail.place_of_birth.isEmpty()) {
            pobView.setText("Place of birth: " + item.detail.place_of_birth);
        } else {
            pobView.setVisibility(View.GONE);
        }


        TextView hpView = (TextView) view.findViewById(R.id.detail_website_title);
        if (item.detail.homepage != null && !item.detail.homepage.isEmpty()) {
            hpView.setText("Website: " + item.detail.homepage);
        } else {
            hpView.setVisibility(View.GONE);
        }

        TextView summaryView = (TextView) view.findViewById(R.id.detail_content_main);
        summaryView.setText(item.getDescription());
        view.findViewById(R.id.detail_content_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MediaDetailActivity) activity).openPage(MediaDetailActivity.PAGE_ABOUT);
            }
        });

        ViewGroup extraContainer = (ViewGroup) view.findViewById(R.id.detail_footer_data);
        MovieModel[] movies = MovieModel.createArray(item.detail.movie_credits.cast);
        addDataCardRow(activity, extraContainer, movies, R.string.actor_in_movies, MediaDetailActivity.PAGE_MOVIES, true);
        SerieModel[] series = SerieModel.createArray(item.detail.tv_credits.cast);
        addDataCardRow(activity, extraContainer, series, R.string.actor_in_series, MediaDetailActivity.PAGE_SERIES, true);
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, PersonModel item, ViewGroup view) {

    }

    @Override
    protected void setupAboutBase(BaseActivity activity, PersonModel item, ViewGroup view) {
        ((TextView) view.findViewById(R.id.detail_about_summary_title)).setText(getDescriptionTitle());

    }

    @Override
    protected void setupAboutDetail(BaseActivity activity, PersonModel item, ViewGroup view, boolean hasDetail) {
        super.setupAboutDetail(activity, item, view, hasDetail);
        ((TextView) view.findViewById(R.id.detail_about_summary)).setText(item.getDescription());
    }

    @Override
    protected String getDescriptionTitle() {
        return "Biography";
    }

}
