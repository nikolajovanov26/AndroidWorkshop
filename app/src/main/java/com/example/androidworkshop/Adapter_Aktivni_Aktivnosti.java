package com.example.androidworkshop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_Aktivni_Aktivnosti extends RecyclerView.Adapter<Adapter_Aktivni_Aktivnosti.MyViewHolder> {

    static Context context;
    User lice;

    DatabaseReference reference;

    ArrayList<Aktivnost> list;





    public Adapter_Aktivni_Aktivnosti(Context context, ArrayList<Aktivnost> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.aktivnosti_volonter,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Aktivnost aktivnost = list.get(position);

        holder.tip.setText(aktivnost.getIme());
        holder.datum.setText(aktivnost.getDatum());
        holder.vreme.setText(aktivnost.getVreme());
        holder.lokacija.setText(aktivnost.getLokacija());
        holder.itno.setText(aktivnost.getItnost());
        //holder.rejting.setText(aktivnost.getRejting());
        holder.aktivnost = aktivnost;
        holder.id = aktivnost.getKluc();
        holder.povtorlivo.setText(aktivnost.getPovtorlivost());
        //holder.test.setText(aktivnost.getVozrasnoliceID());
        holder.liceId = aktivnost.getVozrasnoliceID();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(aktivnost.getVozrasnoliceID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lice = snapshot.getValue(User.class);
                if (lice != null) {
                    String ime = lice.ime;
                    String prezime = lice.prezime;
                    String ocena = lice.rejting;
                    holder.imeprezime.setText(ime + " " + prezime);
                    holder.rejting.setText(ocena);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tip,datum,vreme,lokacija,itno,povtorlivo,rejting,test,imeprezime;
        Button volonterCTA;
        Aktivnost aktivnost;
        String id, liceId;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            //test = itemView.findViewById(R.id.ime);
            imeprezime = itemView.findViewById(R.id.ime);
            tip = itemView.findViewById(R.id.tip);
            rejting = itemView.findViewById(R.id.rejting);
            datum = itemView.findViewById(R.id.datum);
            vreme = itemView.findViewById(R.id.vreme);
            lokacija = itemView.findViewById(R.id.lokacija);
            itno = itemView.findViewById(R.id.itno);
            povtorlivo = itemView.findViewById(R.id.povtorlivo);
            volonterCTA = itemView.findViewById(R.id.volonterCTA);
            volonterCTA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prijaviAktivnost(id);
                }
            });

        }

    }

    public static void prijaviAktivnost(String id){
        Log.d("demo", "onClick: fired " + id);

        DatabaseReference reference2;
        HashMap status, volonterId;
        status = new HashMap();
        volonterId = new HashMap();
        FirebaseUser volonter;
        String volonterID;
        Aktivnost aktivnost;



        volonter = FirebaseAuth.getInstance().getCurrentUser();
        reference2 = FirebaseDatabase.getInstance().getReference("Users");
        volonterID = volonter.getUid();

        status.put("status", "Prijaven volonter");

        volonterId.put("volonterID", volonterID);

        reference2 = FirebaseDatabase.getInstance().getReference("Aktivnosti");
        DatabaseReference finalReference = reference2;
        DatabaseReference finalReference1 = reference2;
        reference2.child(id).updateChildren(status).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                finalReference1.child(id).updateChildren(volonterId).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        Toast.makeText(context, "Se Prijavivte", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Moi_Aktivnosti.class);
                        context.startActivity(intent);

                    }
                });


            }
        });


        //Toast.makeText(context, "Zapisana aktivnost", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(context, MoiAktivnosti.class);
        //intent.putExtra("id", id);
        //context.startActivity(intent);
    }


}
