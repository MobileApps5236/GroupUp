package com.example.tonyrobb.groupup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionListFragment extends Fragment {

    DatabaseReference databaseSections;
    EditText editTextAddSection;
    Button btnAddSection;
    ListView listViewSections;
    String dept;
    boolean isProf;
    int classNum;
    List<Section> sectionList;

    ConnectivityManager connectionManager;
    NetworkInfo activeNetwork;

    DatabaseReference user;
    User enrolledUser;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section_list, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("sections").child(getArguments().getString("classId"));
        editTextAddSection = v.findViewById(R.id.editTextAddSection);
        btnAddSection = v.findViewById(R.id.btnAddSection);
        listViewSections = v.findViewById(R.id.listViewSections);
        sectionList = new ArrayList<Section>();
        auth = FirebaseAuth.getInstance();
        user = FirebaseDatabase.getInstance().getReference("users").child(auth.getUid());

        connectionManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        btnAddSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSection();
            }
        });

        listViewSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                activeNetwork = connectionManager.getActiveNetworkInfo();

                if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final int pos = i;
                builder.setMessage("Do you want to enroll in section " + sectionList.get(i).getSectionNumber() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                setDatabaseEntries(sectionList.get(pos));
                                Toast.makeText(getActivity(), "Enrollment Successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Do nothing
                            }
                        });
                builder.create().show();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        dept = getArguments().getString("dept");
        classNum = getArguments().getInt("classNum");

        activeNetwork = connectionManager.getActiveNetworkInfo();

        if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
            Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.print("HEY");
                if (dataSnapshot.getValue(User.class).getIsProf()) {
                    editTextAddSection.setVisibility(View.VISIBLE);
                    btnAddSection.setVisibility(View.VISIBLE);
                }

                enrolledUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("HELLO");
            }
        });
        databaseSections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sectionList.clear();
                for (DataSnapshot sectionSnapshot : dataSnapshot.getChildren()) {
                    Section section = sectionSnapshot.getValue(Section.class);
                    sectionList.add(section);
                }

                if (getActivity() != null) {
                    SectionListAdapter adapter = new SectionListAdapter(getActivity(), sectionList, dept, classNum);
                    listViewSections.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addSection() {
        String sectionNumber = editTextAddSection.getText().toString().trim();

        if (!TextUtils.isEmpty(sectionNumber)) {

            activeNetwork = connectionManager.getActiveNetworkInfo();

            if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = databaseSections.push().getKey();
            HashMap<String, User> enrolledUsers = new HashMap<>();
            HashMap<String, Group> groupsMade = new HashMap<>();
            String classId = getArguments().getString("classId");
            Section section = new Section(id, Integer.parseInt(sectionNumber), classId,enrolledUsers, groupsMade);
            databaseSections.child(id).setValue(section);
        } else {
            Toast.makeText(getActivity(), "Enter a Section Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDatabaseEntries(Section currentSection){
        user.child("sectionsEnrolledIn").child(currentSection.getSectionId())
                .setValue(currentSection);

        enrolledUser.setBio(null);
        enrolledUser.setSkills(null);
        enrolledUser.setMajor(null);
        enrolledUser.setEmail(null);
        enrolledUser.setProfilePicUrl(null);
        enrolledUser.setSectionsEnrolledIn(null);
        enrolledUser.setEnrolledInGroup(null);

        databaseSections.child(currentSection.getSectionId()).child("enrolledUsers")
                .child(enrolledUser.getUserId()).setValue(enrolledUser);
    }
}
