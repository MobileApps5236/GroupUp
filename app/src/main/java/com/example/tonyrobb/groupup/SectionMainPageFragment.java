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
    private Button btnDiscussionBoard, btnGroups, btnClassRoster, btnMyGroup;
    private TextView sectionName;
    DatabaseReference databaseSections;
    DatabaseReference databaseClasses;
    private String sectionId;
    private String classId;
    private String className;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section_main_page, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("sections");
        databaseClasses = FirebaseDatabase.getInstance().getReference("classes");


        btnDiscussionBoard = (Button) v.findViewById(R.id.button_discussion_board);
        btnGroups = (Button) v.findViewById(R.id.button_groups);
        btnClassRoster = (Button) v.findViewById(R.id.button_class_roster);
        sectionName = (TextView) v.findViewById(R.id.section_name);

        btnDiscussionBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForumFragment fragment = new ForumFragment();
                Bundle args = new Bundle();
                args.putString("sectionId", sectionId);
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "forum").addToBackStack(null).commit();
            }
        });

        btnGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupsListFragment fragment = new GroupsListFragment();
                Bundle args = new Bundle();
                args.putString("sectionId", sectionId);
                args.putString("classId", classId);
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "groupList").addToBackStack(null).commit();
            }
        });

        btnClassRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassRosterFragment classRosterFragment = new ClassRosterFragment();

                Bundle args = new Bundle();
                args.putString("classId", classId);
                args.putString("sectionId", sectionId);

                classRosterFragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, classRosterFragment, "toMyProfile")
                        .addToBackStack(null).commit();
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
                    if (sectionSnapshot.hasChild(sectionId)) {
                        classId = sectionSnapshot.child(sectionId).getValue(Section.class).getClassId();
                    }
                }

                databaseClasses.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(classId != null) {
                            className = dataSnapshot.child(classId).getValue(Class.class).getClassName();
                            sectionName.setText(className);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

