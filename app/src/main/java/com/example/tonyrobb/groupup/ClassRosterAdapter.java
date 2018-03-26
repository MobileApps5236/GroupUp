package com.example.tonyrobb.groupup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ClassRosterAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public ClassRosterAdapter(Activity context, List<User> userList){
        super(context, R.layout.class_roster, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.class_roster, null, false);

        TextView nameTextView = (TextView) listViewItem.findViewById(R.id.txt_name);

        String userName = userList.get(position).getFirstName() + " " + userList.get(position).getLastName();
        nameTextView.setText(userName);
        return listViewItem;
    }
}
