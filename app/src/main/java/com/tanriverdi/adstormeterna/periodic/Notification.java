package com.tanriverdi.adstormeterna.periodic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

//Tamamlandı

public class Notification {

    private static final String CHANNEL_ID = "SimpleServiceChannel"; // Bildirim kanalı ID'si
    private static final String TAG = "Notification";
    private Context context;

    public Notification(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();  // Bildirim kanalı oluşturur
        }
    }

    public android.app.Notification createNotification(String title, String text, int iconResId) {
        //Bildirim oluşturma metodu

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title) // Bildirim başlığı
                .setContentText(text) // Bildirim metni
                .setSmallIcon(iconResId) // Küçük simge (ikon)
                .build();
    }

    private void createNotificationChannel() {
        // Android O (API 26) ve üstü sürümlerde bildirim kanalı oluşturulması gereklidir
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Simple Background Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                // Bildirim kanalı sisteme kaydedilir
                manager.createNotificationChannel(serviceChannel);
                Log.d(TAG, "Bildirim kanalı oluşturuldu: " + CHANNEL_ID);
            } else {
                Log.e(TAG, "Bildirim kanalı oluşturulamadı. NotificationManager alınamadı.");
            }
        } else {
            Log.d(TAG, "Android sürümü O ve üstü değil, bildirim kanalı oluşturulmadı.");
        }
    }
}
