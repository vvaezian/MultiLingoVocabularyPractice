package com.vvaezian.multilingovocabularypractice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class PracticePage extends ActionBar {

    Cursor cursor;
    ArrayList<Integer> ShuffledIndexes;
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_page);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(getApplicationContext());

        if (!userDB.wordsTableIsEmpty()) {
            cursor = userDB.getAllData(langs);
            if ((cursor != null) && (cursor.getCount() > 0)) {
                int rowsCount = cursor.getCount();

                ShuffledIndexes = new ArrayList<>();
                for (int i = 0; i < rowsCount; i++)
                    ShuffledIndexes.add(i);
                Collections.shuffle(ShuffledIndexes);
                //Log.d("--- shuffled ---", ShuffledIndexes.toString());

                TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);
                // to make the items word-wrap
                tl.setColumnShrinkable(0, true);
                tl.setColumnStretchable(0, true);

                HelperFunctions.Populate(ShuffledIndexes, cursor, tl, getApplicationContext());
                ShuffledIndexes.remove(0); // to avoid using this index again
                //cursor.close();
            }
        }
    }


    public void BtnNextClicked(View view) {

        final Button btnNext = (Button) findViewById(R.id.BtnNext);
        final Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnNext.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);

        // back to top
        ScrollView scroll = (ScrollView) findViewById(R.id.scrolView);
        scroll.setFocusableInTouchMode(true);
        scroll.fullScroll(View.FOCUS_UP);

        final TableLayout tl = (TableLayout) findViewById(R.id.LanguageFlagsArea);

        // clear the page
        tl.removeAllViews();

        if (ShuffledIndexes.size() > 0) { // if there are any records

            // Handler will make the inside code be executed with delay
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    HelperFunctions.Populate(ShuffledIndexes, cursor, tl, getApplicationContext());
                    ShuffledIndexes.remove(0); // to avoid using this index again

                    btnNext.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }, 100);   //.1 seconds
        }
        else{
            // Create a new row to be added.
            TableRow row1 = new TableRow(this);
            TableLayout.LayoutParams params1 = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params1.setMargins(0, 240, 0, 40);
            row1.setLayoutParams(params1);

            // Create a tv to be the row-content.
            TextView tv = new TextView(this);
            tv.setText(R.string.You_practiced_all_words_in_your_database);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);

            // Add the tv to row.
            row1.addView(tv);

            // Add the row to TableLayout.
            tl.addView(row1, params1);

            // Create a new row to be added.
            TableRow row2 = new TableRow(this);
            TableLayout.LayoutParams params2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 40, 0, 40);
            row2.setLayoutParams(params2);

            // Create a button to be the row-content.
            Button btnRepractice = new Button(this);
            btnRepractice.setText(R.string.Practice_Again);

            btnRepractice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // getting user's languages from shared preferences
                    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String LoggedInUser = sp.getString("user", "");
                    final String langsConcated = sp.getString(LoggedInUser, "");
                    final String[] langs = langsConcated.split(" ");

                    DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(getApplicationContext());


                    if (!userDB.wordsTableIsEmpty()) {
                        cursor = userDB.getAllData(langs);
                        if ((cursor != null) && (cursor.getCount() > 0)) {
                            int rowsCount = cursor.getCount();

                            //shuffle again
                            ShuffledIndexes = new ArrayList<>();
                            for (int i = 0; i < rowsCount; i++)
                                ShuffledIndexes.add(i);
                            Collections.shuffle(ShuffledIndexes);

                            // clear the page
                            tl.removeAllViews();

                            //populate rows
                            HelperFunctions.Populate(ShuffledIndexes, cursor, tl, getApplicationContext());
                            ShuffledIndexes.remove(0); // to avoid using this index again

                            final Button btnNext = (Button) findViewById(R.id.BtnNext);
                            btnNext.setVisibility(View.VISIBLE);
                            btnDelete.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        // toast is defined in this way so that subsequent toasts don't have to wait for previous toast to finish
                        if (mToast == null) { // Initialize toast if needed
                            mToast = Toast.makeText(PracticePage.this, "", Toast.LENGTH_SHORT);
                        }
                        mToast.setText("You don't have any words in your database!");
                        mToast.show();
                    }
                }
            });

            // Add the button to row.
            row2.addView(btnRepractice);

            // Add the row to TableLayout.
            tl.addView(row2, params2);

            // Create a new row to be added.
            TableRow row3 = new TableRow(this);
            TableLayout.LayoutParams params3 = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params3.setMargins(0, 40, 0, 40);
            row3.setLayoutParams(params2);

            // Create a button to be the row-content.
            Button btnAddWords = new Button(this);
            btnAddWords.setText(R.string.Add_Words);

            btnAddWords.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PracticePage.this, EditTablePage.class);
                    PracticePage.this.startActivity(intent);
                    finish();
                }
            });

            // Add the button to row.
            row3.addView(btnAddWords);

            // Add the row to TableLayout.
            tl.addView(row3, params3);
        }
    }

    public void btnDeleteClicked(View view){

        // Confirm Deleteing
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(getApplicationContext());

                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (userDB.deleteRow(cursor.getString(0)) == 1) {
                            //make a success toast
                            if (mToast == null) { // Initialize toast if needed
                                mToast = Toast.makeText(PracticePage.this, "", Toast.LENGTH_SHORT);
                            }
                            mToast.setText("Deleted Successfully");
                            mToast.show();

                            //go to next record
                            Button btnNext = (Button) findViewById(R.id.BtnNext);
                            btnNext.performClick();
                            //TODO define btnnext at the top
                        }
                        else {
                            // make a failure toast
                            if (mToast == null) { // Initialize toast if needed
                                mToast = Toast.makeText(PracticePage.this, "", Toast.LENGTH_SHORT);
                            }
                            mToast.setText("Deletion Failed!");
                            mToast.show();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Do nothing if 'no' was selected
                        break;
                }
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
