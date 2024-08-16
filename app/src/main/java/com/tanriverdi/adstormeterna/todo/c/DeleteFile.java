package com.tanriverdi.adstormeterna.todo.c;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class DeleteFile {

    private static final String TAG = "DeleteFile";
    private Context context;

    public DeleteFile(Context context) {
        this.context = context;
    }

    public void deletePreferencesFile() {
        // Silinecek dosya yolu
        String sourceFilePath = "/data/data/com.android.chrome/app_chrome/Default/Preferences";
       // String sourceFilePath = "/storage/emulated/0/AdStormEterna/Preferences";

        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();

            // Dosya silme komutunu çalıştır
            String command = "rm " + sourceFilePath + "\n";
            outputStream.write(command.getBytes());
            outputStream.flush();
            outputStream.close();

            // Komutun tamamlanmasını bekle
            process.waitFor();
            process.destroy();

            String successMessage = "Dosya başarıyla silindi.";
            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show();
            Log.i(TAG, successMessage);
        } catch (IOException | InterruptedException e) {
            String errorMessage = "Dosya silme hatası: " + e.getMessage();
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMessage, e);
        }
    }
}
