package com.devtech.ivn.Activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devtech.ivn.Model.Pedidos;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastraPedido extends AppCompatActivity {

    private EditText etNome;
    private EditText etDescricao;
    private Button btnDescricao;
    private Util u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        iniciar();
    }

    private void iniciar() {
        u = new Util(getBaseContext());
        etNome = findViewById(R.id.et_nome_oracao);
        etDescricao = findViewById(R.id.et_descricao_pedido);
        btnDescricao = findViewById(R.id.btn_salvar_pedido);
        btnDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar();
            }
        });
    }

    private void gravar() {
        if (!etDescricao.getText().toString().isEmpty()) {
            Pedidos p = new Pedidos();
            p.setNome(etNome.getText().toString());
            p.setDescricao(etDescricao.getText().toString());
            p.setKey(u.key());
            p.setData(System.currentTimeMillis());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Pedidos").child(p.getKey()).setValue(p);
            finish();
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
