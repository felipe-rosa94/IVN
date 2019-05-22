package com.devtech.ivn.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devtech.ivn.Model.Agenda;
import com.devtech.ivn.R;

import java.util.ArrayList;

public class AdapterAgenda extends RecyclerView.Adapter<AdapterAgenda.MyViewHolder> {

    private Context context;
    private ArrayList<Agenda> agendas;

    public AdapterAgenda(Context context, ArrayList<Agenda> agendas) {
        this.context = context;
        this.agendas = agendas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_agenda, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Agenda a = agendas.get(position);
        try {
            holder.tvTitulo.setText(a.getTitulo());
            holder.tvDescricao.setText(a.getDescricao());
            holder.tvData.setText(a.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (a.getTipo().equals("cafe")) {
                holder.imLogo.setImageResource(R.drawable.ic_local_cafe_black_24dp);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#1565C0"));
            } else if (a.getTipo().equals("cha")) {
                holder.imLogo.setImageResource(R.drawable.ic_local_florist_black_24dp);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#F06292"));
            } else if (a.getTipo().equals("aniversario")) {
                holder.imLogo.setImageResource(R.drawable.ic_cake_black_24dp);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#43A047"));
            } else if (a.getTipo().equals("jantar")) {
                holder.imLogo.setImageResource(R.drawable.ic_room_service_black_24dp);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#6D4C41"));
            } else if (a.getTipo().equals("atitude")) {
                holder.imLogo.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#ffeb3b"));
            } else if (a.getTipo().equals("cantina")) {
                holder.imLogo.setImageResource(R.drawable.ic_restaurant_black_24dp);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#D61C1C"));
            } else if (a.getTipo().equals("jvn")) {
                holder.imLogo.setImageResource(R.drawable.jvn_e);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#000000"));
            } else {
                holder.imLogo.setImageResource(R.drawable.ivn_e);
                holder.cabecalho.setBackgroundColor(Color.parseColor("#616161"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            holder.cvAgenda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, a.getTitulo(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return agendas.size();
    }

    public void Filtro(ArrayList<Agenda> listaItens) {
        agendas = new ArrayList<>();
        agendas.addAll(listaItens);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cvAgenda;
        private ConstraintLayout cabecalho;
        private ImageView imLogo;
        private TextView tvTitulo, tvDescricao, tvData;

        public MyViewHolder(View itemView) {
            super(itemView);
            cvAgenda = itemView.findViewById(R.id.cv_item_agenda);
            cabecalho = itemView.findViewById(R.id.cabecalho_agenda);
            imLogo = itemView.findViewById(R.id.im_logo_agenda);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_agenda);
            tvDescricao = itemView.findViewById(R.id.tv_descricao_agenda);
            tvData = itemView.findViewById(R.id.tv_data_agenda);
        }
    }
}
