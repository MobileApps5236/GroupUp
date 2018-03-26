package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassRosterFragment extends Fragment {

    DatabaseReference databaseenrolledUsers;
    ListView listViewUsers;
    List<String> userList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class_roster, container, false);
//        databaseSections = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("sectionsEnrolledIn");

        /*
            TODO: 1. Get the sectionID as bundle and get database referance to userID list
            TODO: 2. Loop through user id list and get user names.
         */

        listViewUsers = (ListView) v.findViewById(R.id.listViewUsers);
        userList = new ArrayList<String>();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

//        databaseSections.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                sectionList.clear();
//                //User user = dataSnapshot.getValue(User.class);
//                for(DataSnapshot sectionSnapshot : dataSnapshot.getChildren()){
//                    Section section = sectionSnapshot.getValue(Section.class);
//                    System.out.println("HEY");
//                    sectionList.add(Integer.toString(section.getSectionNumber()));
//                }
//
//                if (getActivity() != null) {
//                    ClassRosterAdapter adapter = new ClassRosterAdapter(getActivity(), userList);
//                    listViewUsers.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

}
