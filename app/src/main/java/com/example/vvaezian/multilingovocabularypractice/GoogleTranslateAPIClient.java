package com.example.vvaezian.multilingovocabularypractice;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleTranslateAPIClient {

    static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl("https://translation.googleapis.com/language/translate/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

}
