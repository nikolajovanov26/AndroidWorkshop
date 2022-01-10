package com.example.androidworkshop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class Adapter_Moi_Aktivnosti extends RecyclerView.Adapter<Adapter_Moi_Aktivnosti.MyViewHolder> {

    static Context context;
    User lice;

    DatabaseReference reference;

    ArrayList<Aktivnost> list;





    public Adapter_Moi_Aktivnosti(Context context, ArrayList<Aktivnost> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.moi_aktivnosti_volonter,parent,false);
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

        holder.aktivnost = aktivnost;
        holder.id = aktivnost.getKluc();
        holder.povtorlivo.setText(aktivnost.getPovtorlivost());
        holder.liceId = aktivnost.getVozrasnoliceID();
        holder.izvestaj.setVisibility(View.GONE);
        holder.telbroj.setVisibility(View.GONE);

        if(aktivnost.getStatus().equals("Prijaven volonter")){
            holder.status.setText("Se ceka odgovor");
        } else if(aktivnost.getStatus().equals("Zakazana aktivnost")){
            holder.status.setText("Zakazana aktivnost");
            holder.telbroj.setVisibility(View.VISIBLE);
        } else{
            holder.status.setText("Zavrsena");
            holder.izvestaj.setVisibility(View.VISIBLE);
            holder.izvestaj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap hashMap = new HashMap();
                    HashMap hashMap2 = new HashMap();
                    String liceId = aktivnost.getVozrasnoliceID();
                    hashMap.put("volonterID", "0");
                    hashMap2.put("vozrasnoliceID", "0");
                    reference = FirebaseDatabase.getInstance().getReference("Aktivnosti");
                    reference.child(aktivnost.getKluc()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Log.d("test", "ok e 1");
                        }
                    });
                    reference.child(aktivnost.getKluc()).updateChildren(hashMap2).addOnSuccessListener(new OnSuccessListener() {

                        @Override
                        public void onSuccess(Object o) {
                            Log.d("test", "ok e 2");
                        }
                    });
                    Intent intent = new Intent(context, Zavrsi_Aktivnost.class);
                    intent.putExtra("id", liceId);
                    context.startActivity(intent);
                }
            });
        }

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(aktivnost.getVozrasnoliceID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lice = snapshot.getValue(User.class);
                if (lice != null) {
                    String ime = lice.ime;
                    String prezime = lice.prezime;
                    String ocena = lice.rejting;
                    String broj = lice.broj;
                    holder.imeprezime.setText(ime + " " + prezime);
                    holder.tel.setText(broj);

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



        TextView tip,datum,vreme,lokacija,itno,povtorlivo,imeprezime,status,tel;
        LinearLayout telbroj;
        Button izvestaj;
        Aktivnost aktivnost;
        String id, liceId;


        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            telbroj = itemView.findViewById(R.id.telbroj);
            tel = itemView.findViewById(R.id.tel);
            imeprezime = itemView.findViewById(R.id.ime);
            izvestaj = itemView.findViewById(R.id.izvestaj);
            status = itemView.findViewById(R.id.status);
            tip = itemView.findViewById(R.id.tip);
            datum = itemView.findViewById(R.id.datum);
            vreme = itemView.findViewById(R.id.vreme);
            lokacija = itemView.findViewById(R.id.lokacija);
            itno = itemView.findViewById(R.id.itno);
            povtorlivo = itemView.findViewById(R.id.povtorlivo);




        }

    }




}
