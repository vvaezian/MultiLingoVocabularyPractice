package com.example.vvaezian.multilingovocabularypractice;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditTableRequest extends StringRequest {
    private static final String SAVE_DATA_REQUEST_URL = "https://vahidvaezian.000webhostapp.com/editTable.php";
    private Map<String,String> params;

    public EditTableRequest(String sourceLangWord, String translatedWord, Response.Listener<String> listener) {
        super(Method.POST, SAVE_DATA_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("sourceLangWord", sourceLangWord);
        params.put("translatedWord", translatedWord);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
