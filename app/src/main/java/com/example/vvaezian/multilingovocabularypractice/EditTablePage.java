package com.example.vvaezian.multilingovocabularypractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.cloud.translate.Translation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditTablePage extends ActionBar {

    GoogleTranslateAPIInterface apiInterface;
    private ProgressBar spinner;

    public void translateWord(String Query, String target, final TextView et) {
        /* This function translates the 'query' into 'target' language, and shows it in the et EditText */
        String apiKey = BuildConfig.ApiKey;
        //using Retrofit to send a request to Google-Translate-API and receive the response
        Call<GoogleTranslateAPIResponse> call = apiInterface.translateWord(
                Query, target,apiKey);
        call.enqueue(new Callback<GoogleTranslateAPIResponse>() {
            @Override
            public void onResponse(Call<GoogleTranslateAPIResponse> call, Response<GoogleTranslateAPIResponse> response) {

                spinner.setVisibility(View.GONE);
                GoogleTranslateAPIResponse apiResponse = response.body();

                if (apiResponse == null) {
                    Toast toast = Toast.makeText(getBaseContext(), "Translation Failed!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }

                GoogleTranslateAPIResponse.Data dataResponse = apiResponse.getData();
                List<Translation> list = dataResponse.getTranslations();
                String returnedText = list.get(0).getTranslatedText();
                et.setText(returnedText);
            }

            @Override
            public void onFailure(Call<GoogleTranslateAPIResponse> call, Throwable t) {
                Log.d("out","fail");
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table_page);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        // getting user's languages from shared preferences
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String LoggedInUser = sp.getString("user","");
        final String langsConcated = sp.getString(LoggedInUser,"");
        final String[] langs = langsConcated.split(" ");
        final String[] translations = new String[langs.length];

        // producing fields to hold translations
        final TableLayout tl = (TableLayout) findViewById(R.id.TranslatedLanguagesArea);
        TableRow[] rows = new TableRow[langs.length];
        final EditText[] et = new EditText[langs.length];

        for (int i=0; i < langs.length; i++){

            final String langsElement = langs[i];  // the language at index i.

            // Create a new row to be added.
            rows[i] = new TableRow(this);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            rows[i].setLayoutParams(params);

            // Create an EditText item to be the row-content.
            et[i] = new EditText(this);
            et[i].setText(langs[i]);

            // Add the button to row.
            rows[i].addView(et[i]);

            // Add the row to TableLayout.
            tl.addView(rows[i], params);
        }

        final EditText etInput = (EditText) findViewById(R.id.etInput);
        Button translateButton = (Button) findViewById(R.id.btnTranslate);
        spinner = (ProgressBar) findViewById(R.id.TranslateProgressspinner);
        spinner.setVisibility(View.GONE);
        apiInterface = GoogleTranslateAPIClient.getClient().create(GoogleTranslateAPIInterface.class);

        //producing and setting translations
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spinner.setVisibility(View.VISIBLE);
                // closing the virtual keyboard when button is clicked
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                String inputWords = etInput.getText().toString();

                // looping through EditTexts to set the translations
                for (int i=0; i < tl.getChildCount(); i++){
                    TableRow tr = (TableRow) tl.getChildAt(i);
                    EditText tv = (EditText) tr.getChildAt(0);
                    translateWord(inputWords, langs[i], tv);
                }
                spinner.setVisibility(View.GONE);
            }
        });

        //saving the data
        Button btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceWord = etInput.getText().toString();

                // looping through TextViews to get the final version of translated words (user may edit the translated words before save)
                for (int i=0; i < tl.getChildCount(); i++) {
                    TableRow tr = (TableRow) tl.getChildAt(i);
                    EditText tv = (EditText) tr.getChildAt(0);
                    translations[i] = tv.getText().toString();

                    com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    final Toast toast = Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.show();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 200);
                                } else {
                                    Toast toast = Toast.makeText(getBaseContext(), "Saving Failed!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    EditTableRequest editTableRequest = new EditTableRequest(sourceWord, translations[i], langs[i], responseListener);
                    RequestQueue queue = Volley.newRequestQueue(EditTablePage.this);
                    queue.add(editTableRequest);
                }
            }
        });
    }
}
