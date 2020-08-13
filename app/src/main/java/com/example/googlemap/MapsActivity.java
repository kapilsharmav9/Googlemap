package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {


    private GoogleMap mMap;
    private String API_KEY = "AIzaSyAiSBxUuZXDqOkbquMbs0Acry34r6Ih_4Q";
    Location mLastLocation;
    Marker marker;
    LatLng currentLatLong;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    String SearchPlaceName;
    Gps gps;
    double lat;
    double lon;
    private static LatLng searchPlace;
    private Marker searchMarker;
    double searchlon;
    double searchlat;
    LatLng position;
    private  String request;
    int i = 0;
    Polyline line, line2;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//      mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build();

        Places.initialize(getApplicationContext(), API_KEY);

        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        assert autocompleteSupportFragment != null;


       autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteSupportFragment.setCountry("IN");
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (i == 0) {
                    SearchPlaceName = place.getName().toString();

                    searchPlace = place.getLatLng();
                    searchlat = searchPlace.latitude;
                    searchlon = searchPlace.longitude;
                    searchMarker = mMap.addMarker(new MarkerOptions().position(searchPlace).title(SearchPlaceName).draggable(true).flat(true));
                    searchMarker.setTag(0);

                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    boundsBuilder.include(currentLatLong);
                    boundsBuilder.include(searchPlace);
                    LatLngBounds bounds = boundsBuilder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));
                    line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(searchlat, searchlon), new LatLng(lat, lon))
                            .width(5)
                            .color(Color.GREEN));

                    Location startPoint = new Location("locationA");
                    startPoint.setLatitude(lat);
                    startPoint.setLongitude(lon);

                    Location endPoint = new Location("locationA");
                    endPoint.setLatitude(searchlat);
                    endPoint.setLongitude(searchlon);

                    double distance = startPoint.distanceTo(endPoint);

                    double distanceKM = distance / 1000;
                    double roundKm = roundTwoDecimals(distanceKM);

                    Toast.makeText(getApplicationContext(), String.valueOf(roundKm) + " KM", Toast.LENGTH_SHORT).show();

                    String url = getDirectionsUrl();
                    MapsActivity.DownloadTask downloadTask = new MapsActivity.DownloadTask();
                    downloadTask.execute(url);
                    i++;
                } else
                {
                    line.remove();
                    line2.remove();
                    searchMarker.remove();
                    SearchPlaceName = place.getName().toString();

                    Toast.makeText(getApplicationContext(), SearchPlaceName, Toast.LENGTH_SHORT).show();
                    searchPlace = place.getLatLng();
                    searchlat = searchPlace.latitude;
                    searchlon = searchPlace.longitude;

                    searchMarker = mMap.addMarker(new MarkerOptions().position(searchPlace).title(SearchPlaceName).draggable(true).flat(true));
                    searchMarker.setTag(0);

                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    boundsBuilder.include(currentLatLong);
                    boundsBuilder.include(searchPlace);
                    LatLngBounds bounds = boundsBuilder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));

                    line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(searchlat, searchlon), new LatLng(lat, lon))
                            .width(5)
                            .color(Color.BLUE));

                    Location startPoint = new Location("locationA");
                    startPoint.setLatitude(lat);
                    startPoint.setLongitude(lon);

                    Location endPoint = new Location("locationA");
                    endPoint.setLatitude(searchlat);
                    endPoint.setLongitude(searchlon);

                    double distance = startPoint.distanceTo(endPoint);

                    double distanceKM = distance / 1000;
                    double roundKm = roundTwoDecimals(distanceKM);

                    Toast.makeText(getApplicationContext(), String.valueOf(roundKm) + " KM", Toast.LENGTH_SHORT).show();

                    String url = getDirectionsUrl();
                    MapsActivity.DownloadTask downloadTask = new MapsActivity.DownloadTask();
                    downloadTask.execute(url);

                }

            }

            @Override
            public void onError(@NonNull Status status) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gps = new Gps(MapsActivity.this);
        mMap.setOnMarkerClickListener(this);
        if (gps.canGetLocation()) {

            lat = gps.getLatitude();
            lon = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
        currentLatLong = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(currentLatLong).title("Current Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLong));
        mMap.getUiSettings().setZoomGesturesEnabled(true);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                    this);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (marker != null) {
            marker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        marker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String name = marker.getTitle();
        Toast.makeText(MapsActivity.this, "Clicked " + name, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Double.valueOf(twoDForm.format(d));
    }
    private String getDirectionsUrl() {

        String str_origin = "origin=" + lat + "," + lon;
        String str_dest = "destination=" + searchlat + "," + searchlon;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();

            Log.e("Result", data);
            br.close();
        } catch (Exception e) {
            Log.e("Exception while", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            MapsActivity.ParserTask parserTask = new MapsActivity.ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                Direction parser = new Direction();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.RED);
            }
            // Drawing polyline in the Google Map for the i-th route
            line2 = mMap.addPolyline(lineOptions);

        }
    }
   
}