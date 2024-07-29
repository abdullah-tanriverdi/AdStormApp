package com.tanriverdi.adstormeterna;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


public class DeviceStateReceiver extends BroadcastReceiver {
    //DeviceStateReceiver sınıfı BroadcastReceiver sınıfından türetildi.

    private static final String TAG = "DeviceStateReceiver"; //Log için sabit TAG

    @Override
    public void onReceive(Context context, Intent intent) {
        //Broadcast mesajı alındığında çağrılır.

        String action = intent.getAction(); //Broadcasst mesajının aksiyonunu alır.

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.d(TAG, "Boot completed event received"); // Log yazdırılır.
            startSimpleService(context); // Arkaplan servisini başlatır.
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            Log.d(TAG, "Screen ON event received");
            startSimpleService(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Log.d(TAG, "Screen OFF event received");
        }

    }


    private void startSimpleService(Context context) { //Arka plan servisi başlatma methodu.
        Intent serviceIntent = new Intent(context, ForegroundService.class); // Foreground servisini başlatmak için kullanılan Intent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Oreo ve üstü sürümler için
            context.startForegroundService(serviceIntent); //Foreground service başlatır
        } else { //Daha eski sürümler için
            context.startService(serviceIntent); //Normal servis başlatılır.
        }



    }
}
