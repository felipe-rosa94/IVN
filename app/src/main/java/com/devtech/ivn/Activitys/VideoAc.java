package com.devtech.ivn.Activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.devtech.ivn.Adapter.AdapterVideos;
import com.devtech.ivn.Model.Video;
import com.devtech.ivn.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoAc extends AppCompatActivity {

    private RecyclerView rvVideo;
    private ArrayList<Video> videos;
    private AdapterVideos adapterVideos;
    private ProgressBar progressBar;

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mVideos = mDatabase.child("Videos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        iniciar();
        getVideo();
    }

    private void iniciar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rvVideo = findViewById(R.id.rv_video);
        rvVideo.setLayoutManager(new LinearLayoutManager(this));
        videos = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar_video);
    }

    public void getVideo() {
        mVideos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videos = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Video v = dataSnapshot1.getValue(Video.class);
                        videos.add(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapterVideos = new AdapterVideos(getBaseContext(), videos);
                rvVideo.setAdapter(adapterVideos);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
