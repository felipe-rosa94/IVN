package com.devtech.ivn.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devtech.ivn.Adapter.AdapterMusica;
import com.devtech.ivn.Model.Musica;
import com.devtech.ivn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MusicaAc extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvMusicas;
    public static ArrayList<Musica> musicas;
    private ProgressBar progressBar;
    private AdapterMusica adapterMusica;
    private ConstraintLayout layoutTocando;

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mMusica = mDatabase.child("Musica");

    private static Activity activity;
    private TextView tvNome;

    public static boolean TOCANDO = false;
    public static int MATA = 0;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        iniciar();
        getMusica();
    }

    private void iniciar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rvMusicas = findViewById(R.id.rv_musicas);
        rvMusicas.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar_musica);

        activity = MusicaAc.this;
        layoutTocando = findViewById(R.id.layout_tocando);
        tvNome = findViewById(R.id.tv_nome_musica_rodape);

        TOCANDO = (boolean) getIntent().getBooleanExtra("tocando", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TOCANDO) {
            layoutTocando.setVisibility(View.VISIBLE);
            nome = (String) getIntent().getStringExtra("nome");
            tvNome.setText(nome);
        } else {
            layoutTocando.setVisibility(View.GONE);
        }

        layoutTocando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getMusica() {
        mMusica.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musicas = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Musica e = dataSnapshot1.getValue(Musica.class);
                        musicas.add(e);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapterMusica = new AdapterMusica(getBaseContext(), musicas);
                rvMusicas.setAdapter(adapterMusica);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void finalizar() {
        activity.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (TOCANDO){
                    Intent it = new Intent(getBaseContext(), Home.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("nome", nome);
                    startActivity(it);
                    finish();
                    MATA = 100;
                } else {
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (TOCANDO){
                    Intent it = new Intent(getBaseContext(), Home.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("nome", nome);
                    startActivity(it);
                    finish();
                    MATA = 100;
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }
}




