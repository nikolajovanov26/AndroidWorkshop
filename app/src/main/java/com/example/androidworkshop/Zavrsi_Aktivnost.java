package com.example.androidworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Zavrsi_Aktivnost extends AppCompatActivity {

    EditText komentarT,ocenaT;
    Button zacuvaj;
    Izvestaj izvestaj;
    DatabaseReference reference;

    private FirebaseUser user;
    private String userID;

    String doId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zavrsi_aktivnost);

        Intent intent = getIntent();
        doId = intent.getStringExtra("id");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        komentarT = findViewById(R.id.komentar);
        ocenaT = findViewById(R.id.ocena);
        zacuvaj = findViewById(R.id.zacuvaj);

        reference = FirebaseDatabase.getInstance().getReference("Izvestai");

        zacuvaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String komentar = komentarT.getText().toString().trim();
                String ocena = ocenaT.getText().toString().trim();
                String od = userID;
                String za = doId;

                if(komentar.isEmpty()){
                    komentarT.setError("Vnesi Komentar");
                    komentarT.requestFocus();
                    return;
                }

                if(ocena.isEmpty()){
                    ocenaT.setError("Vnesi Ocena");
                    ocenaT.requestFocus();
                    return;
                }

                String key = reference.push().getKey();
                izvestaj = new Izvestaj(komentar,ocena,od,za);

                FirebaseDatabase.getInstance().getReference("Izvestai")
                        .child(key)
                        .setValue(izvestaj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Zavrsi_Aktivnost.this, "Vasiot Izvestaj e podnesen", Toast.LENGTH_SHORT).show();

                        reference = FirebaseDatabase.getInstance().getReference("Users");
                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User userProfil = snapshot.getValue(User.class);

                                if(userProfil != null){
                                    String tip = userProfil.tip;
                                    if(tip.equals("Povozrasno Lice")){
                                        startActivity(new Intent(Zavrsi_Aktivnost.this, NajavenProfil.class));
                                    }
                                    else if(tip.equals("Volonter")){

                                        startActivity(new Intent(Zavrsi_Aktivnost.this, NajavenVolonter.class));
                                    }
                                    else {
                                        Toast.makeText(Zavrsi_Aktivnost.this, "Failed to login: " + tip, Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });



            }
        });


    }

}

