package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Message;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.MessageGroup;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Message> messages;
    String messagelocation;
    FirebaseFirestore db;
    String UserReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new MessageAdapter(messages);

        Intent intent = getIntent();
        UserReq = intent.getExtras().getString("Requesting");

        recyclerView = findViewById(R.id.messages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        messagelocation = UserReq + FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

        getMessages();


    }

    public void getMessages(){
        final DocumentReference docRef = db.collection("messages").document(messagelocation);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                MessageGroup message = snapshot.toObject(MessageGroup.class);
                messages.add(message.messages.get(message.messages.size()-1));
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void sendMessage(View view){
        final EditText message = findViewById(R.id.TextInput);
        final Message messagesending = new Message(" ", FirebaseAuth.getInstance().getCurrentUser().getUid(), message.getText().toString());

        final DocumentReference docRef = db.collection("message").document(messagelocation);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        MessageGroup group = document.toObject(MessageGroup.class);
                        group.messages.add(messagesending);
                        docRef.set(group);
                    } else {
                        ArrayList<Message> messagearray = new ArrayList<>();
                        messagearray.add(messagesending);
                        MessageGroup group = new MessageGroup(UserReq, FirebaseAuth.getInstance().getCurrentUser().getUid(), messagearray);
                        docRef.set(group);
                    }
                } else {

                }
            }
        });
    }
}
