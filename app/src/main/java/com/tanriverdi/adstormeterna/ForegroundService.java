package com.tanriverdi.adstormeterna;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class ForegroundService extends Service { //Foreground sınıfı Service sınıfından türetildi

    private static final String CHANNEL_ID = "SimpleServiceChannel"; //Bildirim kanalı için sabit ID

    private Handler handler; // Handler nesnesi
    private Runnable runnable; // Runnable nesnesi

    public void onCreate(){ //Servis oluşturulduğunda çağrılır
        super.onCreate(); //Üst sınıfın onCreate metodunu çağırır

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //Oreo ve üstü sürümler için
            createNotificationChannel();// Bildirim kanalını oluşturur
        }

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID) //Bildirimi oluşturur
                .setContentTitle("Background Service") // Bildirim başlığını ayarlar
                .setContentText("Service is running in the background") //Bildirim içeriğini ayarlar
                .setSmallIcon(R.drawable.baseline_accessibility_24) //Bildirim ikonunu ayarlar
                .build(); // Bildirimi oluşturur

        startForeground(1,notification); //Servisi ön planda başlat ve bildirimi göster

        handler = new Handler(); // Yeni bir handler nesnesi oluşturur
        runnable = new Runnable() { //Yeni bir runnable nesnesi oluşturur
            @Override
            public void run() { //Runnable içeriği
                Toast.makeText(ForegroundService.this,"Servise is running...",Toast.LENGTH_SHORT).show(); //Her çalıştırıldığında toast mesajı gönderir
                handler.postDelayed(this,10000); // 10 saniye sonra tekrar çalıştır

            }
        };
        handler.post(runnable); //Runnable' ı başlatır

    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId){ //Servis başlatıldığında çağrılır
        return START_STICKY; //Servisin yeniden başlatılması durumunda bile çalışmaya devam edilmesini sağlar
    }

    @Override
    public void onDestroy(){ //Servis durdurulduğunda çağrılır
        super.onDestroy(); // Üst sınıfın OnDestroy yöntemini çağırır
        handler.removeCallbacks(runnable); // Runnable' nın tekrar çalışmasını engeller

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) { //Bound service kullanmak için gerekli method
        return null; //Kullanmayacağımız için null döndürür
    }

    private void createNotificationChannel(){ //Bildirim kanalı oluşturma metodu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //// Android Oreo ve üstü sürümler için
            NotificationChannel serviceChannel = new NotificationChannel( //NotifivationChannel nesnesi oluşturur
                    CHANNEL_ID, //Kanalın ID'si
                    "Simple Background Service Channel", //Kanal adı
                    NotificationManager.IMPORTANCE_DEFAULT //Kanalın önemi
            );

            NotificationManager manager = getSystemService(NotificationManager.class); //NotificationManager sınıfından nesne oluşturluyor.
            if (manager!=null){
                manager.createNotificationChannel(serviceChannel); //Bildirim kanalı sisteme kaydeder
            }

        }


    }
}
