package net.egordmitriev.watchall.ui.fragments.media;

import net.egordmitriev.watchall.helpers.SplitMediaListHelper;
import net.egordmitriev.watchall.ui.fragments.base.RecyclerFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SplitMediaListFragment extends RecyclerFragment<SplitMediaListHelper> {

    @Override
    protected SplitMediaListHelper getHelper() {
        return new SplitMediaListHelper(getActivity());
    }
}
