package com.example.tonyrobb.groupup;

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
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {

    DatabaseReference databaseCurrentUser;
    User currentUser;

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

        txtName = v.findViewById(R.id.txt_name);
        txtEmail = v.findViewById(R.id.txt_email);
        editMajor = v.findViewById(R.id.edit_major);
        editSkills = v.findViewById(R.id.edit_skills);
        editBio = v.findViewById(R.id.edit_bio);
        imgPofilePicture = v.findViewById(R.id.profile_pic);
        buttonUpdate = v.findViewById(R.id.button_update);

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

                currentUser = dataSnapshot.getValue(User.class);

                if (txtName != null)
                    txtName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                if (txtEmail != null)
                    txtEmail.setText(currentUser.getEmail());
                if (editMajor != null)
                    editMajor.setText(currentUser.getMajor());
                if (editSkills != null)
                    editSkills.setText(currentUser.getSkills());
                if (editBio != null)
                    editBio.setText(currentUser.getBio());

                if (imgPofilePicture != null) {
                    if (!currentUser.getProfilePicUrl().equals("")) {
                        Picasso.get().load(currentUser.getProfilePicUrl()).resize(120, 120).centerCrop().into(imgPofilePicture);
                    } else {
                        imgPofilePicture.setImageDrawable(null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        txtEmail = null;
        txtName = null;
        editBio = null;
        editMajor = null;
        editSkills = null;
        imgPofilePicture = null;
    }
}

