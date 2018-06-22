package com.example.vvaezian.multilingovocabularypractice;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SyncWithSQLServerRequest extends StringRequest {
    private static final String SYNC_REQUEST_URL = "https://vahidvaezian.000webhostapp.com/sync.php";
    private Map<String,String> params;

    public SyncWithSQLServerRequest(String username, String dirtyRows, Response.Listener<String> listener) {
        super(Request.Method.POST, SYNC_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("dirtyRows", dirtyRows);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
