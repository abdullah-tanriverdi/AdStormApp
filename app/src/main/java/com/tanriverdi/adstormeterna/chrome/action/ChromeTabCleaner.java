package com.tanriverdi.adstormeterna.chrome.action;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//Tamamlandı

public class ChromeTabCleaner {


    private Context context ;
    private static final String TAG = "ChromeTabsCleaner";
    private static final String COMMAND = "rm -rf /data/data/com.android.chrome/app_tabs/*";  // Chrome sekmelerini temizleyen komut

    public ChromeTabCleaner(Context context){
        this.context = context;
    }

    // Chrome sekmelerini temizleyen komutu çalıştıran metod
    public void clearChromeTabs() {
        Log.i(TAG, "Chrome sekme temizleme işlemi başlatıldı");
        try {
            // Çalıştırılacak komutu hazırlama
            String[] cmd = { "su", "-c", COMMAND };

            // Komutu çalıştırma
            Process process = Runtime.getRuntime().exec(cmd);

            // Komutun çıktısını okuma
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);  // Çıktıyı logcat'te bilgi olarak yazma
            }

            // Prosesin bitmesini bekleme
            process.waitFor();

            // Çıkış durumunu kontrol etme
            if (process.exitValue() == 0) {
                Log.i(TAG, "Chrome sekmeleri başarıyla temizlendi");  // Başarılı işlem mesajı
            } else {
                Log.e(TAG, "Chrome sekmeleri temizlenirken hata oluştu: " + process.exitValue());  // Hata mesajı
            }
        } catch (Exception e) {
            Log.e(TAG, "Chrome sekmeleri temizlenirken hata oluştu", e);  // Hata logu
        }
    }
}
