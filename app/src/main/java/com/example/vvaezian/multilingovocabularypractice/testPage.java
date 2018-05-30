package com.example.vvaezian.multilingovocabularypractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class testPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);
        final String[] langs = new String[]{"French", "German", "Spanish"};  // This will be fetched from the duolingo-api
        TableRow[] rows = new TableRow[langs.length];
        //Button[] buttons = new Button[langs.length];
        final TextView[] buttons = new TextView[langs.length];

        for (int i=0; i < langs.length; i++){

            final String langsElement = langs[i];  // the language of index i. Since it will be used "globally", needs to be declared final here

            // Create a new row to be added.
            rows[i] = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 40, 0, 40);
            rows[i].setLayoutParams(params);

            // Create a button to be the row-content.
            buttons[i] = new Button(this);
            buttons[i].setText("");

            // put flag of language at index 'i' in the the background of the button
            String flagName = langsElement.toLowerCase();
            int resID = getResources().getIdentifier(flagName , "drawable", getPackageName());
            buttons[i].setBackgroundResource(resID);

            // clicking the button makes the flag transparent and the text visible
            buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;

                        String transparentFlagName = langsElement.toLowerCase() + "_transparent";
                        int resID = getResources().getIdentifier(transparentFlagName , "drawable", getPackageName());
                        button.setBackgroundResource(resID);
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