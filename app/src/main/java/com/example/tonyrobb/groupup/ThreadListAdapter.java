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
 * Created by tonyrobb on 3/26/18.
 */

public class ThreadListAdapter extends ArrayAdapter<MessageThread> {

    private Activity context;
    private List<MessageThread> threadList;
    public ThreadListAdapter(Activity context, List<MessageThread> threadList){
        super(context, R.layout.thread_list, threadList);
        this.context = context;
        this.threadList = threadList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View threadViewItem = inflater.inflate(R.layout.thread_list, null, true);
        TextView threadSubjectTxt = threadViewItem.findViewById(R.id.threadSubjectTxt);
        MessageThread thread = threadList.get(position);
        threadSubjectTxt.setText(thread.getSubject());

        return threadViewItem;
    }
}
