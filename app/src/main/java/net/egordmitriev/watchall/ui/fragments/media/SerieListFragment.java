package net.egordmitriev.watchall.ui.fragments.media;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.TMDBServiceHelper;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.helpers.ASyncableMediaRecyclerHelper;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.ui.fragments.base.RecyclerFragment;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class SerieListFragment extends RecyclerFragment<ASyncableMediaRecyclerHelper<SerieModel, SerieModel.Base[]>> {

    public SerieListFragment() {
        super();
    }

    @Override
    protected ASyncableMediaRecyclerHelper<SerieModel, SerieModel.Base[]> getHelper() {
        ASyncableMediaRecyclerHelper<SerieModel, SerieModel.Base[]> ret = new ASyncableMediaRecyclerHelper<SerieModel, SerieModel.Base[]>(getActivity()) {
            @Override
            protected void handleData(SerieModel.Base[] data) {
                if (data == null || data.length < 0) {
                    dataEnded = true;
                    setState(LoaderView.STATE_EXTRA);
                    return;
                }

                List<SerieModel> result = new ArrayList<>(data.length);
                for (SerieModel.Base model : data) {
                    //if (!model.adult) {
                    result.add(new SerieModel(model, null));
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
                    TMDBServiceHelper.getSerieList(mRequestData, mRequestCallback, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestInitial() {
                super.onRequestInitial();
                try {
                    if (mRequestData.genres == null || mRequestData.genres.length == 0) {
                        TMDBServiceHelper.getSerieList(mRequestData, mRequestCallback, true);
                    } else {
                        TMDBServiceHelper.getSerieList(mRequestData, mRequestCallback, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ret.setOverflowMenu(R.menu.media_menu_readonly,
                WatchAllServiceHelper.<SerieModel>getMediaMenuListener(getChildFragmentManager(), getActivity()));
        ret.usingCounter = true;
        return ret;
    }


}
