package com.timiowoturo.oluwatimiowoturo.quickno;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timiowoturo.oluwatimiowoturo.quickno.Models.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    ArrayList<Message> messages;
    public MessageAdapter(ArrayList<Message> messages){
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagelayout, null);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = this.messages.get(position);
        holder.messageText.setText(message.message);
        holder.userName.setText(message.userName);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.usertextmessage);
            userName = itemView.findViewById(R.id.usertextname);
        }
    }
}
