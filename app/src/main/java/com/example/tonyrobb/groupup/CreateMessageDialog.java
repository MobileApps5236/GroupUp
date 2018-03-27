package com.example.tonyrobb.groupup;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by tonyrobb on 3/26/18.
 */

public class CreateMessageDialog extends Dialog implements View.OnClickListener{
    private Activity context;
    private Dialog dialog;
    private Button submit, cancel;
    private EditText messageTxt;
    private String threadId;
    private String userId;
    public CreateMessageDialog(Activity context, String threadId, String userId){
        super(context);
        this.context = context;
        this.threadId = threadId;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_message_dialog);
        submit = findViewById(R.id.submitBtn);
        cancel = findViewById(R.id.cancelBtn);
        messageTxt = findViewById(R.id.messageEditText);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.submitBtn:
                DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference("messages").child(threadId);
                String text = messageTxt.getText().toString().trim();
                if(!TextUtils.isEmpty(text)){
                    String id = messageReference.push().getKey();
                    Message message = new Message(id, text, Calendar.getInstance().getTime().toString(), userId, threadId);
                    messageReference.child(id).setValue(message);
                }
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
