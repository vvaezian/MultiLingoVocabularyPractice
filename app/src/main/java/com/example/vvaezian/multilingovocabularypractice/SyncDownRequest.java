package com.example.vvaezian.multilingovocabularypractice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SyncDownRequest extends StringRequest {
    private static final String SYNC_REQUEST_URL = "https://vahidvaezian.000webhostapp.com/syncDown.php";
    private Map<String,String> params;

    public SyncDownRequest(String username, Response.Listener<String> listener) {
        super(Request.Method.POST, SYNC_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("LoggedInUser", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
