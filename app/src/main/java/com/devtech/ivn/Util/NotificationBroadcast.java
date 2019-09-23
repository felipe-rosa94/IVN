package com.devtech.ivn.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.devtech.ivn.Activitys.NewPlayer;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String message = intent.getAction();
            if (message.equals("com.devtech.ivn.Activitys.previous")) {
                NewPlayer.prevoius();
            } else if (message.equals("com.devtech.ivn.Activitys.pause")) {
                NewPlayer.pause();
            } else if (message.equals("com.devtech.ivn.Activitys.next")) {
                NewPlayer.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
