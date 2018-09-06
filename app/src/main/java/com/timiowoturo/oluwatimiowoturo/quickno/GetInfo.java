package com.timiowoturo.oluwatimiowoturo.quickno;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Quickno;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.UserRating;
import com.timiowoturo.oluwatimiowoturo.quickno.Utils.FirestoreService;

import java.util.ArrayList;

public class GetInfo extends AppCompatActivity {

    FirebaseAuth mAuth;

    ArrayList<Quickno> quicknos = new ArrayList<>();

    FirestoreService firestoreS = new FirestoreService();

    public boolean mLocationPermissionGranted;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDClient;
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDClient = Places.getPlaceDetectionClient(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadUser(view);
            }
        });
    }

    public void addChip(View view){
        ChipGroup chipGroup = (ChipGroup) findViewById(R.id.quicknochips);


        TextInputEditText editText = findViewById(R.id.quicknoentry);

        Chip chip = new Chip(this);

        chip.setChipText(editText.getText().toString());
        chip.setCloseIconEnabled(true);
        chip.setChipBackgroundColorResource(R.color.colorAccent);
        chip.setTextAppearanceResource(R.style.ChipTextStyle);

        chipGroup.addView(chip);
        mAuth = FirebaseAuth.getInstance();
        Quickno quickno = new Quickno(editText.getText().toString());
        quicknos.add(quickno);

    }

    public void uploadUser(View view){
        UserRating rating = new UserRating(mAuth.getCurrentUser().getUid(), 0.0);
        firestoreS.addUser(mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getUid(),quicknos,
                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(),
                rating);
        Snackbar.make(view, "Your quickno's have been saved", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Toast.makeText(this, "Your quickno's have been saved", Toast.LENGTH_LONG).show();
        try{
            wait(1000);
        } catch (Exception e){

        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}
