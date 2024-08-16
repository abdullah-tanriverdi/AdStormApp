package com.tanriverdi.adstormeterna.chrome.action;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//Tamamlandı

public class ChromeClearCache {

    private Context context ;
    private static final String TAG = "ChromeClearCache";

    public ChromeClearCache(Context context){
        this.context = context;
    }


    //Choreme verilerini temizleme metodu
    public boolean clearChromeCache() throws Exception {
        // Temizleme komutları
        String[] commands = {
                // Yerel geçici dosyaları temizle
                "rm -rf /data/local",
                "mkdir -p /data/local/tmp",
                "chmod -R 777 /data/local",

                // İzinleri ayarla
                "chmod -R 777 /data/local",
                "chmod -R 777 /data/data/com.android.chrome/cache/Crash\\ Reports",

                // Verileri temizle
                "pm clear com.android.chrome",
                "pm disable com.android.chrome",
                "pm enable com.android.chrome",

                // Önbellek klasörünü oluştur ve izinleri ayarla
                "mkdir -p /data/data/com.android.chrome/cache/Crash\\ Reports",
                "chmod -R 777 /data/data/com.android.chrome/cache/Crash\\ Reports"
        };
        executeRootCommands(commands);
        Log.d(TAG, "Chrome verileri başarıyla temizlendi.");
        return true;
    }

    // Kök komutlarını çalıştıran yardımcı metot
    private void executeRootCommands(String[] commands) throws Exception {
        for (String command : commands) {
            ProcessBuilder processBuilder = new ProcessBuilder("su", "-c", command);
            processBuilder.redirectErrorStream(true); // Hata akışını standart akışa yönlendir
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Komut çıktısını yazdır
            }

            process.waitFor();
        }
    }
}
