package com.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ActionBar extends AppCompatActivity {

    /* handling actionBar */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addWords:
                //go to edit table page
                Intent intent1 = new Intent(this, EditTablePage.class);
                startActivity(intent1);
                return true;

            case R.id.action_settings:
                //go to edit table page
                Intent intent2 = new Intent(this, Settings.class);
                startActivity(intent2);
                return true;

            case R.id.action_logout:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefEditor = sp.edit();
                prefEditor.putBoolean("loggedIn", false);  // writing in sharedPreference that no one is logged in
                prefEditor.apply();

                Intent intent3 = new Intent(this, LoginPage.class);
                startActivity(intent3);
                finish();  // to prevent getting back to UserArea page by pressing 'back' button
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    /* end of handling actionBar */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
    }
}
