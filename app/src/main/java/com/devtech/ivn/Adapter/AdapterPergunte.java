package com.devtech.ivn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtech.ivn.Model.Pergunta;
import com.devtech.ivn.R;

import java.util.ArrayList;

public class AdapterPergunte extends RecyclerView.Adapter<AdapterPergunte.MyViewHolder> {

    private Context context;
    private ArrayList<Pergunta> perguntas;

    public AdapterPergunte(Context context, ArrayList<Pergunta> perguntas) {
        this.context = context;
        this.perguntas = perguntas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pergunte, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pergunta p = perguntas.get(position);

        holder.cvPergunta.setVisibility(View.GONE);
        holder.cvResposta.setVisibility(View.GONE);

        if (p.getTipo().equals("pergunta")){
            holder.cvPergunta.setVisibility(View.VISIBLE);
            holder.tvPergunta.setText(p.getMsg());
        } else {
            holder.cvResposta.setVisibility(View.VISIBLE);
            holder.tvResposta.setText(p.getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return perguntas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cvPergunta, cvResposta;
        private TextView tvPergunta, tvResposta;
        public MyViewHolder(View itemView) {
            super(itemView);
            cvPergunta = itemView.findViewById(R.id.cv_pergunta);
            cvResposta = itemView.findViewById(R.id.cv_resposta);
            tvPergunta = itemView.findViewById(R.id.tv_pergunta);
            tvResposta = itemView.findViewById(R.id.tv_resposta);
        }
    }
}
