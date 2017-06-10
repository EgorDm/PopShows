package net.egordmitriev.popshows.ui.activities;

import android.os.Bundle;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();
    }
}
