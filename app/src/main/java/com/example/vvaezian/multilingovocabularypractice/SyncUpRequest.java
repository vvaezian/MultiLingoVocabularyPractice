package com.example.vvaezian.multilingovocabularypractice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SyncUpRequest extends StringRequest {
    private static final String SYNC_REQUEST_URL = "https://vahidvaezian.000webhostapp.com/syncUp.php";
    private Map<String,String> params;

    public SyncUpRequest(String username, String jsonString, Response.Listener<String> listener) {
        super(Request.Method.POST, SYNC_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("jsonString", jsonString);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
