package com.devtech.ivn.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devtech.ivn.Adapter.AdapterMusica;
import com.devtech.ivn.Model.Musica;
import com.devtech.ivn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.devtech.ivn.Activitys.NewPlayer.NOME;
import static com.devtech.ivn.Activitys.NewPlayer.next;

public class MusicaAc extends AppCompatActivity {

    private RecyclerView rvMusicas;
    public static ArrayList<Musica> musicas;
    private ProgressBar progressBar;
    private AdapterMusica adapterMusica;
    private ConstraintLayout layoutTocando;
    private ToggleButton btnPlay;
    private ImageView btnNext;

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mMusica = mDatabase.child("Musica");

    private static Activity activity;
    private TextView tvNome;

    public static MediaPlayer PLAYER = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        iniciar();
        getMusica();
    }

    private void iniciar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rvMusicas = findViewById(R.id.rv_musicas);
        rvMusicas.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar_musica);

        activity = MusicaAc.this;
        layoutTocando = findViewById(R.id.layout_tocando);
        tvNome = findViewById(R.id.tv_nome_musica_rodape);
        btnPlay = findViewById(R.id.btn_play_barra);
        btnNext = findViewById(R.id.btn_next_barra);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PLAYER.isPlaying()) {
            tvNome.setText(NOME);
            layoutTocando.setVisibility(View.VISIBLE);
        } else {
            layoutTocando.setVisibility(View.GONE);
        }

        layoutTocando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getBaseContext(), NewPlayer.class);
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(it);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                tvNome.setText(NOME);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PLAYER.isPlaying()) {
                    btnPlay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_play_circle));
                    PLAYER.pause();
                } else {
                    btnPlay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_pause_circle));
                    PLAYER.start();
                }
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


    private void voltar() {
        if (PLAYER.isPlaying()) {
            Intent it = new Intent(getBaseContext(), Home.class);
            it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(it);
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            voltar();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            voltar();
        }
        return true;
    }
}




