package com.example.vvaezian.multilingovocabularypractice;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class testPage extends ActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(getApplicationContext());

        if (!userDB.wordsTableIsEmpty()) {
            Cursor cursor = userDB.getAllData(langs);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                int rowsCount = cursor.getCount();

                ArrayList<Integer> indexes = new ArrayList<>();
                for (int i = 0; i < rowsCount; i++)
                    indexes.add(i);
                Collections.shuffle(indexes);
                Log.d("--- shuffled ---", indexes.toString());

                for (int j = 0; j < rowsCount; j++) {
                    cursor.moveToPosition(indexes.get(j));
                    HashMap<String, Object> texts = new HashMap<>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        if (cursor.getString(i) != null)
                            texts.put(cursor.getColumnName(i), cursor.getString(i));
                        else
                            texts.put(cursor.getColumnName(i), "Not Defined");
                    }
                    Log.d("--- texts ---", texts.values().toString());

                }
                cursor.close();
            }
        }

        // programmatically create fields to hold flags and translations
        TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);
        TableRow[] rows = new TableRow[langs.length];
        final TextView[] buttons = new TextView[langs.length];

        for (int i=0; i < langs.length; i++){

            final String langsElement = langs[i];  // the language at index i.

            // Create a new row to be added.
            rows[i] = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 40, 0, 40);
            rows[i].setLayoutParams(params);

            // Create a button to be the row-content.
            buttons[i] = new Button(this);

            // putting flag of the language at index 'i', in the the background of the button
            int resID = getResources().getIdentifier(langsElement , "drawable", getPackageName()); // resID is id of the resource with the name in langsElement
            buttons[i].setBackgroundResource(resID);

            // clicking the button makes the flag transparent and the text visible
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;

                    int resID = getResources().getIdentifier(langsElement + "_transparent" , "drawable", getPackageName());
                    button.setBackgroundResource(resID);
                    button.setText(langsElement);
                }
            });

            // Add the button to row.
            rows[i].addView(buttons[i]);

            // Add the row to TableLayout.
            tl.addView(rows[i], params);
        }
    }


    //
    public void BtnNextClicked(View view) {

        ScrollView scroll = (ScrollView) findViewById(R.id.scrolView);
        scroll.setFocusableInTouchMode(true);
        //scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroll.fullScroll(View.FOCUS_UP);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        // programmatically create fields to hold flags and translations
        final TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);

        // clear the page
        tl.removeAllViews();
        final Button btnNext = (Button) findViewById(R.id.BtnNext);
        btnNext.setVisibility(View.GONE);

        // Handler will make the inside code be executed with delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                TableRow[] rows = new TableRow[langs.length];
                final TextView[] buttons = new TextView[langs.length];

                for (int i=0; i < langs.length; i++){

                    final String langsElement = langs[i];  // the language at index i.

                    // Create a new row to be added.
                    rows[i] = new TableRow(getApplicationContext());
                    TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 40, 0, 40);
                    rows[i].setLayoutParams(params);

                    // Create a button to be the row-content.
                    buttons[i] = new Button(getApplicationContext());

                    // putting flag of the language at index 'i', in the the background of the button
                    int resID = getResources().getIdentifier(langsElement , "drawable", getPackageName()); // resID is id of the resource with the name in langsElement
                    buttons[i].setBackgroundResource(resID);

                    // clicking the button makes the flag transparent and the text visible
                    buttons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button button = (Button) v;

                            int resID = getResources().getIdentifier(langsElement + "_transparent" , "drawable", getPackageName());
                            button.setBackgroundResource(resID);
                            button.setText("test");
                        }
                    });

                    // Add the button to row.
                    rows[i].addView(buttons[i]);

                    // Add the row to TableLayout.
                    tl.addView(rows[i], params);
                }

                btnNext.setVisibility(View.VISIBLE);
            }
        }, 200);   //.2 seconds


    }
}
