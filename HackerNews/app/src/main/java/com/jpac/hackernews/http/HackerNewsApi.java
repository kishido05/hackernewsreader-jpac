package com.jpac.hackernews.http;

import com.jpac.hackernews.data.News;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface HackerNewsApi {

    public static final String END_POINT = "https://hacker-news.firebaseio.com/v0";

    @GET("/topstories.json")
    void listTopStories(Callback<List<String>> cl);

    @GET("/item/{itemid}.json")
    void getDetail(@Path("itemid") String itemId, Callback<News> cn);
}
