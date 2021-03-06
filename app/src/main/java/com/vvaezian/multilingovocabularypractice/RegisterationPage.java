package com.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterationPage extends AppCompatActivity {

    private ProgressBar spinner;  // for showing loading spinner while logging in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_page);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button btnRegister = (Button) findViewById(R.id.BtnReg);

        spinner = (ProgressBar) findViewById(R.id.LoginProgressspinner);
        spinner.setVisibility(View.GONE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                spinner.setVisibility(View.VISIBLE);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean avail = jsonResponse.getBoolean("available");

                            if (success) {
                                spinner.setVisibility(View.GONE);
                                Intent intent = new Intent(RegisterationPage.this, LoginPage.class);
                                RegisterationPage.this.startActivity(intent);
                                finish();  // prevent getting back to this page by pressing 'back' button
                            }
                            else {
                                spinner.setVisibility(View.GONE);
                                String msg = avail ? "Registeration Failed" : "Username is taken. Try a different username.";
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterationPage.this);
                                builder.setMessage(msg)
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterationPage.this);
                queue.add(registerRequest);
            }
        });
    }
}