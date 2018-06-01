package com.example.vvaezian.multilingovocabularypractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class LoginPage extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button btnRegister = (Button) findViewById(R.id.BtnReg);
        final Button btnLogin = (Button) findViewById(R.id.BtnLogin);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // sp = getSharedPreferences("login", MODE_PRIVATE);

        spinner = (ProgressBar) findViewById(R.id.LoginProgressspinner);
        spinner.setVisibility(View.GONE);

        if(sp.getBoolean("loggedIn",false)){  //checking if anyone is logged in
            String LoggedInUser = sp.getString("user",""); // retrieve the username of the logged in person
            if (!LoggedInUser.equals("")) {  // if the username was retrieved correctly
                Intent intent = new Intent(LoginPage.this, UserArea.class); // go to the UserArea page without loging in
                intent.putExtra("username", LoggedInUser);  // we use this extra info in the 'UserArea' page
                LoginPage.this.startActivity(intent);
                finish();  // prevent getting back to this page by pressing 'back' button
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                spinner.setVisibility(View.VISIBLE);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                spinner.setVisibility(View.GONE);
                                String username = jsonResponse.getString("username");

                                SharedPreferences.Editor prefEditor = sp.edit();
                                prefEditor.putBoolean("loggedIn", true);  // writing in sharedPreference that a user is logged in
                                prefEditor.putString("user", username);     // writing in sharedPreference which user is logged in
                                prefEditor.apply();

                                Intent intent = new Intent(LoginPage.this, UserArea.class);
                                intent.putExtra("username", username);  // we use this extra info in the 'UserArea' page
                                LoginPage.this.startActivity(intent);
                                finish();  // prevent getting back to this page by pressing 'back' button

                            } else {
                                spinner.setVisibility(View.GONE);
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
