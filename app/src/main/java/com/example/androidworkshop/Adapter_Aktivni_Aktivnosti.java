package com.example.androidworkshop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

    Integer odalecenost;
    Double lat22,lon22;
    String lat1,lon1;

    LocationManager locationManager;

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

        lat22 = aktivnost.getLat();
        lon22 = aktivnost.getLon();

        //String lat2 = lat22.toString();
        //String lon2 = lon22.toString();


        //Double lt1 = Double.parseDouble(lat1);
        //Double ln1 = Double.parseDouble(lon1);




        if(lat22 != null){

            Location startPoint=new Location("locationA");
            startPoint.setLatitude(Double.parseDouble(lat1));
            startPoint.setLongitude(Double.parseDouble(lon1));

            Location endPoint=new Location("locationA");
            endPoint.setLatitude(lat22);
            endPoint.setLongitude(lon22);

            double distance=startPoint.distanceTo(endPoint);
            double distance2 = distance/1000.0;
            int dis = (int) Math.round(distance2);

            holder.odalecenost.setText(dis+" km");

            Log.d("Dist: ", dis+"km");
            //Log.d("Lon:", lon1 + " " + lon22);
            //Log.d("Lat:", lat1 + " " + lat22);
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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tip,datum,vreme,lokacija,itno,povtorlivo,rejting,odalecenost,imeprezime;
        Button volonterCTA;
        Aktivnost aktivnost;
        String id, liceId;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);



            //test = itemView.findViewById(R.id.ime);
            imeprezime = itemView.findViewById(R.id.ime);
            tip = itemView.findViewById(R.id.tip);
            odalecenost = itemView.findViewById(R.id.odalecenost);
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

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED&&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                },1);
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    //Double lat22 = aktivnost.getLat();
                    //Double lon22 = aktivnost.getLon();

                    //String lat2 = lat22.toString();
                    //String lon2 = lon22.toString();

                    lat1 = String.valueOf(location.getLatitude());
                    lon1 = String.valueOf(location.getLongitude());

                    //Double lt1 = Double.parseDouble(lat1);
                    //Double ln1 = Double.parseDouble(lon1);






                    //Log.d("Lon:", lon1 + " " + lon22);
                    //Log.d("Lat:", lat1 + " " + lat22);

                    //Location startPoint=new Location("locationA");
                    //startPoint.setLatitude(lat1);
                    //startPoint.setLongitude(lon1);

                    //Location endPoint=new Location("locationA");
                    //endPoint.setLatitude(lat2);
                    //endPoint.setLongitude(lon2);

                    //double distance=startPoint.distanceTo(endPoint);
                    //double distance2 = distance/1000.0;
                    //int dis = (int) Math.round(distance2);



                    //Toast.makeText(co, dis +" km", Toast.LENGTH_SHORT).show();
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
