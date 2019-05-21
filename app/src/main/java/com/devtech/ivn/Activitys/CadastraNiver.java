package com.devtech.ivn.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devtech.ivn.Model.Membros;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.devtech.ivn.Activitys.AniversariantesAc.getAniversariantes;
import static com.devtech.ivn.Activitys.AniversariantesAc.todosMembros;

public class CadastraNiver extends AppCompatActivity {

    private EditText etNome;
    private EditText etData;
    private Util u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_niver);
        iniciar();
        getAniversariantes(getBaseContext(), false);
    }

    private void iniciar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        u = new Util(getBaseContext());
        etNome = findViewById(R.id.et_nome_niver);
        etData = findViewById(R.id.et_data_niver);
        Button btnGravar = findViewById(R.id.btn_gravar_niver);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravar();
            }
        });
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(etData, smf);
        etData.addTextChangedListener(mtw);
    }

    private void gravar() {
        if (etData.getText().toString().length() == 10) {

            String[] arrayData = etData.getText().toString().split("/");
            int dia = Integer.parseInt(arrayData[0]);
            String mes = arrayData[1];
            String nome = "";

            String[] arrayNome = etNome.getText().toString().split(" ");
            for (int i = 0; i < arrayNome.length; i++) {
                nome += arrayNome[i].substring(0, 1).toUpperCase().concat(arrayNome[i].substring(1) + " ");
            }
            nome = nome.substring(0, nome.length() - 1);

            Membros m = new Membros();
            m.setId(u.key());
            m.setDescricao(nome.trim());
            m.setDia(dia);
            m.setMes(mes);

            boolean cadastrado = false;
            for (int i = 0; i < todosMembros.size(); i++) {
                if ((todosMembros.get(i).getDescricao().equalsIgnoreCase(nome)) && (todosMembros.get(i).getDia() == dia) && (todosMembros.get(i).getMes().equalsIgnoreCase(mes))) {
                    cadastrado = true;
                }
            }

            if (cadastrado) {
                Toast.makeText(this, "Esse nome já está cadastrado.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Membros").child(m.getId()).setValue(m);
                finish();
            }

        } else {
            etData.setError("Exemplo: 00/00/0000");
            Toast.makeText(this, "Preencha a data corretamente", Toast.LENGTH_SHORT).show();
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
