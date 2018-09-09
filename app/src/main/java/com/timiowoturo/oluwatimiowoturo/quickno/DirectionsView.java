package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.timiowoturo.oluwatimiowoturo.quickno.Fragments.Home;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Locator;

import java.util.ArrayList;

public class DirectionsView extends AppCompatActivity implements Home.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_view);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Locator> userLocations = (ArrayList<Locator>) args.getSerializable("locators");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment homef = Home.newInstance(userLocations);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragContainer2, homef);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
