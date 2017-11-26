package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {

    //DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //myDB = new DatabaseHelper(this);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button btnRegister = (Button) findViewById(R.id.BtnReg);
        final Button btnLogin = (Button) findViewById(R.id.BtnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                String username = jsonResponse.getString("username");

                                Intent intent = new Intent(LoginPage.this, UserArea.class);
                                intent.putExtra("username", username);

                                LoginPage.this.startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                queue.add(loginRequest);
            }
        });

    }

    public void BtnRegClicked(View view) {
        Intent intent = new Intent(this, RegisterationPage.class);
        startActivity(intent);
    }
}
