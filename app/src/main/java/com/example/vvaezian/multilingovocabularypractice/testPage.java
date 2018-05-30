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

        final String[] langs = new String[]{"French", "German", "Spanish", "Italian"};  // This will be fetched from the duolingo-api
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
            String flagName = langsElement.toLowerCase();   // to lowercase because drawable folder doesn't accept capital letters and
                                                            // duolingo-api returns language names with the first letter capitalized
            int resID = getResources().getIdentifier(flagName , "drawable", getPackageName()); // resID is id of the resource named 'flagName'
            buttons[i].setBackgroundResource(resID);

            // clicking the button makes the flag transparent and the text visible
            buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;

                        String transparentFlagName = langsElement.toLowerCase() + "_transparent";
                        int resID = getResources().getIdentifier(transparentFlagName , "drawable", getPackageName());
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