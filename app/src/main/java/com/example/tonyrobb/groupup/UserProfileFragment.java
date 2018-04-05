package com.example.tonyrobb.groupup;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileFragment extends Fragment {

    DatabaseReference databaseCurrentUser;
    User currentUser;
    ProgressDialog mProgress;

    private TextView txtName, txtEmail;
    private EditText editMajor, editSkills, editBio;
    private ImageView imgPofilePicture;
    private Button buttonUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        String userId = null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }

        txtName = (TextView) v.findViewById(R.id.txt_name);
        txtEmail = (TextView) v.findViewById(R.id.txt_email);
        editMajor = (EditText) v.findViewById(R.id.edit_major);
        editSkills = (EditText) v.findViewById(R.id.edit_skills);
        editBio = (EditText) v.findViewById(R.id.edit_bio);
        imgPofilePicture = (ImageView) v.findViewById(R.id.profile_pic);
        buttonUpdate = (Button) v.findViewById(R.id.button_update);

        disableClickableFields();

        ConnectivityManager connectionManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectionManager.getActiveNetworkInfo();

        if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
            Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
        } else {

            if (userId != null) {
                databaseCurrentUser = FirebaseDatabase.getInstance().getReference("users")
                        .child(userId);
                mProgress = new ProgressDialog(getContext());

                populateFields(databaseCurrentUser);
            }
        }

        return v;
    }

    private void disableClickableFields() {
        editMajor.setClickable(false);
        editMajor.setCursorVisible(false);
        editMajor.setFocusable(false);
        editMajor.setFocusableInTouchMode(false);

        editSkills.setClickable(false);
        editSkills.setCursorVisible(false);
        editSkills.setFocusable(false);
        editSkills.setFocusableInTouchMode(false);

        editBio.setClickable(false);
        editBio.setCursorVisible(false);
        editBio.setFocusable(false);
        editBio.setFocusableInTouchMode(false);

        buttonUpdate.setVisibility(View.GONE);
    }

    private void populateFields(DatabaseReference databaseCurrentUser) {
        databaseCurrentUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mProgress.setTitle("Fetching Data");
                mProgress.setMessage("Please wait....");
                mProgress.show();

                currentUser = dataSnapshot.getValue(User.class);

                txtName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                txtEmail.setText(currentUser.getEmail());
                editMajor.setText(currentUser.getMajor());
                editSkills.setText(currentUser.getSkills());
                editBio.setText(currentUser.getBio());

                if (!currentUser.getProfilePicUrl().equals("")){
                    new DownloadImageTask(imgPofilePicture).execute(currentUser.getProfilePicUrl());
                } else {
                    imgPofilePicture.setImageDrawable(null);
                }

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

