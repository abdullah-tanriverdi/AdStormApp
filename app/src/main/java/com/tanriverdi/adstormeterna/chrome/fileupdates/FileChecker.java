package com.tanriverdi.adstormeterna.chrome.fileupdates;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//Tamamlandı

public class FileChecker {

    private Context context ;
    private static final String TAG ="FileChecker";

    public FileChecker(Context context){
        this.context = context;
    }


    // Dosya varlığını kontrol eden metot
    public  boolean fileChecker() throws Exception {
        // Dosya yolunu burada tanımlıyoruz
        String filePath = "/data/data/com.android.chrome/app_chrome/Default/Preferences";

        // Komutu oluşturuyoruz
        String command = String.format("[ -e %s ] && echo 1 || echo 0", filePath);

        // Komutu çalıştırıyoruz
        Process process = Runtime.getRuntime().exec(new String[] { "su", "-c", command });

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String result = reader.readLine();
            Log.d(TAG, "Komut çıktısı: " + result);
            return "1".equals(result);
        } catch (Exception e) {
            Log.e(TAG, "Komut çıktısını okurken hata oluştu", e);
            throw e;
        }
    }
}
