package com.example.vvaezian.multilingovocabularypractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.cloud.translate.Translation;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditTablePage extends ActionBar {

    GoogleTranslateAPIInterface apiInterface;
    private ProgressBar loadingSpinner;
    Toast mToast;

    public void translateWord(String query, String source, String target, final EditText et) {
        /* Translates 'query' into 'target' language, and shows it in the et EditText */

        String apiKey = BuildConfig.ApiKey;
        //using Retrofit to send a request to Google-Translate-API and receive the response
        Call<GoogleTranslateAPIResponse> call = apiInterface.translateWord(
                query, source, target, apiKey);
        call.enqueue(new Callback<GoogleTranslateAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<GoogleTranslateAPIResponse> call, @NonNull Response<GoogleTranslateAPIResponse> response) {

                loadingSpinner.setVisibility(View.GONE);
                GoogleTranslateAPIResponse apiResponse = response.body();

                if (apiResponse == null) {
                    Toast toast = Toast.makeText(getBaseContext(), "Translation Failed!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }

                assert apiResponse != null;
                GoogleTranslateAPIResponse.Data dataResponse = apiResponse.getData();
                List<Translation> list = dataResponse.getTranslations();
                String returnedText = list.get(0).getTranslatedText();
                String output = returnedText.replace("&#39;","'");
                et.setText(output);
            }

            @Override
            public void onFailure(@NonNull Call<GoogleTranslateAPIResponse> call, @NonNull Throwable t) {
                Log.d("out","fail");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table_page);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DatabaseHelper userDB = HelperFunctions.getDataBaseHelper(this);

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");

        // setting dropDown Spinner items
        final Spinner dropDownSpinner = (Spinner) findViewById(R.id.spSourceLang);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownSpinner.setAdapter(adapter);

        // producing fields to hold translations
        final TableLayout tl = (TableLayout) findViewById(R.id.TranslatedLanguagesArea);

        TableRow[] rows = new TableRow[langs.length];
        final EditText[] et = new EditText[langs.length];

        ImageView iv;

        for (int i=0; i < langs.length; i++){

            // Create a new row to be added.
            rows[i] = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            rows[i].setLayoutParams(params);

            iv = new ImageView(this);
            int resID = getApplicationContext().getResources().getIdentifier(langs[i] + "_icon" , "drawable", getApplicationContext().getPackageName());
            iv.setImageResource(resID);
            iv.setBaselineAlignBottom(true);

            // Create an EditText item to be the row-content.
            et[i] = new EditText(this);
            et[i].setHint("  " + HelperFunctions.deAbbreviate(langs[i]) + " Translation");
            et[i].setGravity(Gravity.LEFT);

            rows[i].addView(iv);
            // Add the button to row.
            rows[i].addView(et[i]);

            // Add the row to TableLayout.
            tl.addView(rows[i], params);
        }

        final EditText etInput = (EditText) findViewById(R.id.etInput);
        Button translateButton = (Button) findViewById(R.id.btnTranslate);
        loadingSpinner = (ProgressBar) findViewById(R.id.TranslateProgressspinner);
        loadingSpinner.setVisibility(View.GONE);
        apiInterface = GoogleTranslateAPIClient.getClient().create(GoogleTranslateAPIInterface.class);

        //producing and setting translations
        translateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                loadingSpinner.setVisibility(View.VISIBLE);

                // closing the virtual keyboard when button is clicked
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                String inputWords = etInput.getText().toString();
                String sourceLang;
                String spinnerText = dropDownSpinner.getSelectedItem().toString();
                if (spinnerText.equals("Auto-Detect"))
                    sourceLang = "";
                else {
                    switch (spinnerText) {
                        case "Chinese (Traditional)":
                            sourceLang = "zh-TW";
                            break;
                        case "Chinese (Simplified)":
                            sourceLang = "zh-CN";
                            break;
                        default:
                            sourceLang = HelperFunctions.abbreviate(spinnerText);
                            break;
                    }
                }

                // looping through EditTexts to set the translations
                for (int i=0; i < tl.getChildCount(); i++){
                    TableRow tr = (TableRow) tl.getChildAt(i);
                    EditText tv = (EditText) tr.getChildAt(1); // index 0 is for icons

                    String targetLang;
                    switch (langs[i]) {
                        case "tw":
                            targetLang = "zh-TW";
                            break;
                        case "ch":
                            targetLang = "zh-CN";
                            break;
                        default:
                            targetLang = langs[i];
                    }

                    translateWord(inputWords, sourceLang, targetLang, tv);
                }
                loadingSpinner.setVisibility(View.GONE);
            }
        });

        //saving the data
        Button btnSave = (Button) findViewById(R.id.btnSave);

        final String[] translations = new String[langs.length];

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sourceWord = etInput.getText().toString();

                if (sourceWord.equals("")) {
                    // make a toast
                    if (mToast == null) { // Initialize toast if needed
                        mToast = Toast.makeText(EditTablePage.this, "", Toast.LENGTH_LONG);
                    }
                    mToast.setText("'source' field is empty.");
                    mToast.show();
                }
                else {
                    // looping through TextViews to get the final version of translated words (user may edit the translated words before save)
                    boolean flag = true; // checking if all fields are empty
                    for (int i = 0; i < tl.getChildCount(); i++) {
                        TableRow tr = (TableRow) tl.getChildAt(i);
                        EditText tv = (EditText) tr.getChildAt(1);
                        String text = tv.getText().toString();
                        if (!text.equals(""))
                            flag = false;
                        translations[i] = text;
                        tv.setText("");
                    }

                    if (flag){
                        // making a toast
                        if (mToast == null) { // Initialize toast if needed
                            mToast = Toast.makeText(EditTablePage.this, "", Toast.LENGTH_LONG);
                        }
                        mToast.setText("No Translation Is Given");
                        mToast.show();
                    }

                    else {
                        boolean isInserted = userDB.insertData(sourceWord, langs, translations, 0);
                        if (isInserted) {
                            etInput.setText("");
                            // making a toast
                            if (mToast == null) { // Initialize toast if needed
                                mToast = Toast.makeText(EditTablePage.this, "", Toast.LENGTH_LONG);
                            }
                            mToast.setText("Data Saved In Your Database");
                            mToast.show();

                            // writing in sharedPreference that a dirty rows added and syncUp is needed
                            SharedPreferences.Editor prefEditor = sp.edit();
                            prefEditor.putBoolean("syncedUp", false);
                            prefEditor.apply();
                        } else {
                            // making a toast
                            if (mToast == null) { // Initialize toast if needed
                                mToast = Toast.makeText(EditTablePage.this, "", Toast.LENGTH_LONG);
                            }
                            mToast.setText("Saving Failed!");
                            mToast.show();
                        }
                    }
                }
            }
        });
    }
}
