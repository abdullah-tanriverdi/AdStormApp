package com.tanriverdi.adstormeterna.swipeactions;

import android.content.Context;
import android.util.Log;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Tamamlandı
public class ScrollingOperations {
    Context context;
    Random random ;
    ExecutorService executorService;   // Arka planda işlemler için ExecutorService nesnesi
    private static  final String TAG = "ScrollingOperations";

    public  ScrollingOperations(Context context){
        this.context = context;
        this.random = new Random();
        this.executorService = Executors.newSingleThreadExecutor(); // tek bir thread kullanacak olan nesnenin başlatılması
    }

    //Rastgele bir gecikme süresi döndüren method
    private int getRandomDelay(){
        int delay=  250 + random.nextInt(500); //.25 sn ile .75 sn arası random gecikme süresi
        Log.d(TAG,"Oluşan gecikme süresi: "+delay);

        return  delay;
    }



    //Tekil kaydırma işlemleri

    public  void swipeRight(){
        try {
            Log.i(TAG, "Sağa kaydırma işlemi başlatıldı");

            Process process =Runtime.getRuntime().exec(new String[] {
                    "su", "-c" ,"input swipe 0 500 1000 500"
            });
            process.waitFor();
            Log.d(TAG,"Sağa kaydırma işlemi başarılı");
        }catch (Exception e){
            Log.e(TAG,"Sağa kaydırılırken hata oluştu", e);
            e.printStackTrace();
        }
    }


    public  void swipeLeft(){

        try {
            Log.i(TAG, "Sola kaydırma işlemi başlatıldı");
            Process process = Runtime.getRuntime().exec(new String[]{
                    "su", "-c", "input swipe 1000 500  0 500"
            });
            process.waitFor();
            Log.d(TAG,"Sola kaydırma işlemi başarılı");
        }catch (Exception e){
            Log.e(TAG,"Sola kaydırılırken hata oluştu", e);
            e.printStackTrace();
        }
    }



    public void swipeUp(){
        try {
            Log.i(TAG ,"Yukarı kaydırma işlemi başlatıldı");
            Process process = Runtime.getRuntime().exec(new String[]{
                    "su", "-c", "input swipe 500 1000 500 0"
            });
            process.waitFor();
            Log.d(TAG, "Yukarı kaydırma işlemi başarılı");
        }catch (Exception e ){
            Log.e(TAG, "Yukarı kaydırılırken hata oluştu", e);
            e.printStackTrace();
        }
    }



    public  void swipeDown(){
        try {
            Log.i(TAG, "Aşağıya kaydırma işlemi başlatıldı");
            Process process =  Runtime.getRuntime().exec(new String []{
                    "su", "-c", "input swipe 500 0 500 1000"
            });
            process.waitFor();
            Log.d(TAG, "Aşağıya kaydırma işlemi başarılı");

        }catch (Exception e){
            Log.e(TAG, "Aşağıya kaydırılırken hata oluştu", e);
            e.printStackTrace();
        }
    }



    //Parametreli kaydırma işlemleri

    public  void  swipeRightParametres ( int n ){
        executorService.execute(new Runnable() { //yeni asenkron işlemi başlatma
            @Override
            public void run() {
                try {
                    for (int i = 0 ; i<n; i++){ //n kere

                        //İterasyon logu
                        Log.i(TAG , "Sağa kaydırma işlemi" + (i+1)+ "/"+n);
                        Process process = Runtime.getRuntime().exec(new String[]{
                                "su" , "-c" , " input swipe 0 500 1000 500"
                        });
                        process.waitFor();
                        Thread.sleep(getRandomDelay()); //Rastgele gecikme süresi kadar bekleme
                    }
                    Log.d(TAG , n + "kere sağa kaydırma işlemi başarılı");
                }catch (Exception e ){
                    Log.e(TAG, n + "kere sağa kaydırılırken hata oluştu", e);
                    e.printStackTrace();
                }
            }
        });

    }


    public  void  swipeLeftParametres( int n ){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int  i = 0 ; i< n ; i++){
                        Log.i(TAG , "Sola kaydırma işlemi" + (i+1)+ "/"+n);
                        Process process  = Runtime.getRuntime().exec(new String []{
                                "su" , "-c" , "input swipe 1000 500 0 500"
                        });
                        process.waitFor();
                        Thread.sleep(getRandomDelay());
                    }
                    Log.d(TAG , n + "kere sola kaydırma işlemi başarılı");
                }catch (Exception e){
                    Log.e(TAG, n + "kere sola kaydırılırken hata oluştu", e);
                    e.printStackTrace();

                }
            }
        });

    }


    public void swipeUpParametres(int n ){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i =0 ; i< n;  i++){
                        Log.i(TAG , "Yukarı  kaydırma işlemi" + (i+1)+ "/"+n);
                        Process process =Runtime.getRuntime().exec(new String[]{
                                "su", "-c", "input swipe 500 1000 500 0"
                        });
                        process.waitFor();
                        Thread.sleep(getRandomDelay());
                        Log.d(TAG , n + "kere yukarı  kaydırma işlemi başarılı");
                    }
                }catch (Exception e){
                    Log.e(TAG, n + "kere yukarı kaydırılırken hata oluştu", e);
                    e.printStackTrace();
                }
            }
        });

    }


      public void  swipeDownParametres(int n){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0 ; i < n ; i++){
                        Log.i(TAG , "Aşağı  kaydırma işlemi" + (i+1)+ "/"+n);
                        Process process = Runtime.getRuntime().exec(new  String[] {
                                "su" , "-c" , "input swipe 500 0 500 1000"
                        });
                        process.waitFor();
                        Thread.sleep(getRandomDelay());
                    }
                    Log.d(TAG , n + "kere aşağı  kaydırma işlemi başarılı");
                }catch (Exception e){
                    Log.e(TAG, n + "kere aşağı kaydırılırken hata oluştu", e);
                    e.printStackTrace();
                }
            }
        });

      }
}
