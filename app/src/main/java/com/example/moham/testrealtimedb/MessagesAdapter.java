package com.example.moham.testrealtimedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moham.testrealtimedb.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    ArrayList<MessageModel> msgs;
    Context cx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView usrName, msg;

        public MyViewHolder(View view) {
            super(view);
            usrName = (TextView) view.findViewById(R.id.text_usrname);
            msg = (TextView) view.findViewById(R.id.listView_msgs);
        }
    }


    public MessagesAdapter(Context cx, ArrayList<MessageModel> msgs) {
        this.msgs = msgs;
        this.cx = cx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.usrName.setText(msgs.get(position).getUser());
        holder.msg.setText(msgs.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }
}