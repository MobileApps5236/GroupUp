package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        databaseSections = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("sectionsEnrolledIn");
        listViewSections = (ListView) v.findViewById(R.id.listViewSections);
        sectionList = new ArrayList<String>();

        listViewSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String aSection = sectionList.get(i);
                SectionMainPageFragment fragment = new SectionMainPageFragment();
                Bundle args = new Bundle();
                args.putString("sectionId", aSection);

                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "toSectionMainPages").addToBackStack(null).commit();

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

                sectionList.clear();
                //User user = dataSnapshot.getValue(User.class);
                for(DataSnapshot sectionSnapshot : dataSnapshot.getChildren()){
                    Section section = sectionSnapshot.getValue(Section.class);
                    System.out.println("HEY");
                    sectionList.add(Integer.toString(section.getSectionNumber()));
                }
                Log.i("UserID?", currentUser.getUid());

                if (getActivity() != null) {
                    MyClassesAdapter adapter = new MyClassesAdapter(getActivity(), sectionList);
                    listViewSections.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
