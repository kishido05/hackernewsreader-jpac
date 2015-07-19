package com.jpac.hackernews.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class HackerNewsClient {

    private static HackerNewsApi hackerNewsService;

    public static HackerNewsApi getHackerNewsClient() {
        if (hackerNewsService == null) {
            Gson gson = new GsonBuilder().create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HackerNewsApi.END_POINT)
                    .setConverter(new GsonConverter(gson))
                    .build();

            hackerNewsService = restAdapter.create(HackerNewsApi.class);
        }

        return hackerNewsService;
    }
}
