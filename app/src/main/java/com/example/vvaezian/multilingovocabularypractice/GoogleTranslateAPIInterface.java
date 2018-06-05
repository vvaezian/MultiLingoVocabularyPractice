package com.example.vvaezian.multilingovocabularypractice;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface GoogleTranslateAPIInterface {
    @POST("?")
    Call<GoogleTranslateAPIResponse> translateWord(@Query("q") String inputWord,
                                           @Query("target") String targetLang,
                                           @Query("key") String apiKey);
}