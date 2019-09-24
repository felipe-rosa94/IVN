package com.devtech.ivn.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devtech.ivn.Activitys.MusicaAc;
import com.devtech.ivn.Model.Album;
import com.devtech.ivn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.MyViewHolder> {

    private Context context;
    private ArrayList<Album> albums;

    public AdapterAlbum(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_album, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Album a = albums.get(position);

        holder.tvNome.setText(a.getNome());

        try {
            if (!a.getCapa().equals("")){
                Picasso.with(context).load(a.getCapa()).into(holder.imCapa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.cvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicaAc.filtrarMusicas(a.getNome());
                MusicaAc.FILTRANDO = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cvAlbum;
        private TextView tvNome;
        private ImageView imCapa;

        public MyViewHolder(View itemView) {
            super(itemView);

            imCapa = itemView.findViewById(R.id.im_capa_album);
            tvNome = itemView.findViewById(R.id.tv_nome_album);
            cvAlbum = itemView.findViewById(R.id.cv_album);
        }
    }
}
