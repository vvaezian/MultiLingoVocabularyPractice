package com.example.vvaezian.multilingovocabularypractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HelperFunctions  {

    public static String abbreviate(String langName){
        String output = "";
        switch (langName) {
            case "French": output = "fr";
                break;
            case "English": output = "en";
                break;
            case "German": output = "de";
                break;
            case "Spanish": output = "es";
                break;
            case "Italian": output = "it";
                break;
            //TODO think of a default value to add
        }
        return output;
    }

    public static String deAbbreviate(String langName){
        String output = "";
        switch (langName) {
            case "fr": output = "French";
                break;
            case "en": output = "English";
                break;
            case "de": output = "German";
                break;
            case "es": output = "Spanish";
                break;
            case "it": output = "Italian";
                break;
        }
        return output;
    }

    /* This is needed because I wanted to used `loggedInUser` to define the name of database
     (i.e. each user has a database named after his/her username), so I needed to access
     shared preferences in DatabaseHelper before initialising the database. */
    public static DatabaseHelper getDataBaseHelper(final Context context){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String loggedInUser = sp.getString("user","");
        final String databaseName = loggedInUser + "Database2.db";
        return new DatabaseHelper(context, databaseName);
    }

    public static void syncUp(final Context context) {
        /* Updating the remote database with dirty rows from local database*/
        final DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(context);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");

        com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    final boolean success = jsonResponse.getBoolean("success");
                    final String allLangs = jsonResponse.getString("allLangs");
                    final String tableCreated = jsonResponse.getString("tableCreated");
                    if (success) {
                        Log.d(" ----- sync ----", "success");
                        // setting status in local database to 1
                        userDB.cleanData();
                    } else {
                        Log.d(" ----- sync ----", "failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JSONArray test = new JSONArray();

        if (!userDB.wordsTableIsEmpty()) {
            Cursor cursor = userDB.getDirtyData(langsConcated);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                cursor.moveToFirst();
                do {
                    JSONObject dirtyRow = new JSONObject();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (cursor.getString(i) != null) {
                            try {
                                dirtyRow.put(cursor.getColumnName(i), cursor.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        test.put(dirtyRow);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        String jsonString = test.toString();

        SyncWithSQLServerRequest syncRequest = new SyncWithSQLServerRequest(LoggedInUser, jsonString, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(syncRequest);
    }
}
