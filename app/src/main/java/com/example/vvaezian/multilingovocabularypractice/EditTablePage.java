package com.example.vvaezian.multilingovocabularypractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditTablePage extends AppCompatActivity {

    EditText etEN, etFR;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_table_page);

        etEN = (EditText) findViewById(R.id.etEN);
        etFR = (EditText) findViewById(R.id.etFR);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String french = etFR.getText().toString();
                final String english = etEN.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast toast = Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getBaseContext(), "Saving Failed!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                EditTableRequest editTableRequest = new EditTableRequest(english, french, responseListener);
                RequestQueue queue = Volley.newRequestQueue(EditTablePage.this);
                queue.add(editTableRequest);
            }
        });
    }
}
