package com.groupproject.Controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.groupproject.R;


public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.maps_fragment, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    return;
                }
                createMap();
            }
        });

        return rootView;
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //TODO: Error message if location denied?
        switch (requestCode) {
            case 1: {
                if ((grantResults.length > 0) && (grantResults[0] +
                        grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                    createMap();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        }
    }

    private void createMap() {
        googleMap.setMyLocationEnabled(true);
        //TODO: Get user's location and zoom in on it using the code below

        // For zooming automatically to the location of the marker
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(location.getLatitude(),
//                        location.getLongitude())).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
