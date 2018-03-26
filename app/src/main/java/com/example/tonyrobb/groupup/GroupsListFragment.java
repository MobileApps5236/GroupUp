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
import android.widget.Toast;

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

public class GroupsListFragment extends Fragment {

    DatabaseReference databaseGroups, databaseCurrentSection;
    ListView listViewGroups;
    List<Group> groupList;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String sectionId, classId;
    EditText editAddGroupName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groupslist_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            classId = bundle.getString("classId");
            sectionId = bundle.getString("sectionId");
        }

        editAddGroupName = v.findViewById(R.id.editAddGroupName);
        Button buttonAddGroup = v.findViewById(R.id.btnAddGroup);

        databaseGroups = FirebaseDatabase.getInstance().getReference("groups");
        databaseCurrentSection = FirebaseDatabase.getInstance().getReference("sections")
                .child(classId).child(sectionId);
        listViewGroups = (ListView) v.findViewById(R.id.listViewGroups);
        groupList = new ArrayList<Group>();

        buttonAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
            }
        });

        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group aGroup = groupList.get(i);
                GroupPageFragment fragment = new GroupPageFragment();

                Bundle args = new Bundle();
                args.putString("groupId", aGroup.getGroupId());
                fragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment, "toGroupPage")
                        .addToBackStack(null).commit();

            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                groupList.clear();

                for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()){
                    Group group = groupSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                Log.i("UserID?", currentUser.getUid());

                if (getActivity() != null) {
                    GroupsListAdapter adapter = new GroupsListAdapter(getActivity(), groupList);
                    listViewGroups.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addGroup() {
        String groupName = editAddGroupName.getText().toString().trim();

        if (!TextUtils.isEmpty(groupName)) {
            String id = databaseGroups.push().getKey();
            HashMap<String, User> groupMembers = new HashMap<>();

            Group group = new Group(id, groupName, 6, currentUser.getUid(), sectionId, groupMembers);
            databaseGroups.child(id).setValue(group);

            setDatabaseEntries(group);
        } else {
            Toast.makeText(getActivity(), "Enter a Group Name", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDatabaseEntries(Group currentGroup){

        currentGroup.setGroupMembers(null);
        currentGroup.setGroupOwnerUId(null);
        currentGroup.setSectionId(null);

        databaseCurrentSection.child("groupsMade").setValue(currentGroup);
    }

}
