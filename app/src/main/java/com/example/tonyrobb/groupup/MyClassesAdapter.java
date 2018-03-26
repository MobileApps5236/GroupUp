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

public class MyClassesAdapter extends ArrayAdapter<Section> {
    private Activity context;
    private List<Section> sectionList;
    private String dept;
    private int classNum;

    public MyClassesAdapter(Activity context, List<Section> sectionList){
        super(context, R.layout.my_classes, sectionList);
        this.context = context;
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.my_classes, null, false);

        TextView deptTextView = (TextView) listViewItem.findViewById(R.id.txtSectionID);

        String section = Integer.toString(sectionList.get(position).getSectionNumber());
        deptTextView.setText(section);
        return listViewItem;
    }
}
