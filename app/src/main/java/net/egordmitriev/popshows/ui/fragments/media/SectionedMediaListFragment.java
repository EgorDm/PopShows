package net.egordmitriev.popshows.ui.fragments.media;

import net.egordmitriev.popshows.helpers.SectionedMediaRecyclerHelper;
import net.egordmitriev.popshows.ui.fragments.base.RecyclerFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SectionedMediaListFragment extends RecyclerFragment<SectionedMediaRecyclerHelper> {

    @Override
    protected SectionedMediaRecyclerHelper getHelper() {
        return new SectionedMediaRecyclerHelper(getActivity());
    }

}
