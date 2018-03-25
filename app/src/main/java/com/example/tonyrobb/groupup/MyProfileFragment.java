package com.example.tonyrobb.groupup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class MyProfileFragment extends Fragment {

    DatabaseReference databaseCurrentUser;
    StorageReference storageRef;
    User currentUser;
    Uri imageUri;
    ProgressDialog mProgress;

    private TextView txtName, txtEmail;
    private EditText editMajor, editSkills, editBio;
    private ImageView imgPofilePicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        txtName = (TextView) v.findViewById(R.id.txt_name);
        txtEmail = (TextView) v.findViewById(R.id.txt_email);
        editMajor = (EditText) v.findViewById(R.id.edit_major);
        editSkills = (EditText) v.findViewById(R.id.edit_skills);
        editBio = (EditText) v.findViewById(R.id.edit_bio);
        imgPofilePicture = (ImageView) v.findViewById(R.id.profile_pic);
        Button buttonUpdate = (Button) v.findViewById(R.id.button_update);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseCurrentUser = FirebaseDatabase.getInstance().getReference("users").child(userID);
        storageRef = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(getContext());

        populateFields(databaseCurrentUser);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(databaseCurrentUser);
            }
        });

        imgPofilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicSelection();
            }
        });

        return v;
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

    private void profilePicSelection() {

        final CharSequence[] items = {"Select or Take a Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Profile Picture");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Select or Take a Photo")) {
                    getCroppedPhotoIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void getCroppedPhotoIntent() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                imageUri = result.getUri();
                imgPofilePicture.setImageURI(imageUri);

                if (imageUri != null){
                    mProgress.setTitle("Updating Profile");
                    mProgress.setMessage("Please wait....");
                    mProgress.show();

                    StorageReference childStorage = storageRef.child("User_Profile").child(imageUri.getLastPathSegment());

                    childStorage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            final Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();

                            currentUser.setProfilePicUrl(imageDownloadUrl.toString());
                            databaseCurrentUser.setValue(currentUser);
                        }
                    });

                    mProgress.dismiss();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

