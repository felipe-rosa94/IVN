package com.devtech.ivn.Activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devtech.ivn.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.devtech.ivn.Activitys.MusicaAc.TOCANDO;
import static com.devtech.ivn.Activitys.MusicaAc.finalizar;
import static com.devtech.ivn.Activitys.MusicaAc.musicas;

public class Player extends AppCompatActivity {

    private ToggleButton btnPlayPause;
    private ImageView btnVoltar;
    private ImageView btnProximo;
    private ImageView imMusic;
    private TextView tvNome;
    private TextView tvDescricao;
    private SeekBar positionBar;
    private SeekBar volumeBar;
    private TextView elapsedTimeLabel;
    private TextView remainingTimeLabel;
    private static MediaPlayer mp;
    private int totalTime;
    private int position;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        iniciar();

        btnPlayPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btnPlayPause.setBackgroundDrawable(Player.this.getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
                    if (!mp.isPlaying()) {
                        mp.start();
                        finalizar();
                    }
                } else {
                    btnPlayPause.setBackgroundDrawable(Player.this.getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
                    mp.pause();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent it = new Intent(getBaseContext(), Player.class);
                it.putExtra("position", --position);
                startActivity(it);
                finish();
            }
        });

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
                Intent it = new Intent(getBaseContext(), Player.class);
                it.putExtra("position", ++position);
                startActivity(it);
                finish();
            }
        });

        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }

    private void iniciar() {
        btnPlayPause = findViewById(R.id.btn_play_pause);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);
        imMusic = findViewById(R.id.im_music_player);
        btnVoltar = findViewById(R.id.btn_voltar_music);
        btnProximo = findViewById(R.id.btn_proximo_music);
        volumeBar = findViewById(R.id.volumeBar);
        tvNome = findViewById(R.id.tv_nome_music);
        tvDescricao = findViewById(R.id.tv_descricao_music);
        positionBar = findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        activity = Player.this;

        try {
            position = (int) getIntent().getIntExtra("position", 0);
            String urlSom = musicas.get(position).getUrlSom();
            String urlImagem = musicas.get(position).getUrlImagem();
            String nome = musicas.get(position).getNome();
            String descricao = musicas.get(position).getDescricao();

            if (urlSom.contains("http://")) {
                player(urlSom);
            } else {
                player("http://" + urlSom);
            }

            if (!urlImagem.equals("")) {
                Picasso.with(getBaseContext()).load(urlImagem).into(imMusic);
            }
            tvNome.setText(nome);
            tvDescricao.setText(descricao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            positionBar.setProgress(currentPosition);
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);
            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    private void player(String urlSom) {
        try {
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(urlSom);
            mp.prepare();
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f, 0.5f);
            totalTime = (int) duration(urlSom);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finalizarPlayer() {
        try {
            mp.stop();
            activity.finish();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private long duration(String urlSom) {
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(urlSom);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
        mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        return Long.parseLong(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                if (mp.isPlaying()) {
                    Intent it = new Intent(getBaseContext(), MusicaAc.class);
                    it.putExtra("tocando", true);
                    it.putExtra("nome", musicas.get(position).getNome());
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                } else {
                    TOCANDO = false;
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
                try {
                    if (mp.isPlaying()) {
                        Intent it = new Intent(getBaseContext(), MusicaAc.class);
                        it.putExtra("tocando", true);
                        it.putExtra("nome", musicas.get(position).getNome());
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } else {
                        TOCANDO = false;
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return true;
    }
}