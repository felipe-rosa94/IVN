package com.devtech.ivn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtech.ivn.Model.Membros;
import com.devtech.ivn.R;

import java.util.ArrayList;

public class AdapterNiver extends RecyclerView.Adapter<AdapterNiver.MyViewHolder> {

    private Context context;
    private ArrayList<Membros> membros;

    public AdapterNiver(Context context, ArrayList<Membros> membros) {
        this.context = context;
        this.membros = membros;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_niver, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Membros m = membros.get(position);
        try {
            String niver = "Dia " + String.valueOf(m.getDia()) + " - " + m.getDescricao();
            holder.tvNiver.setText(niver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return membros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNiver;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNiver = itemView.findViewById(R.id.tv_niver);
        }
    }
}
