package com.vvaezian.multilingovocabularypractice;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// when syncDown is completed, we need to set status of all rows in remote to 1

public class SyncDownDeliveryRequest extends StringRequest {
    private static final String SYNC_REQUEST_URL = "https://vahidvaezian.000webhostapp.com/syncDownDelivery.php";
    private Map<String,String> params;

    public SyncDownDeliveryRequest(String username) {
        super(Request.Method.POST, SYNC_REQUEST_URL, null, null);
        params = new HashMap<>();
        params.put("LoggedInUser", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
