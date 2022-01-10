package com.example.androidworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NajavenProfil extends AppCompatActivity {

    private Button odjava, dodAktivnost, vidiAktivnosti;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_najaven_profil);

        odjava = (Button) findViewById(R.id.odjava);


        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(NajavenProfil.this, MainActivity.class));
            }
        });

        dodAktivnost = (Button) findViewById(R.id.aktivnost);

        dodAktivnost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NajavenProfil.this, DodadiAktivnost.class));
            }
        });




        vidiAktivnosti = (Button) findViewById(R.id.VidiAktivnost);

        vidiAktivnosti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NajavenProfil.this, Aktivnosti.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView n_ime = (TextView) findViewById(R.id.ime_user);
        final TextView n_prezime = (TextView) findViewById(R.id.prezime_user);
        final TextView n_broj = (TextView) findViewById(R.id.broj_user);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfil = snapshot.getValue(User.class);

                if(userProfil != null){
                    String ime = userProfil.ime;
                    String prezime = userProfil.prezime;
                    String broj = userProfil.broj;

                    n_ime.setText(ime);
                    n_prezime.setText(prezime);
                    n_broj.setText(broj);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NajavenProfil.this, "Nastana Greska", Toast.LENGTH_SHORT).show();
            }
        });
    }
}