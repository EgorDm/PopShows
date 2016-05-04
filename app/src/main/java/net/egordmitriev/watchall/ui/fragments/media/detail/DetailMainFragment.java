package net.egordmitriev.watchall.ui.fragments.media.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;
import net.egordmitriev.watchall.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.watchall.ui.fragments.base.BaseFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class DetailMainFragment extends BaseFragment {

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_detail_main, container, false);
        ((MediaDetailActivity) getActivity()).getModel().getModelView().setupMain((BaseActivity) getActivity(),
                ((MediaDetailActivity) getActivity()).getModel(), (ViewGroup) layout.findViewById(R.id.detail));
        return layout;
    }

    public int getScrollY() {
        if (getView() != null) {
            NestedScrollView nestedScrollView = (NestedScrollView) getView().findViewById(R.id.detail_main);
            return nestedScrollView.computeVerticalScrollOffset();
        }
        return -1;
    }
}
