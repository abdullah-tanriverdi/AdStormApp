package com.tanriverdi.adstormeterna.chrome.action;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//Tamamlandı

public class OpenCloseChrome {

    private static final String TAG = "OpenCloseChrome";
    private Context context;

    public OpenCloseChrome(Context context) {
        this.context = context;
    }

    // Chrome uygulamasını açma
    public boolean openChrome() {
        try {
            String command = "am start -n com.android.chrome/com.google.android.apps.chrome.Main";
            runCommand(command);
            Log.d(TAG, "Chrome açma komutu çalıştırıldı" );
        } catch (Exception e) {
            Log.e(TAG, "Chrome açma komutunu çalıştırırken hata oluştu: " + e.getMessage(), e);
        }
        return true;
    }

    // Chrome uygulamasını kapama
    public boolean closeChrome() {
        try {
            String command = "am force-stop com.android.chrome";
            runCommand(command);
            Log.d(TAG, "Chrome kapama komutu çalıştırıldı" );
        } catch (Exception e) {
            Log.e(TAG, "Chrome kapama komutunu çalıştırırken hata oluştu: " + e.getMessage(), e);
        }
        return true;
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
