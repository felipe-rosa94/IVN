package com.devtech.ivn.Activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devtech.ivn.Bancos.DBConfig;
import com.devtech.ivn.Model.Aviso;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.devtech.ivn.Activitys.MusicaAc.TOCANDO;
import static com.devtech.ivn.Activitys.Player.manager;
import static com.devtech.ivn.Activitys.Player.mp;

public class Home extends AppCompatActivity {

    private ImageView btnIvn;
    private ImageView btnJvn;
    private ImageView btnAgenda;
    private ImageView btnLouvor;
    private ImageView btnPergunta;
    private ImageView btnDardos;
    private ImageView btnVideoKids;
    private int width;
    private int heigth;
    private int cont = 0;
    public static String KEY;
    public static String ID_PERGUNTA;
    private String nome;
    public static boolean ENVIA = false;

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mAgenda = mDatabase.child("Avisos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            iniciar();
            btnIvn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(getBaseContext(), Web.class);
                        it.putExtra("url", "http://www.ividanova.com.br/");
                        it.putExtra("titulo", "Vida Nova");
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnJvn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(getBaseContext(), Web.class);
                        it.putExtra("url", "http://www.jovensvidanova.com.br/");
                        it.putExtra("titulo", "Juventude Vida Nova");
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnAgenda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(getBaseContext(), AgendaAc.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnPergunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(getBaseContext(), PergunteAc.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnDardos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(getBaseContext(), DardosAc.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnLouvor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (TOCANDO) {
                            finish();
                            Intent it = new Intent(getBaseContext(), MusicaAc.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            it.putExtra("nome", nome);
                            it.putExtra("tocando", true);
                            startActivity(it);
                        } else {
                            Intent it = new Intent(getBaseContext(), MusicaAc.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(it);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            btnVideoKids.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent it = new Intent(getBaseContext(), VideoAc.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            permissoes();
            getAvisos();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void permissoes() {
        ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void iniciar() {
        try {
            Util u = new Util(getBaseContext());
            DBConfig db = new DBConfig(getBaseContext());

            db.open();
            if (db.Fields.IdPg.equals("")) {
                db.Fields.IdPg = u.key();
                db.update(1);
                db.close();
            }

            ID_PERGUNTA = db.Fields.IdPg;
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnIvn = findViewById(R.id.btn_ivn);
        btnJvn = findViewById(R.id.btn_jvn);
        btnAgenda = findViewById(R.id.btn_agenda);
        btnLouvor = findViewById(R.id.btn_louvor);
        btnPergunta = findViewById(R.id.btn_pergunta);
        btnVideoKids = findViewById(R.id.btn_video_kds);
        btnDardos = findViewById(R.id.btn_dardos);
        TextView tvPoliticas = findViewById(R.id.tv_politicas);
        tvPoliticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getBaseContext(), Web.class);
                it.putExtra("url", "file:///android_asset/politicas.html");
                it.putExtra("titulo", "Políticas de privacidade");
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
            }
        });

        nome = (String) getIntent().getStringExtra("nome");
    }

    public void getAvisos() {
        mAgenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Aviso e = dataSnapshot1.getValue(Aviso.class);
                        if (e.isAtivo()) {
                            if (cont == 0) {
                                if (e.isProporcional()) {
                                    Intent it = new Intent(getBaseContext(), AvisoAc.class);
                                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    it.putExtra("titulo", e.getTitulo());
                                    it.putExtra("msg", e.getMsg());
                                    it.putExtra("imagem", e.getUrlImagem());
                                    startActivity(it);
                                } else {
                                    MsgAvisos(e.getTitulo(), e.getMsg(), e.getUrlImagem());
                                }
                                cont++;
                            }
                        }
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

    public void MsgAvisos(String titulo, String msg, String urlImagem) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_eventos, null, false);

        TextView tvTitulo = view.findViewById(R.id.tv_titulo_evento);
        if (titulo.equals("")) {
            tvTitulo.setVisibility(View.GONE);
        } else {
            tvTitulo.setText(titulo);
        }

        TextView tvMsg = view.findViewById(R.id.tv_msg_evento);
        if (msg.equals("")) {
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setText(msg);
        }

        ImageView imView = view.findViewById(R.id.im_imagem_evento);
        if (urlImagem.equals("")) {
            imView.setVisibility(View.GONE);
        } else {
            screen();
            Picasso.with(getBaseContext()).load(urlImagem).resize(width, heigth).centerCrop().into(imView);
        }
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void screen() {
        width = getBaseContext().getResources().getDisplayMetrics().widthPixels;
        heigth = (int) (width * 0.66666666666666666666666666666667);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.cancelAll();
        mp.stop();
    }
}



