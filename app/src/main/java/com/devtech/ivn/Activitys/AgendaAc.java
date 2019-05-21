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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devtech.ivn.Adapter.AdapterAgenda;
import com.devtech.ivn.Model.Agenda;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AgendaAc extends AppCompatActivity {

    private RecyclerView rvAgenda;
    private ArrayList<Agenda> agendas;
    private AdapterAgenda adapterAgenda;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mAgenda = mDatabase.child("Eventos");
    private Util u;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        iniciar();
        getEventos();
    }

    private void iniciar() {
        u = new Util(getBaseContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView tvTitle = findViewById(R.id.tv_title_agenda);
        tvTitle.setText(u.mesPorExtenso());
        progressBar = findViewById(R.id.progressBar_agenda);

        adapterAgenda = new AdapterAgenda(getBaseContext(), agendas);
        rvAgenda = findViewById(R.id.rv_agenda);
        rvAgenda.setLayoutManager(new LinearLayoutManager(this));

        ImageView cvAdicioneAniversarios = findViewById(R.id.add_aniversariantes);
        cvAdicioneAniversarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(getBaseContext(), CadastraNiver.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView cvAniversariantes = findViewById(R.id.aniversariantes);

        cvAniversariantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(getBaseContext(), AniversariantesAc.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void filtraMes() {
        adapterAgenda = new AdapterAgenda(getBaseContext(), agendas);
        ArrayList<Agenda> filtarModoLista;
        filtarModoLista = Filtrar(agendas, u.anoMes());
        adapterAgenda.Filtro(filtarModoLista);
        rvAgenda.setAdapter(adapterAgenda);
    }

    private ArrayList<Agenda> Filtrar(ArrayList<Agenda> list, String query) {
        query = removerAcentos(query.toLowerCase());
        final ArrayList<Agenda> filtarLista = new ArrayList<>();
        for (Agenda item : list) {
            String text = removerAcentos(item.getAnoMes().toLowerCase());
            if (text.contains(query)) {
                filtarLista.add(item);
            }
        }
        return filtarLista;
    }

    public String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public void getEventos() {
        mAgenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                agendas = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Agenda e = dataSnapshot1.getValue(Agenda.class);
                        agendas.add(e);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(agendas, new Comparator<Agenda>() {
                    @Override
                    public int compare(Agenda p1, Agenda p2) {
                        return p1.getId() - p2.getId();
                    }
                });
                try {
                    filtraMes();
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
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
