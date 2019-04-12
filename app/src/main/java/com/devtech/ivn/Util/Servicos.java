package com.devtech.ivn.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.devtech.ivn.Activitys.PergunteAc;
import com.devtech.ivn.Bancos.DBConfig;
import com.devtech.ivn.Model.Notificacao;
import com.devtech.ivn.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Servicos extends Service {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPush = mDatabase.child("Notificacao");
    private DBConfig dbConfig;
    public static boolean ENVIA = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbConfig = new DBConfig(getBaseContext());
        startForeground();
        getPerguntas();
        getNotifica();
    }

    private void startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "com.devtech.ivn";
            String channelName = "Rodando em modo serviço";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("")
                    .setPriority(NotificationManager.IMPORTANCE_NONE)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);
        }
    }

    private void getPerguntas() {
        dbConfig.open();
        DatabaseReference mPergunte = mDatabase.child("Pergunte/" + dbConfig.Fields.IdPg);
        dbConfig.close();
        mPergunte.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (ENVIA) {
                        notificar();
                    }
                }
                ENVIA = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void notificar() {
        Intent it = new Intent(getBaseContext(), PergunteAc.class);
        PendingIntent p = PendingIntent.getActivity(getBaseContext(), 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "1");
        Notification n = null;

        NotificationManager nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n1 = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "ADMIN";
            String desc = "notification";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            final String ChannelID = "channel01";
            NotificationChannel mChannel = new NotificationChannel(ChannelID, name, imp);
            mChannel.setDescription(desc);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            nm.createNotificationChannel(mChannel);

            n1 = new Notification.Builder(getBaseContext(), ChannelID)
                    .setContentTitle("Pergunte aos Pastores")
                    .setContentText("Existe uma nova resposta")
                    .setBadgeIconType(R.mipmap.ic_launcher)
                    .setNumber(5)
                    .setSmallIcon(R.drawable.bt05_a)
                    .setAutoCancel(true)
                    .setContentIntent(p)
                    .build();
            nm.notify(101, n1);
        } else {
            builder.setTicker("tiker");
            builder.setContentTitle("Pergunte aos Pastores");
            builder.setContentText("Existe uma nova resposta");
            builder.setSmallIcon(R.drawable.ic_comment_black_24dp);
            builder.setAutoCancel(true);
            builder.setGroup("");
            builder.setContentIntent(p);
            builder.setChannelId("1");
            n = builder.build();
            n.flags |= Notification.FLAG_AUTO_CANCEL;
            nm.notify(1, n);
        }
    }

    private void notificacao(String titulo, String msg) {
        Intent it = new Intent(getBaseContext(), PergunteAc.class);
        PendingIntent p = PendingIntent.getActivity(getBaseContext(), 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "1");
        Notification n = null;

        NotificationManager nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n1 = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "ADMIN";
            String desc = "notification";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            final String ChannelID = "channel01";
            NotificationChannel mChannel = new NotificationChannel(ChannelID, name, imp);
            mChannel.setDescription(desc);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            nm.createNotificationChannel(mChannel);

            n1 = new Notification.Builder(getBaseContext(), ChannelID)
                    .setContentTitle(titulo)
                    .setContentText(msg)
                    .setBadgeIconType(R.mipmap.ic_launcher)
                    .setNumber(5)
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentIntent(p)
                    .build();
            nm.notify(101, n1);
        } else {
            builder.setTicker("tiker");
            builder.setContentTitle(titulo);
            builder.setContentText(msg);
            builder.setSmallIcon(R.drawable.logo);
            builder.setAutoCancel(true);
            builder.setGroup("");
            builder.setContentIntent(p);
            builder.setChannelId("1");
            n = builder.build();
            n.flags |= Notification.FLAG_AUTO_CANCEL;
            nm.notify(1, n);
        }
    }

    private void getNotifica() {
        mPush.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        Notificacao n = dataSnapshot1.getValue(Notificacao.class);
                        if (n.isAtivo()) {
                            notificacao(n.getTitulo(), n.getDescricao());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
