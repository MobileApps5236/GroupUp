package com.example.tonyrobb.groupup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Egg on 3/8/2018.
 */

public class LoginFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        auth = FirebaseAuth.getInstance();
        System.out.println("=========================");
        System.out.println(auth);

        // If user is not logged in then continue as normal
        Log.v("Login", "onCreate triggered");

        Button loginBtn = (Button) v.findViewById(R.id.btnLogin);
        Button btnSignUp = (Button) v.findViewById(R.id.btnCreateAccount);

        inputEmail = (EditText) v.findViewById(R.id.username);
        inputPassword = (EditText) v.findViewById(R.id.password);

        // Get another instance
        auth = FirebaseAuth.getInstance();
/*  Still working on this
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });
*/

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println(email);
                System.out.println(password);
                // Authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() > 6) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Password too short (under 6 characters)", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), MainPage.class);
                                    startActivity(intent);

                                    getActivity().finish();
                                }
                            }
                        });
            }
        });
        return v;
    }
}
