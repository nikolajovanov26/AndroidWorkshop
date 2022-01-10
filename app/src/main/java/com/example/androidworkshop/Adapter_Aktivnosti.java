package com.example.androidworkshop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Aktivnosti extends RecyclerView.Adapter<Adapter_Aktivnosti.MyViewHolder> {

    static Context context;

    ArrayList<Aktivnost> list;

    public Adapter_Aktivnosti(Context context, ArrayList<Aktivnost> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.aktivnosti_list,parent,false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Aktivnost aktivnost = list.get(position);

        holder.ime.setText(aktivnost.getIme());
        holder.datum.setText(aktivnost.getDatum());
        holder.vreme.setText(aktivnost.getVreme());
        holder.lokacija.setText(aktivnost.getLokacija());
        holder.status.setText(aktivnost.getStatus());
        holder.itno.setText(aktivnost.getItnost());
        holder.aktivnost = aktivnost;
        holder.id = aktivnost.getKluc();
        holder.povtorlivo.setText(aktivnost.getPovtorlivost());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ime,datum,vreme,lokacija,status,itno,povtorlivo;
        Button volonterCTA;
        Aktivnost aktivnost;
        String id;
        //String id = aktivnost.getKluc();

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            ime = itemView.findViewById(R.id.ime);
            datum = itemView.findViewById(R.id.datum);
            vreme = itemView.findViewById(R.id.vreme);
            lokacija = itemView.findViewById(R.id.lokacija);
            status = itemView.findViewById(R.id.status);
            itno = itemView.findViewById(R.id.itno);
            povtorlivo = itemView.findViewById(R.id.povtorlivo);
            volonterCTA = itemView.findViewById(R.id.volonterCTA);
            volonterCTA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vidiAktivnost(id);
                }
            });

        }

    }

    public static void vidiAktivnost(String id){
        Log.d("demo", "onClick: fired " + id);
        Intent intent = new Intent(context, UrediAktivnost.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


}
