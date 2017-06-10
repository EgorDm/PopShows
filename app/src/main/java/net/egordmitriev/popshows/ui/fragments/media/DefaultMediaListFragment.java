package net.egordmitriev.popshows.ui.fragments.media;

import net.egordmitriev.popshows.helpers.DefaultMediaListHelper;
import net.egordmitriev.popshows.ui.fragments.base.RecyclerFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class DefaultMediaListFragment extends RecyclerFragment<DefaultMediaListHelper> {

    @Override
    protected DefaultMediaListHelper getHelper() {
        return new DefaultMediaListHelper(getActivity());
    }
}