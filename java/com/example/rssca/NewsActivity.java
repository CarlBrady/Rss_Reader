package com.example.rssca;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.TextView;

public class NewsActivity extends FragmentActivity{
    TextView title;
    TextView context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        context = findViewById(R.id.contentText);
        title = findViewById(R.id.titleText);

        int orientation = Resources.getSystem().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) { // if the orientation is in portrait mode then hide the information fragment
            Fragment mFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.infoFrag);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(mFragment);
            ft.commit();
        }

        else{ // if in landscape show the information fragment
            Fragment mFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.infoFrag);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.show(mFragment);
            ft.commit();
        }






    }


}
