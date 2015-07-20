package com.jpac.hackernews.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jpac.hackernews.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<News> newsList;
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
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
        ViewHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_news, null);

            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.textTitle);
            holder.author = (TextView) view.findViewById(R.id.textAuthor);
            holder.date = (TextView) view.findViewById(R.id.textDate);
            holder.points = (TextView) view.findViewById(R.id.textPoints);
            holder.url = (ImageButton) view.findViewById(R.id.buttonOpenLink);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        News news = (News) getItem(i);

        holder.title.setText(news.getTitle());
        holder.author.setText(news.getBy());
        holder.date.setText(news.getTime());
        holder.points.setText(news.getScore());

        holder.url.setTag(news.getUrl());
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = (String) view.getTag();

                openUrl(url);
            }
        });

        return view;
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        ((Activity) context).startActivity(i);
    }

    public void add(News news) {
        newsList.add(news);
    }

    public static class ViewHolder {

        TextView title, author, date, points;
        ImageButton url;

    }
}
