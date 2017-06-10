package net.egordmitriev.watchall.ui.activities;

import android.os.Bundle;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.ui.activities.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();
    }
}
