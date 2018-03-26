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

public class GroupsListAdapter extends ArrayAdapter<Group> {
    private Activity context;
    private List<Group> groupList;
    private String dept;
    private int classNum;

    public GroupsListAdapter(Activity context, List<Group> groupList){
        super(context, R.layout.groups_list, groupList);
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.groups_list, null, false);

        TextView deptTextView = (TextView) listViewItem.findViewById(R.id.txtGroupName);

        String group = groupList.get(position).getGroupName();
        deptTextView.setText(group);
        return listViewItem;
    }
}
