package com.example.tonyrobb.groupup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;


public class Login extends AppCompatActivity {

    private Button loginBtn, btnSignUp;
    private CheckBox chkboxRememberMe;
    private EditText inputEmail, inputPassword;
    private String email, password;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // If user is not logged in then continue as normal
        setContentView(R.layout.activity_login);
        Log.v("Login", "onCreate triggered");

        loginBtn = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnCreateAccount);
        chkboxRememberMe = (CheckBox) findViewById(R.id.chkboxRemember);
        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            inputEmail.setText(loginPreferences.getString("email", ""));
            inputPassword.setText(loginPreferences.getString("password", ""));
            chkboxRememberMe.setChecked(true);
        }

        // Get another instance
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() > 6) {
                                        Toast.makeText(Login.this, "Wrong email or password", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Login.this, "Password too short (under 6 characters)", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (chkboxRememberMe.isChecked()) {
                                        loginPrefsEditor.putBoolean("saveLogin", true);
                                        loginPrefsEditor.putString("email", email);
                                        loginPrefsEditor.putString("password", password);
                                        loginPrefsEditor.commit();
                                    } else {
                                        loginPrefsEditor.clear();
                                        loginPrefsEditor.commit();
                                    }

                                    Intent intent = new Intent(Login.this, MainPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Login", "onPause triggered");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("Login","onResume triggered");
    }
}
