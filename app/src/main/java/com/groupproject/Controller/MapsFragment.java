package com.groupproject.Controller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Event;
import com.groupproject.Model.LocationHelper;
import com.groupproject.Model.LocationHelper.LocationRes;
import com.groupproject.R;

import java.util.Calendar;


public class MapsFragment extends Fragment {


    private static final int DEFAULT_ZOOM = 15;
    LocationHelper locHelper;
    MapView mMapView;
    private GoogleMap googleMap;
    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private LocationControl locationControlTask;
    private boolean hasLocation = false;
    private Location currentLocation;
    public LocationRes locationResult = new LocationRes() {

        @Override
        public void gotLocation(Location location) {
            currentLocation = new Location(location);
            hasLocation = true;
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.maps_fragment, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        DataBaseAPI.loadActiveEvents();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() { //THIS DEALS WITH THE FIRST TIME THE MAP IS LOADED
                        addPinsToMap();
                    }
                });

                if (ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }

        });

        FloatingActionButton createEvent
                = (FloatingActionButton) rootView.findViewById(R.id.create_event_fab);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Tap to add a pin!", Toast.LENGTH_LONG).show();
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        MarkerOptions marker = new MarkerOptions().position(
                                new LatLng(point.latitude, point.longitude)).title("");
                        googleMap.addMarker(marker);
                        googleMap.setOnMapClickListener(null);
                        Intent intent = new Intent(getActivity(), AddEventActivity.class);
                        intent.putExtra("event_location",
                                new LatLng(point.latitude, point.longitude));
                        startActivity(intent);
                    }
                });
            }
        });

        Context context = this.getContext();

        locHelper = new LocationHelper();
        locHelper.getLocation(context, locationResult);
        locationControlTask = new LocationControl();
        locationControlTask.execute(this.getActivity());

        setUpListener();
        return rootView;
    }
//
    private void setUpListener() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
                if (event != null) {
                    addPinsToMap();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dataBaseAPI.addChildListener("events", childEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        locHelper.stopLocationUpdates();
        locationControlTask.cancel(true);
    }

    public void addPinsToMap() {
        googleMap.clear();
        for (Event event : DataBaseAPI.getEventMap().values()){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(event.getCustomLocation().getLatitude(), event.getCustomLocation().getLongitude()))
                    .title(event.getName())
                    .snippet(event.getEndDate().toString()));
        }
    }

    private void useLocation() {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, DEFAULT_ZOOM));
    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        //TODO: Error message if location denied?
        switch (requestCode) {
            case 1: {
                if ((grantResults.length > 0) && (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                    //TODO EMPTY IF STATEMENT
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        }
    }

    private class LocationControl extends AsyncTask<Context, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            this.dialog.setMessage("Searching");
            this.dialog.show();
        }

        protected Void doInBackground(Context... params) {
            //Wait 10 seconds to see if we can get a location from either network or GPS, otherwise stop
            Long t = Calendar.getInstance().getTimeInMillis();
            while (!hasLocation && Calendar.getInstance().getTimeInMillis() - t < 15000) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            if (currentLocation != null) {
                useLocation();
            } else {
                //Couldn't find location, do something like show an alert dialog
            }
        }
    }


}


