package com.example.tonyrobb.groupup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class ThreadListAdapter extends ArrayAdapter<MessageThread> {

    private FragmentActivity context;
    private List<MessageThread> threadList;
    public ThreadListAdapter(FragmentActivity context, List<MessageThread> threadList){
        super(context, R.layout.thread_list, threadList);
        this.context = context;
        this.threadList = threadList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View threadViewItem = inflater.inflate(R.layout.thread_list, null, true);
        final TextView emailTxt = threadViewItem.findViewById(R.id.nameTxt);
        TextView threadSubjectTxt = threadViewItem.findViewById(R.id.threadSubjectTxt);
        MessageThread thread = threadList.get(position);
        threadSubjectTxt.setText(thread.getSubject());
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(thread.getCreatorId());

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailTxt.setText(dataSnapshot.getValue(User.class).getFirstName()+" "+dataSnapshot.getValue(User.class).getLastName());
                final String userId = dataSnapshot.getValue(User.class).getUserId();
                emailTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserProfileFragment profileFragment = new UserProfileFragment();
                        Bundle arg = new Bundle();
                        arg.putString("userId", userId);
                        profileFragment.setArguments(arg);
                        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment, "toUserProfile").addToBackStack(null).commit();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return threadViewItem;
    }
}
