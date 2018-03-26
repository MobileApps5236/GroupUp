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
    private String sectionId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section_main_page, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("sections");


        btnDiscussionBoard = (Button) v.findViewById(R.id.button_discussion_board);
        btnGroups = (Button) v.findViewById(R.id.button_groups);
        btnClassRoster = (Button) v.findViewById(R.id.button_class_roster);
        sectionName = (TextView) v.findViewById(R.id.sectionName);

        btnDiscussionBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO: implement onclick listeners
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
                ClassListFragment fragment = new ClassListFragment();
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "toEnroll").addToBackStack(null).commit();
            }
        });

        btnClassRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                //getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.fragment_container, myProfileFragment, "toMyProfile")
                        //.addToBackStack(null).commit();
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
                        String className = sectionSnapshot.getKey();
                        sectionName.setText(className);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

