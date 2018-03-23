package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyrobb on 3/20/18.
 */

public class SectionListFragment extends Fragment {

    DatabaseReference databaseSections;
    EditText editTextAddSection;
    Button btnAddSection;
    ListView listViewSections;
    String dept;
    int classNum;
    List<Section> sectionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_section_list, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("sections").child(getArguments().getString("classId"));
        editTextAddSection = (EditText) v.findViewById(R.id.editTextAddSection);
        btnAddSection = (Button) v.findViewById(R.id.btnAddSection);
        listViewSections = (ListView) v.findViewById(R.id.listViewSections);
        sectionList = new ArrayList<Section>();

        btnAddSection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addSection();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        dept = getArguments().get("dept").toString();

        classNum = getArguments().getInt("classNum");

        databaseSections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sectionList.clear();
                for(DataSnapshot sectionSnapshot : dataSnapshot.getChildren()){
                    Section section = sectionSnapshot.getValue(Section.class);
                    sectionList.add(section);
                }

                SectionListAdapter adapter = new SectionListAdapter(getActivity(), sectionList, dept, classNum);
                listViewSections.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addSection(){
        String sectionNumber = editTextAddSection.getText().toString().trim();
        //TODO: make sure the user enters in a number

        if(!TextUtils.isEmpty(sectionNumber)){
            String id = databaseSections.push().getKey();

            Section section = new Section(id, Integer.parseInt(sectionNumber));
            databaseSections.child(id).setValue(section);
        }

    }
}
