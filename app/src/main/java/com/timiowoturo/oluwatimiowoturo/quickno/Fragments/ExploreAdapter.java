package com.timiowoturo.oluwatimiowoturo.quickno.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timiowoturo.oluwatimiowoturo.quickno.Models.User;
import com.timiowoturo.oluwatimiowoturo.quickno.R;

import java.util.ArrayList;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {
    Context mContext;
    ArrayList<User> users;
    private static final String TAG = "ExploreAdapter";
    @NonNull
    @Override
    public ExploreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.explorelayout, null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public ExploreAdapter(Context context, ArrayList<User> users){
        this.mContext = context;
        this.users = users;
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreAdapter.ViewHolder holder, int position) {
        User currentBindUser = this.users.get(position);
        holder.userName.setText(currentBindUser.getName());
        String quicknos = "";
        for (int i = 0; i < currentBindUser.getQuicknos().size(); i++){
            String hh = currentBindUser.getQuicknos().get(i).getTag();
            quicknos = quicknos + hh + ", ";
        }
        holder.userQuicknos.setText(quicknos);
        holder.user = currentBindUser;

        // Image set next
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "size is:" + String.valueOf(this.users.size()));
        return this.users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView userQuicknos;
        ImageView userImage;
        Button getQuickno;
        User user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userName);
            this.userQuicknos = itemView.findViewById(R.id.quicknosuser);
            this.userImage = itemView.findViewById(R.id.imageView);
            this.getQuickno = itemView.findViewById(R.id.getQuickno);
            this.user = null;
            this.getQuickno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Button Clicked", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
