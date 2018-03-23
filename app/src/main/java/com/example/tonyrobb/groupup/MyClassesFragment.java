package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class MyClassesFragment extends Fragment {

    DatabaseReference databaseSections;
    ListView listViewSections;
    String dept;
    int classNum;
    List<String> sectionList;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_classes, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        listViewSections = (ListView) v.findViewById(R.id.listViewSections);
        sectionList = new ArrayList<String>();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseSections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sectionList.clear();
                User user = dataSnapshot.getValue(User.class);
                Log.i("UserID?", currentUser.getUid());
                Log.i("DatabaseUserID?", user.getEmail());
                List<String> temp = user.getSectionsEnrolledIn();
                sectionList.add(temp.get(0));

                MyClassesAdapter adapter = new MyClassesAdapter(getActivity(), sectionList);
                listViewSections.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
