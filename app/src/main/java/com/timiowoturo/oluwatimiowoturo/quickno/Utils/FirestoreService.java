package com.timiowoturo.oluwatimiowoturo.quickno.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.User;
public class FirestoreService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    private final String TAG = FirestoreService.class.getSimpleName();
    public FirestoreService(){ }
    public void addUser(String uid, String name){
        User user = new User(uid,name);
        db.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "User added with id: " + documentReference.getId());
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
                    }
                }
            }
        });
    }
}
