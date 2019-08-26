package com.example.banco.Menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.UUID;

public class TransferirFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public User user;
    Acount acount, acount2;
    public Action action;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transferir_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        transferir();
    }

    public void User(User user){
        this.user = user;
    }

    private void transferir() {

        //SE BUSCA LA CUENTA A ACTUALIZAR
        mRootRef.child("acounts").orderByChild("number").equalTo(user.getAcountnumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    acount = child.getValue(Acount.class);
                }

                action = new Action(UUID.randomUUID().toString(),"TRA-"+user.getNombre(),
                        100,"TRANSFERENCIA",user.getAcountnumber(),"808080008321");
                //SE ACTUALIZA CUENTA DEL EMISOR
                acount.setAmount(acount.getAmount() - action.getAmount());

                mRootRef.child("acounts").orderByChild("number").equalTo("808080008321").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            acount2 = child.getValue(Acount.class);
                        }

                        //SE ACTUALIZA CUENTA DEL RECEPTOR
                        acount2.setAmount(acount2.getAmount()+action.getAmount());

                        //SE INGRESA ACCION A LA DB
                        mRootRef.child("actions").child(action.getId()).setValue(action);

                        //SE ACTUALIZA CUENTA 1 EN LA DB
                        mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);

                        //SE ACTUALIZA CUENTA 2 EN LA DB
                        mRootRef.child("acounts").child(acount2.getNumber()).setValue(acount2);

                        Toast.makeText(getActivity(),"INGRESO EXITOSO",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
