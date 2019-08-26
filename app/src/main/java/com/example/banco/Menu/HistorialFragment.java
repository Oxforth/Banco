package com.example.banco.Menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banco.Model.User;
import com.example.banco.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistorialFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.historial_fragment, container, false);
    }

    public void User(User user){
        this.user = user;
    }
}
