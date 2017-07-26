package com.example.moham.testrealtimedb;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button add_chat_button;
    EditText add_chat_eddittext;
    ListView chats_listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> List_of_chats = new ArrayList<String>();
    String name;
    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_chat_button = (Button) findViewById(R.id.buttonaddchat_id);
        add_chat_eddittext = (EditText) findViewById(R.id.edittextaddchat_id);
        chats_listview = (ListView) findViewById(R.id.listview_rooms_id);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, List_of_chats);
        chats_listview.setAdapter(adapter);
        root = FirebaseDatabase.getInstance().getReference().getRoot();
        Request_username_dialog();
        add_chat_button.setOnClickListener(this);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                }
                List_of_chats.clear();

                List_of_chats.addAll(set);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       chats_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String room_name=parent.getItemAtPosition(position).toString();
               Intent intent=new Intent(MainActivity.this,Chat_activity.class);
               intent.putExtra("room_name",room_name);
               intent.putExtra("user_name",name);
               startActivity(intent);

           }
       });

    }

    private void Request_username_dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText name_edittext = new EditText(this);
        builder.setView(name_edittext);
        builder.setMessage("Enter your username");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = name_edittext.getText().toString();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Request_username_dialog();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v == add_chat_button) {
            if (TextUtils.isEmpty(add_chat_eddittext.getText().toString())) {
                Toast.makeText(MainActivity.this, "please type a chat name", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put(add_chat_eddittext.getText().toString(), "");
            root.updateChildren(map);
            add_chat_eddittext.setText("");
        }
    }
}
