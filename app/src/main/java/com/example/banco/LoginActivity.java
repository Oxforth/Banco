package com.example.banco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.banco.Model.Acount;
import com.example.banco.Model.Debt;
import com.example.banco.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button ingresar;
    private EditText number;
    private EditText pass;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ingresar = (Button) findViewById(R.id.btnLog);
        number = (EditText) findViewById(R.id.edtNumber);
        pass = (EditText) findViewById(R.id.edtpass);
        ingresar.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLog:
                login();
                //insert();
                break;
        }
    }

    /*private void insert() {
        User user = new User("95568c00-a03f-4309-878c-0d5aa3f7ed7a","Josue",
                "Diaz","Curo","12345678","420040004321");
        Acount acount = new Acount("808080008321","4321",10000);
        mRootRef.child("acounts").child(acount.getNumber()).setValue(acount);
        User google = new User("95568c45-7777-4309-878c-0d5aa3f7ed7a","Google",
                "Perez","Real","87654321",acount.getNumber());
        mRootRef.child("users").child(google.getId()).setValue(google);
        mRootRef.child("users").child(user.getId()).setValue(user);
        Debt luz = new Debt(UUID.randomUUID().toString(),"PAGO DE LUZ",300,
                "808080008321","420040004321","1");
        Debt agua = new Debt(UUID.randomUUID().toString(),"PAGO DE AGUA",300,
                "808080008321","420040004321","1");
        Debt internet = new Debt(UUID.randomUUID().toString(),"PAGO DE INTERNET",300,
                "808080008321","420040004321","1");
        mRootRef.child("debts").child(luz.getId()).setValue(luz);
        mRootRef.child("debts").child(agua.getId()).setValue(agua);
        mRootRef.child("debts").child(internet.getId()).setValue(internet);
    }*/

    private void login() {

        final String numero = number.getText().toString();
        final String passw = pass.getText().toString();
        mRootRef.child("acounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(numero).exists()){
                    if (!numero.isEmpty()){
                        Acount acount = dataSnapshot.child(numero).getValue(Acount.class);
                        if (acount.getPass().equals(passw)){
                            Toast.makeText(getApplicationContext(),"SESION INICIADA!",Toast.LENGTH_LONG).show();
                            enterMain(acount);
                        }else{
                            Toast.makeText(getApplicationContext(),"CLAVE INCORRECTA",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"LA CUENTA NO EXISTE",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"NO SE QUE PASA :(",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void enterMain(Acount acount){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("USER",acount.getNumber());
        startActivity(intent);
    }
}
