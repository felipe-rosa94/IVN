package com.devtech.ivn.Activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devtech.ivn.Model.Musica;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.NotificationBroadcast;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.devtech.ivn.Activitys.App.CHANNEL_2_ID;
import static com.devtech.ivn.Activitys.MusicaAc.PLAYER;
import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;


public class NewPlayer extends AppCompatActivity {

    private static TextView tvNome, tvDescricao, tvCorrente, tvRestante;

    private static SeekBar seekBarDuracao, seekBarVolume;
    private static ImageView btnPrevious, btnNext;
    private static ImageView ivCapa;
    private static ToggleButton btnPlay;

    public static String nome, url, descricao;
    public static long duracao;
    public static int position;

    private static Thread updateseekbar;
    private static AudioManager mAudioManager;
    private static ArrayList<Musica> musicas;
    private static String urlImagem;

    private static Activity activity;

    private static NotificationManagerCompat notificationManager;
    private static MediaSessionCompat mediaSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        musicas = new ArrayList<>();
        urlImagem = getIntent().getStringExtra("capa");
        url = getIntent().getStringExtra("url");
        nome = getIntent().getStringExtra("nome");
        duracao = getIntent().getLongExtra("duracao", 0);
        position = getIntent().getIntExtra("position", 0);
        descricao = getIntent().getStringExtra("descricao");
        musicas = (ArrayList<Musica>) getIntent().getSerializableExtra("musicas");
        activity = NewPlayer.this;

        notificationManager = NotificationManagerCompat.from(this);
        mediaSession = new MediaSessionCompat(this, "tag");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.registerReceiver(new NotificationBroadcast(), new IntentFilter("android.intent.action.PHONE_STATE"));
        }

        iniciar();
    }

    private void iniciar() {
        tvNome = findViewById(R.id.tv_nome_musica);
        tvDescricao = findViewById(R.id.tv_descricao_musica);
        tvCorrente = findViewById(R.id.elapsedTimeLabel);
        tvRestante = findViewById(R.id.remainingTimeLabel);
        ivCapa = findViewById(R.id.im_music_player);
        seekBarDuracao = findViewById(R.id.seekBar_duracao);
        seekBarVolume = findViewById(R.id.seekBar_volume);
        btnPrevious = findViewById(R.id.btn_previous_musica);
        btnPlay = findViewById(R.id.btn_play_musica);
        btnNext = findViewById(R.id.btn_next_musica);
        player(url, (int) duracao, nome, descricao);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        seekBarVolume.setMax(15);
        seekBarVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(), AudioManager.FLAG_SHOW_UI);
            }
        });
    }

    private static void player(String urlSom, final int duracao, String nome, String descricao) {

        try {
            if (PLAYER.isPlaying()) {
                PLAYER.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PLAYER = new MediaPlayer();
            PLAYER.setAudioStreamType(AudioManager.STREAM_MUSIC);
            PLAYER.setDataSource(urlSom);
            PLAYER.prepare();
            PLAYER.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyMusic(activity);

        btnPlay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));

        try {
            tvNome.setText(nome);
            tvDescricao.setText(descricao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!urlImagem.equals("")) {
                Picasso.with(activity).load(urlImagem).into(ivCapa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateseekbar = new Thread() {
            @Override
            public void run() {
                final int totalDuration = duracao;
                int currentPosition = 0;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (PLAYER != null) {
                            try {
                                Message msg = new Message();
                                msg.what = PLAYER.getCurrentPosition();
                                handler.sendMessage(msg);
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = PLAYER.getCurrentPosition();
                        seekBarDuracao.setProgress(currentPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    next();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        seekBarDuracao.setMax(duracao);
        updateseekbar.start();
        seekBarDuracao.getProgressDrawable().setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekBarDuracao.getThumb().setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        seekBarDuracao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PLAYER.seekTo(seekBar.getProgress());
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevoius();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            String tempo_corrente = createTimeLabel((int) duracao);
            tvCorrente.setText(tempo_corrente);
            String remainingTime = createTimeLabel((int) duracao - currentPosition);
            tvRestante.setText("- " + remainingTime);
        }
    };

    private static void notifyMusic(Activity activity) {
        Bitmap artwork = BitmapFactory.decodeResource(activity.getResources(), R.drawable.logo);

        Intent previous = new Intent(activity, NotificationBroadcast.class)
                .setAction("com.devtech.ivn.Activitys.previous");

        Intent pause = new Intent(activity, NotificationBroadcast.class)
                .setAction("com.devtech.ivn.Activitys.pause");

        Intent next = new Intent(activity, NotificationBroadcast.class)
                .setAction("com.devtech.ivn.Activitys.next");

        int icone;
        if (PLAYER.isPlaying()) {
            icone = R.drawable.ic_pause_circle;
        } else {
            icone = R.drawable.ic_play_circle;
        }

        Notification notification = new NotificationCompat.Builder(activity, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.bt04_a)
                .setContentTitle(nome)
                .setContentText(descricao)
                .setLargeIcon(artwork)
                .addAction(0, null, null)
                .addAction(R.drawable.ic_skip_previou, "Previous", PendingIntent.getBroadcast(activity, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(icone, "Pause", PendingIntent.getBroadcast(activity, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(R.drawable.ic_skip_next, "Next", PendingIntent.getBroadcast(activity, 0, next, PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(0, null, null)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1, 2, 3)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setSubText("Músicas")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(1, notification);
    }

    public static void pause() {
        seekBarDuracao.setMax((int) duracao);
        if (PLAYER.isPlaying()) {
            btnPlay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
            PLAYER.pause();
            notifyMusic(activity);
        } else {
            btnPlay.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
            PLAYER.start();
            notifyMusic(activity);
        }
    }

    public static void next() {
        PLAYER.stop();
        try {
            position++;
            if (position < musicas.size()) {
                url = musicas.get(position).getUrlSom();
                nome = musicas.get(position).getNome();
                descricao = musicas.get(position).getDescricao();
                urlImagem = musicas.get(position).getUrlImagem();
                duracao = musicas.get(position).getDuration();
            } else {
                url = musicas.get(0).getUrlSom();
                nome = musicas.get(0).getNome();
                descricao = musicas.get(0).getDescricao();
                urlImagem = musicas.get(0).getUrlImagem();
                duracao = musicas.get(0).getDuration();
                position = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        player(url, (int) duracao, nome, descricao);
        btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
    }

    public static void prevoius() {
        PLAYER.stop();
        try {
            position--;
            if (position >= 0) {
                url = musicas.get(position).getUrlSom();
                nome = musicas.get(position).getNome();
                descricao = musicas.get(position).getDescricao();
                urlImagem = musicas.get(position).getUrlImagem();
                duracao = musicas.get(position).getDuration();
            } else {
                int i = musicas.size() - 1;
                url = musicas.get(i).getUrlSom();
                nome = musicas.get(i).getNome();
                descricao = musicas.get(i).getDescricao();
                urlImagem = musicas.get(i).getUrlImagem();
                duracao = musicas.get(i).getDuration();
                position = i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        player(url, (int) duracao, nome, descricao);
        btnPlay.setBackgroundResource(R.drawable.ic_pause_black_24dp);
    }

    public static String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;
        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }

    private void voltar() {
        if (PLAYER.isPlaying()) {
            Intent it = new Intent(getBaseContext(), MusicaAc.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            PLAYER.pause();
            notifyMusic(activity);
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
