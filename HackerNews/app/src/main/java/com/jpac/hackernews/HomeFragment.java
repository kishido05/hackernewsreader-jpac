package com.jpac.hackernews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jpac.hackernews.data.Footer;
import com.jpac.hackernews.data.News;
import com.jpac.hackernews.data.NewsAdapter;
import com.jpac.hackernews.http.HackerNewsClient;
import com.jpac.hackernews.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    public interface ListCallback {

        void onItemSelected(int id, News news);
    }

    private HomeFragment.ListCallback callback;

    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipe;

    // id list for already downloaded data
    private HashMap<String, News> cachedNews;
    private List<String> storyList;
    private List<String> downloadQueue;

    private Footer footer;
    private int page = 1;
    private int storyIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView list = (RecyclerView) rootView.findViewById(R.id.list);

        list.addItemDecoration(new SpacesItemDecoration(5));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        newsAdapter = new NewsAdapter(getActivity(), this);

        list.setAdapter(newsAdapter);
        list.setOnTouchListener(this);

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadTopStories();
            }
        });

        footer = new Footer();

        cachedNews = new HashMap<String, News>();
        storyList = new ArrayList<String>();
        downloadQueue = new ArrayList<String>();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (swipe.isRefreshing()) {
            return;
        }

        switch (view.getId()) {
            case R.id.buttonOpenLink:
                openUrl((String) view.getTag());
                break;
            default:
                if (view.getTag() == null) {
                    loadMore();
                } else {
                    int id = (Integer) view.getTag();
                    callback.onItemSelected(id, newsAdapter.get(id));
                }
        }
    }

    private void loadMore() {
        page++; // increment page count

        int len = Math.min(page * 10, storyList.size());

        for (int i=storyIndex; i<len; i++) {
            if (!cachedNews.containsKey(storyList.get(i)))
                downloadQueue.add(storyList.get(i));
        }

        if (downloadQueue.isEmpty()) {
            displayNews();
        } else {
            swipe.post(new Runnable() {
                @Override
                public void run() {
                swipe.setRefreshing(true);
                downloadItemDetail();
                }
            });
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (swipe.isRefreshing()) {
            return true;
        }

        return false;
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

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        getActivity().startActivity(i);
    }

    private void downloadTopStories() {
        page = 1; // reset page count

        HackerNewsClient.getHackerNewsClient(getActivity()).listTopStories(new Callback<List<String>>() {
            @Override
            public void success(List<String> ids, Response response) {
                storyList = new ArrayList<String>(ids);

                // prepare download queue
                int len = Math.min(ids.size(), 10);
                for (int i = 0; i < len; i++) {
                    if (!cachedNews.containsKey(ids.get(i)))
                        downloadQueue.add(ids.get(i));
                }

                if (downloadQueue.isEmpty()) {
                    displayNews();
                } else {
                    downloadItemDetail();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                displayNews();
            }
        });
    }

    private void downloadItemDetail() {
        String id = downloadQueue.remove(0);

        HackerNewsClient.getHackerNewsClient(getActivity()).getDetail(id, new Callback<News>() {
            @Override
            public void success(News news, Response response) {
                cachedNews.put(news.getId(), news);

                if (downloadQueue.isEmpty()) {
                    displayNews();
                } else {
                    downloadItemDetail();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (downloadQueue.isEmpty()) {
                    displayNews();
                } else {
                    downloadItemDetail();
                }
            }
        });
    }

    private void displayNews() {
        Activity activity = getActivity();

        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page > 1) {
                        displayNewsMore();
                    } else {
                        displayNewsFresh();
                    }
                }
            });
        }
    }

    private void displayNewsMore() {
        newsAdapter.remove(footer);
        int len = Math.min(storyList.size(), page * 10);

        for (;storyIndex<len;storyIndex++) {
            News news = cachedNews.get(storyList.get(storyIndex));
            if (news != null) {
                newsAdapter.add(news);
            }
        }

        if (len < storyList.size()) {
            newsAdapter.add(footer);
        }

        newsAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    private void displayNewsFresh() {
        newsAdapter.clear();
        int len = Math.min(storyList.size(), 10);

        for (storyIndex=0; storyIndex<len; storyIndex++) {
            News news = cachedNews.get(storyList.get(storyIndex));
            if (news != null) {
                newsAdapter.add(news);
            }
        }

        if (len < storyList.size()) {
            newsAdapter.add(footer);
        }

        newsAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }
}