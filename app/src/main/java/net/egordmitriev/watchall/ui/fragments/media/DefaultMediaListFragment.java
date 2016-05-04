package net.egordmitriev.watchall.ui.fragments.media;

import net.egordmitriev.watchall.helpers.DefaultMediaListHelper;
import net.egordmitriev.watchall.ui.fragments.base.RecyclerFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class DefaultMediaListFragment extends RecyclerFragment<DefaultMediaListHelper> {

    @Override
    protected DefaultMediaListHelper getHelper() {
        return new DefaultMediaListHelper(getActivity());
    }
}