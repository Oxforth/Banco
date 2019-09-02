package com.example.banco.Menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banco.Model.Acount;
import com.example.banco.Model.User;
import com.example.banco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CuentaFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private User user;
    private Acount acount;
    private String numero;

    private TextView cuenta, nombres, dni, monto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cuenta_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        cuenta = (TextView) getActivity().findViewById(R.id.tvCuenta);
        nombres = (TextView) getActivity().findViewById(R.id.tvNomres);
        dni = (TextView) getActivity().findViewById(R.id.tvDni);
        monto = (TextView) getActivity().findViewById(R.id.tvMonto);
        verCuenta();
    }

    public void User(String acount) {
        this.numero = acount;
    }

    private void verCuenta() {
        mRootRef.child("users").orderByChild("acountnumber").equalTo(numero).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    user = child.getValue(User.class);
                }
                nombres.setText("Nombres: " + user.getNombre() + " " + user.getAppaterno() + " " + user.getApmaterno());
                dni.setText("Dni: " + user.getDni());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRootRef.child("acounts").orderByChild("number").equalTo(numero).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    acount = child.getValue(Acount.class);
                }
                cuenta.setText("Cuenta: " + acount.getNumber());
                monto.setText("Monto: " + String.valueOf(acount.getAmount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
