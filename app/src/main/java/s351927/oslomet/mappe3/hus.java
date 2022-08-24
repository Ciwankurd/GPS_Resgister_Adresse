package s351927.oslomet.mappe3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class hus extends DialogFragment {
    EditText etasje;
    EditText beskrivelse;
    TextView adresse;
    String gateadresse;
    LatLng k;
    Button Registere;
    Marker marker;
    String output;
    Boolean boolRegistering;
    ArrayList<lagrehus> lagrehuse;
    public hus(LatLng k,Marker marker,String gateadresse, ArrayList<lagrehus> lagrehuse) {
        this.k=k;
        this.marker=marker;
        this.gateadresse=gateadresse;
        this.boolRegistering=false;
        this.lagrehuse=lagrehuse;

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {

        if(boolRegistering=false){
            marker.remove();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.activity_hus,container,false);
       etasje= v.findViewById(R.id.etasjer);
       beskrivelse= v.findViewById(R.id.beskrivelse);
       Registere=v.findViewById(R.id.Registere);
       adresse=v.findViewById(R.id.adresse);
       adresse.setText(gateadresse);
       Registere.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               RegistereHus task = new RegistereHus();
               if(!etasje.getText().toString().isEmpty() && !beskrivelse.getText().toString().isEmpty() && !adresse.getText().toString().isEmpty()) {
                   String id = task.execute("http://studdata.cs.oslomet.no/~dbuser18/registerhus.php/?beskrivelse=" + beskrivelse.getText().toString().replaceAll(" ", "%20").replaceAll("\n", "\\n")
                           + "&adresse=" + gateadresse.replaceAll(" ", "%20") +
                           "&koordinatX=" + k.latitude + "&koordinatY=" + k.longitude + "&etasjer=" + etasje.getText().toString()).toString();
                   marker.setTitle(id);
                   Log.d("iininserted", id);

                  // Intent intent=new Intent(getContext(),MapsActivity.class);
                   //startActivity(intent);
               }
               else {

                   Toast toast = Toast.makeText(getContext(), "Feil! Alle feltene må fylles!", Toast.LENGTH_SHORT);
                   toast.show();
               }
           }
       });
       return v;
    }

    class RegistereHus extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... voids) {
            try {
                URL urlen = new URL(voids[0]);
                HttpURLConnection conn = (HttpURLConnection)urlen.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept","application/json");
                if(conn.getResponseCode()!=200){
                    throw new RuntimeException("Failed : HTTP error code: "+conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                String s="";
                 output ="";
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }


            }
            catch (IOException e){
                Log.d("Test Registering","feil i Registering");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            marker.setTitle(output);
            boolRegistering=true;
            /*
            lagrehus lagrehus =new lagrehus();
            lagrehus.setBeskrivelse(beskrivelse.getText().toString());
            lagrehus.setEtasjer(Integer.parseInt(etasje.getText().toString()));
             */
            lagrehus ethusreg = new lagrehus(Long.parseLong(output),beskrivelse.getText().toString(),gateadresse,Double.parseDouble(k.latitude+""),Double.parseDouble(k.longitude+""),Integer.parseInt(etasje.getText().toString()));
            lagrehuse.add(ethusreg);
            dismiss();
        }
    }

}