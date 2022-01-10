package com.example.androidworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UrediAktivnost extends AppCompatActivity implements View.OnClickListener {


    private DatabaseReference reference, reference2;
    private FirebaseUser volonterUser;
    HashMap hashMap, delVolonter;


    Button odbiVolonter, prifatiVolonter, zavrsi, nazad;
    Aktivnost aktivnost;
    String volonterId;
    String id;
    LinearLayout volonterButtons, volonterInfo, nemaVolonter;
    TextView txIme, txDatum, txVreme, txLokacija, txStatus;
    TextView imePrezime, tel, rejting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_aktivnost);

        TextView test = findViewById(R.id.ime);


        txIme = findViewById(R.id.ime);
        txDatum = findViewById(R.id.datum);
        txVreme = findViewById(R.id.vreme);
        txLokacija = findViewById(R.id.lokacija);
        txStatus = findViewById(R.id.status);

        imePrezime = findViewById(R.id.volonterIme);
        tel = findViewById(R.id.volonterTel);
        rejting = findViewById(R.id.volonterRejting);

        volonterButtons = findViewById(R.id.volonterButtons);
        zavrsi = findViewById(R.id.zavrsi);
        nemaVolonter = findViewById(R.id.nemaVolonter);
        volonterInfo = findViewById(R.id.volonterInfo);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        test.setText(id);

        reference = FirebaseDatabase.getInstance().getReference("Aktivnosti");
        reference2 = FirebaseDatabase.getInstance().getReference("Users");



        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aktivnost = snapshot.getValue(Aktivnost.class);


                if (aktivnost != null) {
                    String ime = aktivnost.ime;
                    String datum = aktivnost.datum;
                    String vreme = aktivnost.vreme;
                    String lokacija = aktivnost.lokacija;
                    String status = aktivnost.status;
                    volonterId = aktivnost.volonterID;

                    if(status.equals("Aktivno")){
                        volonterInfo.setVisibility(View.GONE);
                        volonterButtons.setVisibility(View.GONE);
                        zavrsi.setVisibility(View.GONE);
                        nemaVolonter.setVisibility(View.VISIBLE);
                    } else if (status.equals("Prijaven volonter")){
                        volonterInfo.setVisibility(View.VISIBLE);
                        volonterButtons.setVisibility(View.VISIBLE);
                        zavrsi.setVisibility(View.GONE);
                        nemaVolonter.setVisibility(View.GONE);
                    } else if (status.equals("Zakazana aktivnost")){
                        volonterInfo.setVisibility(View.VISIBLE);
                        volonterButtons.setVisibility(View.GONE);
                        zavrsi.setVisibility(View.VISIBLE);
                        nemaVolonter.setVisibility(View.GONE);
                    } else if (status.equals("Zavrsena aktivnost")){
                        volonterInfo.setVisibility(View.VISIBLE);
                        volonterButtons.setVisibility(View.GONE);
                        zavrsi.setVisibility(View.GONE);
                        nemaVolonter.setVisibility(View.GONE);
                    }

                    txIme.setText(ime);
                    txDatum.setText(datum);
                    txVreme.setText(vreme);
                    txLokacija.setText(lokacija);
                    txStatus.setText(status);


                    //Toast.makeText(UrediAktivnost.this, " " + volonterId, Toast.LENGTH_SHORT).show();

                    if(volonterId != null){

                        reference2.child(volonterId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {

                                User volonter = snapshot2.getValue(User.class);

                                if(volonter != null){

                                    String imePrezimeS = volonter.ime + " " + volonter.prezime;
                                    String telS = volonter.broj;
                                    String rejtingS = volonter.rejting;

                                    //Toast.makeText(UrediAktivnost.this, imePrezimeS + " " + telS , Toast.LENGTH_SHORT).show();

                                    imePrezime.setText(imePrezimeS);
                                    tel.setText(telS);
                                    rejting.setText(rejtingS);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(UrediAktivnost.this, "Nastana Greska", Toast.LENGTH_SHORT).show();

                            }


                        });

                    }

                    }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        nazad = findViewById(R.id.nazad);
        nazad.setOnClickListener(this);


        odbiVolonter = findViewById(R.id.volonterOdbi);
        odbiVolonter.setOnClickListener(this);

        prifatiVolonter = findViewById(R.id.volonterPrifati);
        prifatiVolonter.setOnClickListener(this);

        zavrsi = findViewById(R.id.zavrsi);
        zavrsi.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nazad:
                startActivity(new Intent(this, Aktivnosti.class));
                break;
            case R.id.volonterOdbi:
                hashMap = new HashMap();
                delVolonter = new HashMap();

                delVolonter.put("volonterID", "0");

                hashMap.put("status", "Aktivno");

                reference.child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        reference.child(id).updateChildren(delVolonter).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(UrediAktivnost.this, "Odbien Volonter", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });

                startActivity(new Intent(this, Aktivnosti.class));
                break;
            case R.id.volonterPrifati:
                hashMap = new HashMap();
                hashMap.put("status", "Zakazana aktivnost");
                reference.child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UrediAktivnost.this, "Prifaten Volonter", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(this, Aktivnosti.class));
                break;
            case R.id.zavrsi:
                hashMap = new HashMap();
                hashMap.put("status", "Zavrsena aktivnost");

                reference.child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(UrediAktivnost.this, "Zavrsena aktivnost", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UrediAktivnost.this, Zavrsi_Aktivnost.class);
                        intent.putExtra("id", volonterId);
                        startActivity(intent);
                    }
                });
                break;
        }
    }
}