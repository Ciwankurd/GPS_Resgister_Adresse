package s351927.oslomet.mappe3;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import s351927.oslomet.mappe3.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ImageButton addlocation;
    private ArrayList<lagrehus> listhus = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getJSON task = new getJSON();
        task.execute(new
                String[]{"http://studdata.cs.oslomet.no/~dbuser18/henthusene.php"});
        mMap.getUiSettings().setAllGesturesEnabled(true);
        LatLng Oslomet = new LatLng(59.923932724093085, 10.731422583341356);
        mMap.addMarker(new MarkerOptions().position(Oslomet).title("Pilestredet 56, 0167 Oslo")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Oslomet, 18));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0 && addressList.get(0).getThoroughfare()!=null && addressList.get(0).getSubThoroughfare()!=null) {
                        Address address = addressList.get(0);
                        hus hus = new hus(latLng,mMap.addMarker(new MarkerOptions().position(latLng).title("Select Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.myloca))),
                                address.getThoroughfare()+" "+address.getSubThoroughfare(),listhus);
                        hus.show(getSupportFragmentManager().beginTransaction(), "Registering");
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Det er noen adresser her!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e("Feil", "Unable connect to Geocoder", e);
                }
            }




        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("id",marker.getTitle());
                for(int i=0; i<listhus.size(); i++){
                    if(marker.getTitle().equals(String.valueOf(listhus.get(i).husId))){
                        EndreSlettehus endreSlettehus = new EndreSlettehus(listhus.get(i),marker);
                        endreSlettehus.show(getSupportFragmentManager().beginTransaction(), "Endrehus");
                    }
                }

                return true;
            }

        });

    }

    private class getJSON extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... urls) {
            String retur = "";
            String s = "";
            String output = "";
            for (String url : urls) {
                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)
                            urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code :" + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    System.out.println("Output from Server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();
                    try {
                        JSONArray mat = new JSONArray(output);
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            lagrehus ethus= new lagrehus(jsonobject.getLong("husId"),jsonobject.getString("beskrivelse"),
                                    jsonobject.getString("adresse"),jsonobject.getDouble("koordinatX"),
                                    jsonobject.getDouble("koordinatY")
                                    , jsonobject.getInt("etasjer"));


                            listhus.add(ethus);
                        }
                        return "testlagring";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
                } catch (Exception e) {
                    return "Noe gikk feil";
                }
            }
            return retur;
        }
        @Override
        protected void onPostExecute(String ss) {
            for(int i=0; i<listhus.size();i++){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(listhus.get(i).KooordinatX,listhus.get(i).KooordinatY));
                markerOptions.title(listhus.get(i).husId+"");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.myloca));
                mMap.addMarker(markerOptions);

            }
        }
    }
}