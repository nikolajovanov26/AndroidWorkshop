package com.example.androidworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NajavenVolonter extends AppCompatActivity implements View.OnClickListener {


    Button aktivni, moi, odjava;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_najaven_volonter);


        aktivni = findViewById(R.id.aktivni);
        moi = findViewById(R.id.moi);
        odjava = findViewById(R.id.odjava);

        aktivni.setOnClickListener(this);
        moi.setOnClickListener(this);
        odjava.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aktivni:
                startActivity(new Intent(NajavenVolonter.this, Aktivni_Aktivnosti.class));
                break;
            case R.id.moi:
                startActivity(new Intent(NajavenVolonter.this, Moi_Aktivnosti.class));
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(NajavenVolonter.this, MainActivity.class));
                break;
        }
    }
}