package com.example.androidworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText email,password;
    private Button najava;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.SwitchRegister);
        register.setOnClickListener(this);

        najava = (Button) findViewById(R.id.Login);
        najava.setOnClickListener(this);

        email = (EditText) findViewById(R.id.Email_login);
        password = (EditText) findViewById(R.id.Password_login);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SwitchRegister:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.Login:
                userLogin();
                break;
        }

    }

    private void userLogin() {
        String email_db = email.getText().toString().trim();
        String password_db = password.getText().toString().trim();

        if(email_db.isEmpty()){
            email.setError("Vnesi Ime");
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

        mAuth.signInWithEmailAndPassword(email_db,password_db).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user;
                    DatabaseReference reference;
                    String userID;

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    userID = user.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfil = snapshot.getValue(User.class);

                            if(userProfil != null){
                                String tip = userProfil.tip;
                                if(tip.equals("Povozrasno Lice")){
                                    startActivity(new Intent(MainActivity.this, NajavenProfil.class));
                                }
                                else if(tip.equals("Volonter")){
                                    startActivity(new Intent(MainActivity.this, NajavenVolonter.class));
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Failed to login: " + tip, Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    //startActivity(new Intent(MainActivity.this, NajavenProfil.class));

                    //startActivity(new Intent(MainActivity.this, NajavenVolonter.class));


                } else {
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}