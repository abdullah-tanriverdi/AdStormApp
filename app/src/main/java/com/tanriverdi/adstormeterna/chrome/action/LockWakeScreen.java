package com.tanriverdi.adstormeterna.chrome.action;

import android.content.Context;
import android.util.Log;
import com.tanriverdi.adstormeterna.swipeactions.ScrollingOperations;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//Tamamlandı

public class LockWakeScreen {

    private static final String TAG = "LockWakeScreen";
    private Context context;

    public LockWakeScreen(Context context){
        this.context = context;
    }



    // Ekranı açma ve ana menüye kaydırma
    public void wakeUpScreenAndGoHome() {
        ScrollingOperations scrollingOperations= new ScrollingOperations(context);
        try {
            // Ekranı açma
            String wakeUpCommand = "input keyevent KEYCODE_WAKEUP";
            runCommand(wakeUpCommand);
            Log.d(TAG, "Ekranı açma komutu çalıştırıldı" );

            // Ekranın açılmasını bekle
            Thread.sleep(2000); // 2 saniye

            // Ana menüye kaydırma
            scrollingOperations.swipeUpParametres(1);
            Log.d(TAG, "Ana menüye kaydırma komutu çalıştırıldı" );

        } catch (Exception e) {
            Log.e(TAG, "Ekranı açma veya ana menüye kaydırma komutunu çalıştırırken hata oluştu: " + e.getMessage(), e);
        }
    }

    // Ekranı kapama
    public void lockScreen() {
        try {
            //Ekranı kapama
            String command = "input keyevent KEYCODE_SLEEP";
             runCommand(command);
            Log.d(TAG, "Ekranı kapama komutu çalıştırıldı");
        } catch (Exception e) {
            Log.e(TAG, "Ekranı kapama komutunu çalıştırırken hata oluştu: " + e.getMessage(), e);
        }
    }

    // Root yetkisi ile komut çalıştırma
    private static String runCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        process.waitFor();
        return output.toString().trim();
    }
}
