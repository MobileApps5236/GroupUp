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
/**
 * Created by tonyrobb on 3/21/18.
 */

public class SectionListAdapter extends ArrayAdapter<Section> {
    private Activity context;
    private List<Section> sectionList;
    private String dept;
    private int classNum;

    public SectionListAdapter(Activity context, List<Section> sectionList, String dept, int classNum){
        super(context, R.layout.section_list, sectionList);
        this.context = context;
        this.sectionList = sectionList;
        this.dept = dept;
        this.classNum = classNum;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.section_list, null, false);

        TextView deptTextView = (TextView) listViewItem.findViewById(R.id.txtClassDepartment);
        TextView classNumberTextView = (TextView) listViewItem.findViewById(R.id.txtClassNumber);
        TextView sectionNumberTextView = (TextView) listViewItem.findViewById(R.id.txtSectionNumber);

        Section section = sectionList.get(position);
        deptTextView.setText(dept);
        classNumberTextView.setText(String.format("%d", classNum));
        sectionNumberTextView.setText(String.format("%d", section.getSectionNumber()));
        return listViewItem;
    }
}
