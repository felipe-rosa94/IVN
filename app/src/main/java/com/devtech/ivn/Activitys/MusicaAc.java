package com.devtech.ivn.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devtech.ivn.Adapter.AdapterAlbum;
import com.devtech.ivn.Adapter.AdapterMusica;
import com.devtech.ivn.Model.Album;
import com.devtech.ivn.Model.Musica;
import com.devtech.ivn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.Normalizer;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.devtech.ivn.Activitys.NewPlayer.nome;
import static com.devtech.ivn.Activitys.NewPlayer.next;
import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class MusicaAc extends AppCompatActivity {

    private static RecyclerView rvMusicas;
    public static ArrayList<Musica> musicas;
    private ArrayList<Album> albums;
    private ProgressBar progressBar;
    private static AdapterMusica adapterMusica;
    private AdapterAlbum adapterAlbum;
    private ConstraintLayout layoutTocando;
    private ToggleButton btnPlay;
    private ImageView btnNext;
    private TextView tvFalha;
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mMusica = mDatabase.child("Musica");
    private static DatabaseReference mAlbum = mDatabase.child("Album");
    private static Activity activity;
    private static TextView tvNome, tvProgress;
    public static MediaPlayer PLAYER = new MediaPlayer();
    public static boolean FILTRANDO;
    private static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        isConnection();
        iniciar();
        getAlbum();
        getMusica();
    }

    private void iniciar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rvMusicas = findViewById(R.id.rv_musicas);
        rvMusicas.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        progressBar = findViewById(R.id.progressBar_musica);

        activity = MusicaAc.this;
        layoutTocando = findViewById(R.id.layout_tocando);
        tvNome = findViewById(R.id.tv_nome_musica_rodape);
        tvFalha = findViewById(R.id.tv_mensagem_falha_net);
        tvFalha.setVisibility(View.GONE);
        btnPlay = findViewById(R.id.btn_play_barra);
        btnNext = findViewById(R.id.btn_next_barra);
        FILTRANDO = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (PLAYER.isPlaying()) {
            tvNome.setText(nome);
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
                tvNome.setText(nome);
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

    private static ArrayList<Musica> Filtrar(ArrayList<Musica> list, String query) {
        query = removerAcentos(query.toLowerCase());
        final ArrayList<Musica> filtarLista = new ArrayList<>();
        for (Musica item : list) {
            String text = removerAcentos(item.getAlbum().toLowerCase());
            if (text.equals(query)) {
                filtarLista.add(item);
            }
        }
        return filtarLista;
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static void filtrarMusicas(String nome) {
        rvMusicas.setLayoutManager(new LinearLayoutManager(activity));
        ArrayList<Musica> list = Filtrar(musicas, nome);
        adapterMusica = new AdapterMusica(activity, list);
        rvMusicas.setAdapter(adapterMusica);
    }


    private void isConnection() {
        final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(5000);
        client.get("http://ividanova.com.br", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                tvFalha.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rvMusicas.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }


    public static void download(final String Url, String nome) {

        permissao(activity);
        msgDownload(nome);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Url, new FileAsyncHttpResponseHandler(activity) {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                String FileName = Url.substring(Url.lastIndexOf('/') + 1, Url.length());
                File DestFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), FileName);
                copiando(file, DestFile);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                long total = ((bytesWritten * 100) / totalSize);
                tvProgress.setText("(" + total + "%)");
            }
        });
    }

    public static void copiando(File src, File dst) {
        try {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            String arquivo = String.valueOf(dst);
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
                outChannel.force(true);
            } finally {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            }
            dialog.dismiss();
            Toast.makeText(activity, "Download concluido com sucesso.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void permissao(Activity activity) {
        try {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity, "Você já negou está permissão!", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity, "Você já negou está permissão!", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {

                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void msgDownload(String musica) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            final LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.dialog_download, null, false);
            TextView tvTitulo = view.findViewById(R.id.tv_dialog_nome_musica);
            tvProgress = view.findViewById(R.id.tv_procentagem);
            tvTitulo.setText(musica);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAlbum() {
        mAlbum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                albums = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Album a = dataSnapshot1.getValue(Album.class);
                        albums.add(a);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapterAlbum = new AdapterAlbum(getBaseContext(), albums);
                rvMusicas.setAdapter(adapterAlbum);
                rvMusicas.setVisibility(View.VISIBLE);
                tvFalha.setVisibility(View.GONE);
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
        } else if (FILTRANDO) {
            rvMusicas.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
            adapterAlbum = new AdapterAlbum(getBaseContext(), albums);
            rvMusicas.setAdapter(adapterAlbum);
            FILTRANDO = false;
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            voltar();
            return false;
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




