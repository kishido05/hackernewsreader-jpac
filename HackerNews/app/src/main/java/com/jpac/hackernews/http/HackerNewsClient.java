package com.jpac.hackernews.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class HackerNewsClient {

    public static final int N_THREADS = 50;
    
    private static HackerNewsApi hackerNewsService;

    public static HackerNewsApi getHackerNewsClient() {
        if (hackerNewsService == null) {
            Gson gson = new GsonBuilder().create();

            ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HackerNewsApi.END_POINT)
                    .setConverter(new GsonConverter(gson))
                    .setExecutors(executorService, executorService)
                    .build();

            hackerNewsService = restAdapter.create(HackerNewsApi.class);
        }

        return hackerNewsService;
    }
}
