package com.vch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

public class NotifyReceiver extends BroadcastReceiver {
    private static final String TAG = NotifyReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
            // gcm successfully registered
            // now subscribe to `global` topic to receive app wide notifications
            FirebaseMessaging.getInstance().subscribeToTopic(Constant.TOPIC_GLOBAL);

            displayFireBaseRegId();

        } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
            // new push notification is received

            String message = intent.getStringExtra("message");

            AppConfig.showToast("Push notification: ");
        }
    }

    private void displayFireBaseRegId() {
        String regId = App.RegPref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
    }
}
