package net.egordmitriev.popshows.ui.fragments.media;

import net.egordmitriev.popshows.helpers.SplitMediaListHelper;
import net.egordmitriev.popshows.ui.fragments.base.RecyclerFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SplitMediaListFragment extends RecyclerFragment<SplitMediaListHelper> {

    @Override
    protected SplitMediaListHelper getHelper() {
        return new SplitMediaListHelper(getActivity());
    }
}
