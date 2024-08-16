package com.tanriverdi.adstormeterna.backgroundservice;

import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;

//Tamamlandı

public class SocketManager {

    private static final String TAG = "SocketManager";
    private static final String SOCKET_URL = "http://192.168.1.85:1100"; //socket sunucu url
    private Socket mSocket;

    public SocketManager() {
        initializeSocket(); // Socket'i başlatır
        Log.d(TAG, "SocketManager oluşturuldu ve socket başlatıldı");
    }

    private void initializeSocket() {
        try {
            // Socket nesnesini belirlenen URL ile başlatır
            mSocket = IO.socket(SOCKET_URL);
            Log.d(TAG, "Socket başarılı bir şekilde başlatıldı");
        } catch (Exception e) {
            Log.e(TAG, "Socket başlatma hatası", e);
        }
    }

    public void connect() {
        if (mSocket != null) {
            mSocket.connect();
            Log.d(TAG, "Socket bağlantısı başlatıldı");
        }else {
            Log.e(TAG, "Socket nesnesi null, bağlantı başlatılamadı");
        }
    }

    public void disconnect() {
        if (mSocket != null) {
            if (mSocket.connected()) {
                mSocket.disconnect(); // Socket bağlantısını keser
                Log.d(TAG, "Socket bağlantısı kesildi");
            } else {
                Log.d(TAG, "Socket zaten bağlı değil");
            }
            mSocket.off(); // Socket üzerindeki olay dinleyicilerini temizler
            Log.d(TAG, "Socket olay dinleyicileri temizlendi");
        } else {
            Log.e(TAG, "Socket nesnesi null, bağlantı kesilemedi");
        }
    }

    public Socket getSocket() {
        return mSocket; //socket nesnesi
    }
}
