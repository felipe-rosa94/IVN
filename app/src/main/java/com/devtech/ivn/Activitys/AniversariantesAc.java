package com.devtech.ivn.Activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devtech.ivn.Adapter.AdapterNiver;
import com.devtech.ivn.Model.Membros;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AniversariantesAc extends AppCompatActivity {

    private static RecyclerView rvNiver;
    private static ArrayList<Membros> membros;
    public static ArrayList<Membros> todosMembros;
    private TextView tvTitle;
    private static AdapterNiver adapterNiver;
    private static Util u;
    private static ProgressBar progressBar;

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mAgenda = mDatabase.child("Membros");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes);
        iniciar();
        getAniversariantes(getBaseContext(), true);
    }

    private void iniciar() {
        u = new Util(getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressBar = findViewById(R.id.progressBar_niver);

        tvTitle = findViewById(R.id.tv_titulo_aniversario);
        tvTitle.setText("Aniversariantes do mês de " + u.mesPorExtenso1(u.mes()));

        rvNiver = findViewById(R.id.rv_niver);
        rvNiver.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void getAniversariantes(final Context context, final boolean preenche) {
        mAgenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                membros = new ArrayList<>();
                todosMembros = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Membros e = dataSnapshot1.getValue(Membros.class);
                        todosMembros.add(e);
                        if (u.mes().equals(e.getMes())) {
                            membros.add(e);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(membros, new Comparator<Membros>() {
                    @Override
                    public int compare(Membros p1, Membros p2) {
                        return p1.getDia() - p2.getDia();
                    }
                });
                if (preenche) {
                    adapterNiver = new AdapterNiver(context, membros);
                    rvNiver.setAdapter(adapterNiver);
                    progressBar.setVisibility(View.GONE);
                }
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
