package net.egordmitriev.watchall.ui.fragments;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.TMDBServiceHelper;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.helpers.ASyncableMediaRecyclerHelper;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.ui.fragments.base.RecyclerFragment;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class MovieListFragment extends RecyclerFragment<ASyncableMediaRecyclerHelper<MovieModel, MovieModel.Base[]>> {

    public MovieListFragment() {
        super();
    }

    @Override
    protected ASyncableMediaRecyclerHelper<MovieModel, MovieModel.Base[]> getHelper() {
        ASyncableMediaRecyclerHelper<MovieModel, MovieModel.Base[]> ret = new ASyncableMediaRecyclerHelper<MovieModel, MovieModel.Base[]>(getActivity()) {
            @Override
            protected void handleData(MovieModel.Base[] data) {
                if (data == null || data.length < 0) {
                    dataEnded = true;
                    setState(LoaderView.STATE_EXTRA);
                    return;
                }

                List<MovieModel> result = new ArrayList<>(data.length);
                for (MovieModel.Base model : data) {
                    //if (!model.adult) {
                    result.add(new MovieModel(model, null));
                    //}
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
                    TMDBServiceHelper.getMovieList(mRequestData, mRequestCallback, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestInitial() {
                super.onRequestInitial();
                try {
                    if (mRequestData.genres == null || mRequestData.genres.length < 1) {
                        TMDBServiceHelper.getMovieList(mRequestData, mRequestCallback, true);
                    } else {
                        TMDBServiceHelper.getMovieList(mRequestData, mRequestCallback, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ret.setOverflowMenu(R.menu.media_menu_readonly,
                WatchAllServiceHelper.<MovieModel>getMediaMenuListener(getChildFragmentManager(), getActivity()));
        ret.usingCounter = true;
        return ret;
    }


}
