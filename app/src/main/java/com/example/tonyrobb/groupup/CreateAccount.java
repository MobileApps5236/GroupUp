package com.example.tonyrobb.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    private Button btnSignUp;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputConfirmPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Log.v("CreateAccount", "onCreate triggered");

        auth = FirebaseAuth.getInstance();

        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
//      inputConfirmPassword = (EditText) findViewById(R.id.confirm_password);

        btnSignUp.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
//              String confirmPassword = inputConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (!inputConfirmPassword.equals(password)){
//                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                //This is where a user is created
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(CreateAccount.this, "Created user: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(CreateAccount.this, MainPage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("CreateAccount", "onPause triggered");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("CreateAccount","onResume triggered");
    }
}
