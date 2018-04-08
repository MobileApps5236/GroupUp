package com.example.tonyrobb.groupup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    DatabaseReference databaseCurrentGroup, databaseGroupMembers, databaseUsers, databaseCurrentUser;
    Group currentGroup;
    Button btnJoinGroup, btnAddMember, btnRemoveMember;
    User currentUser, currentGroupOwner;

    ListView listViewMembers;
    List<User> userList;

    ConnectivityManager connectionManager;
    NetworkInfo activeNetwork;

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

        connectionManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        editUserEmail = v.findViewById(R.id.edit_user_email);
        btnJoinGroup = v.findViewById(R.id.btn_join_group);
        btnAddMember = v.findViewById(R.id.btn_add_member);
        btnRemoveMember = v.findViewById(R.id.btn_remove_member);

        databaseCurrentGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupId);
        databaseGroupMembers = databaseCurrentGroup.child("groupMembers");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseCurrentUser = databaseUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        listViewMembers = (ListView) v.findViewById(R.id.listViewMembers);
        userList = new ArrayList<User>();

        listViewMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                activeNetwork = connectionManager.getActiveNetworkInfo();

                if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

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

                activeNetwork = connectionManager.getActiveNetworkInfo();

                if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                joinGroup(currentGroup, currentUser);
            }
        });

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = editUserEmail.getText().toString().trim();
                addUser(currentGroup, userEmail);
            }
        });

        btnRemoveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = editUserEmail.getText().toString().trim();
                removeUser(currentGroup, userEmail);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        activeNetwork = connectionManager.getActiveNetworkInfo();

        if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
            Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }

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

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentGroupOwner = dataSnapshot.child(currentGroup.getGroupOwnerUId())
                        .getValue(User.class);
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

    private void addUser(final Group group, final String userEmail){

        if(!TextUtils.isEmpty(userEmail)){
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userId = null;
                    User newMember = null;

                    for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                        String currentEmail = userSnapshot.child("email").getValue(String.class);
                        if(currentEmail.equals(userEmail)){
                            newMember = userSnapshot.getValue(User.class);
                            userId = newMember.getUserId();
                        }
                    }
                    if(userId == null) {

                        Toast.makeText(getActivity(), "Email does not exist", Toast.LENGTH_SHORT).show();
                    } else if (!newMember.getSectionsEnrolledIn().containsKey(group.getSectionId())) {

                        Toast.makeText(getActivity(),"User not enrolled in this section", Toast.LENGTH_SHORT).show();
                    } else {

                        User user = dataSnapshot.child(userId).getValue(User.class);
                        group.setGroupMembers(null);
                        group.setGroupOwnerUId(null);
                        group.setSectionId(null);

                        DatabaseReference databaseAddedUser = FirebaseDatabase.getInstance()
                                .getReference("users").child(userId);
                        databaseAddedUser.child("enrolledInGroup").child(group.getGroupId()).setValue(group);

                        user.setBio(null);
                        user.setSkills(null);
                        user.setMajor(null);
                        user.setProfilePicUrl(null);
                        user.setSectionsEnrolledIn(null);
                        user.setEnrolledInGroup(null);

                        databaseCurrentGroup.child("groupMembers").child(user.getUserId()).setValue(user);
                        Toast.makeText(getActivity(),"User Added", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getActivity(),"Enter an email address", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeUser(final Group group, final String userEmail){

        if(!TextUtils.isEmpty(userEmail)){

            activeNetwork = connectionManager.getActiveNetworkInfo();

            if (!(activeNetwork != null && activeNetwork.isConnectedOrConnecting())) {
                Toast.makeText(getActivity().getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                return;
            }

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
                        if(!currentGroupOwner.getEmail().equals(userEmail)) {

                            DatabaseReference databaseRemovedUser = FirebaseDatabase.getInstance()
                                    .getReference("users").child(userId);
                            databaseRemovedUser.child("enrolledInGroup").child(group.getGroupId()).removeValue();

                            databaseCurrentGroup.child("groupMembers").child(userId).removeValue();
                            Toast.makeText(getActivity(),"User Removed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"You can't delete yourself!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getActivity(),"Enter an email address", Toast.LENGTH_SHORT).show();
        }
    }

}
