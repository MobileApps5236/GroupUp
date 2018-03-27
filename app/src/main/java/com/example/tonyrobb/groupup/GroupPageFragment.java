package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupPageFragment extends Fragment {

    DatabaseReference databaseCurrentGroup, databaseGroupMembers, databaseCurrentUser;
    Group currentGroup;
    Button btnJoinGroup, btnAddMember, btnRemoveMember;
    User currentUser;

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
        btnJoinGroup = v.findViewById(R.id.btn_join_group);
        btnAddMember = v.findViewById(R.id.btn_add_member);
        btnRemoveMember = v.findViewById(R.id.btn_remove_member);
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(currentGroup);
            }
        });
        databaseCurrentGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupId);
        databaseGroupMembers = databaseCurrentGroup.child("groupMembers");
        databaseCurrentUser = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

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

        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup(currentGroup, currentUser);
            }
        });

        btnRemoveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeUser(currentGroup);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseGroupMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();


                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
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

        databaseCurrentGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentGroup = dataSnapshot.getValue(Group.class);

                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(currentGroup.getGroupOwnerUId())){
                    btnJoinGroup.setVisibility(View.GONE);
                } else {
                    editUserEmail.setVisibility(View.GONE);
                    btnAddMember.setVisibility(View.GONE);
                    btnRemoveMember.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void joinGroup(Group group, User user){

        group.setGroupMembers(null);
        group.setGroupOwnerUId(null);
        group.setSectionId(null);

        databaseCurrentUser.child("enrolledInGroup").child(group.getGroupId()).setValue(group);

        user.setBio(null);
        user.setSkills(null);
        user.setMajor(null);
        user.setProfilePicUrl(null);
        user.setSectionsEnrolledIn(null);
        user.setEnrolledInGroup(null);

        databaseCurrentGroup.child("groupMembers").child(user.getUserId()).setValue(user);
    }

    private void addUser(final Group group){
        final String userEmail = editUserEmail.getText().toString().trim();

        if(!TextUtils.isEmpty(userEmail)){
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userId = null;
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        String currentEmail = userSnapshot.child("email").getValue(String.class);
                        if(currentEmail.equals(userEmail)){
                            userId = userSnapshot.getValue(User.class).getUserId();
                        }
                    }
                    if(userId == null){
                        Toast.makeText(getActivity(), "Email does not exist", Toast.LENGTH_SHORT).show();
                    }else{
                        User user = dataSnapshot.child(userId).getValue(User.class);
                        group.setGroupMembers(null);
                        group.setGroupOwnerUId(null);
                        group.setSectionId(null);

                        databaseCurrentUser.child("enrolledInGroup").child(group.getGroupId()).setValue(group);

                        user.setBio(null);
                        user.setSkills(null);
                        user.setMajor(null);
                        user.setProfilePicUrl(null);
                        user.setSectionsEnrolledIn(null);
                        user.setEnrolledInGroup(null);

                        databaseCurrentGroup.child("groupMembers").child(user.getUserId()).setValue(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void removeUser(final Group group){
        final String userEmail = editUserEmail.getText().toString().trim();

        if(!TextUtils.isEmpty(userEmail)){
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userId = null;
                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        String currentEmail = userSnapshot.child("email").getValue(String.class);
                        if(currentEmail.equals(userEmail)){
                            userId = userSnapshot.getValue(User.class).getUserId();
                        }
                    }
                    if(userId == null){
                        Toast.makeText(getActivity(), "Email does not exist", Toast.LENGTH_SHORT).show();
                    }else{
                        User user = dataSnapshot.child(userId).getValue(User.class);
                        if(!user.getEmail().equals(userEmail)) {
                            databaseCurrentUser.child("enrolledInGroup").child(group.getGroupId()).removeValue();

                            databaseCurrentGroup.child("groupMembers").child(user.getUserId()).removeValue();
                        }else{
                            Toast.makeText(getActivity(),"You can't delete yourself!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
