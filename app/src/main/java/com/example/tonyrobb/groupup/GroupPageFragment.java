package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class GroupPageFragment extends Fragment {

    DatabaseReference databaseCurrentGroup;
    Group currentGroup;

    ListView listViewMembers;
    List<User> userList;

    private EditText editUserEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_page_list, container, false);

        String groupId = null;
        String classId = null;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            classId = bundle.getString("classId");
            groupId = bundle.getString("groupId");
        }

        editUserEmail = v.findViewById(R.id.edit_user_email);
        Button btnJoinGroup = v.findViewById(R.id.btn_join_group);
        Button btnAddMember = v.findViewById(R.id.btn_add_member);
        Button btnRemoveMember = v.findViewById(R.id.btn_remove_member);

        databaseCurrentGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupId);

        listViewMembers = (ListView) v.findViewById(R.id.listViewMembers);
        userList = new ArrayList<User>();

        listViewMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                User aUser = userList.get(i);
                UserProfileFragment userProfileFragment = new UserProfileFragment();

                Bundle args = new Bundle();
                args.putString("userId", aUser.getUserId());

                userProfileFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, userProfileFragment, "toUserProfile")
                        .addToBackStack(null).commit();

            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseCurrentGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();

                currentGroup = dataSnapshot.getValue(Group.class);

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }

                if (getActivity() != null) {
                    ClassRosterAdapter adapter = new ClassRosterAdapter(getActivity(), userList);
                    listViewMembers.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void joinGroup(){

    }

    private void addUser(){

    }

    private void removeUser(){

    }

}
