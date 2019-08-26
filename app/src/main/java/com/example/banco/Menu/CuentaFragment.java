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
    public Acount acount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cuenta_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        verCuenta();
    }

    public void User(User user,Acount acount){
        this.user = user;
        this.acount = acount;
    }

    private void verCuenta() {
        mRootRef.child("acounts").orderByChild("number").equalTo(user.getAcountnumber()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            acount = child.getValue(Acount.class);
                        }
                        Toast.makeText(getActivity(),"" + acount.toString(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
