package com.timiowoturo.oluwatimiowoturo.quickno.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Locator;
import com.timiowoturo.oluwatimiowoturo.quickno.R;
import com.timiowoturo.oluwatimiowoturo.quickno.Utils.DirectionsDownloader;
import com.timiowoturo.oluwatimiowoturo.quickno.Utils.FirestoreService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Home";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public boolean mLocationPermissionGranted;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDClient;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private FusedLocationProviderClient mFusedLocationClient;
    ArrayList<Locator> locators = new ArrayList<>();

    private FirebaseAuth mAuth;

    public  GoogleMap mMap;
    public MapView mapView;
    ArrayList<Locator> usersL;
    public Home() {
        // Required empty public constructor
    }

    public Home (ArrayList<Locator> lsu){
        this.usersL = lsu;


        new DownloadFilesTask().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" +
                String.valueOf(lsu.get(1).getLat()) + "," + String.valueOf(lsu.get(1).getLng())+ "&destination=" +
                String.valueOf(lsu.get(0).getLat()) + "," + String.valueOf(lsu.get(0).getLng()) + "&mode=driving&key=AIzaSyAb0Ai52RbZysGtSEjZhH0x0YxFM3_x-Go");

    }

    // TODO: Rename and change types and number of parameters
    public static Home newInstance(ArrayList<Locator> locatorsusers) {
        Home fragment = new Home(locatorsusers);
        return fragment;
    }

    public static Home newInstance(){
        Home fragment = new Home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mGeoDataClient = Places.getGeoDataClient(this.getContext());
        mPlaceDClient = Places.getPlaceDetectionClient(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mapView = (MapView) view.findViewById(R.id.quicknoMap);
        mapView.onCreate(savedInstanceState);

        Log.d(TAG, "here ==> " + mapView.toString());
        mapView.getMapAsync(this);
        mapView.onResume();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermision();


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getLocationPermision(){

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Log.d(TAG, "Here ==> " + mMap.getMapType());
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this.getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                    .zoom(17)                   // Sets the zoom
                                    .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            FirestoreService service = new FirestoreService();
                            service.writeUserLocation(location.getLatitude(), location.getLongitude());
                            //Toast.makeText(getContext(), "Your Quickno's location has been saved", Toast.LENGTH_LONG).show();
                            // Getting every user's location on the map.
                            service.db.collection("CurrentUserLocations")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value,
                                                            @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.w(TAG, "Listen failed.", e);
                                                return;
                                            }
                                            for (QueryDocumentSnapshot doc : value) {
                                                Locator locator = new Locator((String) doc.getData().get("uid"),
                                                        (Double) doc.getData().get("lat"),
                                                        (Double) doc.getData().get("lng"));
                                                locators.add(locator);
                                            }
                                            Log.d(TAG, "Current locators: " + locators);
                                            // Adding markers for all locations
                                            plotLocations();
                                        }
                                    });
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    public void plotLocations(){

    }

    private class DownloadFilesTask extends AsyncTask<String, Void, String> {

        DirectionsDownloader download;

        protected String doInBackground(String... url) {

            download = new DirectionsDownloader(url[0]);

            return download.getData();
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(String result) {
            try{
                PolylineOptions options = new PolylineOptions();
                JSONObject directionsObject = new JSONObject(download.getData());
                Log.d(TAG, "Current object: " + directionsObject);
                ArrayList<LatLng> points = new ArrayList<>();
                JSONArray LEGS = directionsObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                JSONArray steps = LEGS.getJSONObject(0).getJSONArray("steps");


//                for (int i = 0; i < steps.length(); i++){
//                    points.add(new LatLng(Double.parseDouble(steps.getJSONObject(i).getJSONObject("start_location").getString("lat")),
//                            Double.parseDouble(steps.getJSONObject(i).getJSONObject("start_location").getString("lng"))));
//
//                }





                String polyline = directionsObject.getJSONArray("routes").getJSONObject(0).
                        getJSONObject("overview_polyline").getString("points");
//                options.addAll(points);
//                options.width(5);
//                options.color(R.color.colorPrimary);

                List<LatLng> list = decodePoly(polyline);

//                mMap.addPolyline(decodePoly(polyline));

                for (int z = 0; z < list.size() - 1; z++) {
                    LatLng src = list.get(z);
                    LatLng dest = list.get(z + 1);
                    Polyline line = mMap.addPolyline(new PolylineOptions().add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude)).width(4).color(R.color.colorPrimary).geodesic(true));
                }

                Log.d(TAG,"polyline");


                mMap.addMarker( new MarkerOptions()
                .position(new LatLng(usersL.get(0).getLat(), usersL.get(0).getLng()))
                        .title("Requester"));

            } catch (Exception e){
                Log.d(TAG,"Catch in onPostExecute");
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            //Log.d(TAG, "Data: " + productList.size());
        }

        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }
}
