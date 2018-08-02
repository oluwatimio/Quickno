package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Onboarding extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mAuth = FirebaseAuth.getInstance();

         currentUser = mAuth.getCurrentUser();

         //Wont Display a java null pointer exception. A user is always signed in during onboarding.
         String userName = currentUser.getDisplayName();

        TextView userWelcome = findViewById(R.id.userWelcome);

        userWelcome.setText("Welcome to Quickno, " + userName);

    }

    public void userInfo(View view){
        Intent intent = new Intent(this, GetInfo.class);
        startActivity(intent);
    }
}
