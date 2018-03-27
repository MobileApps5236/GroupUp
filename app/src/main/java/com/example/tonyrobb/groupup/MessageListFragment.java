package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class MessageListFragment extends Fragment {
    TextView subjectTxt;
    TextView authorTxt;
    ListView listViewMessages;
    Button createMessageBtn;
    String threadId;
    FirebaseAuth auth;
    List<Message> messageList;
    DatabaseReference messagesReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_list, container,false);
        auth = FirebaseAuth.getInstance();

        subjectTxt = v.findViewById(R.id.subjectTxt);
        authorTxt = v.findViewById(R.id.byLineTxt);
        subjectTxt.setText(getArguments().getString("subject"));
        authorTxt.setText("By: "+getArguments().getString("userName"));
        threadId = getArguments().getString("threadId");
        messageList = new ArrayList<>();
        messagesReference = FirebaseDatabase.getInstance().getReference("messages").child(threadId);
        authorTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileFragment fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("userId", getArguments().getString("threadCreatorId"));
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "toUserProfile").addToBackStack(null).commit();
            }
        });
        listViewMessages = v.findViewById(R.id.listViewMessages);
        createMessageBtn = v.findViewById(R.id.createMessageBtn);
        createMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateMessageDialog dialog = new CreateMessageDialog(getActivity(), threadId, auth.getUid());
                dialog.show();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        messagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
                    Message message = messageSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                MessageListAdapter adapter = new MessageListAdapter(getActivity(), messageList);
                listViewMessages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
