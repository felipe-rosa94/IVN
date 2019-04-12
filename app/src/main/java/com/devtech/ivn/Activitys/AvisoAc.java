package com.devtech.ivn.Activitys;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.squareup.picasso.Picasso;

public class AvisoAc extends AppCompatActivity {

    private CardView cvAviso;
    private ImageView imAviso;
    private ImageView btnFecha;
    private ConstraintLayout layout_compartilha;
    private Util u;

    private String nomeArquivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        permissao(AvisoAc.this);

        u = new Util(getBaseContext());

        final String url = (String) getIntent().getStringExtra("imagem");
        String titulo = (String) getIntent().getStringExtra("titulo");
        String msg = (String) getIntent().getStringExtra("msg");

        nomeArquivo = url;
        nomeArquivo = nomeArquivo.substring(url.lastIndexOf("/") + 1, nomeArquivo.length());

        imAviso = findViewById(R.id.im_aviso_grande);
        cvAviso = findViewById(R.id.cv_aviso);
        btnFecha = findViewById(R.id.btn_fecha_aviso);
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_compartilha = findViewById(R.id.layout_compartilha_aviso);
        layout_compartilha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u.download(url, nomeArquivo, "", AvisoAc.this);
            }
        });

        Picasso.with(getBaseContext()).load(url).into(imAviso);
    }

    public void permissao(Activity activity) {
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
