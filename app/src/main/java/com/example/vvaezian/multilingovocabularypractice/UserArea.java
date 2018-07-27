package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.example.vvaezian.multilingovocabularypractice.R.id.tvGreet;

public class UserArea extends ActionBar {

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
            tvLangsStats.setText("Chosen Languages for Practice: \n" + langStats.toString());
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
            wordsStats.setText("You have " + rowCounts + " records in your database");
        }

        ConstraintLayout clEditTablePageTransition = (ConstraintLayout) findViewById(R.id.CLgoToEditTablePageArea);

        if (langsConcated.length() == 0 || userDB.wordsTableIsEmpty()){  // checking if no language selected or no words added
            clEditTablePageTransition.setVisibility(View.GONE);
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

        TextView wordsStats = (TextView) findViewById(R.id.tvWordsStats);
        long rowCounts = userDB.getRowCount();
        if (rowCounts == 0){
            wordsStats.setText("You don't have any records in your database");
        }
        else if (rowCounts == 1) {
            wordsStats.setText("You have 1 record in your database");
        }
        else {
            wordsStats.setText("You have " + rowCounts + " records in your database");
        }

        // show button for going to practice page if languages selected and words added
        ConstraintLayout clEditTablePageTransition = (ConstraintLayout) findViewById(R.id.CLgoToEditTablePageArea);
        if (langsConcated.length() != 0 && !userDB.wordsTableIsEmpty()) {
            clEditTablePageTransition.setVisibility(View.VISIBLE);
        }

        Boolean syncedUp = sp.getBoolean("syncedUp", false);
        if (!syncedUp) {
            Log.d("out", "Syncing ...");
            HelperFunctions.syncUp(getApplicationContext());
        }
    }

    public void BtnSelectLangsClicked(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void BtnAddWordsClicked(View view) {
        Intent intent = new Intent(this, EditTablePage.class);
        startActivity(intent);
    }

    public void BtnTestClicked(View view) {
        Intent intent = new Intent(this, PracticePage.class);
        startActivity(intent);
    }

}
