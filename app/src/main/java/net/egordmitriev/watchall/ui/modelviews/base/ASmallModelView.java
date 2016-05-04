package net.egordmitriev.watchall.ui.modelviews.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.DetailedModel;

/**
 * Created by EgorDm on 5/3/2016.
 */
public abstract class ASmallModelView<T extends DetailedModel> extends AModelView<T> {

    @Override
    public int getActivityLayout() {
        return R.layout.activity_media_detail_small;
    }

    @Override
    public void inflateMain(LayoutInflater inflater, final T item, final ViewGroup view) {
        inflater.inflate(R.layout.view_detail_header_small, view, true);
        inflater.inflate(R.layout.view_detail_content, view, true);
        inflater.inflate(R.layout.view_detail_footer, view, true);
    }
}
