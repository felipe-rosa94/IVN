package com.devtech.ivn.Activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devtech.ivn.Adapter.AdapterNiver;
import com.devtech.ivn.Adapter.AdapterPergunte;
import com.devtech.ivn.Bancos.DBConfig;
import com.devtech.ivn.Model.Membros;
import com.devtech.ivn.Model.Pergunta;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.devtech.ivn.Util.Servicos.ENVIA;

public class PergunteAc extends AppCompatActivity {

    private DBConfig dbConfig;
    private Util u;
    private Toolbar toolbar;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPerguntas;

    private EditText etMsg;
    private ImageView btnEnvia;
    private RecyclerView rvPerguntas;
    private CardView cvDescricao;
    private AdapterPergunte adapterPergunte;

    private ArrayList<Pergunta> perguntas;

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

        toolbar = findViewById(R.id.toolbar);
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
                if (!dbConfig.Fields.BkpConversa.equals("")){
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
                ((LinearLayoutManager) rvPerguntas.getLayoutManager()).scrollToPositionWithOffset(perguntas.size() + 1, 0);
            }
        });
    }

    private void envia() {
        Pergunta p = new Pergunta();
        p.setId(u.key());
        p.setMsg(etMsg.getText().toString());
        p.setTipo("pergunta");
        dbConfig.open();
        mDatabase.child("Pergunte/" + dbConfig.Fields.IdPg).child(p.getId()).setValue(p);
        dbConfig.close();
        etMsg.setText("");
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

                if (!dbConfig.Fields.BkpConversa.equals("") && !new Gson().toJson(perguntas).equals("[]")){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
