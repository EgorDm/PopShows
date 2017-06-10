package net.egordmitriev.popshows.ui.modelviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import net.egordmitriev.popshows.MainApplication;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardLarge;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.popshows.pojo.CardedModel;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.pojo.anilist.CharacterModel;
import net.egordmitriev.popshows.pojo.anilist.StaffModel;
import net.egordmitriev.popshows.pojo.data.Section;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.popshows.ui.modelviews.base.ALargeModelView;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.MediaUtils;
import net.egordmitriev.popshows.utils.TypeRunnable;
import net.egordmitriev.popshows.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class AnimeView extends ALargeModelView<AnimeModel> {
    public static MediaCard onCreateCard(Context context, AnimeModel item, String prefix, boolean small) {
        if (small) {
            return new MediaCardSmall(context, item.type, (prefix != null) ? prefix + item.getTitle() : item.getTitle(), getSubtitle(item),
                    item.getPoster(true), getRating(item), null);
        } else {
            return new MediaCardLarge(context, item.type, item.getTitle(),
                    getSubtitle(item),
                    item.getPoster(false), null);
        }
    }

    public static String getSubtitle(AnimeModel item) {
        if (item.base.relation_type != null) {
            return Utils.capitalize(item.base.relation_type);
        }
        StringBuilder ret = new StringBuilder();
        if (item.base.total_episodes > 0) {
            ret.append("Episodes: ").append(item.base.total_episodes).append(", ");
        }
        ret.append("Type: ").append(item.base.type_anime);
        return ret.toString();
    }

    public static float getRating(AnimeModel item) {
        if (item.base.average_score == -1f) {
            return -1f;
        } else {
            return MediaUtils.convertAnimeVote(item.base.average_score);
        }
    }

    private static AnimeView sInstance;

    public static AnimeView getInstance() {
        if (sInstance == null) {
            sInstance = new AnimeView();
        }
        return sInstance;
    }

    @Override
    public void setupActivity(final AnimeModel item, final ViewGroup view) {
        final ImageView backdropView = (ImageView) view.findViewById(R.id.backdrop);
        Glide.with(MainApplication.getAppContext()).load(R.drawable.nobackdrop_media).centerCrop().into(backdropView);
        scheduleDetailAction(item, new TypeRunnable<Boolean>() {
            @Override
            public void run(Boolean args) {
                initBackdrop(item, backdropView, args);
            }
        });

    }

    @Override
    protected String getBackdrop(AnimeModel item) {
        return item.detail.backdrop;
    }

    private void initBackdrop(final AnimeModel item, ImageView view, boolean hasDetail) {
        if (hasDetail) {
            Glide.with(MainApplication.getAppContext()).load(getBackdrop(item)).asBitmap().centerCrop().placeholder(R.drawable.nobackdrop_media).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(view) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                    super.onResourceReady(bitmap, anim);
                    Glide.with(MainApplication.getAppContext()).load(getBackdrop(item)).centerCrop().into(view);
                }
            });
        }
    }

    @Override
    protected void setupMainBase(final BaseActivity activity, AnimeModel item, ViewGroup view) {
        super.setupMainBase(activity, item, view);
        ViewGroup badgeGroup = (ViewGroup) view.findViewById(R.id.detail_badge_container);
        addRatingBadge(activity.getLayoutInflater(), badgeGroup, getRating(item), "Rating");
        addInformationBadge(((MediaDetailActivity) activity), badgeGroup);

        view.findViewById(R.id.detail_content_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MediaDetailActivity) activity).openPage(MediaDetailActivity.PAGE_ABOUT);
            }
        });
    }

    @Override
    protected void setupMainDetail(BaseActivity activity, AnimeModel item, ViewGroup view, boolean hasDetail) {
        super.setupMainDetail(activity, item, view, hasDetail);
        if (!hasDetail) return;
        ((TextView) view.findViewById(R.id.detail_subtitle)).setText(getSubtitleDetail(item));
        TextView summaryView = (TextView) view.findViewById(R.id.detail_content_main);
        summaryView.setText((item.getDescription() != null) ? Html.fromHtml(item.getDescription()) : "No summary available");

        ViewGroup badgeGroup = (ViewGroup) view.findViewById(R.id.detail_badge_container);
        if (item.detail.classification != null && !item.detail.classification.isEmpty()) {
            addClassificationBadge(activity.getLayoutInflater(), badgeGroup, item.detail.classification, 1);
        }
        if (item.detail.genres != null && item.detail.genres.length > 0) {
            addGenreBadge(activity.getLayoutInflater(), badgeGroup, item.detail.genres[0], R.drawable.ic_genre_comedy, 0);
        }

        ViewGroup extraContainer = (ViewGroup) view.findViewById(R.id.detail_footer_data);

        //TODO: Sectioned list relations
        if (item.detail.relations != null && item.detail.relations.length > 0) {
            LinkedList<AnimeModel.Base> models = new LinkedList<>(Arrays.asList(item.detail.relations));
            Collections.sort(models, new Comparator<AnimeModel.Base>() {
                @Override
                public int compare(AnimeModel.Base lhs, AnimeModel.Base rhs) {
                    int sort1 = Utils.coalesce(MediaUtils.ANIME_RELATION_ORDER.get(lhs.relation_type), 12);
                    int sort2 = Utils.coalesce(MediaUtils.ANIME_RELATION_ORDER.get(rhs.relation_type), 12);
                    return (sort1 < sort2) ? -1 : ((sort1 == sort2) ? 0 : 1);
                }
            });

            ArrayList<CardedModel> listData = new ArrayList<>();
            String prevRel = null;
            for (int i = 0; i < models.size(); i++) {
                if (prevRel == null || !prevRel.contentEquals(models.get(i).relation_type)) {
                    listData.add(new Section(models.get(i).relation_type));
                }
                prevRel = models.get(i).relation_type;
                listData.add(new AnimeModel(models.get(i), null));
            }
            addDataCardRow(((MediaDetailActivity) activity), extraContainer,
                    listData.toArray(new CardedModel[listData.size()]), R.string.relations, MediaDetailActivity.PAGE_SEASONS, true, APIUtils.MEDIA_LIST_SECTIONED);
        }

        StaffModel[] staff = StaffModel.createArray(item.detail.staff);
        addDataCardRow(activity, extraContainer, staff, R.string.staff, MediaDetailActivity.PAGE_STAFF, true);

        CharacterModel[] characters = CharacterModel.createArray(item.detail.characters);
        addDataCardRow(((MediaDetailActivity) activity), extraContainer, characters, R.string.characters, MediaDetailActivity.PAGE_CAST, false, APIUtils.MEDIA_LIST_SPLIT);
    }

    @Override
    protected String getRuntime(AnimeModel item) {
        return item.detail.duration + " minutes";
    }

    @Override
    protected void inflateAbout(LayoutInflater inflater, AnimeModel item, ViewGroup view) {
        ViewStub infoStub = (ViewStub) view.findViewById(R.id.detail_about_info_stub);
        infoStub.setLayoutResource(R.layout.view_detail_about_info_anime);
        infoStub.inflate();

    }

    @Override
    protected void setupAboutBase(BaseActivity activity, AnimeModel item, ViewGroup view) {
        ((TextView) view.findViewById(R.id.detail_about_summary_title)).setText(getDescriptionTitle());

        ViewStub crewStub = ((ViewStub) view.findViewById(R.id.detail_about_crew_stub));
        crewStub.setLayoutResource(R.layout.view_detail_about_nameinfo);
        View nameView = crewStub.inflate();
        displayTextContainer((TextView) nameView.findViewById(R.id.detail_about_name_synonyms_content),
                (item.base.synonyms != null) ? TextUtils.join(", ", item.base.synonyms) : "None");
        displayTextContainer((TextView) nameView.findViewById(R.id.detail_about_name_japanese_content),
                item.base.title_japanese);
        displayTextContainer((TextView) nameView.findViewById(R.id.detail_about_name_english_content),
                item.base.title_english);
        displayTextContainer((TextView) nameView.findViewById(R.id.detail_about_name_romanji_content),
                item.base.title_romaji);
    }

    @Override
    protected void setupAboutDetail(BaseActivity activity, AnimeModel item, ViewGroup view, boolean hasDetail) {
        super.setupAboutDetail(activity, item, view, hasDetail);
        if (!hasDetail) return;

        ((TextView) view.findViewById(R.id.detail_about_summary)).setText(Html.fromHtml(item.getDescription()));
        View nameView = view.findViewById(R.id.detail_about_crew);
        displayTextContainer((TextView) nameView.findViewById(R.id.detail_about_name_studios_content),
                TextUtils.join(", ", item.detail.studio));

        View infoView = view.findViewById(R.id.detail_about_info);
        ((TextView) infoView.findViewById(R.id.detail_about_info_first_aired))
                .setText((item.detail.start_date != null) ? APIUtils.sFriendlyDateFormat.format(item.detail.start_date) : "Unknown");
        ((TextView) infoView.findViewById(R.id.detail_about_info_last_aired))
                .setText((item.detail.end_date != null) ? APIUtils.sFriendlyDateFormat.format(item.detail.end_date) : "Unknown");
        ((TextView) infoView.findViewById(R.id.detail_about_info_episode_runtime))
                .setText((item.detail.duration > 0) ? item.detail.duration + " minutes" : "Unknown");
        ((TextView) infoView.findViewById(R.id.detail_about_info_classification))
                .setText((item.detail.classification != null) ? item.detail.classification : "Unknown");

        if (item.detail.genres != null && item.detail.genres.length > 0) {
            ViewGroup group = (ViewGroup) ((ViewStub) view.findViewById(R.id.detail_about_genre_stub)).inflate();
            if (group != null) {
                createGenreList(
                        activity,
                        MediaUtils.getPredefinedCategoryList(AnimeModel.TYPE, item.detail.genres),
                        group);
            }
        }
    }

    @Override
    protected String getDescriptionTitle() {
        return "Sypnose";
    }

    public String getSubtitleDetail(AnimeModel item) {

        return (item.detail.start_date != null)
                ? APIUtils.sFriendlyDateFormat.format(item.detail.start_date) : "Unknown start date";
    }
}
