package com.example.vvaezian.multilingovocabularypractice;

import com.google.cloud.translate.Translation;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("translations")
    private List<Translation> translations = null;

    public List<Translation> getTranslations() {
        return translations;
    }

}
