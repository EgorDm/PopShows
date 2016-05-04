package net.egordmitriev.watchall.ui.fragments.media.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.ui.activities.media.MediaDetailActivity;
import net.egordmitriev.watchall.ui.fragments.base.BaseFragment;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class DetailAboutFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_detail_about, container, false);
        ((MediaDetailActivity) getActivity()).getModel().getModelView().setupAbout((MediaDetailActivity) getActivity(),
                ((MediaDetailActivity) getActivity()).getModel(), layout);
        return layout;
    }
}
