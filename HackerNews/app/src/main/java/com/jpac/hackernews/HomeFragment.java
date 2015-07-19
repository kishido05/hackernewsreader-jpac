package com.jpac.hackernews;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.jpac.hackernews.data.NewsAdapter;

public class HomeFragment extends ListFragment {

    private NewsAdapter newsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsAdapter = new NewsAdapter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
