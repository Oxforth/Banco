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

public class RetirarFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public User user;
    Acount acount = new Acount();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.retirar_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        retirar();
    }

    public void User(User user){
        this.user = user;
    }

    private void retirar() {

        //SE BUSCA LA CUENTA A ACTUALIZAR
        mRootRef.child("acounts").orderByChild("number").equalTo(user.getAcountnumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    acount = child.getValue(Acount.class);
                }

                Action action = new Action(UUID.randomUUID().toString(),"RET-"+user.getNombre(),
                        50,"retiro",user.getAcountnumber());

                //SE ACTUALIZA CUENTA
                acount.setAmount(acount.getAmount()-action.getAmount());

                //SE INGRESA ACCION A LA DB
                mRootRef.child("actions").child(action.getId()).setValue(action);

                //SE ACTUALIZA CUENTA EN LA DB
                mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);

                Toast.makeText(getActivity(),"RETIRO EXITOSO",Toast.LENGTH_LONG);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
