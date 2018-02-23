package com.example.tonyrobb.groupup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.btnLogin);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        Log.v("TAG", "onCreate triggered");
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginOperation(username.getText().toString(), password.getText().toString());
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
