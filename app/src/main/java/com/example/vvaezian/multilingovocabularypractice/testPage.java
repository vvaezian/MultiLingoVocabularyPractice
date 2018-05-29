package com.example.vvaezian.multilingovocabularypractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class testPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);
        String[] langs = new String[]{"French", "German", "Spanish"};  // This will be fetched from the duolingo-api
        TableRow[] rows = new TableRow[langs.length];
        Button[] button = new Button[langs.length];

        for (int i=0; i < langs.length; i++){

            /* Create a new row to be added. */
            rows[i] = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 200, 0, 200);
            rows[i].setLayoutParams(params);

            /* Create a Button to be the row-content. */
            button[i] = new Button(this);
            button[i].setText(langs[i]);

            //button[i].setBackgroundResource(R.drawable.italy);

            /* Add Button to row. */
            rows[i].addView(button[i]);

            /* Add row to TableLayout. */
            //rows[i].setBackgroundResource(R.drawable.france);
            tl.addView(rows[i], params);
        }

    }
}