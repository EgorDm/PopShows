package net.egordmitriev.watchall.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.adapters.WatchAllAuthenticator;
import net.egordmitriev.watchall.api.GlobalHelper;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.services.SyncService;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;
import net.egordmitriev.watchall.utils.DataCallback;

/**
 * Created by EgorDm on 4/29/2016.
 */
public class SettingsActivity extends BaseActivity {
    private static int prefs = R.xml.preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
    }

    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(SettingsActivity.prefs);

            findPreference("storage_clear_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    GlobalHelper.clearListCache();
                    Toast.makeText(getActivity(), R.string.toast_list_cache_cleared, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            findPreference("storage_clear_history").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    GlobalHelper.clearSearchHistory();
                    Toast.makeText(getActivity(), R.string.toast_history_cleared, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            findPreference("account_prefs_logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    WatchAllAuthenticator.logout();
                    Toast.makeText(getActivity(), R.string.toast_logged_out, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            findPreference("account_prefs_login").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    WatchAllAuthenticator.setupAccount(getActivity(), new DataCallback<Boolean>() {
                        @Override
                        public void success(Boolean data) {
                            if(data) {
                                Toast.makeText(getActivity(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void failure(APIError error) {
                        }
                    });
                    return true;
                }
            });

            findPreference("synclists").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SyncService.syncData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "Synchronizing", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
        }
    }
}
