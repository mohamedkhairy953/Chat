package com.example.moham.testrealtimedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Chat_activity extends AppCompatActivity {

    Button send_button;
    EditText msg_eddittext;
    TextView listview;
    String user_name, root_name;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);
        send_button = (Button) findViewById(R.id.button_sendmsg);
        msg_eddittext = (EditText) findViewById(R.id.editText);
        listview = (TextView) findViewById(R.id.listView_msgs);
        user_name = getIntent().getExtras().get("user_name").toString();
        root_name = getIntent().getExtras().get("room_name").toString();
        root = FirebaseDatabase.getInstance().getReference().child(root_name);
        setTitle("Room -" + root_name);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap map = new HashMap();
                String key = root.push().getKey();
                root.updateChildren(map);
                DatabaseReference into_key = root.child(key);
                HashMap<String, Object> map2 = new HashMap();
                map2.put("name", user_name);
                map2.put("msg", msg_eddittext.getText().toString());
                into_key.updateChildren(map2);


            }
        });
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_msg(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    String msg, msg_user_name;

    private void append_msg(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            msg = (String) ((DataSnapshot) i.next()).getValue();
            msg_user_name = (String) ((DataSnapshot) i.next()).getValue();
        }
        listview.append(msg_user_name + ":" + msg + "\n");

    }
}
