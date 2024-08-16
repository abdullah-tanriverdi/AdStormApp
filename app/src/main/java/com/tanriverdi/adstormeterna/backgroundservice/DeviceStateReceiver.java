package com.tanriverdi.adstormeterna.backgroundservice;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.tanriverdi.adstormeterna.ForegroundService;

//Tamamlandı

public class DeviceStateReceiver extends BroadcastReceiver {
    //BroadcastReceiver sınıfından türetildi.
    private static final String TAG = "DeviceStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Broadcast mesajı alındığında çağrılır.


        String action = intent.getAction(); //Broadcasst mesajının aksiyonunu alır


        // Aksiyon türüne göre işlemler yapılır
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {

            //Cihazın önyüklemesi tamamlandığında
            Log.d(TAG, "Boot completed tamamlandı olayı alındı");

            startSimpleService(context);// Background servisini başlatır.

        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            // Ekran açıldığında
            Log.d(TAG, "Ekran açıldı olayı alındı");

            startSimpleService(context);

        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            // Ekran kapandığında
            Log.d(TAG, "Ekran kapandı olayı alındı");
        }
    }

    //Arka plan servisi başlatma methodu.
    private void startSimpleService(Context context) {

        Intent serviceIntent = new Intent(context, ForegroundService.class); // Background servisini başlatmak için kullanılan Intent.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Oreo ve üstü sürümler için
            Log.d(TAG, "Background servisi başlatılıyor");
            context.startForegroundService(serviceIntent); //Foreground service başlatır
        } else { //Daha eski sürümler için
            Log.d(TAG, "Background servisi başlatılıyor");
            context.startService(serviceIntent); //Normal servis başlatılır.
        }

    }
}
