package com.jpac.hackernews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.utils.FontUtils;

public class MainActivity extends AppCompatActivity implements HomeFragment.ListCallback {

    protected boolean isDualPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FontUtils.init(this);

        // show the 2 fragments if screen is landscape
        isDualPane = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onItemSelected(int id, News news) {
        if (isDualPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("news", news);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, fragment).commit();
        } else {
            toDetailActivity(news);
        }
    }

    private void toDetailActivity(News news) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("news", news);
        startActivity(intent);
    }
}
