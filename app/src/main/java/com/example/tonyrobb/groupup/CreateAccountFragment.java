package com.example.tonyrobb.groupup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccountFragment extends Fragment {
    private Button btnSignUp;
    private EditText inputEmail;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private CheckBox chkboxIsProfessor;

    private FirebaseAuth auth;
    private DatabaseReference databaseUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);
        auth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        btnSignUp = v.findViewById(R.id.sign_up_button);
        inputEmail = v.findViewById(R.id.email);
        inputFirstName = v.findViewById(R.id.first_name);
        inputLastName = v.findViewById(R.id.last_name);
        inputPassword = v.findViewById(R.id.password);
        inputConfirmPassword = v.findViewById(R.id.confirm_password);
        chkboxIsProfessor = v.findViewById(R.id.professor_checkbox);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp();
            }
        });
        return v;
    }

    private void signUp() {

        final String email = inputEmail.getText().toString().trim();
        final String firstName = inputFirstName.getText().toString().trim();
        final String lastName = inputLastName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity().getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {

            Toast.makeText(getActivity().getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {

            Toast.makeText(getActivity().getApplicationContext(), "Password too short (under 6 characters)", Toast.LENGTH_LONG).show();
            return;
        } else if (!password.equals(confirmPassword)) {

            Toast.makeText(getActivity().getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ConnectivityManager connectionManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectionManager.getActiveNetworkInfo();

        if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
            Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        //This is where a user is created
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                } else {
                    addUser(email, firstName, lastName, chkboxIsProfessor.isChecked());

                    Toast.makeText(getActivity(), "User successfully created ", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
    }

    private void addUser(String email, String firstName, String lastName, boolean isProf) {
        String userId = auth.getCurrentUser().getUid();
        HashMap<String, Section> sectionsList = new HashMap<>();
        HashMap<String, Group> enrolledInGroup = new HashMap<>();

        User user = new User(userId, email, firstName, lastName, isProf, sectionsList, enrolledInGroup);
        databaseUsers.child(userId).setValue(user);
    }
}
