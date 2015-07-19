package com.jpac.hackernews;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.data.NewsAdapter;
import com.jpac.hackernews.http.HackerNewsClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends ListFragment {

    private NewsAdapter newsAdapter;

    private int newsCount = 0;
    private int downloadCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsAdapter = new NewsAdapter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        downloadTopStories();
    }

    private void downloadTopStories() {
        HackerNewsClient.getHackerNewsClient().listTopStories(new Callback<List<String>>() {
            @Override
            public void success(List<String> ids, Response response) {
                newsCount = ids.size();
                // get list of strings and retrieve detail for each
                for (String id : ids) {
                    downloadStoryDetail(id);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                newsCount = 0;
                downloadCount = 0;
            }
        });
    }

    private void downloadStoryDetail(String id) {
        HackerNewsClient.getHackerNewsClient().getDetail(id, new Callback<News>() {
            @Override
            public void success(News news, Response response) {
                downloadCount++;

                if (news != null) {
                    newsAdapter.add(news);
                }

                // check if already finished downloading all details
                if (downloadCount >= newsCount) {
                    
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}