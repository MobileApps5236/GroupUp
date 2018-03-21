package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class ClassListFragment extends Fragment {

    DatabaseReference databaseClasses;
    ListView listViewClasses;
    List<Class> classList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class_list, container, false);
        databaseClasses = FirebaseDatabase.getInstance().getReference("classes");
        listViewClasses = (ListView) v.findViewById(R.id.listViewClasses);
        classList = new ArrayList<>();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                classList.clear();

                for(DataSnapshot classSnapshot : dataSnapshot.getChildren()){
                    Class aClass = classSnapshot.getValue(Class.class);

                    classList.add(aClass);
                }

                ClassListAdapter adapter = new ClassListAdapter(getActivity(), classList);
                listViewClasses.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
