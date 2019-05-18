package com.devtech.ivn.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.devtech.ivn.Activitys.Player.playPause;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            playPause();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
