package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.DashBoard;
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.Explore;
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.Home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener,
        DashBoard.OnFragmentInteractionListener, Explore.OnFragmentInteractionListener {
    private TextView mTextMessage;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    Fragment currentFragment;
    private FirebaseAuth mAuth;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activitymainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuhome:
                Fragment homef = Home.newInstance();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragContainer, homef);
                    transaction.addToBackStack(null);
                    transaction.commit();
                return true;
            case R.id.app_bar_search:
                Fragment searchf = Explore.newInstance();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragContainer, searchf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);

            case R.id.profile:
                Fragment profilef = Explore.newInstance();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragContainer, profilef);
                    transaction.addToBackStack(null);
                    transaction.commit();
                return true;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            firebaseUI();
        }

        else{
            checkDbRequests();
            Fragment tutors = Home.newInstance();
            currentFragment = tutors;
            transaction.replace(R.id.fragContainer, tutors);
            transaction.commit();
        }
    }
    //Firebase Auth Stuff
    public void firebaseUI(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Starting onboarding activity with intent and startActivity()
                Intent intent = new Intent(this, Onboarding.class);
                startActivity(intent);
            } else { Log.d(LOG_TAG, resultCode+"Sign in unsucessful"); } } }
            //End Firebase Auth stuff


    @Override
    protected void onPause() {
        super.onPause();
        transaction.hide(currentFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transaction.detach(currentFragment);
    }

    public void checkDbRequests(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("messages")
                .whereEqualTo("User Requesting", FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                else{
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc.get("User Requesting") != null) { //Change it back to User Receiving.
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.container),
                                    "A user needs your help " + doc.getString("User Requesting"), Snackbar.LENGTH_SHORT);
                            mySnackbar.show();
                        }
                    }
                }
            }
        });
    }
}
