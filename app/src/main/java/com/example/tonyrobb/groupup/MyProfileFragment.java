package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileFragment extends Fragment {

    DatabaseReference databaseCurrentUser;
    User currentUser;
    private TextView txtFirstName, txtLastName, txtEmail;
    private EditText editMajor, editSkills, editBio;
    private ImageView imgPofilePicture;
    private Button buttonUpdate, buttonSelectImage, buttonTakePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        txtFirstName = (TextView) v.findViewById(R.id.txt_first_name);
        txtLastName = (TextView) v.findViewById(R.id.txt_last_name);
        txtEmail = (TextView) v.findViewById(R.id.txt_email);
        editMajor = (EditText) v.findViewById(R.id.edit_major);
        editSkills = (EditText) v.findViewById(R.id.edit_skills);
        editBio = (EditText) v.findViewById(R.id.edit_bio);
        imgPofilePicture = (ImageView) v.findViewById(R.id.profile_pic);
        buttonUpdate = (Button) v.findViewById(R.id.button_update);
        buttonSelectImage = (Button) v.findViewById(R.id.button_select_image);
        buttonTakePhoto = (Button) v.findViewById(R.id.button_take_photo);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseCurrentUser = FirebaseDatabase.getInstance().getReference("users").child(userID);
        populateFields(databaseCurrentUser);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(databaseCurrentUser);
            }
        });

        return v;
    }

    private void populateFields(DatabaseReference databaseCurrentUser) {
        databaseCurrentUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(User.class);

                txtFirstName.setText(currentUser.getFirstName());
                txtLastName.setText(currentUser.getLastName());
                txtEmail.setText(currentUser.getEmail());
                editMajor.setText(currentUser.getMajor());
                editSkills.setText(currentUser.getSkills());
                editBio.setText(currentUser.getBio());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile(DatabaseReference databaseCurrentUser) {

        String major = editMajor.getText().toString().trim();
        if (TextUtils.isEmpty(major)){
            major = "";
        }
        String skills = editSkills.getText().toString().trim();
        if (TextUtils.isEmpty(skills)){
            skills = "";
        }
        String bio = editBio.getText().toString().trim();
        if (TextUtils.isEmpty(bio)){
            bio = "";
        }

        currentUser.setMajor(major);
        currentUser.setSkills(skills);
        currentUser.setBio(bio);

        databaseCurrentUser.setValue(currentUser);
        Toast.makeText(getActivity(), "Profile successfully updated ", Toast.LENGTH_SHORT).show();
    }
}

