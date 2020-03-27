package com.example.rssca;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    // This activity is created when a user selects an article in portrait mode,
    // the detials passed from the recycler adapter intent are then displayed in text views
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");
        TextView titleText = findViewById(R.id.art_title);
        TextView descText = findViewById(R.id.art_desc);
        titleText.setText(title);
        descText.setText(content);

        int orientation = Resources.getSystem().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        }


    }
}
