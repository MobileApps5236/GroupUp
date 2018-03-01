package com.example.tonyrobb.groupup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;


public class Login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        //if (auth.getCurrentUser() != null) {
        //    Intent intent = new Intent(Login.this, MainPage.class);
        //    startActivity(intent);
        //}

        // If user is not logged in then continue as normal
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.btnLogin);
        Button btnSignUp = (Button) findViewById(R.id.btnCreateAccount);
        Button btnReset = (Button) findViewById(R.id.btn_reset_password);

        inputEmail = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);

        // Get another instance
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });

        Log.v("TAG", "onCreate triggered");
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Time to authenticate user
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
        Log.v("TAG", "onPause triggered");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("TAG","onResume triggered");
    }

    private void loginOperation(String username, String password){
        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
