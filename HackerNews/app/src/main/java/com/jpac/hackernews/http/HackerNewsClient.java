package com.jpac.hackernews.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class HackerNewsClient {

    public static final int N_THREADS = 10;
    public static final int TIMEOUT = 20;
    
    private static HackerNewsApi hackerNewsService;

    public static HackerNewsApi getHackerNewsClient(Context context) {
        if (hackerNewsService == null) {
            Gson gson = new GsonBuilder().create();

            ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);

            OkHttpClient ok = new OkHttpClient();
            try {
                Cache responseCache = new Cache(context.getCacheDir(), N_THREADS);
                ok.setCache(responseCache);
            } catch (IOException e) {

            }
            ok.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
            ok.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(HackerNewsApi.END_POINT)
                    .setConverter(new GsonConverter(gson))
                    .setClient(new OkClient(ok))
                    .setExecutors(executorService, executorService)
                    .build();

            hackerNewsService = restAdapter.create(HackerNewsApi.class);
        }

        return hackerNewsService;
    }
}
