package com.example.vvaezian.multilingovocabularypractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

        // programmatically create fields to hold flags and translations
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
