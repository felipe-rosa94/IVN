package com.devtech.ivn.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devtech.ivn.Model.Dardos;
import com.devtech.ivn.R;
import com.devtech.ivn.Util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterDardos extends RecyclerView.Adapter<AdapterDardos.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<Dardos> dardos;
    private int width;
    private int heigth;
    private Util u;

    public AdapterDardos(Context context, ArrayList<Dardos> dardos, Activity activity) {
        this.context = context;
        this.dardos = dardos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dardos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Dardos d = dardos.get(position);

        u = new Util(context);

        if (d.getTitulo().equals("")) {
            holder.tvTitulo.setVisibility(View.GONE);
        }

        if (d.getDescricao().equals("")) {
            holder.tvDescricao.setVisibility(View.GONE);
        }

        if (d.getUrl().equals("")) {
            holder.imDardos.setVisibility(View.GONE);
        }

        try {
            holder.tvTitulo.setText(d.getTitulo());
            holder.tvDescricao.setText(d.getDescricao());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            screen();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Picasso.with(context).load(d.getUrl()).resize(width, heigth).centerCrop().into(holder.imDardos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.compartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    u.Compartilhar(activity, position);
                    //u.download(d.getUrl(), d.getNomeArquivo(), d.getTitulo() + "\n" + d.getDescricao(), activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dardos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imDardos;
        private TextView tvTitulo;
        private TextView tvDescricao;
        private ConstraintLayout compartilhar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imDardos = itemView.findViewById(R.id.im_dardos);
            tvDescricao = itemView.findViewById(R.id.tv_descricao_dardos);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_dardos);
            compartilhar = itemView.findViewById(R.id.layout_compartilhar);
        }
    }

    public void screen() {
        width = context.getResources().getDisplayMetrics().widthPixels;
        heigth = (int) (width * 0.66666666666666666666666666666667);
    }
}
