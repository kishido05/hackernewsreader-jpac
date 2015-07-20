package com.jpac.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.data.NewsAdapter;
import com.jpac.hackernews.http.HackerNewsClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends ListFragment {

    public interface ListCallback {

        void onItemSelected(int id, News news);
    }

    private HomeFragment.ListCallback callback;

    private NewsAdapter newsAdapter;

    private SwipeRefreshLayout swipe;

    private List<News> newsList;
    private int newsCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        ListView list = (ListView) rootView.findViewById(android.R.id.list);

        newsAdapter = new NewsAdapter(getActivity());
        newsList = new ArrayList<News>();

        list.setAdapter(newsAdapter);
        list.setEmptyView(rootView.findViewById(android.R.id.empty));

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadTopStories();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        callback = (HomeFragment.ListCallback) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                downloadTopStories();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (callback != null) {
            callback.onItemSelected(position, (News) newsAdapter.getItem(position));
        }
    }

    private void downloadTopStories() {
        newsAdapter.clear();
        newsList.clear();

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
                displayNews();
            }
        });
    }

    private void downloadStoryDetail(String id) {
        HackerNewsClient.getHackerNewsClient().getDetail(id, new Callback<News>() {
            @Override
            public void success(News news, Response response) {
                newsCount--;

                if (news != null) {
                    newsList.add(news);
                }

                // check if already finished downloading all details
                if (newsCount <= 0) {
                    displayNews();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                newsCount--;

                // check if already finished downloading all details
                if (newsCount <= 0) {
                    displayNews();
                }
            }
        });
    }

    private void displayNews() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.sort(newsList, new NewsComparator());
                newsAdapter.add(newsList);
                newsAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        });
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    private class NewsComparator implements java.util.Comparator<News> {
        @Override
        public int compare(News n1, News n2) {
            return n2.getTime().compareTo(n1.getTime());
        }
    }
}