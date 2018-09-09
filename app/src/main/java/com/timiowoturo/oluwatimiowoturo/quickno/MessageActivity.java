package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.Home;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Locator;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Message;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.MessageGroup;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<Message> messages = new ArrayList<>();
    String messagelocation;
    FirebaseFirestore db;
    String UserReq;
    Context contexts = this;
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


        if (UserReq != null) {
            messagelocation = UserReq + FirebaseAuth.getInstance().getCurrentUser().getUid();

            db = FirebaseFirestore.getInstance();

            getMessages();
        } else {
            UserReq = intent.getExtras().getString("Receiving");
            messagelocation = FirebaseAuth.getInstance().getCurrentUser().getUid() + UserReq;
            db = FirebaseFirestore.getInstance();
            getMessages();
        }


    }

    public void getMessages(){
        final DocumentReference docRef = db.collection("message").document(messagelocation);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot.exists()) {
                    messages.clear();
                    MessageGroup message = snapshot.toObject(MessageGroup.class);
                    for (int i = 0; i < message.messages.size(); i++){
                        messages.add(message.messages.get(i));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void sendMessage(View view){
        final EditText message = findViewById(R.id.TextInput);
        final Message messagesending = new Message(" ", FirebaseAuth.getInstance().getCurrentUser().getUid(), message.getText().toString());
        message.setText("");

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

    public void getDirections(View view) {
        String directionLink = "https://maps.googleapis.com/maps/api/directions/json?origin=45.4308874,-75.6748964&destination=45.384700,-75.694560&mode=driving&key=AIzaSyAb0Ai52RbZysGtSEjZhH0x0YxFM3_x-Go";
        final DocumentReference docRef = db.collection("CurrentUserLocations").document(UserReq);
        final DocumentReference myRef = db.collection("CurrentUserLocations")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final ArrayList<Locator> locators = new ArrayList<>();

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()){
                        Locator requesterlocator = document.toObject(Locator.class);
                        locators.add(requesterlocator);
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()){
                                        Locator tutorlocator = document.toObject(Locator.class);
                                        locators.add(tutorlocator);
                                        //Start Map Fragment
                                        Intent intent = new Intent(contexts, DirectionsView.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("locators", locators);
                                        intent.putExtra("BUNDLE", bundle);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }
}
