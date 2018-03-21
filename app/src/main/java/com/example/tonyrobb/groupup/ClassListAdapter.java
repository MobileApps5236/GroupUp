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
 * Created by tonyrobb on 3/20/18.
 */

public class ClassListAdapter extends ArrayAdapter<Class> {
    private Activity context;
    private List<Class> classList;

    public ClassListAdapter(Activity context, List<Class> classList){
        super(context, R.layout.class_list, classList);
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.class_list, null, true);

        TextView deptTextView = (TextView) listViewItem.findViewById(R.id.txtClassDepartment);
        TextView classNumberTextView = (TextView) listViewItem.findViewById(R.id.txtClassNumber);
        TextView classNameTextView = (TextView) listViewItem.findViewById(R.id.txtClassName);

        Class aClass = classList.get(position);
        deptTextView.setText(aClass.getDepartment());
        classNumberTextView.setText(String.format("%d",aClass.getClassNumber()));
        classNameTextView.setText(aClass.getClassName());

        return listViewItem;
    }
}
