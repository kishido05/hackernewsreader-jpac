package com.jpac.hackernews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.data.NewsAdapter;
import com.jpac.hackernews.http.HackerNewsClient;
import com.jpac.hackernews.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public interface ListCallback {

        void onItemSelected(int id, News news);
    }

    private HomeFragment.ListCallback callback;

    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipe;

    // temporary list for News
    private List<News> newsList;
    private int newsCount = 0;

    private List<String> cachedNews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView list = (RecyclerView) rootView.findViewById(R.id.list);

        list.addItemDecoration(new SpacesItemDecoration(15));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(getActivity(), this);
        newsList = new ArrayList<News>();
        cachedNews = new ArrayList<String>();

        list.setAdapter(newsAdapter);

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
    public void onClick(View view) {
        int id = (Integer) view.getTag();

        callback.onItemSelected(id, newsAdapter.get(id));
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

    private void downloadTopStories() {
        newsList.clear();

        HackerNewsClient.getHackerNewsClient(getActivity()).listTopStories(new Callback<List<String>>() {
            @Override
            public void success(List<String> ids, Response response) {
                newsCount = ids.size();

                if (cachedNews.size() < newsCount) {
                    // get list of strings and retrieve detail for each
                    for (String id : ids) {
                        if (!cachedNews.contains(id)) {
                            downloadStoryDetail(id);
                        }
                    }
                } else {
                    displayNews();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                displayNews();
            }
        });
    }

    private void downloadStoryDetail(String id) {
        HackerNewsClient.getHackerNewsClient(getActivity()).getDetail(id, new Callback<News>() {
            @Override
            public void success(News news, Response response) {
                newsCount--;

                if (news != null) {
                    newsList.add(news);
                    cachedNews.add(news.getId());
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
                newsAdapter.add(newsList);
                newsAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        });
    }
}