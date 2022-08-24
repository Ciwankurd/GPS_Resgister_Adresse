package s351927.oslomet.mappe3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class EndreSlettehus extends DialogFragment {

    EditText etasjeEndre;
    EditText beskrivelseEndre;
    TextView adresseEndre;
    LatLng ko;
    Button Endrehus;
    Button Sletthus;
    TextView opplysninger;
    Marker marker;

    lagrehus lagrehus;
    public EndreSlettehus(lagrehus ethus, Marker marker) {
        this.lagrehus=ethus;
        this.marker=marker;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_endre_slettehus,container,false);
        etasjeEndre= v.findViewById(R.id.etasjerendring);
        beskrivelseEndre= v.findViewById(R.id.beskrivelseendring);
        adresseEndre=v.findViewById(R.id.adresseendring);
        Endrehus=v.findViewById(R.id.Endre);
        opplysninger=v.findViewById(R.id.visInfo);
        beskrivelseEndre.setText(lagrehus.getBeskrivelse());
        adresseEndre.setText(lagrehus.getAdresse());
        etasjeEndre.setText(lagrehus.getEtasjer()+"");
        Sletthus=v.findViewById(R.id.sletthus);
        opplysninger.setText("GPS info :\nAdresse : "+lagrehus.getAdresse()+"\nBeskrivelse : "+lagrehus.getBeskrivelse()+"\nEtasje Nr. : "+lagrehus.getEtasjer());
        Endrehus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndreHus task= new EndreHus();
                task.execute("http://studdata.cs.oslomet.no/~dbuser18/endrehus.php/?husId="+lagrehus.husId+"&beskrivelse="+beskrivelseEndre.getText().toString().replaceAll(" ","%20").replaceAll("\n", "\\n")
                        +"&adresse="
                        +lagrehus.adresse.replaceAll(" ","%20")+
                        "&koordinatX="+lagrehus.getKooordinatX()+"&koordinatY="+lagrehus.KooordinatY+"&etasjer="+etasjeEndre.getText().toString());
            }
        });
        Sletthus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlettHus task= new SlettHus();
                task.execute("http://studdata.cs.oslomet.no/~dbuser18/sletthus.php/?husId="+lagrehus.husId);

            }

        });
        return v;

    }

class EndreHus extends AsyncTask<String,Void,Void> {

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
        }
        catch (IOException e){
            Log.d("Test Registering","feil i Registering");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        lagrehus.setBeskrivelse(beskrivelseEndre.getText().toString());
        lagrehus.setEtasjer(Integer.parseInt(etasjeEndre.getText().toString()));
        dismiss();

    }
}
    class SlettHus extends AsyncTask<String,Void,Void> {

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
            }
            catch (IOException e){
                Log.d("Test Registering","feil i Registering");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            marker.remove();
            dismiss();
        }
    }
}
