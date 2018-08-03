package com.timiowoturo.oluwatimiowoturo.quickno.Utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Locator;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Quickno;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.User;

import java.util.ArrayList;

public class FirestoreService {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final String TAG = FirestoreService.class.getSimpleName();
    private User cUser;
    public ArrayList<Locator> locators = new ArrayList<>();
    public FirestoreService(){
    }
    public void addUser(String name,String uid, ArrayList<Quickno> quicknos){

        User user = new User(name,uid, quicknos);

        db.collection("Users").document(uid).set(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failure" + e);
            }
        });
    }
    public void getCurrentUser() {
        mAuth = FirebaseAuth.getInstance();
        db.collection("Users").whereEqualTo("uid",
                mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        User cUser = document.toObject(User.class);
                        setUser(cUser);
                    }
                }
            }
        });
    }
    public void setUser(User user){
        this.cUser = user;
    }
    public User getUser(){
        return this.cUser;
    }

    public void writeUserLocation(Double lat, Double lng){
        Locator locator = new Locator(mAuth.getCurrentUser().getUid(), lat, lng);
        db.collection("CurrentUserLocations").document(mAuth.getCurrentUser().getUid())
                .set(locator);
    }
}
