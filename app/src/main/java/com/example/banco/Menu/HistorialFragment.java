package com.example.banco.Menu;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banco.Model.Acount;
import com.example.banco.Model.Action;
import com.example.banco.Model.Debt;
import com.example.banco.Model.User;
import com.example.banco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HistorialFragment extends Fragment {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private User user;
    private Acount acount;
    private Action action;
    private ListView historial;
    private List<Action> listAction = new ArrayList<Action>();
    private ArrayAdapter<Action> adapterAction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.historial_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
            historial = (ListView) getActivity().findViewById(R.id.lvhistorial);
        verHistorial();
        historial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                action = (Action) adapterView.getItemAtPosition(i);
                openDialog(action);
            }
        });
    }

    private void openDialog(Action action) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("MAS INFORMACION");
        alertDialog.setMessage("NUMERO Y FECHA: " + action.getNumber() + "\nPara: " +
                action.getReceiver() + "\nDe: " + action.getTransmitter() + "\nTipo: " +
                action.getType() + "\nTotal: " + action.getAmount());
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void User(User user, Acount acount) {
        this.user = user;
        this.acount = acount;
    }

    private void verHistorial() {
        final List<Action> listMyAction = new ArrayList<>();
        final List<Action> OtherMyAction = new ArrayList<>();
        mRootRef.child("actions").orderByChild("transmitter").equalTo(acount.getNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Action action = child.getValue(Action.class);
                    listMyAction.add(action);
                }
                listAction.addAll(listMyAction);
                Collections.sort(listAction, Collections.reverseOrder());
                adapterAction = new ArrayAdapter<Action>(getActivity(), android.R.layout
                        .simple_list_item_1, listAction);
                historial.setAdapter(adapterAction);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mRootRef.child("actions").orderByChild("receiver").equalTo(acount.getNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Action action = child.getValue(Action.class);
                    OtherMyAction.add(action);
                }
                listAction.addAll(OtherMyAction);
                Collections.sort(listAction, Collections.reverseOrder());
                adapterAction = new ArrayAdapter<Action>(getActivity(), android.R.layout
                        .simple_list_item_1, listAction);
                historial.setAdapter(adapterAction);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
