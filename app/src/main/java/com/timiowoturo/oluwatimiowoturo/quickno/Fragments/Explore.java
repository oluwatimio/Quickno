package com.timiowoturo.oluwatimiowoturo.quickno.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Locator;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.Quickno;
import com.timiowoturo.oluwatimiowoturo.quickno.Models.User;
import com.timiowoturo.oluwatimiowoturo.quickno.R;
import com.timiowoturo.oluwatimiowoturo.quickno.Utils.FirestoreService;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Explore.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Explore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Explore extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Explore";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirestoreService service = new FirestoreService();
    public final ArrayList<User> usersQueried = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    ArrayList<Locator> locators = new ArrayList<>();
    ArrayList<Locator> sortedLocators = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private ArrayList<User> dataSet = new ArrayList<>();

    public Explore() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Explore.
     */
    // TODO: Rename and change types and number of parameters
    public static Explore newInstance(String param1, String param2) {
        Explore fragment = new Explore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Explore newInstance() {
        Explore fragment = new Explore();
        return fragment;
    }

    public void doMySearch(final String string){
        final ArrayList<User> users = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                users.add(user);
                            }

                            for(int i = 0; i< users.size(); i++){
                                for (int j = 0; j < users.get(i).getQuicknos().size(); j++){
                                    if (users.get(i).getQuicknos().get(j).getTag().equals(string)){
                                        usersQueried.add(users.get(i));
                                    }
                                }
                            }
                        } else {
                        }
                    }
                });


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        Locator currentUser = getUserLocator();
                        calcDistance(currentUser);
                    }
                });

    }

    public Locator getUserLocator(){
        //First get current user;
        Locator locator = new Locator();
        for (int i = 0; i<locators.size(); i++){
            if (locators.get(i).getUid().equals(mAuth.getCurrentUser().getUid())){
                locator = locators.get(i);
                break;
            }
        }

        return locator;
    }

    // Calculating distance
    public void calcDistance(Locator currentUser){

        for (int i = 0; i < locators.size(); i++){
            float[] results = new float[1];
            if (locators.get(i).getUid() != currentUser.getUid()){
                Location.distanceBetween(currentUser.getLat(), currentUser.getLng(),
                        locators.get(i).getLat(), locators.get(i).getLng(), results);
                if (results[0] <= 10000){
                    sortedLocators.add(locators.get(i));
                }
            }
        }

        getUsers();
    }

    //Getting close users from db to display in recycler view
    public void getUsers(){
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new ExploreAdapter(getContext(), usersQueried);

        if (getView() != null){
            recyclerView = getView().findViewById(R.id.recyclerExplore);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Setting recycler view
        final View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        final EditText search = rootView.findViewById(R.id.searchView);

        search.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) &&( i == KeyEvent.KEYCODE_ENTER)){
                    doMySearch(search.getText().toString());
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
