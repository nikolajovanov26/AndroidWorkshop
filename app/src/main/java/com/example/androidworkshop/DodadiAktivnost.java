package com.example.androidworkshop;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// import com.google.android.gms.location.places.Place;
// import com.google.android.gms.location.places.ui.PlacePicker;

public class DodadiAktivnost extends AppCompatActivity implements View.OnClickListener{

    Button datumButton, vremeButton, lokacijaButton;
    EditText datumText, vremeText, lokacijaText;
    CheckBox povt,itno;
    private int godina, mesec, den, saat, minuta;
    private int PLACE_PICKER_REQUEST = 1;
    Double lat,lon;

    String lokacija;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodadi_aktivnost);


        Spinner aktivnosti = (Spinner) findViewById(R.id.aktivnosti);
        TextView opis = (TextView) findViewById(R.id.aktivnostiOpis);

        String aktivnost = aktivnosti.getSelectedItem().toString();

        aktivnosti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String aktivnost = aktivnosti.getSelectedItem().toString();
                switch (aktivnost){
                    case "Odenje vo market":
                        opis.setText("Kupuvanje proizvodi od market");
                        break;
                    case "Odenje vo apteka":
                        opis.setText("Kupuvanje lekovi od apteka");
                        break;
                    case "Gotvenje":
                        opis.setText("Gotvenje na obrok");
                        break;
                    case "Odrzuvanje na gradina":
                        opis.setText("Vadenje na cvekjinja, kosenje na trava i slicno");
                        break;
                    case "Pridruzba":
                        opis.setText("Pridruzba do banka, bolnica, vo park i slicno ");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        datumButton= (Button) findViewById(R.id.datum);
        vremeButton= (Button) findViewById(R.id.vreme);
        datumText= (EditText) findViewById(R.id.tDatum);
        vremeText= (EditText) findViewById(R.id.tVreme);
        lokacijaButton = (Button) findViewById(R.id.lokacija);
        lokacijaText = (EditText) findViewById(R.id.tLokacija);

        datumButton.setOnClickListener(this);
        vremeButton.setOnClickListener(this);
        lokacijaButton.setOnClickListener(this);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();



        // Retrieve the content view that renders the map.
        //setContentView(R.layout.activity_maps);

        // Construct a PlacesClient
        //Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        //placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.datum:
                final Calendar c = Calendar.getInstance();
                godina = c.get(Calendar.YEAR);
                mesec = c.get(Calendar.MONTH);
                den = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datumText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, godina, mesec, den);
                datePickerDialog.show();
                break;
            case R.id.vreme:
                // Get Current Time
                final Calendar c2 = Calendar.getInstance();
                saat = c2.get(Calendar.HOUR_OF_DAY);
                minuta = c2.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                vremeText.setText(hourOfDay + ":" + minute);
                            }
                        }, saat, minuta, true);
                timePickerDialog2.show();
                break;
            case R.id.lokacija:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DodadiAktivnost.this)
                    ,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                break;
        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                StringBuilder stringBuilder = new StringBuilder();
                //String latitude = String.valueOf(place.getLatLng().latitude);
                //String longitude = String.valueOf(place.getLatLng().longitude);

                lat = Double.valueOf(place.getLatLng().latitude);
                lon = Double.valueOf(place.getLatLng().longitude);

                getAddress(lat, lon);

                lokacijaText.setText(lokacija);
            }

        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            lokacija = add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void result(View view) {
        Spinner aktivnosti = (Spinner) findViewById(R.id.aktivnosti);
        String aktivnost = aktivnosti.getSelectedItem().toString();
        String povtorlivost = "Ne";
        String itnost = "Ne";
        povt = (CheckBox) findViewById(R.id.povtorlivo);
        itno = (CheckBox) findViewById(R.id.itno);

        if(povt.isChecked()){
            povtorlivost = "Da";
        }

        if(itno.isChecked()){
            itnost = "Da";
        }


        datumText=(EditText)findViewById(R.id.tDatum);
        String datum = datumText.getText().toString().trim();
        vremeText=(EditText)findViewById(R.id.tVreme);
        String vreme = vremeText.getText().toString().trim();
        lokacijaText=(EditText)findViewById(R.id.tLokacija);
        String lokacija = lokacijaText.getText().toString().trim();



        if(datum.isEmpty()){
            datumText.setError("Vnesi Datum");
            datumText.requestFocus();
            return;
        }

        if(vreme.isEmpty()){
            vremeText.setError("Vnesi Vreme");
            vremeText.requestFocus();
            return;
        }

        if(lokacija.isEmpty()){
            lokacijaText.setError("Vnesi Lokacija");
            lokacijaText.requestFocus();
            return;
        }




        String id = (String) userID.toString();


        String key = reference.push().getKey();
        Aktivnost aktiv = new Aktivnost(id, itnost, lokacija, datum, povtorlivost, aktivnost, vreme, key, lat, lon);


        FirebaseDatabase.getInstance().getReference("Aktivnosti")
                .child(key)
                .setValue(aktiv).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DodadiAktivnost.this, "Dodadovte Aktivnost", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DodadiAktivnost.this, Aktivnosti.class));
                } else {
                    Toast.makeText(DodadiAktivnost.this, "Nastana problem", Toast.LENGTH_SHORT).show();
                }
            }
        });



            }


}