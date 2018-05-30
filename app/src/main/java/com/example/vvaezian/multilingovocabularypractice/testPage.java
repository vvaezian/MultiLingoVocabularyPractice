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
        String[] langs = new String[]{"French", "German", "Spanish"};  // This will be fetched from the duolingo-api
        TableRow[] rows = new TableRow[langs.length];
        //Button[] buttons = new Button[langs.length];
        TextView[] buttons = new TextView[langs.length];

        for (int i=0; i < langs.length; i++){

            /* Create a new row to be added. */
            rows[i] = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 80, 0, 80);
            rows[i].setLayoutParams(params);

            /* Create a textview to be the row-content. */
            buttons[i] = new Button(this);
            buttons[i].setText(langs[i]);
            //buttons[i].setBackgroundColor(0xFF00FF00);
            buttons[i].setBackgroundResource(R.drawable.france);

            buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        button.setVisibility(View.INVISIBLE);
                    }
                });
            /* Add Button to row. */
            rows[i].addView(buttons[i]);


            /* Add row to TableLayout. */
            //rows[i].setBackgroundResource(R.drawable.france);
            tl.addView(rows[i], params);
        }

    }
}