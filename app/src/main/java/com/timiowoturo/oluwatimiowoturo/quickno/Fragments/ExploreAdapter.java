package com.timiowoturo.oluwatimiowoturo.quickno.Fragments;

import android.content.Context;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.User;
import com.timiowoturo.oluwatimiowoturo.quickno.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            if (i != currentBindUser.getQuicknos().size() - 1){
                quicknos = quicknos + hh + ", ";
            } else{
                quicknos = quicknos + hh;
            }
        }
        holder.userQuicknos.setText(quicknos);
        holder.user = currentBindUser;

        Glide.with(holder.v.getContext())
                .load(Uri.parse(currentBindUser.photoUrl)).apply(RequestOptions.circleCropTransform())
                .into(holder.userImage);

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
        View v;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userName);
            this.userQuicknos = itemView.findViewById(R.id.quicknosuser);
            this.userImage = itemView.findViewById(R.id.imageView);
            this.getQuickno = itemView.findViewById(R.id.getQuickno);
            this.user = null;
            v = itemView;
            this.getQuickno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser(); // requested
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    if (user != null){
                        Toast.makeText(mContext, user.getUid() + user1.getUid(), Toast.LENGTH_LONG).show();
                        Map<String, Object> structure = new HashMap<>();
                        structure.put("User Requesting", user1.getUid());
                        structure.put("User Receiving", user.getUid());
                        db.collection("messages").document(user.getUid()+user1.getUid()).set(structure)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext, "User has been notified", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else{

                    }
                }
            });
        }
    }
}
