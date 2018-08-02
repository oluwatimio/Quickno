package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.DashBoard;
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.Explore;
import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.Home;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Home.OnFragmentInteractionListener,
        DashBoard.OnFragmentInteractionListener, Explore.OnFragmentInteractionListener {
    private TextView mTextMessage;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    Fragment currentFragment;
    private FirebaseAuth mAuth;
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
                if (currentFragment != homef){
                    transaction = getSupportFragmentManager().beginTransaction();
                    currentFragment = homef;
                    transaction.replace(R.id.fragContainer, homef);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return true;
            case R.id.app_bar_search:
                Fragment searchf = Explore.newInstance();
                if (currentFragment != searchf){
                    transaction = getSupportFragmentManager().beginTransaction();
                    currentFragment = searchf;
                    transaction.replace(R.id.fragContainer, searchf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

            case R.id.profile:
                Fragment profilef = DashBoard.newInstance();
                if (currentFragment != profilef){
                    transaction = getSupportFragmentManager().beginTransaction();
                    currentFragment = profilef;
                    transaction.replace(R.id.fragContainer, profilef);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
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

            if (currentFragment == null){
                Fragment tutors = Home.newInstance();
                currentFragment = tutors;
                transaction.replace(R.id.fragContainer, tutors);
                transaction.commit();
            }

            else{
                transaction.show(currentFragment);
            }
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
}
