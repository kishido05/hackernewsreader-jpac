package com.jpac.hackernews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jpac.hackernews.data.News;
import com.jpac.hackernews.data.NewsAdapter;
import com.jpac.hackernews.http.HackerNewsClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment {

    private NewsAdapter newsAdapter;

    private int newsCount = 0;
    private int downloadCount = 0;

    @Override
    public void onResume() {
        super.onResume();

        downloadTopStories();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.list);

        newsAdapter = new NewsAdapter(getActivity());

        list.setAdapter(newsAdapter);
        list.setEmptyView(rootView.findViewById(R.id.empty));

        return rootView;
    }

    private void downloadTopStories() {
        getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);

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

                getView().findViewById(R.id.loading).setVisibility(View.GONE);
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().findViewById(R.id.loading).setVisibility(View.GONE);
                            newsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                downloadCount++;

                // check if already finished downloading all details
                if (downloadCount >= newsCount) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getView().findViewById(R.id.loading).setVisibility(View.GONE);
                            newsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}