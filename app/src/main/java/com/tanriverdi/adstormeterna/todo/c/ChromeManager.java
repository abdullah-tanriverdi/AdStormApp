package com.tanriverdi.adstormeterna.todo.c;

import android.content.Context;
import android.util.Log;

import com.tanriverdi.adstormeterna.chrome.action.OpenCloseChrome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChromeManager {

    private static final String TAG = "ChromeManager";
    private Context context;

    public ChromeManager(Context context) {
        this.context = context;
    }

    public void manageChrome() {
        try {
            // Chrome'u aç
            startChrome();
            Thread.sleep(10000); // 10 saniye bekle

            // Chrome'u kapat
            stopChrome();
            Thread.sleep(10000); // 10 saniye bekle

            // Dosyayı kopyala
            copyFile();
            Thread.sleep(10000); // 10 saniye bekle

            // Dosyayı sil
            deleteFile();
            Thread.sleep(10000); // 10 saniye bekle

            // Chrome verilerini sıfırla (fabrika ayarlarına döndür)
            resetChromeData();
            Thread.sleep(10000); // 10 saniye bekle

            // Chrome'u aç
            startChrome();
            Thread.sleep(10000); // 10 saniye bekle

            // Chrome'u kapat
            stopChrome();
            Thread.sleep(10000); // 10 saniye bekle

            // Kopyalanan dosyayı düzenle
            modifyCopiedFile();
            Thread.sleep(10000); // 10 saniye bekle

            // Dosyayı kopyalanan klasöre kopyala
            copyToDestination();
            Thread.sleep(10000); // 10 saniye bekle

            // Chrome'u yeniden başlat
            restartChrome();

        } catch (InterruptedException e) {
            Log.e(TAG, "Bekleme süresi sırasında hata: " + e.getMessage(), e);
        }
    }


    private void startChrome() {
        OpenCloseChrome openCloseChrome = new OpenCloseChrome(context);
        openCloseChrome.openChrome();
    }

    private void stopChrome() {
        OpenCloseChrome openCloseChrome = new OpenCloseChrome(context);
        openCloseChrome.closeChrome();
    }

    private void copyFile() {
        // Dosya kopyalama işlemini buraya ekleyin
        // Örnek: CopyFile sınıfını kullanarak dosya kopyalama işlemi gerçekleştirin
        new CopyFile(context).copyPreferencesFile();
    }

    private void deleteFile() {
        // Kaynak dosyanın yolu
      DeleteFile deleteFile =new DeleteFile(context);
      deleteFile.deletePreferencesFile();
    }

    public boolean resetChromeData() {
        try {
            Process process = Runtime.getRuntime().exec("pm clear com.android.chrome");
            process.waitFor();
            Log.i(TAG, "Chrome verileri sıfırlandı.");
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Chrome verilerini sıfırlama hatası: " + e.getMessage(), e);
        }
        return true;
    }

    private void modifyCopiedFile() {
        // Kopyalanan dosyayı JSON formatına dönüştür ve düzenle
        new ModifyJsonFile(context).modifyAndSaveJsonFile();
    }

    public boolean copyToDestination() {
        // Kopyalanan dosyayı hedef klasöre kopyala
        // Kaynak ve hedef dosya yolları
        String sourceFilePath = "/storage/emulated/0/AdStormEterna/Preferences";
        String destinationFilePath = "/data/data/com.android.chrome/app_chrome/Default/Preferences";

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        try {
            if (sourceFile.exists()) {
                try (InputStream in = new FileInputStream(sourceFile);
                     OutputStream out = new FileOutputStream(destinationFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    Log.i(TAG, "Dosya kopyalandı: " + sourceFilePath + " -> " + destinationFilePath);
                }
            } else {
                Log.e(TAG, "Kaynak dosya bulunamadı: " + sourceFilePath);
            }
        } catch (IOException e) {
            Log.e(TAG, "Dosya kopyalama hatası: " + e.getMessage(), e);
        }
        return true;
    }

    private void restartChrome() {
        // Chrome'u yeniden başlat
        stopChrome();
        startChrome();
    }
}
