package com.devtech.ivn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devtech.ivn.Activitys.PlayerVideos;
import com.devtech.ivn.Model.Video;
import com.devtech.ivn.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterVideos extends RecyclerView.Adapter<AdapterVideos.MyViewHolder> {

    private Context context;
    private ArrayList<Video> videos;

    public AdapterVideos(Context context, ArrayList<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Video vd = videos.get(position);
        holder.tvTitulo.setText(vd.getTitulo());
        holder.tvCopy.setText(vd.getCopyright());
        Picasso.with(context).load(vd.getImagem()).fit().centerCrop().into(holder.imVideo);
        holder.cvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, PlayerVideos.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("url", vd.getLink());
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCopy, tvTitulo;
        private ImageView imVideo;
        private CardView cvVideo;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCopy = itemView.findViewById(R.id.tv_copy_video);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_video);
            imVideo = itemView.findViewById(R.id.im_video);
            cvVideo = itemView.findViewById(R.id.cv_video);
        }
    }
}
