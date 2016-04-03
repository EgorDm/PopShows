package net.egordmitriev.watchall.api;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import net.egordmitriev.watchall.MainApplication;

/**
 * Created by EgorDm on 4/3/2016.
 */
@SuppressLint("CommitPrefEdits")
public class PreferencesHelper {

    private static PreferencesHelper sInstance;

    public static PreferencesHelper getInstance() {
        if (sInstance == null) {
            sInstance = new PreferencesHelper(MainApplication.getAppContext().getResources(),
                    PreferenceManager.getDefaultSharedPreferences(MainApplication.getAppContext()));
        }
        return sInstance;
    }

    protected Resources mRes;
    protected SharedPreferences mPref;

    public PreferencesHelper(Resources res, SharedPreferences pref) {
        mRes = res;
        mPref = pref;
    }

    public boolean isPrefContains(int resId) {
        return (mPref.contains(mRes.getString(resId)));
    }

    public void removePref(int resId) {
        mPref.edit().remove(mRes.getString(resId)).commit();
    }

    public String getString(int resId, String defValue) {
        return (mPref.getString(mRes.getString(resId), defValue));
    }

    public String getString(int resId) {
        return getString(resId, null);
    }

    public boolean getBoolean(int resId, boolean defValue) {
        return (mPref.getBoolean(mRes.getString(resId), defValue));
    }

    public boolean getBoolean(int resId) {
        return getBoolean(resId, false);
    }

    public int getInt(int resId, int defValue) {
        return (mPref.getInt(mRes.getString(resId), defValue));
    }

    public int getInt(int resId) {
        return getInt(resId, Integer.MIN_VALUE);
    }

    public long getLong(int resId, long defValue) {
        return (mPref.getLong(mRes.getString(resId), defValue));
    }

    public long getLong(int resId) {
        return getLong(resId, Long.MIN_VALUE);
    }

    public float getFloat(int resId, float defValue) {
        return (mPref.getFloat(mRes.getString(resId), defValue));
    }

    public float getFloat(int resId) {
        return getFloat(resId, Float.MIN_VALUE);
    }

    public PrefSetBuilder build() {
        return new PrefSetBuilder(mPref.edit());
    }

    public class PrefSetBuilder {

        private SharedPreferences.Editor editor;

        public PrefSetBuilder(SharedPreferences.Editor editor) {
            this.editor = editor;
        }

        public PrefSetBuilder setString(int resId, String value) {
            editor.putString(mRes.getString(resId), value);
            return this;
        }

        public PrefSetBuilder setBoolean(int resId, boolean value) {
            editor.putBoolean(mRes.getString(resId), value);
            return this;
        }

        public PrefSetBuilder setInt(int resId, int value) {
            editor.putInt(mRes.getString(resId), value);
            return this;
        }

        public PrefSetBuilder setLong(int resId, long value) {
            editor.putLong(mRes.getString(resId), value);
            return this;
        }

        public PrefSetBuilder setFloat(int resId, float value) {
            editor.putFloat(mRes.getString(resId), value);
            return this;
        }

        public boolean commit() {
            return editor.commit();
        }

        public void apply() {
            editor.apply();
            editor = null;
        }
    }
}
