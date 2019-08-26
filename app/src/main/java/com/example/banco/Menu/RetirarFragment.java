package com.example.banco.Menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class RetirarFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public User user;
    public Acount acount;

    //DATE IMPORT
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();
    private int maxid;

    private Button retirar;
    private EditText retiro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.retirar_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        retirar = (Button) getActivity().findViewById(R.id.btnretirar);
        retiro = (EditText) getActivity().findViewById((R.id.editText1));
        maxid = 0;
        Toast.makeText(getActivity(), "RETIRARA", Toast.LENGTH_LONG).show();
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
        retirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retirar();
                retiro.setText("");
            }
        });
    }

    public void User(User user, Acount acount) {
        this.user = user;
        this.acount = acount;
    }

    private void retirar() {

        final Double monto = Double.parseDouble(retiro.getText().toString());
        Action action = new Action(maxid + 1, "RET-" + user.getNombre() +
                " - " + dtf.format(now), monto, "RETIRO", user.getAcountnumber());

        //SE ACTUALIZA CUENTA
        acount.setAmount(acount.getAmount() - monto);

        //SE INGRESA ACCION A LA DB
        mRootRef.child("actions").child(String.valueOf(maxid + 1)).setValue(action);

        //SE ACTUALIZA CUENTA EN LA DB
        mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);

        Toast.makeText(getActivity(), "RETIRO EXITOSO", Toast.LENGTH_LONG).show();
    }
}
