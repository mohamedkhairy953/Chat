package com.example.moham.testrealtimedb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    FloatingActionButton add_chat_button;
    ListView chats_listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> List_of_chats = new ArrayList<String>();

    DatabaseReference root;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_chat_button = (FloatingActionButton) findViewById(R.id.fab);
        chats_listview = (ListView) findViewById(R.id.listview_rooms_id);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, List_of_chats);
        chats_listview.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
        }
        root = FirebaseDatabase.getInstance().getReference().getRoot();
        add_chat_button.setOnClickListener(this);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
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
                String room_name = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(MainActivity.this, Chat_activity.class);
                intent.putExtra("room_name", room_name);
                String name = auth.getCurrentUser().getEmail().split("@")[0];
                intent.putExtra("user_name", name);
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == add_chat_button) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText room_edittext = new EditText(this);
            builder.setView(room_edittext);
            builder.setTitle("Adding Room");
            builder.setMessage("Enter Room Name");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!TextUtils.isEmpty(room_edittext.getText())) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(room_edittext.getText().toString(), "");
                        root.updateChildren(map);
                        dialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (auth.getCurrentUser() != null) {
            finishAffinity();
        }
    }
}
