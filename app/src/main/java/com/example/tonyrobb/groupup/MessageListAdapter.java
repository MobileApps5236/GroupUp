package com.example.tonyrobb.groupup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
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

public class MessageListAdapter extends ArrayAdapter<Message> {
    private FragmentActivity context;
    private List<Message> messageList;
    public MessageListAdapter(FragmentActivity context, List<Message> messageList){
        super(context,R.layout.message_list_item ,messageList);
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View messageListItem = inflater.inflate(R.layout.message_list_item, null, true);
        final TextView byLineTxt = messageListItem.findViewById(R.id.byLineTxt);
        TextView messageTxt = messageListItem.findViewById(R.id.messageTxt);
        messageTxt.setText(messageList.get(position).getText());
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(messageList.get(position).getUserId());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                byLineTxt.setText(dataSnapshot.getValue(User.class).getFirstName()+" "+dataSnapshot.getValue(User.class).getLastName());
                byLineTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserProfileFragment fragment = new UserProfileFragment();
                        Bundle args = new Bundle();
                        args.putString("userId", dataSnapshot.getValue(User.class).getUserId());
                        fragment.setArguments(args);
                        context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "toUserProfile").addToBackStack(null).commit();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return messageListItem;
    }
}
