package com.example.tonyrobb.groupup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

public class CreateAccountFragment extends Fragment {
    private Button btnSignUp;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputConfirmPassword;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);
        auth = FirebaseAuth.getInstance();

        btnSignUp = (Button) v.findViewById(R.id.sign_up_button);
        inputEmail = (EditText) v.findViewById(R.id.email);
        inputPassword = (EditText) v.findViewById(R.id.password);
        inputConfirmPassword = (EditText) v.findViewById(R.id.confirm_password);

        btnSignUp.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)){
                    Toast.makeText(getActivity().getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //This is where a user is created
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "User successfully created ", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStackImmediate();
                        }
                    }
                });
            }
        }));
        return v;
    }
}
