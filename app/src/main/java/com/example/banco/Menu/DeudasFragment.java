package com.example.banco.Menu;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.banco.MainActivity;
import com.example.banco.Model.Acount;
import com.example.banco.Model.Action;
import com.example.banco.Model.Debt;
import com.example.banco.Model.User;
import com.example.banco.R;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeudasFragment extends Fragment {

    //DATE IMPORT
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private User user;
    private List<Debt> listDebt = new ArrayList<Debt>();
    private ArrayAdapter<Debt> adapterPersona;
    private Acount acount;
    private Debt debt;
    private Action action;
    private Acount acount2;
    private int maxid;

    private Button pagar;
    private ListView ldeudas;
    private EditText monto;
    private TextView deuda;
    private TextView pago;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deuda_fragment, container, false);
    }

    public void User(User user, Acount acount){
        this.user = user;
        this.acount = acount;
    }

    @Override
    public void onStart() {
        super.onStart();

        //INICIAR COMPONENTES
        ldeudas = (ListView) getActivity().findViewById(R.id.lvDeudas);
        pagar = (Button) getActivity().findViewById(R.id.btnPagar);
        monto = (EditText) getActivity().findViewById(R.id.edtMonto);
        deuda = (TextView) getActivity().findViewById(R.id.tvdeuda);
        pago = (TextView) getActivity().findViewById(R.id.tvPago);

        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagar();
                limpiar();
            }
        });
        ldeudas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                limpiar();
                debt = (Debt) adapterView.getItemAtPosition(i);
                pago.setText(String.valueOf(debt.getAmount()));
                deuda.setText(debt.getName());
                monto.setText(String.valueOf(debt.getAmount()));
            }
        });
        listar();
    }

    private void limpiar() {
        monto.setText("");
        deuda.setText("");
        pago.setText("");
    }

    private void listar() {
        String value = "1_" + acount.getNumber();
        mRootRef.child("debts").orderByChild("value").equalTo(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDebt.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Debt debt = child.getValue(Debt.class);
                    listDebt.add(debt);
                    adapterPersona = new ArrayAdapter<Debt>(getActivity(),
                            android.R.layout.simple_list_item_1, listDebt);
                    ldeudas.setAdapter(adapterPersona);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void pagar() {
        final Double mont = Double.parseDouble(monto.getText().toString());

        mRootRef.child("users").orderByChild("id").equalTo(user.getId());

        maxid = 0;

        mRootRef.child("actions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid = (int) dataSnapshot.getChildrenCount();
                    action = new Action(maxid + 1, "PAG-" + user.getNombre() + " " +
                            debt.getName() + dtf.format(now), mont, "PAGO", user.getAcountnumber(), debt.getTransmitter());

                    //SE ACTUALIZA CUENTA DEL EMISOR
                    acount.setAmount(acount.getAmount() - action.getAmount());

                    mRootRef.child("acounts").orderByChild("number").equalTo(debt.getTransmitter()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                acount2 = child.getValue(Acount.class);
                            }
                            //SE ACTUALIZA CUENTA DEL RECEPTOR
                            acount2.setAmount(acount2.getAmount() + mont);

                            //SE PREPARA DEUDA
                            debt.setAmount(debt.getAmount() - mont);
                            if (debt.getAmount() == 0) {
                                debt.setEstate("0");
                                debt.setValue("0_" + debt.getReceiver());
                            }

                            //SE INGRESA ACCION A LA DB
                            mRootRef.child("actions").child(String.valueOf(maxid + 1)).setValue(action);

                            //SE ACTUALIZA CUENTA 1 EN LA DB
                            mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);

                            //SE ACTUALIZA CUENTA 2 EN LA DB
                            mRootRef.child("acounts").child(acount2.getNumber()).setValue(acount2);

                            //SE ACTUALIZAN LAS DEUDAS
                            mRootRef.child("debts").child(debt.getId()).setValue(debt);

                            Toast.makeText(getActivity(), "PAGO EXITOSO", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
