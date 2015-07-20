package com.jpac.hackernews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.http.HackerNewsClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragment extends ListFragment {

    private News news;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("news")) {
            news = (News) getArguments().getSerializable("news");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if (news != null) {
            setDetails(rootView);
        }

        return rootView;
    }

    private void setDetails(View rootView) {
        TextView title = (TextView) rootView.findViewById(R.id.textTitle);
        TextView author = (TextView) rootView.findViewById(R.id.textAuthor);
        TextView date = (TextView) rootView.findViewById(R.id.textDate);
        TextView points = (TextView) rootView.findViewById(R.id.textPoints);

        title.setText(news.getTitle());
        author.setText(news.getBy());
        date.setText(news.getTime());
        points.setText(news.getScore());

    }
}
