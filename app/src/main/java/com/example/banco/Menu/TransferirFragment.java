package com.example.banco.Menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.banco.Model.Acount;
import com.example.banco.Model.Action;
import com.example.banco.Model.User;
import com.example.banco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TransferirFragment extends Fragment {

    //FIREBASE
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //MODELS
    public User user;
    Acount acount;
    Acount acount2;
    public Action action;

    //COMPONENTS
    private Button transferir;
    private TextView destino;
    private TextView monto;
    private int maxid;

    //DATE IMPORT
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transferir_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        transferir = (Button) getView().findViewById(R.id.btnTransferir);
        destino = (TextView) getView().findViewById(R.id.tvDestino);
        monto = (TextView) getView().findViewById(R.id.tvMonto);
        maxid = 0;

        mRootRef.child("actions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid = (int) dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(destino.getText().toString().isEmpty()||monto.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"FALTA MONTO O DESIINATARIO",Toast.LENGTH_LONG).show();
                } else {
                    transferir();
                }
            }
        });
    }

    public void User(User user, Acount acounts) {
        this.user = user;
        this.acount = acounts;
    }

    private void transferir() {

        //SE PREPARA VARIABLES
        final String destine = destino.getText().toString();
        final Double mont = Double.parseDouble(monto.getText().toString());

        if (destine == acount.getNumber()) {
            Toast.makeText(getActivity(), "NO PUEDE HACERSE UNA TRANSFERENCIA A SI MISMO", Toast.LENGTH_LONG).show();
        } else {

            mRootRef.child("acounts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(destine).exists()) {
                        acount2 = dataSnapshot.child(destine).getValue(Acount.class);


                        action = new Action(maxid + 1, "TRA-" + user.getNombre() + " - " +
                                destine + " - " + dtf.format(now), mont, "TRANSFERENCIA",
                                user.getAcountnumber(), destine);
                        //SE ACTUALIZA CUENTA DEL EMISOR
                        acount.setAmount(acount.getAmount() - mont);

                        //SE ACTUALIZA CUENTA DEL RECEPTOR
                        acount2.setAmount(acount2.getAmount() + mont);

                        //SE INGRESA ACCION A LA DB
                        mRootRef.child("actions").child(String.valueOf(maxid + 1)).setValue(action);

                        //SE ACTUALIZA CUENTA 1 EN LA DB
                        mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);

                        //SE ACTUALIZA CUENTA 2 EN LA DB
                        mRootRef.child("acounts").child(acount2.getNumber()).setValue(acount2);

                        Toast.makeText(getActivity(), "TRANSFERENCIA EXITOSA DE: " + String.valueOf(mont), Toast.LENGTH_LONG).show();

                        limpiar();
                    } else {
                        Toast.makeText(getActivity(), "EL DESTINATARIO NO EXISTE", Toast.LENGTH_LONG).show();
                        limpiar();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void limpiar() {
        monto.setText("");
        destino.setText("");
    }
}
