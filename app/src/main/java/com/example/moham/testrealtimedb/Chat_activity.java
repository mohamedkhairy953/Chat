package com.example.moham.testrealtimedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class Chat_activity extends AppCompatActivity {

    Button send_button;
    EditText msg_eddittext;
    @BindView(R.id.recycler_view)
    RecyclerView recyc;
    String user_name, root_name;
    DatabaseReference root;
    ArrayList<MessageModel> msgs;
    private MessagesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);
        send_button = (Button) findViewById(R.id.button_sendmsg);
        msg_eddittext = (EditText) findViewById(R.id.editText);
        ButterKnife.bind(this);
        msgs=new ArrayList<>();
        user_name = getIntent().getExtras().get("user_name").toString();
        root_name = getIntent().getExtras().get("room_name").toString();
        root = FirebaseDatabase.getInstance().getReference().child(root_name);
        setTitle("Room -" + root_name);
        mAdapter = new MessagesAdapter(this,msgs);
        recyc.setLayoutManager(new LinearLayoutManager(getBaseContext()) );
        recyc.setItemAnimator(new DefaultItemAnimator());
        recyc.setAdapter(mAdapter);
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
                msg_eddittext.setText("");
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
            MessageModel model=new MessageModel();
            msg = (String) ((DataSnapshot) i.next()).getValue();
            msg_user_name = (String) ((DataSnapshot) i.next()).getValue();
            model.setMsg(msg);
            model.setUser(msg_user_name);
            msgs.add(model);
        }
        mAdapter.notifyDataSetChanged();

    }
}
