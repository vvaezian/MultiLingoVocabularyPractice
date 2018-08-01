package com.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Settings extends ActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        // making the user's languages checked
        ConstraintLayout checkBoxesArea = (ConstraintLayout) findViewById(R.id.CLcheckBoxesArea1);
        for (int i=0; i < checkBoxesArea.getChildCount(); i++){
            CheckBox cb = (CheckBox) checkBoxesArea.getChildAt(i);
            // the method abbreviate is defined in HelperFunctions class (it has been imported)
            String cbText = HelperFunctions.abbreviate(cb.getText().toString());
            for (String lang:langs)
                if (cbText.equals(lang))
                    cb.setChecked(true);
        }
        ConstraintLayout checkBoxesArea2 = (ConstraintLayout) findViewById(R.id.CLcheckBoxesArea2);
        for (int i=0; i < checkBoxesArea2.getChildCount(); i++){
            CheckBox cb = (CheckBox) checkBoxesArea2.getChildAt(i);
            // the method abbreviate is defined in HelperFunctions class (it has been imported)
            String cbText = HelperFunctions.abbreviate(cb.getText().toString());
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
                CheckBox cbNl = (CheckBox) findViewById(R.id.cbNl);
                CheckBox cbChS = (CheckBox) findViewById(R.id.cbZh_CN);
                CheckBox cbChT = (CheckBox) findViewById(R.id.cbZh_TW);
                CheckBox cbAr = (CheckBox) findViewById(R.id.cbAr);
                CheckBox cbFa = (CheckBox) findViewById(R.id.cbFa);
                CheckBox cbHi = (CheckBox) findViewById(R.id.cbHi);
                CheckBox cbPt = (CheckBox) findViewById(R.id.cbPt);
                CheckBox cbRu = (CheckBox) findViewById(R.id.cbRu);
                CheckBox cbSw = (CheckBox) findViewById(R.id.cbSw);
                CheckBox cbTu = (CheckBox) findViewById(R.id.cbTu);
                CheckBox cbJa = (CheckBox) findViewById(R.id.cbJa);

                if (cbEn.isChecked()) langs += HelperFunctions.abbreviate(cbEn.getText().toString()) + " ";
                if (cbFr.isChecked()) langs += HelperFunctions.abbreviate(cbFr.getText().toString()) + " ";
                if (cbIt.isChecked()) langs += HelperFunctions.abbreviate(cbIt.getText().toString()) + " ";
                if (cbGer.isChecked()) langs += HelperFunctions.abbreviate(cbGer.getText().toString()) + " ";
                if (cbSp.isChecked()) langs += HelperFunctions.abbreviate(cbSp.getText().toString()) + " ";
                if (cbNl.isChecked()) langs += HelperFunctions.abbreviate(cbNl.getText().toString()) + " ";
                if (cbRu.isChecked()) langs += HelperFunctions.abbreviate(cbRu.getText().toString()) + " ";
                if (cbSw.isChecked()) langs += HelperFunctions.abbreviate(cbSw.getText().toString()) + " ";
                if (cbTu.isChecked()) langs += HelperFunctions.abbreviate(cbTu.getText().toString()) + " ";
                if (cbJa.isChecked()) langs += HelperFunctions.abbreviate(cbJa.getText().toString()) + " ";
                if (cbAr.isChecked()) langs += HelperFunctions.abbreviate(cbAr.getText().toString()) + " ";
                if (cbFa.isChecked()) langs += HelperFunctions.abbreviate(cbFa.getText().toString()) + " ";
                if (cbHi.isChecked()) langs += HelperFunctions.abbreviate(cbHi.getText().toString()) + " ";
                if (cbPt.isChecked()) langs += HelperFunctions.abbreviate(cbPt.getText().toString()) + " ";
                if (cbChS.isChecked()) langs += HelperFunctions.abbreviate(cbChS.getText().toString()) + " ";
                if (cbChT.isChecked()) langs += HelperFunctions.abbreviate(cbChT.getText().toString()) + " ";

                //Adding selected languages to the Shared Preferences
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
