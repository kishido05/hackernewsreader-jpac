package com.jpac.hackernews.data;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<News> newsList;

    public NewsAdapter() {
        newsList = new ArrayList<News>();
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return newsList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public void add(News news) {
        newsList.add(news);

        // redraw list to add the new story
        notifyDataSetChanged();
    }
}
