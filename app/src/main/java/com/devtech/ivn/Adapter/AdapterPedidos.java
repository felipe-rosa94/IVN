package com.devtech.ivn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtech.ivn.Model.Pedidos;
import com.devtech.ivn.R;

import java.util.ArrayList;

import static com.devtech.ivn.Activitys.PedidosAc.deletaItem;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.MyViewHolder> {

    private Context context;
    private ArrayList<Pedidos> pedidos;

    public AdapterPedidos(Context context, ArrayList<Pedidos> pedidos) {
        this.context = context;
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public AdapterPedidos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pedidos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPedidos.MyViewHolder holder, int position) {
        Pedidos p = pedidos.get(position);
        holder.tvNome.setText("Pedido feito por: " + p.getNome());
        holder.tvDescricao.setText(p.getDescricao());
        try {
            long milisegundos = (System.currentTimeMillis() - p.getData());
            if ((((milisegundos / 1000) / 60) / 60) > 24) {
                deletaItem(p.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescricao;
        private TextView tvNome;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome_pedido);
            tvDescricao = itemView.findViewById(R.id.tv_descricao_pedido);
        }
    }
}
