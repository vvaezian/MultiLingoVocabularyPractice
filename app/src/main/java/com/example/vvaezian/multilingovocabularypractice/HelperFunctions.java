package com.example.vvaezian.multilingovocabularypractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HelperFunctions  {
    //TODO: Generalize for more languages (check other files to see if they need change regarding this)
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
        String [] langs = langsConcated.split(" ");

        com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    final boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Log.d(" ----- sync ----", "success");
                        // setting status in local database to 1
                        userDB.cleanData();
                        // writing in SharedPreferences that syncedUp was successful
                        SharedPreferences.Editor prefEditor = sp.edit();
                        prefEditor.putBoolean("syncedUp", true);
                        prefEditor.apply();
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
            Cursor cursor = userDB.getDirtyData(langs);
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

        SyncUpRequest syncRequest = new SyncUpRequest(LoggedInUser, jsonString, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(syncRequest);
    }


    public static void syncDown(final Context context) {
        /* Updates the local database with dirty rows from the remote database*/

        final DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(context);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    JSONObject jsonObj;
                    boolean flag = true; // to check if all rows inserted successfully

                    Log.d("------ syncDown ------", "Starting to process data");

                    for (int i=0; i < jsonResponse.length(); i++){
                        jsonObj = jsonResponse.getJSONObject(i);
                        final String[] translations = new String[langs.length];
                        for (int j=0; j < langs.length; j++){
                            translations[j] = jsonObj.getString(langs[j]);
                        }
                        Log.d(" ----- syncDown ----", Arrays.toString(translations));
                        String sourceWord = jsonObj.getString("source");
                        boolean isInserted = userDB.insertData(sourceWord, langs, translations, 1);
                        if (isInserted)
                            Log.d(" ----- syncDown ----", "inserted");
                        else
                            flag = false;
                    }

                    if (flag){
                        SyncDownDeliveryRequest syncDownDeliveryRequest = new SyncDownDeliveryRequest(LoggedInUser);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(syncDownDeliveryRequest);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SyncDownRequest syncDownRequest = new SyncDownRequest(LoggedInUser, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(syncDownRequest);
    }

    public static void Populate(ArrayList<Integer> ShuffledIndexes, Cursor cursor, TableLayout tl, final Context context){

        final int fontSize = 30;

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        cursor.moveToPosition(ShuffledIndexes.get(0));
        final HashMap<String, Object> texts = new HashMap<>();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            if (cursor.getString(i) != null)
                texts.put(cursor.getColumnName(i), cursor.getString(i));
            else
                texts.put(cursor.getColumnName(i), "Not Defined");
        }
        Log.d("--- texts keys ---", texts.keySet().toString());
        Log.d("--- texts values ---", texts.values().toString());

        // programmatically create fields to hold flags and translations

        // creating source field
        TableRow row = new TableRow(context);
        TableLayout.LayoutParams tvParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0, 40, 0, 40);
        row.setLayoutParams(tvParams);

        TextView tv = new TextView(context);
        tv.setText(texts.get("source").toString());
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(fontSize);
        tv.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);


        // Add the tv to row.
        row.addView(tv);

        // Add the row to TableLayout.
        tl.addView(row, tvParams);

        TableRow[] rows = new TableRow[langs.length];
        final TextView[] buttons = new TextView[langs.length];

        for (int i=0; i < langs.length; i++){

            final String langsElement = langs[i];  // the language at index i.

            // Create a new row to be added.
            rows[i] = new TableRow(context);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 40, 0, 40);
            rows[i].setLayoutParams(params);

            // Create a button to be the row-content.
            buttons[i] = new Button(context);

            // putting flag of the language at index 'i', in the background of the button
            int resID = context.getResources().getIdentifier(langsElement , "drawable", context.getPackageName()); // resID is id of the resource with the name in langsElement
            buttons[i].setBackgroundResource(resID);

            // clicking the button makes the flag transparent and the text visible
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;

                    int resID = context.getResources().getIdentifier(langsElement + "_transparent" , "drawable", context.getPackageName());
                    button.setBackgroundResource(resID);
                    button.setText(texts.get(langsElement).toString());
                    button.setTextColor(Color.BLACK);
                    button.setTextSize(fontSize);
                    button.setTransformationMethod(null); // preventing all caps
                }
            });

            // Add the button to row.
            rows[i].addView(buttons[i]);

            // Add the row to TableLayout.
            tl.addView(rows[i], params);
        }
    }

    //TODO access sharedPrefs and DatabaseHelper outside onCreate using this type of function to avoid repititive calls to them
    public static SharedPreferences getPref(Context Context){
        return PreferenceManager.getDefaultSharedPreferences(Context);
    }

}
