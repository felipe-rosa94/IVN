package com.devtech.ivn.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.devtech.ivn.Adapter.AdapterPedidos;
import com.devtech.ivn.Model.Pedidos;
import com.devtech.ivn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PedidosAc extends AppCompatActivity {

    private ArrayList<Pedidos> pedidos;
    private static AdapterPedidos adapterPedidos;
    private RecyclerView rvPedidos;
    private ProgressBar progressBar;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mPedidos = mDatabase.child("Pedidos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        iniciar();
        getPedidos();
    }

    private void iniciar() {
        rvPedidos = findViewById(R.id.rv_pedidos);
        rvPedidos.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar_pedidos);
        Button btnPedidos = findViewById(R.id.btn_pedido_oracao);
        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getBaseContext(), CadastraPedido.class);
                startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    public static void deletaItem(String key){
        mPedidos.child(key).removeValue();
        adapterPedidos.notifyDataSetChanged();
    }

    public void getPedidos() {
        mPedidos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pedidos = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Pedidos p = dataSnapshot1.getValue(Pedidos.class);
                        pedidos.add(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                adapterPedidos = new AdapterPedidos(getBaseContext(), pedidos);
                rvPedidos.setAdapter(adapterPedidos);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}
