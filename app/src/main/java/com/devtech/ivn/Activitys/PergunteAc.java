package com.devtech.ivn.Activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devtech.ivn.Adapter.AdapterPergunte;
import com.devtech.ivn.Bancos.DBConfig;
import com.devtech.ivn.Model.Admin;
import com.devtech.ivn.Model.notification;
import com.devtech.ivn.Model.Pergunta;
import com.devtech.ivn.Model.Push;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.devtech.ivn.Activitys.Home.ENVIA;

public class PergunteAc extends AppCompatActivity {

    private DBConfig dbConfig;
    private Util u;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private EditText etMsg;
    private ImageView btnEnvia;
    private RecyclerView rvPerguntas;
    private CardView cvDescricao;
    private AdapterPergunte adapterPergunte;

    private ArrayList<Pergunta> perguntas;

    private String TOKEN;
    private String TOKEN_ADMIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pergunte);
        inicar();
    }

    private void inicar() {
        u = new Util(getBaseContext());
        dbConfig = new DBConfig(getBaseContext());
        gravaId();
        getPerguntas();
        getToken();
        getTokenAdmin();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cvDescricao = findViewById(R.id.cv_descricao);

        etMsg = findViewById(R.id.et_msg_pergunte);
        rvPerguntas = findViewById(R.id.rv_pergunta);
        rvPerguntas.setLayoutManager(new LinearLayoutManager(this));
        btnEnvia = findViewById(R.id.btn_envia_msg);
        btnEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMsg.getText().toString().trim().equals("") || etMsg.getText().toString().trim().equals("text")) {
                    Toast.makeText(PergunteAc.this, "Texto inválido", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbConfig.open();
                if (!dbConfig.Fields.BkpConversa.equals("")) {
                    envia();
                    try {
                        JSONArray array = new JSONArray(dbConfig.Fields.BkpConversa);
                        perguntas.clear();
                        for (int i = 0; i < array.length(); i++) {
                            Pergunta p = new Pergunta();
                            p.setId(array.getJSONObject(i).getString("id"));
                            p.setTipo(array.getJSONObject(i).getString("tipo"));
                            p.setMsg(array.getJSONObject(i).getString("msg"));
                            mDatabase.child("Pergunte/" + dbConfig.Fields.IdPg).child(p.getId()).setValue(p);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    envia();
                }

                dbConfig.close();
                ENVIA = false;
                try {
                    ((LinearLayoutManager) rvPerguntas.getLayoutManager()).scrollToPositionWithOffset(perguntas.size() + 1, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void envia() {
        Pergunta p = new Pergunta();
        p.setId(u.key());
        p.setMsg(etMsg.getText().toString());
        p.setTipo("pergunta");
        p.setToken(TOKEN);
        dbConfig.open();
        mDatabase.child("Pergunte/" + dbConfig.Fields.IdPg).child(p.getId()).setValue(p);
        dbConfig.close();
        etMsg.setText("");
        enviarNotificacao();
    }

    private void gravaId() {
        try {
            dbConfig.open();
            if (dbConfig.Fields.IdPg.equals("")) {
                dbConfig.Fields.IdPg = u.key();
                dbConfig.update(1);
                dbConfig.close();
            } else {
                dbConfig.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarNotificacao() {
        try {
            Push p = new Push();
            p.setTo(TOKEN_ADMIN);
            notification n = new notification();
            n.setTitle("Pergunte aos pastores");
            n.setBody("Existem novas perguntas");
            p.setNotification(n);
            String push = new Gson().toJson(p);

            ByteArrayEntity entity = new ByteArrayEntity(push.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.addHeader("content-type", "application/json");
            client.addHeader("authorization", "key=AAAAPxMnHjY:APA91bF80R0S-X7xZlFz_JVeNEm3hY4Pt2lfZRJg-y5N5s5ZIh5TykAvu3FMHwVAcfRm1ztvPY5XQqVHSm9p-hctY-UTGmjwbhC6bu9RHeJ9aaUuCTvNULUyoLSySvyp2kuIzMisvre0");
            client.post(getBaseContext(), "https://fcm.googleapis.com/fcm/send", entity, "application/json", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTokenAdmin() {
        DatabaseReference dbRef = mDatabase.child("Admin");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Admin a = dataSnapshot1.getValue(Admin.class);
                    if (a.getUsuario().equals("James")) {
                        TOKEN_ADMIN = a.getToken();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPerguntas() {
        dbConfig.open();
        DatabaseReference mPergunte = mDatabase.child("Pergunte/" + dbConfig.Fields.IdPg);
        dbConfig.close();
        final boolean[] bkp = {false};
        mPergunte.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                perguntas = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Pergunta e = dataSnapshot1.getValue(Pergunta.class);
                        perguntas.add(e);
                        bkp[0] = e.isRespondida();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dbConfig.open();
                if (bkp[0]) {
                    if (!new Gson().toJson(perguntas).equals("[]")) {
                        dbConfig.Fields.BkpConversa = new Gson().toJson(perguntas);
                        dbConfig.update(1);
                        mDatabase.child("Pergunte/" + dbConfig.Fields.IdPg).removeValue();
                    }
                }

                if (!dbConfig.Fields.BkpConversa.equals("") && !new Gson().toJson(perguntas).equals("[]")) {
                    dbConfig.Fields.BkpConversa = new Gson().toJson(perguntas);
                    dbConfig.update(1);
                }

                try {
                    JSONArray array = new JSONArray(dbConfig.Fields.BkpConversa);
                    perguntas.clear();
                    for (int i = 0; i < array.length(); i++) {
                        Pergunta p = new Pergunta();
                        p.setId(array.getJSONObject(i).getString("id"));
                        p.setTipo(array.getJSONObject(i).getString("tipo"));
                        p.setMsg(array.getJSONObject(i).getString("msg"));
                        perguntas.add(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dbConfig.close();

                adapterPergunte = new AdapterPergunte(getBaseContext(), perguntas);
                rvPerguntas.setAdapter(adapterPergunte);
                if (perguntas.size() != 0) {
                    cvDescricao.setVisibility(View.GONE);
                }
                try {
                    ((LinearLayoutManager) rvPerguntas.getLayoutManager()).scrollToPositionWithOffset(perguntas.size(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "getInstanceId failed", task.getException());
                            return;
                        }
                        TOKEN = task.getResult().getToken();
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
