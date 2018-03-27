package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class ForumFragment extends Fragment {

    DatabaseReference threadReference;
    EditText subjectTxt;
    Button btnCreateThread;
    ListView listViewThreads;
    List<MessageThread> threadList;
    FirebaseAuth auth;
    String sectionId;
    DatabaseReference userReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forum, container, false);
        sectionId = getArguments().getString("sectionId");
        System.out.println(sectionId);
        auth = FirebaseAuth.getInstance();
        threadReference = FirebaseDatabase.getInstance().getReference("threads").child(sectionId);

        subjectTxt = (EditText) v.findViewById(R.id.createThreadSubject);
        btnCreateThread = v.findViewById(R.id.createThreadBtn);
        listViewThreads = v.findViewById(R.id.threadList);
        threadList = new ArrayList<MessageThread>();



        btnCreateThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addThread();
            }
        });
        listViewThreads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageThread thread = threadList.get(position);
                userReference = FirebaseDatabase.getInstance().getReference("users").child(thread.getCreatorId());
                final Bundle args = new Bundle();
                args.putString("subject", thread.getSubject());
                args.putString("threadId", thread.getThread_id());
                args.putString("threadCreatorId", thread.getCreatorId());
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("HEY");
                        System.out.println(dataSnapshot.getValue(User.class).getFirstName());
                        args.putString("userName", dataSnapshot.getValue(User.class).getFirstName() + " " + dataSnapshot.getValue(User.class).getLastName());
                        MessageListFragment messageListFragment = new MessageListFragment();
                        messageListFragment.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, messageListFragment, "toMessageList").addToBackStack(null).commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        threadReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                threadList.clear();
                for(DataSnapshot threadSnapshot : dataSnapshot.getChildren()){
                    MessageThread thread = threadSnapshot.getValue(MessageThread.class);
                    threadList.add(thread);
                }
                ThreadListAdapter threadListAdapter = new ThreadListAdapter(getActivity(), threadList);
                listViewThreads.setAdapter(threadListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addThread(){
        String subject = subjectTxt.getText().toString().trim();

        if(!TextUtils.isEmpty(subject)){
            String id = threadReference.push().getKey();
            String timeCreated = Calendar.getInstance().getTime().toString();
            String creatorId = auth.getUid();
            HashMap<String, Message> messages = new HashMap<>();
            MessageThread thread = new MessageThread(id, timeCreated, subject, creatorId, sectionId, messages);
            threadReference.child(id).setValue(thread);
        }
    }
}
