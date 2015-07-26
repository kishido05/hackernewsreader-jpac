package com.jpac.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.utils.FontUtils;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            News news = (News) getIntent().getSerializableExtra("news");
            Bundle arguments = new Bundle();
            arguments.putSerializable("news", news);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, fragment).commit();
        }

    }
}
