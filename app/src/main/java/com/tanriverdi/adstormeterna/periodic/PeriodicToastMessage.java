package com.tanriverdi.adstormeterna.periodic;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

//Tamamlandı

public class PeriodicToastMessage {
    private Handler handler;
    private Runnable runnable;
    private boolean isConnected = false; //sunucu bağlantı durumu
    private Context context;

    private static final String TAG = "PeriodicToastMessage";

    public PeriodicToastMessage(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper()); // Main thread handler
        Log.d(TAG, "Handler oluşturuldu ve ana iş parçacığına bağlandı");
    }

    public void startPeriodicTasks() {
        // Periyodik görevleri başlatır
        runnable = new Runnable() {
            @Override
            public void run() {
                // Sunucu bağlantısını kontrol eder ve kullanıcıya bilgi verir
                if (isConnected) {
                    showToast("Connected to server");
                    Log.i(TAG, "Sunucuya bağlı");
                } else {
                    showToast("Not connected to server");
                    Log.i(TAG, "Sunucuya bağlı değil");
                }
                handler.postDelayed(this, 1000); // 1 saniye sonra tekrar çalıştır
            }
        };
        handler.post(runnable); //ilk çalıştırma
        Log.d(TAG, "Periyodik görevler başlatıldı");
    }


    // Periyodik görevleri durdurur
    public void stopPeriodicTasks() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            Log.d(TAG, "Periyodik görevler durduruldu");

        }
    }

    // Bağlantı durumunu ayarlar
    public void setConnected(boolean connected) {
        this.isConnected = connected;
        Log.d(TAG, "Bağlantı durumu ayarlandı: " + connected);
    }

    // Toast mesajı gösterir
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Toast mesajı gösterildi: " + message);
    }
}
