package net.egordmitriev.watchall.ui.modelviews.base;

import android.graphics.Bitmap;

import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.utils.TypeRunnable;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class AModelView<T extends DetailedModel> {

    public static Bitmap transitionPoster = null;

    protected static <T extends DetailedModel> void scheduleDetailAction(final T item, TypeRunnable<Boolean> runnable) {
        if (item.detail != null) {
            runnable.run(true);
        } else {
            DetailedModel.DetailCallback callback = item.isRequestingDetail();
            if (callback != null) {
                callback.addCallback(runnable);
            } else {
                item.requestDetail(new DetailedModel.DetailCallback(runnable));
            }
        }
    }
}
