package com.devtech.ivn.Activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devtech.ivn.R;

public class NewPlayer extends AppCompatActivity {

    TextView tvNome;
    SeekBar seekBarDuracao, seekBarVolume;
    ImageView btnPrevious, btnPlay, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iniciar();
    }

    private void iniciar() {
        tvNome = findViewById(R.id.tv_nome_musica);
        seekBarDuracao = findViewById(R.id.seekBar_duracao);
        btnPrevious = findViewById(R.id.btn_previous_musica);
        btnPlay = findViewById(R.id.btn_play_musica);
        btnNext = findViewById(R.id.btn_next_musica);
    }

}
