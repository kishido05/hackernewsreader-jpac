package com.jpac.hackernews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpac.hackernews.data.Comments;
import com.jpac.hackernews.data.CommentsAdapter;
import com.jpac.hackernews.data.News;
import com.jpac.hackernews.http.HackerNewsClient;
import com.jpac.hackernews.utils.Utils;
import com.jpac.hackernews.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragment extends Fragment {

    private CommentsAdapter commentsAdapter;

    private News news;

    private int commentsCount;
    private int downloadCount;

    private SwipeRefreshLayout swipe;

    private List<Comments> commentsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("news")) {
            news = (News) getArguments().getSerializable("news");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                downloadDetail();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if (news != null) {
            setDetails(rootView);
        }

        RecyclerView list = (RecyclerView) rootView.findViewById(R.id.commentList);

        list.addItemDecoration(new SpacesItemDecoration(5));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        commentsAdapter = new CommentsAdapter(getActivity());
        commentsList = new ArrayList<Comments>();

        list.setAdapter(commentsAdapter);

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadDetail();
            }
        });

        return rootView;
    }

    private void setDetails(View rootView) {
        TextView title = (TextView) rootView.findViewById(R.id.textTitle);
        TextView author = (TextView) rootView.findViewById(R.id.textAuthor);
        TextView date = (TextView) rootView.findViewById(R.id.textDate);
        TextView points = (TextView) rootView.findViewById(R.id.textPoints);

        title.setText(news.getTitle());
        author.setText(news.getBy());
        date.setText(Utils.getTimeAgo(news.getTime()));
        points.setText(news.getScore());

    }

    private void downloadDetail() {
        if (news == null) {
            displayComments();
            return;
        }

        String id = news.getId();

        HackerNewsClient.getHackerNewsClient(getActivity()).getDetail(id, new Callback<News>() {

            @Override
            public void success(News news, Response response) {
                DetailFragment.this.news = news;

                downloadTopComments();
            }

            @Override
            public void failure(RetrofitError error) {
                downloadTopComments();
            }
        });
    }

    private void downloadTopComments() {
        if (news == null || news.getKids() == null) {
            displayComments();
            return;
        }

        String[] ids = news.getKids();
        int len = ids.length;

        // make sure that only the top 10 comments will be downloaded
        len = len > 10 ? 10 : len;

        commentsCount = len;

        for (int i=0; i<len; i++) {
            downloadComment(ids[i]);
        }
    }

    private void downloadReply(final News parent) {
        String[] ids = parent.getKids();

        if (ids != null && ids.length > 0) {
            HackerNewsClient.getHackerNewsClient(getActivity()).getDetail(ids[0], new Callback<News>() {
                @Override
                public void success(News news, Response response) {
                    if (!isVisible()) {
                        return;
                    }

                    Comments comment = new Comments();
                    comment.setComment(parent);
                    if (news != null) {
                        comment.setReply(news);
                    }

                    downloadCount++;

                    commentsList.add(comment);

                    if (downloadCount >= commentsCount) {
                        displayComments();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (!isVisible()) {
                        return;
                    }

                    downloadCount++;

                    if (downloadCount >= commentsCount) {
                        displayComments();
                    }
                }
            });
        } else {
            Comments comment = new Comments();
            comment.setComment(parent);

            downloadCount++;

            commentsList.add(comment);

            if (downloadCount >= commentsCount) {
                displayComments();
            }
        }
    }

    private void downloadComment(String id) {
        HackerNewsClient.getHackerNewsClient(getActivity()).getDetail(id, new Callback<News>() {

            @Override
            public void success(News news, Response response) {
                downloadReply(news);
            }

            @Override
            public void failure(RetrofitError error) {
                downloadCount++;

                if (downloadCount >= commentsCount) {
                    displayComments();
                }
            }
        });
    }

    private void displayComments() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                commentsAdapter.clear();
                Collections.sort(commentsList, new Comparator<Comments>() {
                    @Override
                    public int compare(Comments c1, Comments c2) {
                        return c2.getComment().getTime().compareTo(c1.getComment().getTime());
                    }
                });
                commentsAdapter.add(commentsList);
                commentsAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);

                commentsCount = 0;
                downloadCount = 0;
            }
        });
    }
}
