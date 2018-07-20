package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import static com.example.vvaezian.multilingovocabularypractice.HelperFunctions.abbreviate;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        // making the user's languages checked
        ConstraintLayout checkBoxesArea = (ConstraintLayout) findViewById(R.id.CLcheckBoxesArea);
        for (int i=0; i < checkBoxesArea.getChildCount(); i++){
            CheckBox cb = (CheckBox) checkBoxesArea.getChildAt(i);
            // the method abbreviate is defined in HelperFunctions class (it has been imported)
            String cbText = abbreviate(cb.getText().toString());
            for (String lang:langs)
                if (cbText.equals(lang))
                    cb.setChecked(true);
        }

        //saving user's language choices
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
                //final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefEditor = sp.edit();
                String username = sp.getString("user", "");
                prefEditor.remove(username);    // clearing the previous value
                prefEditor.putString(username, langs);  // adding the new value
                boolean res = prefEditor.commit();
                if (res)
                    Toast.makeText(Settings.this, "Settings Saved", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Settings.this, "Saving Failed!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.this, UserArea.class);
                Settings.this.startActivity(intent);
                finish();  // prevent getting back to this page by pressing 'back' button
            }
        });

    }
    // this is needed.
    public void onCheckboxClicked(View view) {
        // Clicking the checkboxes isn't gonna trigger any action
    }
}
