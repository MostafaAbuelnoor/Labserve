package com.project.labserve;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.labserve.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //This function returns the color as a BitmapDescriptor which the marker uses
    //Taken off https://stackoverflow.com/questions/19076124/android-map-marker-color
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE); //Changed the map view to satellite type
        String labName = getIntent().getStringExtra("labName"); //Grabbing the lab name from LabActivity intent
        if(labName.contains("ESB 1043")){
            // Add a marker in ESB 1043 and move the camera
            LatLng esb = new LatLng(25.312126, 55.490149);
            mMap.addMarker(new MarkerOptions()
                    .position(esb)
                    .title("ESB")
                    .snippet("1043")
                    .icon(getMarkerIcon("#EE5A24")));
            //Add the marker on the latlang and give it a title and subtitle for when the user clicks on it also change color to match app theme
            mMap.moveCamera(CameraUpdateFactory.newLatLng(esb));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18)); //Zoom level 18 so that the user can see where the lab without zooming himself
        }
        else if(labName.contains("EB2 103")){
            // Add a marker in EB 2 103 and move the camera
            LatLng eb2 = new LatLng(25.311775, 55.491378);
            mMap.addMarker(new MarkerOptions()
                    .position(eb2)
                    .title("EB2")
                    .snippet("103")
                    .icon(getMarkerIcon("#EE5A24")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eb2));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
        else if(labName.contains("IC 2")){
            // Add a marker in IC 2 and move the camera
            LatLng ic = new LatLng(25.311185, 55.492213);
            mMap.addMarker(new MarkerOptions()
                    .position(ic)
                    .title("Library")
                    .snippet("IC2")
                    .icon(getMarkerIcon("#EE5A24")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ic));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
        else if(labName.contains("ART 103")){
            // Add a marker in ARTS 103 and move the camera
            LatLng arts = new LatLng(25.309041, 55.491342);
            mMap.addMarker(new MarkerOptions()
                    .position(arts)
                    .title("Arts")
                    .snippet("103")
                    .icon(getMarkerIcon("#EE5A24")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arts));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        }
    }
}
