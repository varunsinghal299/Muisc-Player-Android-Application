package com.example.music_try1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null){
            String toastMessage = "intent Action is not known";
            switch (intentAction) {
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMessage = "Power is Disconnected!";
                    break;

                case Intent.ACTION_POWER_CONNECTED:
                    toastMessage = "Power ic Connected!";
                    break;

                case Intent.ACTION_BATTERY_LOW:
                    toastMessage = "BATTERY LOW!";
                    break;

                case Intent.ACTION_BATTERY_OKAY:
                    toastMessage = "BATTERY OK!";
                    break;

                case Intent.ACTION_BATTERY_CHANGED:
                    toastMessage = "BATTERY CHANGED!";

            }
            Toast.makeText(context, toastMessage,Toast.LENGTH_SHORT).show();
        }
    }
}
