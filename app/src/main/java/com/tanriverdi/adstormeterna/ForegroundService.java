package com.tanriverdi.adstormeterna;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "SimpleServiceChannel";
    private Handler handler;
    private Runnable runnable;
    private Socket mSocket;
    private boolean isConnected = false;

    {
        try {
            // Socket.IO sunucu adresi belirlenir
            mSocket = IO.socket("https://pcwa-e079d7711976.herokuapp.com/");
        } catch (Exception e) {
            // Socket.IO bağlantısı oluşturulurken bir hata oluşursa burada yakalanır
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Android O ve üstü sürümlerde bildirim kanalını oluştur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        // Bildirim kanalı ilerleyen safhalarda ping event ile sunucuya mesaj gönderecek şekilde ayarlanacaktır.
        // Foreground servisi için bir bildirim oluşturuluyor
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Background Service")
                .setContentText("Service is running in the background")
                .setSmallIcon(R.drawable.baseline_accessibility_24) // Küçük simge (ikon)
                .build();

        // Servis foreground olarak başlatılır
        startForeground(1, notification);


        // İlerleyen dönemlerde kaldırılacaktır
        // Handler ve Runnable ile periyodik işlemler yapılır
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Sunucu bağlantısını kontrol eder
                if (isConnected) {
                    Toast.makeText(ForegroundService.this, "Connected to server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForegroundService.this, "Not connected to server", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(this, 5000); // 5 saniye sonra tekrar çalıştır
            }
        };
        handler.post(runnable);

        // Socket.IO bağlantısını başlat
        if (mSocket != null) {
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Bağlantı başarılı olduğunda tetiklenir
                    Log.d("Socket", "Connected to server");
                    isConnected = true;
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Bağlantı kesildiğinde tetiklenir
                    Log.d("Socket", "Disconnected from server");
                    isConnected = false;
                }
            }).on("your_event", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Sunucudan gelen özel veriyi işler
                    Log.d("Socket", "Received data: " + args[0]);
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Servis yeniden başlatıldığında sürekli çalışmasını sağlar
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Handler'daki runnable işlemi kaldırılır
        handler.removeCallbacks(runnable);
        // Socket.IO bağlantısı kapatılır ve olay dinleyicileri temizlenir
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
            mSocket.off(); // Olay dinleyicilerini temizle
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //Bu kısımı detaylıca araştır
        // Bu servis için binding işlemi yapılmaz
        return null;
    }

    private void createNotificationChannel() {
        // Android O ve üstü sürümlerde bildirim kanalı oluşturulması gereklidir
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Simple Background Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                // Bildirim kanalı sisteme kaydedilir
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
