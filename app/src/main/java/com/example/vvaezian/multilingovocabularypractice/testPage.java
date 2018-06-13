package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

        // when no language is added yet by the user, show only a msg with a button for adding language
        RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.noLanguageArea);
        rl1.setVisibility(View.GONE);
        LinearLayout ll1 = (LinearLayout) findViewById(R.id.editLanguageBox);

        // if it is a new user and no language is added, show the settings page for selecting languages
        if (langs.length == 1 && langs[0].equals("")){  // checking if there is no language added
              Intent intent1 = new Intent(getApplicationContext(), Settings.class);
              testPage.this.startActivity(intent1);
        }


        // if the user has not added any words yet, show a button for transitioning to editTable page
        /*
        if (?){  // checking SQLite db to see if user has not added any words
            rl1.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
            Button btnAddWords = (Button) findViewById(R.id.btnAddWords);
            btnAddWords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(getApplicationContext(), test2Page.class);
                    testPage.this.startActivity(intent1);
                }
            });
        }
        */

        else { // programmatically create fields to hold flags and translations
            TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);
            TableRow[] rows = new TableRow[langs.length];
            final TextView[] buttons = new TextView[langs.length];

            for (int i=0; i < langs.length; i++){

                final String langsElement = langs[i];  // the language at index i. Since it will be used "globally", needs to be declared final here

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


    }
}
