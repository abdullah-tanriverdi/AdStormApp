package com.tanriverdi.adstormeterna.todo.c;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

public class CopyFile {

    private static final String TAG = "CopyFile";
    private Context context;

    public CopyFile(Context context) {
        this.context = context;
    }

    public boolean copyPreferencesFile() {
        // Kaynak ve hedef dosya yolları
        String sourceFilePath = "/data/data/com.android.chrome/app_chrome/Default/Preferences";
        String destinationDirPath = "/storage/emulated/0/AdStormEterna/";
        String destinationFilePath = destinationDirPath + "Preferences";

        // Hedef dizinin varlığını kontrol et
        File destinationDir = new File(destinationDirPath);
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                String errorMessage = "Dizin oluşturulamadı: " + destinationDirPath;
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, errorMessage);
                return false;
            }
        }

        // Root erişimi kullanarak dosyayı kopyala
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();

            // Dosya kopyalama komutlarını çalıştır
            String command = "cp " + sourceFilePath + " " + destinationFilePath + "\n";
            outputStream.write(command.getBytes());
            outputStream.flush();
            outputStream.close();

            // Komutun tamamlanmasını bekle
            process.waitFor();
            process.destroy();

            String successMessage = "Dosya başarıyla kopyalandı.";
            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show();
            Log.i(TAG, successMessage);
        } catch (IOException | InterruptedException e) {
            String errorMessage = "Dosya kopyalama hatası: " + e.getMessage();
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMessage, e);
        }
        return true;
    }
}
