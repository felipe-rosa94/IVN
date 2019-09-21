package com.devtech.ivn.Activitys;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class PopUp extends Activity {

    private Util u;
    private String nomeArquivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigth = dm.heightPixels;

        getWindow().setLayout(610, 1280);

        u = new Util(getBaseContext());

        final String url = (String) getIntent().getStringExtra("imagem");

        nomeArquivo = url;
        nomeArquivo = nomeArquivo.substring(url.lastIndexOf("/") + 1, nomeArquivo.length());

        ImageView imAviso = findViewById(R.id.im_aviso_grande);
        ImageView btnFecha = findViewById(R.id.btn_fecha_aviso);
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ConstraintLayout layout_compartilha = findViewById(R.id.layout_compartilha_aviso);
        layout_compartilha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissao(PopUp.this);
                u.download(url, nomeArquivo, "", PopUp.this);
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
}
