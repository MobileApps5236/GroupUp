package com.example.tonyrobb.groupup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button loginBtn, btnSignUp;
    private CheckBox chkboxRememberMe;
    private EditText inputEmail, inputPassword;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private FirebaseAuth auth;
    private static final String KEY = "2Icgi778begEFK89";

    ConnectivityManager connectionManager;
    NetworkInfo activeNetwork;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        auth = FirebaseAuth.getInstance();
        // If user is not logged in then continue as normal
        Log.v("Login", "onCreate triggered");

        loginBtn = (Button) v.findViewById(R.id.btnLogin);
        btnSignUp = (Button) v.findViewById(R.id.btnCreateAccount);
        chkboxRememberMe = (CheckBox) v.findViewById(R.id.chkboxRemember);
        inputEmail = (EditText) v.findViewById(R.id.username);
        inputPassword = (EditText) v.findViewById(R.id.password);
        loginPreferences = this.getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            inputEmail.setText(loginPreferences.getString("email", ""));
            try {
                String decryptedPassword = decrypt(loginPreferences.getString("password", ""));
                inputPassword.setText(decryptedPassword);
            } catch (Exception e) {

            }
            chkboxRememberMe.setChecked(true);
        }

        connectionManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = connectionManager.getActiveNetworkInfo();

        // Get another instance
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                activeNetwork = connectionManager.getActiveNetworkInfo();

                if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                CreateAccountFragment createAccountFragment = new CreateAccountFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createAccountFragment, "nextFrag")
                        .addToBackStack(null).commit();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                login();
            }
        });
        return v;
    }

    private void login() {

        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        activeNetwork = connectionManager.getActiveNetworkInfo();

        if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
            Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            if (password.length() >= 6) {
                                Toast.makeText(getActivity().getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Password too short (under 6 characters)", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (chkboxRememberMe.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("email", email);
                                try {
                                    String encryptedPassword = encrypt(password);
                                    loginPrefsEditor.putString("password", encryptedPassword);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                loginPrefsEditor.apply();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainMenu.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
    }

    public static String encrypt(String value) throws Exception{
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedyteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedyteValue, Base64.DEFAULT);
        return encryptedValue64;
    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(KEY.getBytes(),"AES");
        return key;
    }

}
