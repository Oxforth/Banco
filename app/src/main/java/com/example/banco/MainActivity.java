package com.example.banco;

import android.content.Intent;
import android.os.Bundle;

import com.example.banco.Menu.CuentaFragment;
import com.example.banco.Menu.DeudasFragment;
import com.example.banco.Menu.HistorialFragment;
import com.example.banco.Menu.IngresarFragment;
import com.example.banco.Menu.MainFragment;
import com.example.banco.Menu.RetirarFragment;
import com.example.banco.Menu.TransferirFragment;
import com.example.banco.Model.Acount;
import com.example.banco.Model.User;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private User user;
    private Acount acount = new Acount();

    private TextView user1;

    private MainFragment mainFragment = new MainFragment();
    private RetirarFragment retirarFragment = new RetirarFragment();
    private IngresarFragment ingresarFragment = new IngresarFragment();
    private TransferirFragment transferirFragment = new TransferirFragment();
    private CuentaFragment cuentaFragment = new CuentaFragment();
    private HistorialFragment historialFragment = new HistorialFragment();
    private DeudasFragment deudasFragment = new DeudasFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user1 = (TextView) findViewById(R.id.user_name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        inicializar();


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void inicializar() {
        Intent intent = getIntent();

        //SE INICIALIZA EL USUARIO
        mRootRef.child("users").orderByChild("acountnumber").equalTo(intent.getStringExtra("USER")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    user = child.getValue(User.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //SE INICIALIZA LA CUENTA
        mRootRef.child("acounts").orderByChild("number").equalTo(intent.getStringExtra("USER")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    acount = child.getValue(Acount.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        setUser();

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
        } else if (id == R.id.nav_retirar) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, retirarFragment).commit();
        } else if (id == R.id.nav_ingresar) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ingresarFragment).commit();
        } else if (id == R.id.nav_tranferir) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, transferirFragment).commit();
        } else if (id == R.id.nav_deudas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, deudasFragment).commit();
        } else if (id == R.id.nav_cuenta) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, cuentaFragment).commit();
        } else if (id == R.id.nav_historial) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, historialFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUser(){
        mainFragment.User(user,acount);
        retirarFragment.User(user,acount);
        ingresarFragment.User(user,acount);
        transferirFragment.User(user,acount);
        deudasFragment.User(user,acount);
        cuentaFragment.User(acount.getNumber());
        historialFragment.User(user,acount);
    }
}
