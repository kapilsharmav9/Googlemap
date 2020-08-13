//package com.example.googlemap;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.model.RectangularBounds;
//import com.google.android.libraries.places.api.model.TypeFilter;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
//
//import java.util.Arrays;
//
//public class code {
//    package com.example.googlemap;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentActivity;
//
//import android.nfc.Tag;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.model.RectangularBounds;
//import com.google.android.libraries.places.api.model.TypeFilter;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
//import com.google.android.libraries.places.widget.internal.ui.AutocompleteImplFragment;
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
//
//import java.util.Arrays;
//
//    public class MapsActivity extends AppCompatActivity {
//
//        private GoogleMap mMap;
//        private String API_KEY = "AIzaSyAiSBxUuZXDqOkbquMbs0Acry34r6Ih_4Q";
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_maps);
//            Places.initialize(getApplicationContext(), API_KEY);
//            PlacesClient placesClient = Places.createClient(this);
//            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
////        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.autocomplete_fragment);
////        mapFragment.getMapAsync(this);
//
//
//            AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//            assert autocompleteSupportFragment != null;
//            autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
//            autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance
//                    (new LatLng(30.885147, 75.856706), new LatLng(30.935837, 75.730587)));
//            autocompleteSupportFragment.setCountries("IN");
//            autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//            autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(@NonNull Place place) {
////        Log.i(TAG,"Place:"+place.getName()+","+ place.getName());
//                }
//
//                @Override
//                public void onError(@NonNull Status status) {
////Log.i(TAG,"an error occur:"+status);
//                }
//            });
//        }
//
//
//    }
//
//}
