package com.devtech.ivn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devtech.ivn.Activitys.Player;
import com.devtech.ivn.Model.Musica;
import com.devtech.ivn.R;

import java.util.ArrayList;

import static com.devtech.ivn.Activitys.MusicaAc.TOCANDO;
import static com.devtech.ivn.Activitys.Player.finalizarPlayer;

public class AdapterMusica extends RecyclerView.Adapter<AdapterMusica.MyViewHolder> {

    private Context context;
    private ArrayList<Musica> musicas;

    public AdapterMusica(Context context, ArrayList<Musica> musicas) {
        this.context = context;
        this.musicas = musicas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_musicas, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Musica m = musicas.get(position);

        holder.tvNome.setText(m.getNome());
        holder.tvDescricao.setText(m.getDescricao());

        if (m.getDescricao().equals("")) {
            holder.tvDescricao.setVisibility(View.GONE);
        }

        holder.cvMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (TOCANDO) {
                        finalizarPlayer();
                    }
                    Toast.makeText(context, "Carregando música ...", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(context, Player.class);
                    it.putExtra("position", position);
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cvMusica;
        private TextView tvNome;
        private TextView tvDescricao;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.tv_nome_musica);
            tvDescricao = itemView.findViewById(R.id.tv_descricao_musica);
            cvMusica = itemView.findViewById(R.id.cv_musica);
        }
    }
}
