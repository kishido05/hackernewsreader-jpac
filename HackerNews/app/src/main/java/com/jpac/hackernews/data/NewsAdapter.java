package com.jpac.hackernews.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jpac.hackernews.R;
import com.jpac.hackernews.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    List<News> newsList;
    Context context;
    private View.OnClickListener itemClickListener;

    public NewsAdapter(Context context, View.OnClickListener listener) {
        this.itemClickListener = listener;
        this.context = context;
        newsList = new ArrayList<News>();
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item_news, viewGroup, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int i) {
        News news = newsList.get(i);

        holder.parent.setTag(i);
        holder.parent.setOnClickListener(itemClickListener);

        holder.title.setText(news.getTitle());
        holder.author.setText(news.getBy());
        holder.date.setText(Utils.getTimeAgo(news.getTime()));
        holder.points.setText(news.getScore());

        holder.url.setTag(news.getUrl());
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = (String) view.getTag();

                openUrl(url);
            }
        });
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void add(List<News> news) {
        newsList.addAll(news);
        notifyDataSetChanged();
    }

    public News get(int i) {
        return newsList.get(i);
    }
}
