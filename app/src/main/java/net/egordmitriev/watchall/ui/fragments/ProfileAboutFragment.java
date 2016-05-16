package net.egordmitriev.watchall.ui.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.adapters.ALoaderHeaderCardsAdapter;
import net.egordmitriev.watchall.appui.adapters.LoaderCardsAdapter;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.helpers.ASyncableMediaRecyclerHelper;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.user.ListRequestData;
import net.egordmitriev.watchall.pojo.watchall.ActivityModel;
import net.egordmitriev.watchall.ui.fragments.base.RecyclerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by EgorDm on 5/15/2016.
 */
public class ProfileAboutFragment extends RecyclerFragment<ProfileAboutFragment.ProfileActivitiesHelper> {

    @Override
    protected ProfileActivitiesHelper getHelper() {
        ProfileActivitiesHelper ret = new ProfileActivitiesHelper(getActivity());
        ret.usingCounter = false;
        return ret;
    }

    public class ProfileActivitiesHelper extends ASyncableMediaRecyclerHelper<ActivityModel, ActivityModel[]> {

        public ProfileActivitiesHelper(Context context) {
            super(context);
            mRequestData = new ListRequestData(-1, ActivityModel.TYPE);
        }

        @Override
        public void onRequestInitial() {
            ActivityModel[] ret = new ActivityModel[2];
            ret[0] = new ActivityModel(AnimeModel.TYPE, ActivityModel.ACTION_WATCHED, 1, new Date(), "Cowboy Bebop", "1");
            ret[1] = new ActivityModel(MovieModel.TYPE, ActivityModel.ACTION_REVIEWED, 550, new Date(), "Fight Club", "/hNFMawyNDWZKKHU4GYCBz1krsRM.jpg");
            mRequestCallback.success(ret);
            //TODO: ask server for activities
        }

        @Override
        protected void handleData(ActivityModel[] data) {
            if (data == null || data.length < 0) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
                return;
            }

            List<ActivityModel> result = Arrays.asList(data);
            addData(result);
            dataEnded = true;
            setState(LoaderView.STATE_EXTRA);
        }

        @Override
        public LoaderCardsAdapter<MediaCard> createAdapter() {
            return new UserActivityAdapter(mContext, new ArrayList<MediaCard>());
        }

        @Override
        public void requestData() {
            super.requestData();
            mRequestCallback.success(null);
            //TODO: ask server for even more activities
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_profile_activities;
        }
    }

    protected class UserActivityAdapter extends ALoaderHeaderCardsAdapter<MediaCard> {

        public UserActivityAdapter(Context context, List<MediaCard> data) {
            super(context, data);
            enableHeader();
        }

        public UserActivityAdapter(Context context, List<MediaCard> data, int loaderViewRes) {
            super(context, data, loaderViewRes);
        }

        public void refeshHeader() {
            onBindHeaderViewHolder(mHeader, 0);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ((UserActivityViewHolder) mHeader).bindView(1337, 1337, 1337, 1337);
        }

        @Override
        protected int getLoaderViewRes() {
            return R.layout.card_media_list_loader_empty;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeader(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_header_profile_about, parent, false);
            return new UserActivityViewHolder(view);
        }
    }

    protected class UserActivityViewHolder extends RecyclerView.ViewHolder {

        public UserActivityViewHolder(View itemView) {
            super(itemView);
        }

        public void bindView(int watched, int reviews, int followers, int following) {
            ((TextView) itemView.findViewById(R.id.user_total_watched)).setText(String.valueOf(watched));
            ((TextView) itemView.findViewById(R.id.user_reviews)).setText(String.valueOf(reviews));
            ((TextView) itemView.findViewById(R.id.user_followers)).setText(String.valueOf(followers));
            ((TextView) itemView.findViewById(R.id.user_following)).setText(String.valueOf(following));
        }
    }
}
