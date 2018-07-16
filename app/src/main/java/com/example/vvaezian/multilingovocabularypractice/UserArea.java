package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        final TextView etGreetings = (TextView) findViewById(tvGreet);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String msg = "Welcome " + username;
        etGreetings.setText(msg);
    }

    @Override
    public void onResume(){
        super.onResume();
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean syncedUp = sp.getBoolean("syncedUp", false);
        if (!syncedUp) {
            Log.d("out", "Syncing ...");
            HelperFunctions.syncUp(getApplicationContext());
        }
    }

    public void BtnTestClicked(View view) {
        Intent intent = new Intent(this, testPage.class);
        startActivity(intent);
    }

    public void BtnSyncDownClicked(View view) {
        HelperFunctions.syncDown(getApplicationContext());
    }
}
