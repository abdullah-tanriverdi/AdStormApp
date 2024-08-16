package com.tanriverdi.adstormeterna.todo.c;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class CreateFolder {

    private static final String TAG = "FolderManager"; // Log etiketi
    private Context context;

    public CreateFolder(Context context) {
        this.context = context;
    }

    public void clearChromeData() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            String command = "pm clear com.android.chrome";
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
            process.getOutputStream().close();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                Log.i(TAG, "Chrome uygulamasının tüm verileri başarıyla silindi.");
            } else {
                Log.e(TAG, "Chrome verileri silinemedi. Çıkış kodu: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Chrome verilerini silme sırasında bir hata oluştu.", e);
        }
    }
    public void createFolder() {
        String folderPath = "/storage/emulated/0/AdStormEterna";
        File folder = new File(folderPath);

        if (folder.exists()) {
            Log.i(TAG, "Klasör zaten mevcut: " + folderPath);
            return;
        }

        try {
            Process process = Runtime.getRuntime().exec("su");
            String command = "mkdir -p " + folderPath;
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
            process.getOutputStream().close();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                Log.i(TAG, "Klasör başarıyla oluşturuldu: " + folderPath);
            } else {
                Log.e(TAG, "Klasör oluşturulamadı. Çıkış kodu: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Klasör oluşturma sırasında bir hata oluştu.", e);
        }
    }

    public void copyFileToFolder() {
       String sourcePath = "/data/data/com.android.chrome/app_chrome/Default/Preferences";
     String destinationPath = "/storage/emulated/0/AdStormEterna/Preferences";


        try {
            Process process = Runtime.getRuntime().exec("su");
            String command = "cp " + sourcePath + " " + destinationPath;
            process.getOutputStream().write((command + "\n").getBytes());
            process.getOutputStream().flush();
            process.getOutputStream().close();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                Log.i(TAG, "Dosya başarıyla kopyalandı: " + destinationPath);
            } else {
                Log.e(TAG, "Dosya kopyalanamadı. Çıkış kodu: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Dosya kopyalama sırasında bir hata oluştu.", e);
        }
    }
}
