package com.example.banco.Menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class IngresarFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public User user;
    public Acount acount;
    private int maxid;

    //DATE IMPORT
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();

    private Button ingresar;
    private EditText ingreso;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ingresar_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ingresar = (Button) getActivity().findViewById(R.id.btningresar);
        ingreso = (EditText) getActivity().findViewById((R.id.editText));
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

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingreso.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"INGRESE MONTO",Toast.LENGTH_LONG).show();
                } else {
                    ingresar();
                    ingreso.setText("");
                }
            }
        });
    }

    public void User(User user, Acount acount) {
        this.user = user;
        this.acount = acount;
    }

    private void ingresar() {
        final Double monto = Double.parseDouble(ingreso.getText().toString());

        Action action = new Action(maxid + 1, "ING-" + user.getNombre() +
                " - " + dtf.format(now), monto, "INGRESO", user.getAcountnumber());

        //SE ACTUALIZA CUENTA
        acount.setAmount(acount.getAmount() + monto);

        //SE INGRESA ACCION A LA DB
        mRootRef.child("actions").child(String.valueOf(maxid + 1)).setValue(action);

        //SE ACTUALIZA CUENTA EN LA DB
        mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);

        Toast.makeText(getActivity(), "INGRESO EXITOSO", Toast.LENGTH_LONG).show();
    }
}
