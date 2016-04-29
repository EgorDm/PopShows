package net.egordmitriev.watchall.ui.activities;

import android.os.Bundle;

import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.ui.activities.base.HomeActivity;

public class MainActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        Logger.d("Hi!");
        initDrawer();

    }
}
