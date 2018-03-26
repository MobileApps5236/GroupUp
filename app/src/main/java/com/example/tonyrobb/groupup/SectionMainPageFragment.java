package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SectionMainPageFragment extends Fragment {
    private Button btnDiscussionBoard, btnGroups, btnClassRoster;
    private TextView sectionName;
    DatabaseReference databaseSections;
    DatabaseReference databaseClasses;
    private String sectionId;
    String sectionTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section_main_page, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("sections");
        databaseClasses = FirebaseDatabase.getInstance().getReference("classes");


        btnDiscussionBoard = (Button) v.findViewById(R.id.button_discussion_board);
        btnGroups = (Button) v.findViewById(R.id.button_groups);
        btnClassRoster = (Button) v.findViewById(R.id.button_class_roster);
        sectionName = (TextView) v.findViewById(R.id.txtSectionTitle);

        btnDiscussionBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO: implement onclick listeners
                MyClassesFragment fragment = new MyClassesFragment();
            }
        });

        btnGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassListFragment fragment = new ClassListFragment();
            }
        });

        btnClassRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment myProfileFragment = new MyProfileFragment();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseSections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sectionId = getArguments().getString("sectionId");
                //User user = dataSnapshot.getValue(User.class);
                for(DataSnapshot sectionSnapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot classSnapshot : sectionSnapshot.getChildren()) {
                        Section curSection = classSnapshot.getValue(Section.class);
                        if (curSection.getSectionNumber() == Integer.parseInt(sectionId)) {
                            sectionTitle = sectionSnapshot.getKey();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot classSnapshot : dataSnapshot.getChildren()){
                    if (classSnapshot.getKey().equals(sectionTitle)) {
                        Class aClass = classSnapshot.getValue(Class.class);
                        sectionName.setText(aClass.getClassName());
                    }
                }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }
}

