package net.egordmitriev.popshows.ui.fragments.media;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.AnilistServiceHelper;
import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.helpers.ASyncableMediaRecyclerHelper;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.ui.fragments.base.RecyclerFragment;
import net.egordmitriev.popshows.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class AnimeListFragment extends RecyclerFragment<ASyncableMediaRecyclerHelper<AnimeModel, AnimeModel.Base[]>> {

    public AnimeListFragment() {
        super();
    }

    @Override
    protected ASyncableMediaRecyclerHelper<AnimeModel, AnimeModel.Base[]> getHelper() {
        ASyncableMediaRecyclerHelper<AnimeModel, AnimeModel.Base[]> ret = new ASyncableMediaRecyclerHelper<AnimeModel, AnimeModel.Base[]>(getActivity()) {
            @Override
            protected void handleData(AnimeModel.Base[] data) {
                if (data == null || data.length < 0) {
                    dataEnded = true;
                    setState(LoaderView.STATE_EXTRA);
                    return;
                }

                List<AnimeModel> result = new ArrayList<>(data.length);
                for (AnimeModel.Base model : data) {
                    //Logger.d(model.title_english + " Adult:"+ model.adult);
                    if (!model.adult) {
                        result.add(new AnimeModel(model, null));
                    }
                }
                addData(result);
                if (data.length < APIUtils.TMDB_RESULTS_PERPAGE) {
                    dataEnded = true;
                    setState(LoaderView.STATE_EXTRA);
                }
            }

            @Override
            public void requestData() {
                super.requestData();
                try {
                    AnilistServiceHelper.getAnimeList(mRequestData, mRequestCallback, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestInitial() {
                super.onRequestInitial();
                try {
                    if (mRequestData.genres == null || mRequestData.genres.length < 1) {
                        AnilistServiceHelper.getAnimeList(mRequestData, mRequestCallback, true);
                    } else {
                        AnilistServiceHelper.getAnimeList(mRequestData, mRequestCallback, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ret.setOverflowMenu(R.menu.media_menu_readonly,
                WatchAllServiceHelper.<AnimeModel>getMediaMenuListener(getChildFragmentManager(), getActivity()));
        ret.usingCounter = true;
        return ret;
    }


}
