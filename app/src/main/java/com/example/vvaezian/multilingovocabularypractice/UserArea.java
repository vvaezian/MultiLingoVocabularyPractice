package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static com.example.vvaezian.multilingovocabularypractice.R.id.tvGreet;

public class UserArea extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView etGreetings = (TextView) findViewById(tvGreet);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        String msg = "Welcome " + username;
        etGreetings.setText(msg);
    }

    public void BtnEditClicked(View view) {
        Intent intent = new Intent(this, EditTablePage.class);
        startActivity(intent);
    }


    public void BtnLogOutClicked(View view) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefEditor = sp.edit();
        prefEditor.putBoolean("loggedIn", false);
        prefEditor.apply();
        //sp.edit().putBoolean("loggedIn",false).apply();  // writing in sharedPreference that no one is logged in
        //sp.edit().putString("user", "").apply();

        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}
