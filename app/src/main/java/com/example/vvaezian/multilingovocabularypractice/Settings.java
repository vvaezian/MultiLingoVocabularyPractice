package com.example.vvaezian.multilingovocabularypractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Settings extends AppCompatActivity {

    String abbreviate(String langName){
        String output = "";
        switch (langName) {
            case "French": output = "fr";
                break;
            case "English": output = "en";
                break;
            case "German": output = "de";
                break;
            case "Spanish": output = "es";
                break;
            case "Italian": output = "it";
                break;
                // think of a default value to add
        }
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Button btnSave = (Button) findViewById(R.id.btnSaveLangs);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String langs = "";
                CheckBox cbFr = (CheckBox) findViewById(R.id.cbFr);
                CheckBox cbEn = (CheckBox) findViewById(R.id.cbEn);
                CheckBox cbIt = (CheckBox) findViewById(R.id.cbIt);
                CheckBox cbGer = (CheckBox) findViewById(R.id.cbGer);
                CheckBox cbSp = (CheckBox) findViewById(R.id.cbSp);
                if (cbEn.isChecked()){
                    langs += abbreviate(cbEn.getText().toString()) + " ";
                }
                if (cbFr.isChecked()){
                    langs += abbreviate(cbFr.getText().toString()) + " ";
                }
                if (cbIt.isChecked()){
                    langs += abbreviate(cbIt.getText().toString()) + " ";
                }
                if (cbGer.isChecked()){
                    langs += abbreviate(cbGer.getText().toString()) + " ";
                }
                if (cbSp.isChecked()){
                    langs += abbreviate(cbSp.getText().toString()) + " ";
                }

                //Adding selected languages to the Shared Preferences
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefEditor = sp.edit();
                String username = sp.getString("user", "");
                prefEditor.remove(username);    // clearing the previous value
                prefEditor.putString(username, langs);  // adding the new value
                prefEditor.apply();
            }
        });
    }
    // this is needed.
    public void onCheckboxClicked(View view) {
        // Clicking the checkboxes isn't gonna do anything
    }
}
