package com.tanriverdi.adstormeterna.chrome.action;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//Tamamlandı

public class HomeBack {

    private Context context;
    private static final String TAG = "HomeBack";

    public HomeBack(Context context) {
        this.context = context;
    }

    // Ana ekrana gitme komutunu çalıştıran metod
    public void goToHome() {
        Log.i(TAG, "Ana ekrana gitme işlemi başlatılıyor");
        executeRootCommand("input keyevent KEYCODE_HOME");
        Log.d(TAG, "Ana ekrana gitme işlemi tamamlandı");
    }

    // Geri tuşuna basma komutunu çalıştıran metod
    public void goBack() {
        Log.i(TAG, "Geri tuşuna basma işlemi başlatılıyor");
        try {
            executeRootCommand("input keyevent KEYCODE_BACK");
            Log.d(TAG, "Geri tuşu komutu başarıyla çalıştırıldı");
        } catch (Exception e) {
            Log.e(TAG, "Geri tuşu komutu başarısız oldu, alternatif komut çalıştırılıyor", e);
            // Geri tuşu komutu başarısız olursa alternatif komutu çalıştır
            executeRootCommand("input keyevent 4");
        }
        Log.d(TAG, "Geri tuşuna basma işlemi tamamlandı.");
    }

    // Root ayrıcalıklarıyla bir komutu çalıştıran yardımcı metod
    private void executeRootCommand(String command) {
        try {
            // Çalıştırılacak komutu hazırlama
            String[] cmd = { "su", "-c", command };

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
                Log.i(TAG, "Komut başarıyla çalıştırıldı: " + command);  // Başarılı işlem mesajı
            } else {
                Log.e(TAG, "Komut exit kodu ile başarısız oldu: " + process.exitValue());  // Hata mesajı
            }
        } catch (Exception e) {
            Log.e(TAG, "Komut çalıştırılırken hata oluştu: " + command, e);  // Hata logu
        }
    }
}
