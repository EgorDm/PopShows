package net.egordmitriev.watchall.ui.modelviews.base;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.watchall.MainApplication;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.CardedModel;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;
import net.egordmitriev.watchall.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.watchall.ui.fragments.base.BaseFragment;
import net.egordmitriev.watchall.ui.fragments.media.DefaultMediaListFragment;
import net.egordmitriev.watchall.ui.fragments.media.SectionedMediaListFragment;
import net.egordmitriev.watchall.ui.fragments.media.SplitMediaListFragment;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.SaveUtils;
import net.egordmitriev.watchall.utils.TypeRunnable;

import java.util.ArrayList;
import java.util.Arrays;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class AModelView<T extends DetailedModel> {

    public static Bitmap transitionPoster = null;

    protected static <T extends DetailedModel> void scheduleDetailAction(final T item, TypeRunnable<Boolean> runnable) {
        if (item.detail != null) {
            runnable.run(true);
        } else {
            DetailedModel.DetailCallback callback = item.isRequestingDetail();
            if (callback != null) {
                callback.addCallback(runnable);
            } else {
                item.requestDetail(new DetailedModel.DetailCallback(runnable));
            }
        }
    }

    public abstract int getActivityLayout();

    public void setupActivity(final T item, final ViewGroup view) {
    }

    protected abstract void inflateMain(LayoutInflater inflater, final T item, final ViewGroup view);

    public void setupMain(final BaseActivity activity, final T item, final ViewGroup view) {
        inflateMain(activity.getLayoutInflater(), item, view);
        setupMainBase(activity, item, view);
        scheduleDetailAction(item, new TypeRunnable<Boolean>() {
            @Override
            public void run(Boolean args) {
                if (!activity.isOnDestroyCalled()) {
                    setupMainDetail(activity, item, view, args);
                }
            }
        });
    }

    protected void setupMainBase(final BaseActivity activity, final T item, final ViewGroup view) {
        DrawableTypeRequest<String> posterRequest = Glide.with(MainApplication.getAppContext())
                .load(item.getPoster(false));
        if (transitionPoster != null) {
            posterRequest.placeholder(new BitmapDrawable(activity.getResources(), transitionPoster));
            transitionPoster = null;
        } else {
            posterRequest.placeholder(R.drawable.noposter_media);
        }
        posterRequest.into((ImageView) view.findViewById(R.id.detail_poster));

        ((TextView) view.findViewById(R.id.detail_title)).setText(item.getTitle());
    }

    protected void setupMainDetail(final BaseActivity activity, final T item, final ViewGroup view, boolean hasDetail) {
        final LoaderView loaderView = (LoaderView) view.findViewById(R.id.detail_loaderview);
        if (!hasDetail) {
            loaderView.setListener(new LoaderView.LoaderViewCallback() {
                @Override
                public void onRetryClick() {
                    loaderView.setState(LoaderView.STATE_LOADING);
                    scheduleDetailAction(item, new TypeRunnable<Boolean>() {
                        @Override
                        public void run(Boolean args) {
                            setupMainDetail(activity, item, view, args);
                        }
                    });
                }
            });
            loaderView.setState(LoaderView.STATE_ERROR);
        } else {
            loaderView.setState(LoaderView.STATE_IDLE);
        }
    }

    protected abstract void inflateAbout(LayoutInflater inflater, final T item, final ViewGroup view);

    public void setupAbout(final MediaDetailActivity activity, final T item, final ViewGroup view) {
        inflateAbout(activity.getLayoutInflater(), item, view);
        setupAboutBase(activity, item, view);
        scheduleDetailAction(item, new TypeRunnable<Boolean>() {
            @Override
            public void run(Boolean args) {
                setupAboutDetail(activity, item, view, args);
            }
        });
    }

    protected void setupAboutBase(final BaseActivity activity, final T item, final ViewGroup view) {
        ((TextView) view.findViewById(R.id.detail_about_summary_title)).setText(getDescriptionTitle());
        ((TextView) view.findViewById(R.id.detail_about_summary)).setText(item.getDescription());
    }

    protected void setupAboutDetail(final BaseActivity activity, final T item, final ViewGroup view, boolean hasDetail) {

    }

    protected abstract String getDescriptionTitle();

    public void addDataCardRow(final BaseActivity activity, ViewGroup targetView, CardedModel[] data, int titleRes, int type, boolean grid) {
        addDataCardRow((MediaDetailActivity) activity, targetView, data, titleRes, type, grid, APIUtils.MEDIA_LIST_DEFAULT);
    }

    public void addDataCardRow(final MediaDetailActivity activity, ViewGroup targetView, CardedModel[] data, int titleRes, int type, boolean grid, int listType) {
        if (data == null || data.length == 0) {
            return;
        }

        Bundle args = new Bundle();
        args.putParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST, new ArrayList<>(Arrays.asList(data)));
        args.putBoolean(SaveUtils.SAVED_DISPLAY_DATA_META, grid);
        BaseFragment mediaListFragment;
        switch (listType) {
            case APIUtils.MEDIA_LIST_SECTIONED:
                mediaListFragment = new SectionedMediaListFragment();
                break;
            case APIUtils.MEDIA_LIST_SPLIT:
                mediaListFragment = new SplitMediaListFragment();
                break;
            default:
                mediaListFragment = new DefaultMediaListFragment();
                break;
        }
        mediaListFragment.setArguments(args);
        final int page = activity.addPageFragment(mediaListFragment, type);

        ViewGroup cardRow = (ViewGroup) activity.getLayoutInflater().inflate((grid) ? R.layout.view_detail_footer_extra_grid : R.layout.view_detail_footer_extra_list, targetView, false);
        cardRow.findViewById(R.id.detail_extra_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openPage(page);
            }
        });
        ((TextView) cardRow.findViewById(R.id.detail_extra_section_title)).setText(titleRes);

        CardViewNative[] cardViews = new CardViewNative[4];
        cardViews[0] = (CardViewNative) cardRow.findViewById(R.id.detail_extra_section_item1);
        cardViews[1] = (CardViewNative) cardRow.findViewById(R.id.detail_extra_section_item2);
        cardViews[2] = (CardViewNative) cardRow.findViewById(R.id.detail_extra_section_item3);
        cardViews[3] = null;
        int index = 0;
        for (CardViewNative cardView : cardViews) {
            if (cardView != null) {
                while(data.length > index && !(data[index] instanceof DetailedModel)) {
                    index++;
                }
                if (data.length > index) {
                    final CardedModel model = data[index];
                    MediaCard cardMedia = model.onCreateCard(activity, null, false);
                    if (model instanceof DetailedModel) {
                        cardMedia.setOnClickListener(new Card.OnCardClickListener() {
                            @Override
                            public void onClick(Card card, View view) {
                                MediaDetailActivity.open(activity, (DetailedModel) model);
                            }
                        });
                    }
                    cardView.setCard(cardMedia);
                    index++;
                } else {
                    cardView.setVisibility(View.GONE);
                }
            }
        }
        targetView.addView(cardRow);
    }
}
