package com.example.vvaezian.multilingovocabularypractice;

import com.google.gson.annotations.SerializedName;

public class GoogleTranslateAPIResponse {

    @SerializedName("data")
    public Data data;

    public Data getData() {
        return data;
    }
}


