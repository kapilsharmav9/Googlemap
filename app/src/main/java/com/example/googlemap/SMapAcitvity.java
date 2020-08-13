//package com.example.googlemap;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.Projection;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HttpContext;
//import org.json.JSONObject;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener {
//
//    private GoogleMap mMap;
//    private GoogleMap Map;
//    private GoogleApiClient mGoogleApiClient;
//    Button b1, b2, b3, b4;
//    LocationManager location;
//    double lat;
//    double lon;
//    Gps gps;
//    private static LatLng searchPlace;
//    LatLng current;
//    PlaceAutocompleteFragment autocompleteFragment;
//    private Marker search;
//    String SearchPlaceName;
//    double searchlon;
//    double searchlat;
//    LatLng position;
//    int i = 0;
//    Polyline line, line2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, this).build();
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this);
//        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        b1 = (Button) findViewById(R.id.b1);
//        b2 = (Button) findViewById(R.id.b2);
//        b3 = (Button) findViewById(R.id.b3);
//        b4 = (Button) findViewById(R.id.b4);
//        location = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        b1.setOnClickListener(this);
//        b2.setOnClickListener(this);
//        b3.setOnClickListener(this);
//        b4.setOnClickListener(this);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                if (i == 0) {
//                    SearchPlaceName = place.getName().toString();
//                    searchPlace = place.getLatLng();
//                    searchlat = searchPlace.latitude;
//                    searchlon = searchPlace.longitude;
//
//                    search = mMap.addMarker(new MarkerOptions().position(searchPlace).title(SearchPlaceName).draggable(true).flat(true));
//                    search.setTag(0);
//
//                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//                    boundsBuilder.include(current);
//                    boundsBuilder.include(searchPlace);
//                    LatLngBounds bounds = boundsBuilder.build();
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));
//
//                    line = mMap.addPolyline(new PolylineOptions()
//                            .add(new LatLng(searchlat, searchlon), new LatLng(lat, lon))
//                            .width(5)
//                            .color(Color.BLUE));
//
//                    Location startPoint = new Location("locationA");
//                    startPoint.setLatitude(lat);
//                    startPoint.setLongitude(lon);
//
//                    Location endPoint = new Location("locationA");
//                    endPoint.setLatitude(searchlat);
//                    endPoint.setLongitude(searchlon);
//
//                    double distance = startPoint.distanceTo(endPoint);
//
//                    double distanceKM = distance / 1000;
//                    double roundKm = roundTwoDecimals(distanceKM);
//
//                    Toast.makeText(getApplicationContext(), String.valueOf(roundKm) + " KM", Toast.LENGTH_SHORT).show();
//
//                    String url = getDirectionsUrl();
//                    DownloadTask downloadTask = new DownloadTask();
//                    downloadTask.execute(url);
//                    i++;
//                } else
//                {
//                    line.remove();
//                    line2.remove();
//                    search.remove();
//                    SearchPlaceName = place.getName().toString();
//
//                    Toast.makeText(getApplicationContext(), SearchPlaceName, Toast.LENGTH_SHORT).show();
//                    searchPlace = place.getLatLng();
//                    searchlat = searchPlace.latitude;
//                    searchlon = searchPlace.longitude;
//
//                    search = mMap.addMarker(new MarkerOptions().position(searchPlace).title(SearchPlaceName).draggable(true).flat(true));
//                    search.setTag(0);
//
//                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//                    boundsBuilder.include(current);
//                    boundsBuilder.include(searchPlace);
//                    LatLngBounds bounds = boundsBuilder.build();
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));
//
//                    line = mMap.addPolyline(new PolylineOptions()
//                            .add(new LatLng(searchlat, searchlon), new LatLng(lat, lon))
//                            .width(5)
//                            .color(Color.BLUE));
//
//                    Location startPoint = new Location("locationA");
//                    startPoint.setLatitude(lat);
//                    startPoint.setLongitude(lon);
//
//                    Location endPoint = new Location("locationA");
//                    endPoint.setLatitude(searchlat);
//                    endPoint.setLongitude(searchlon);
//
//                    double distance = startPoint.distanceTo(endPoint);
//
//                    double distanceKM = distance / 1000;
//                    double roundKm = roundTwoDecimals(distanceKM);
//
//                    Toast.makeText(getApplicationContext(), String.valueOf(roundKm) + " KM", Toast.LENGTH_SHORT).show();
//
//                    String url = getDirectionsUrl();
//                    DownloadTask downloadTask = new DownloadTask();
//                    downloadTask.execute(url);
//
//                }
//
//            }
//
//            @Override
//            public void onError(Status status) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        int a = v.getId();
//        switch (a) {
//            case R.id.b1:
//                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                break;
//
//
//            case R.id.b2:
//                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                break;
//
//            case R.id.b3:
//                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                break;
//
//            case R.id.b4:
//                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                break;
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        mMap = googleMap;
//        gps = new Gps(MapsActivity.this);
//        mMap.setOnMarkerClickListener(this);
//        if (gps.canGetLocation()) {
//
//            lat = gps.getLatitude();
//            lon = gps.getLongitude();
//        } else {
//            gps.showSettingsAlert();
//        }
//        current = new LatLng(lat, lon);
//        mMap.addMarker(new MarkerOptions().position(current).title("Current Position"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//        mMap.getUiSettings().setZoomGesturesEnabled(true);
//
//
//    }
//
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        String name = marker.getTitle();
//        Toast.makeText(MapsActivity.this, "Clicked " + name, Toast.LENGTH_SHORT).show();
//        return false;
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    double roundTwoDecimals(double d) {
//        DecimalFormat twoDForm = new DecimalFormat("#");
//        return Double.valueOf(twoDForm.format(d));
//    }
//
//    private String getDirectionsUrl() {
//
//        String str_origin = "origin=" + lat + "," + lon;
//        String str_dest = "destination=" + searchlat + "," + searchlon;
//        String sensor = "sensor=false";
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//        String output = "json";
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//        return url;
//    }
//
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.connect();
//            iStream = urlConnection.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//            StringBuffer sb = new StringBuffer();
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            data = sb.toString();
//
//            Log.e("Result", data);
//            br.close();
//        } catch (Exception e) {
//            Log.e("Exception while", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//    private class DownloadTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            String data = "";
//            try {
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            ParserTask parserTask = new ParserTask();
//            parserTask.execute(result);
//        }
//    }
//
//
//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                Direction parser = new Direction();
//                routes = parser.parse(jObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList<LatLng> points = null;
//            PolylineOptions lineOptions = null;
//
//            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList<LatLng>();
//                lineOptions = new PolylineOptions();
//
//                List<HashMap<String, String>> path = result.get(i);
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    position = new LatLng(lat, lng);
//                    points.add(position);
//                }
//                // Adding all the points in the route to LineOptions
//                lineOptions.addAll(points);
//                lineOptions.width(4);
//                lineOptions.color(Color.RED);
//            }
//            // Drawing polyline in the Google Map for the i-th route
//            line2 = mMap.addPolyline(lineOptions);
//
//        }
//    }
//
//}
