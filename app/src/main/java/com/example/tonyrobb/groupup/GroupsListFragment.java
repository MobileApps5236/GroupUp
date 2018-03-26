package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class GroupsListFragment extends Fragment {

    DatabaseReference databaseSections;
    ListView listViewSections;
    List<Group> groupList;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String sectionId = getArguments().getString("sectionId");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groupslist_list, container, false);
        databaseSections = FirebaseDatabase.getInstance().getReference("sections").child(getArguments().getString("className")).child(sectionId).child("groupsMade");
        listViewSections = (ListView) v.findViewById(R.id.listViewSections);
        groupList = new ArrayList<Group>();

        listViewSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group aGroup = groupList.get(i);
                GroupPageFragment fragment = new GroupPageFragment();
                Bundle args = new Bundle();
                args.putString("sectionId", sectionId);

                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "toGroupPage").addToBackStack(null).commit();

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

                groupList.clear();
                //User user = dataSnapshot.getValue(User.class);
                for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()){
                    Group group = groupSnapshot.getValue(Group.class);
                    System.out.println("HEY");
                    groupList.add(group);
                }
                Log.i("UserID?", currentUser.getUid());

                if (getActivity() != null) {
                    GroupsListAdapter adapter = new GroupsListAdapter(getActivity(), groupList);
                    listViewSections.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
