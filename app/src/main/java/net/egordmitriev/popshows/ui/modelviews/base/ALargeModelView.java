package net.egordmitriev.popshows.ui.modelviews.base;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import net.egordmitriev.popshows.MainApplication;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.pojo.data.Category;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.pojo.tmdb.SerieModel;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.popshows.ui.fragments.media.CategoryListFragment;
import net.egordmitriev.popshows.utils.TypeRunnable;

/**
 * Created by EgorDm on 5/3/2016.
 */
public abstract class ALargeModelView<T extends DetailedModel> extends AModelView<T> {

    @Override
    public int getActivityLayout() {
        return R.layout.activity_media_detail_large;
    }

    @Override
    public void setupActivity(final T item, final ViewGroup view) {
        super.setupActivity(item, view);

        if (isLite(item)) {
            scheduleDetailAction(item, new TypeRunnable<Boolean>() {
                @Override
                public void run(Boolean args) {
                    setupActivity(item, view);
                }
            });
        } else {
            final ImageView backdropView = (ImageView) view.findViewById(R.id.backdrop);
            Glide.with(MainApplication.getAppContext()).load(getBackdrop(item)).asBitmap().centerCrop().placeholder(R.drawable.nobackdrop_media).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(backdropView) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                    super.onResourceReady(bitmap, anim);
                    Glide.with(MainApplication.getAppContext()).load(getBackdrop(item)).centerCrop().into(backdropView);
                }
            });
        }
    }

    protected abstract String getBackdrop(T item);

    @Override
    protected void inflateMain(LayoutInflater inflater, final T item, final ViewGroup view) {
        inflater.inflate(R.layout.view_detail_header_large, view, true);
        inflater.inflate(R.layout.view_detail_content, view, true);
        inflater.inflate(R.layout.view_detail_footer, view, true);
    }

    @Override
    protected void setupMainBase(final BaseActivity activity, final T item, final ViewGroup view) {
        super.setupMainBase(activity, item, view);
        if (isLite(item)) {
            scheduleDetailAction(item, new TypeRunnable<Boolean>() {
                @Override
                public void run(Boolean args) {
                    setupLiteMain(activity, item, view, args);
                }
            });
        } else {
            setupLiteMain(activity, item, view, true);
        }
    }

    public void setupLiteMain(final BaseActivity activity, final T item, final ViewGroup view, boolean hasDetail) {
    }

    @Override
    protected void setupMainDetail(final BaseActivity activity, final T item, final ViewGroup view, boolean hasDetail) {
        super.setupMainDetail(activity, item, view, hasDetail);
        if (!hasDetail) {
            return;
        }
        ((TextView) view.findViewById(R.id.detail_extra_title)).setText(getRuntime(item));
    }

    protected abstract String getRuntime(T item);

    @Override
    protected void setupAboutBase(final BaseActivity activity, final T item, final ViewGroup view) {
        ((TextView) view.findViewById(R.id.detail_about_summary_title)).setText(getDescriptionTitle());
        if (isLite(item)) {
            scheduleDetailAction(item, new TypeRunnable<Boolean>() {
                @Override
                public void run(Boolean args) {
                    setupLiteAbout(activity, item, view, args);
                }
            });
        } else {
            setupLiteAbout(activity, item, view, true);
        }
    }

    protected void setupLiteAbout(BaseActivity activity, T item, ViewGroup view, boolean hasDetail) {
        ((TextView) view.findViewById(R.id.detail_about_summary)).setText(item.getDescription());
    }

    private static <T extends DetailedModel> boolean isLite(T item) {
        return (item instanceof MovieModel && ((MovieModel) item).lite) ||
                (item instanceof SerieModel && ((SerieModel) item).lite);
    }

    protected static void addGenreBadge(LayoutInflater inflater, ViewGroup targetView, String title, int icon) {
        addGenreBadge(inflater, targetView, title, icon, -1);
    }

    protected static void addGenreBadge(LayoutInflater inflater, ViewGroup targetView, String title, int icon, int prepend) {
        View badge = inflater.inflate(R.layout.view_badge_genre, targetView, false);
        ((TextView) badge.findViewById(R.id.badge_title)).setText(title);
        ((ImageView) badge.findViewById(R.id.badge_icon)).setImageResource(icon);
        if (prepend == -1) {
            targetView.addView(badge);
        } else {
            targetView.addView(badge, prepend);
        }
    }

    protected static void addRatingBadge(LayoutInflater inflater, ViewGroup targetView, float rating, String count) {
        addRatingBadge(inflater, targetView, rating, count, -1);
    }

    protected static void addRatingBadge(LayoutInflater inflater, ViewGroup targetView, float rating, String count, int prepend) {
        View badge = inflater.inflate(R.layout.view_badge_rating, targetView, false);
        ((TextView) badge.findViewById(R.id.badge_title)).setText(count);
        ((TextView) badge.findViewById(R.id.badge_rating_value)).setText(Double.toString((double) Math.round(rating * 10d) / 10d));
        ((RatingBar) badge.findViewById(R.id.badge_ratingbar)).setRating(rating / 2f);
        if (prepend == -1) {
            targetView.addView(badge);
        } else {
            targetView.addView(badge, prepend);
        }
    }

    protected static void addClassificationBadge(LayoutInflater inflater, ViewGroup targetView, String classification) {
        addClassificationBadge(inflater, targetView, classification, -1);
    }

    protected static void addClassificationBadge(LayoutInflater inflater, ViewGroup targetView, String classification, int prepend) {
        View badge = inflater.inflate(R.layout.view_badge_classification, targetView, false);
        ((TextView) badge.findViewById(R.id.badge_title)).setText(classification);
        if (prepend == -1) {
            targetView.addView(badge);
        } else {
            targetView.addView(badge, prepend);
        }
    }

    protected static void addInformationBadge(final MediaDetailActivity activity, ViewGroup targetView) {
        View badge = activity.getLayoutInflater().inflate(R.layout.view_badge_info, targetView, false);
        badge.findViewById(R.id.badge_clickable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openPage(MediaDetailActivity.PAGE_ABOUT);
            }
        });
        targetView.addView(badge);
    }

    protected static void createGenreList(final BaseActivity activity, Category[] categories, final ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        for (final Category category : categories) {
            View view = inflater.inflate(R.layout.view_category_list_item, parent, false);
            ((TextView) view.findViewById(R.id.media_category_title)).setText(category.title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryListFragment.openCategory(activity, category);
                }
            });
            parent.addView(view);
        }
    }

    protected void displayTextContainer(final TextView view, String text) {
        if (text != null && !text.isEmpty()) {
            view.setText(text);
        } else {
            ((View) view.getParent()).setVisibility(View.GONE);
        }
    }
}
