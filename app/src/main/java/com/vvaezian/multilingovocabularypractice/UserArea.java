package com.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.vvaezian.multilingovocabularypractice.R.id.tvGreet;

public class UserArea extends ActionBar {

    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(getApplicationContext());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = sp.getString("user", "");
        String langsConcated = sp.getString(username,"");
        final String[] langs = langsConcated.split(" ");

        TextView greetings = (TextView) findViewById(tvGreet);
        String msg = "Hello " + username;
        greetings.setText(msg);

        TextView tvLangsStats = (TextView) findViewById(R.id.tvLangsStat);
        if (langsConcated.length() == 0){
            tvLangsStats.setText("You have not selected any language to practice");
        }
        else{
            StringBuilder langStats = new StringBuilder();
            for (String lang : langs) {
                langStats.append(HelperFunctions.deAbbreviate(lang));
                langStats.append(", ");
            }
            langStats.deleteCharAt(langStats.length() - 2);
            tvLangsStats.setText(Html.fromHtml("Chosen Languages for Practice: <br>" + "<font color=blue>" + langStats.toString() + "</font>"));
        }

        TextView wordsStats = (TextView) findViewById(R.id.tvWordsStats);

        long rowCounts = userDB.getRowCount();
        if (rowCounts == 0){
            wordsStats.setText("You don't have any records in your database");
        }
        else if (rowCounts == 1) {
            wordsStats.setText("You have 1 record in your database");
        }
        else {
            wordsStats.setText(Html.fromHtml("You have " + "<font color=blue>" + rowCounts + "</font>" + " records in your database"));
        }

        ConstraintLayout CLgoToPracticePageArea = findViewById(R.id.CLgoToPracticePageArea);
        if (langsConcated.length() == 0 || userDB.wordsTableIsEmpty()){  // checking if no language selected or no words added
            CLgoToPracticePageArea.setVisibility(View.GONE);
        }

        // updating local database with dirty rows from the remote
        HelperFunctions.syncDown(getApplicationContext());
    }

    // when getting back to this page, update remote database with dirty rows from local database
    // (because we may get back here from editTable page, and have added some new words)
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = sp.getString("user", "");
        String langsConcated = sp.getString(username,"");

        DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(getApplicationContext());

        TextView wordsStats = findViewById(R.id.tvWordsStats);
        long rowCounts = userDB.getRowCount();
        if (rowCounts == 0){
            wordsStats.setText(R.string.You_dont_have_any_records_in_your_database);
        }
        else if (rowCounts == 1) {
            wordsStats.setText(R.string.You_have_1_record_in_your_database);
        }
        else {
            wordsStats.setText(Html.fromHtml("You have " + "<font color=blue>" + rowCounts + "</font>" + " records in your database"));
        }

        // Don't show button for going to practice page if no languages selected or words added
        ConstraintLayout CLgoToPracticePageArea = findViewById(R.id.CLgoToPracticePageArea);
        if (langsConcated.length() == 0 || userDB.wordsTableIsEmpty()){  // checking if no language selected or no words added
            CLgoToPracticePageArea.setVisibility(View.GONE);
        }

        // show button for going to practice page if languages selected and words added
        if (langsConcated.length() != 0 && !userDB.wordsTableIsEmpty()) {
            CLgoToPracticePageArea.setVisibility(View.VISIBLE);
        }

        Boolean syncedUp = sp.getBoolean("syncedUp", false);
        if (!syncedUp) {
            //Log.d("out", "Syncing ...");
            HelperFunctions.syncUp(getApplicationContext());
        }
    }

    public void BtnSelectLangsClicked(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void BtnAddWordsClicked(View view) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = sp.getString("user", "");
        String langsConcated = sp.getString(username,"");

        if (langsConcated.length() != 0) {
            Intent intent = new Intent(this, EditTablePage.class);
            startActivity(intent);
        }
        else {
            // toast is defined in this way so that subsequent toasts don't have to wait for previous toast to finish
            //TODO: this doesn't work for toast of different activities
            if (mToast == null) { // Initialize toast if needed
                mToast = Toast.makeText(UserArea.this, "", Toast.LENGTH_SHORT);
            }
            mToast.setText("You need to select languages first.");
            mToast.show();
        }
    }

    public void BtnPracticeClicked(View view) {
        Intent intent = new Intent(this, PracticePage.class);
        startActivity(intent);
    }

}
