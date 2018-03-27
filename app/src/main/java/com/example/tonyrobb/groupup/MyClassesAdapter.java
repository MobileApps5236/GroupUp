package com.example.tonyrobb.groupup;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

        TextView sectionNumTextView = (TextView) listViewItem.findViewById(R.id.txtSectionNumber);
        final TextView deptTextView = listViewItem.findViewById(R.id.txtClassDepartment);
        final TextView classNumberTextView = listViewItem.findViewById(R.id.txtClassNumber);
        String section = Integer.toString(sectionList.get(position).getSectionNumber());
        DatabaseReference classReference = FirebaseDatabase.getInstance().getReference("classes").child(sectionList.get(position).getClassId());
        classReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Class aClass = dataSnapshot.getValue(Class.class);
                deptTextView.setText(aClass.getDepartment());
                classNumberTextView.setText(Integer.toString(aClass.getClassNumber()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sectionNumTextView.setText(section);
        return listViewItem;
    }
}
