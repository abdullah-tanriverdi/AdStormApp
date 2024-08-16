package com.tanriverdi.adstormeterna.swipeactions;

import android.content.Context;
import android.util.Log;


//Tamamlandı

public class NotificationManager {

    Context context ;
    private static final String TAG = "NotificationManager";


    //Constructor
    public NotificationManager (Context context){
        this.context=context;
    }



    //Bildirim çubuğunu açan method
    public void openNotifactionBar(){
        try {

            Log.i(TAG,"Bildirim çubuğu açılıyor");

            // root erişimi sağlayarak su komutu ile ekranın yukarında aşağıya kaydırılması
            Process process = Runtime .getRuntime().exec(new String []{
                    "su", "-c", "input swipe 500 0 500 500"
            });

            //işlemin tamamlanması bekler
            process.waitFor();

            Log.d(TAG , "Bildirim çubuğu açıldı");

        }catch (Exception e){
            //hata oluşması durumunda
            Log.e(TAG, "Bildirim çubuğunu açarken hata oluştu" , e);
            e.printStackTrace();
        }
    }


    // Bildirim çubuğunu kapatan method
    public void closeNotificationBar(){
        try {

            Log.i(TAG, "Bildirm çubuğu kapatılıyor");

            Process process= Runtime.getRuntime().exec(new String []{
                    "su", "-c", "input swipe 500 500 500 0"
            });

            process.waitFor();

            Log.d(TAG, "Bildirim çubuğu kapatıldı");

        }catch (Exception e){

            Log.e(TAG, "Bildirim çubuğu kapatılırken hata oluştu", e);
            e.printStackTrace();
        }
    }

}
