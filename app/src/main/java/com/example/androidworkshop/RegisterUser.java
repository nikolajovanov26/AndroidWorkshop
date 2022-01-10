package com.example.androidworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView najava;
    private Button registracija;
    private EditText ime, prezime, broj, email, password;
    private String tip = "Povozrasno Lice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registracija = (Button) findViewById(R.id.Registracija);
        registracija.setOnClickListener(this);

        ime = (EditText) findViewById(R.id.Name_register);
        prezime = (EditText) findViewById(R.id.Prezime_register);
        broj = (EditText) findViewById(R.id.Broj_register);
        email = (EditText) findViewById(R.id.Email_register);
        password = (EditText) findViewById(R.id.Password_login);






        najava = (TextView) findViewById(R.id.SwitchLogin);
        najava.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SwitchLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.Registracija:
                registerUser();
                break;
        }

    }

    private void najava(){

        if(tip.equals("Povozrasno Lice")){
            startActivity(new Intent(RegisterUser.this, NajavenProfil.class));
        } else if(tip.equals("Volonter")){
            startActivity(new Intent(RegisterUser.this, NajavenVolonter.class));
        } else {
            Toast.makeText(RegisterUser.this, "Nastana problem", Toast.LENGTH_SHORT).show();
        }


    }

    public void radioClick(View view){
        if(view.getId()==R.id.povozrasno){
            tip = "Povozrasno Lice";
        } else {
            tip = "Volonter";
        }
    }


    private void registerUser() {
        String ime_db = ime.getText().toString().trim();
        String prezime_db = prezime.getText().toString().trim();
        String broj_db = broj.getText().toString().trim();
        String email_db = email.getText().toString().trim();
        String password_db = password.getText().toString().trim();


        String tip_db = tip;

        if(ime_db.isEmpty()){
            ime.setError("Vnesi Ime");
            ime.requestFocus();
            return;
        }

        if(prezime_db.isEmpty()){
            prezime.setError("Vnesi Prezime");
            prezime.requestFocus();
            return;
        }

        if(broj_db.isEmpty()){
            broj.setError("Vnesi Broj");
            broj.requestFocus();
            return;
        }

        if(email_db.isEmpty()){
            email.setError("Vnesi Email");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email_db).matches()){
            email.setError("Vnesi Validna Email Adresa");
            email.requestFocus();
            return;
        }

        if(password_db.isEmpty()){
            password.setError("Vnesi Lozinka");
            password.requestFocus();
            return;
        }

        if(password_db.length() < 6){
            password.setError("Vnesi Lozinka podolga od 6 znaci");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email_db, password_db)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(tip_db, ime_db, prezime_db, broj_db, email_db, password_db);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterUser.this, "Se registriravte", Toast.LENGTH_SHORT).show();
                                    najava();

                                } else {
                                    Toast.makeText(RegisterUser.this, "Nastana problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterUser.this, "Nastana problem", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}