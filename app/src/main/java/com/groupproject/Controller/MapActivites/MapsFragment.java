package com.groupproject.Controller.MapActivites;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.groupproject.Controller.EventActivities.AddEventActivity;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Event;
import com.groupproject.R;

import java.util.Calendar;


public class MapsFragment extends Fragment {

    //TODO When clicked back on create event pin stays, also the directions to which pin ???

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private static final int DEFAULT_ZOOM = 15;
    private boolean foundLocation;
    MapView mMapView;
    private GoogleMap googleMap;
    private LocationControl locationControlTask;
    View rootView;

    public static Fragment newInstance(){
        Fragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.maps_fragment, container, false);
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

                askForLocationPermissions();
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
        return rootView;
    }




    @Override
    public void onStop() {
        super.onStop();
        if (locationControlTask != null)
            locationControlTask.cancel(true);
    }

    public void addPinsToMap() {
        googleMap.clear();
        for (Event event : DataBaseAPI.getEventMap().values()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(event.getCustomLocation().getLatitude(), event.getCustomLocation().getLongitude()))
                    .title(event.getName())
                    .snippet(event.getEndDate().toString()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        rootView.requestFocus();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if ((grantResults.length > 0) &&
                    (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                setCurrentLocation();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please enable location settings. " +
                        "If no request is appearing, go to Settings > Apps > Pinned > Permissions ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                askForLocationPermissions();
                            }
                        });
                alertDialog.show();
            }
        }
    }

    private void askForLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            MapsFragment.this.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            setCurrentLocation();
        }
    }

    private class LocationControl extends AsyncTask<Context, Void, Void> {
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Context... params) {
            //Wait 15 seconds to see if we can get a location from either network or GPSp
            Long t = Calendar.getInstance().getTimeInMillis();
            while (!foundLocation) {
                searchForLocation();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(final Void unused) {
            progressBar.setVisibility(View.GONE);
            setCurrentLocation();
        }
    }

    /**
     * Search for location without creating an async task
     */
    private void searchForLocation() {
        foundLocation = false;
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(getActivity());
        //Ignore the red line - we check for permissions before this function is called
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> lastLocation = mFusedLocationProviderClient.getLastLocation();
        lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    Location location = task.getResult();
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                DEFAULT_ZOOM);
                        googleMap.moveCamera(update);
                        foundLocation = true;
                    }
                }
            }
        });
    }

    /**
     * Set current location, creating an async task if the location can't be found at that moment
     */
    private void setCurrentLocation() {
        foundLocation = false;
        //Ignore the red line - we check for permissions before this function is called
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(getActivity());
        //Ignore the red line - we check for permissions before this function is called
        Task<Location> lastLocation = mFusedLocationProviderClient.getLastLocation();
        lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    Location location = task.getResult();
                    if (location == null) {
                        locationControlTask = new LocationControl();
                        locationControlTask.execute(getActivity());
                    } else {
                        LatLng currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                DEFAULT_ZOOM);
                        googleMap.moveCamera(update);
                        foundLocation = true;
                    }
                }
            }
        });
    }
}


