package com.tanriverdi.adstormeterna.chrome.changer;


import android.content.Context;
import android.util.Log;
import com.tanriverdi.adstormeterna.chrome.action.ConnectionManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

//Tamamlandı

public class WifiMacChanger {
    private static final String TAG = "WifiMacChanger";
    private Context context;

    public WifiMacChanger(Context context){
        this.context = context;
    }



    // Yeni MAC adresi oluşturma
    private static String generateMac() {
        Random random = new Random();
        return String.format("02:%02X:%02X:%02X:%02X:%02X",
                random.nextInt(256), random.nextInt(256), random.nextInt(256),
                random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // Wi-Fi MAC adresini değiştirme işlemi
    public static void resetMacAddress(Context context, String ssid, String password) {
        ConnectionManager connectionManager = new ConnectionManager(context);
        try {
            // 1. Mevcut Wi-Fi bilgilerini kontrol et
            Log.d(TAG, "Mevcut Wi-Fi bilgileri:");
            String checkCommand = "ip link show wlan0";
            Log.d(TAG, runCommand(checkCommand));

               // 2. Wi-Fi adaptörünü kapat
              //2.1 yöntem
             //connectionManager.turnOffWiFi();
            // 2.2 yöntem
            String downCommand = "ip link set wlan0 down";
            runCommand(downCommand);
            Log.d(TAG, "Wi-Fi kapatıldı, MAC adresi değiştiriliyor");

            Thread.sleep(2000); // Wi-Fi adaptörünün kapanmasını bekle

            //3. Uçak modunu aç
            //3.1 yöntem
            //connectionManager.turnOnAirplaneMode();
            //3.2 yöntem
            String airplaneModeOnCommand = "settings put global airplane_mode_on 1";
            runCommand(airplaneModeOnCommand);
            String airplaneModeToggleCommand = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true";
            runCommand(airplaneModeToggleCommand);
            Log.d(TAG, "Uçak modu açıldı");
            Thread.sleep(2000);

            // 4. Yeni MAC adresini oluştur
            String newMacAddress = generateMac();
            Log.d(TAG, "Yeni MAC adresi: " + newMacAddress);

            // 5. Yeni MAC adresini uygula
            String setMacCommand = "ip link set wlan0 address " + newMacAddress;
            runCommand(setMacCommand);
            Log.d(TAG, "Wi-Fi MAC Adresi değiştirildi");

            Thread.sleep(3000); // MAC adresinin uygulanmasını bekle

            // 6. Wi-Fi adaptörünü tekrar aç
            //6.1 yöntem
            //connectionManager.turnOnWifi();
            //6.2 yöntem
            String upCommand = "ip link set wlan0 up";
            runCommand(upCommand);
            Log.d(TAG, "Wi-Fi ağı tekrar çalıştırıldı");

            Thread.sleep(2000); // Wi-Fi adaptörünün açılmasını bekle

            //7. Uçak modunu kapat
            //7.1 yöntem
            connectionManager.turnOffAirplaneMode();
            //7.2 yöntem
            String airplaneModeOffCommand = "settings put global airplane_mode_on 0";
            runCommand(airplaneModeOffCommand);
            String airplaneModeToggleOffCommand = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false";
            runCommand(airplaneModeToggleOffCommand);
            Log.d(TAG, "Uçak modu kapatıldı");
            Thread.sleep(2000); //Uçak modun kapatılmasını bekle


            // 8. Yeni MAC adresini doğrula
            String checkMacCommand = "cat /sys/class/net/wlan0/address";
            String currentMacAddress = runCommand(checkMacCommand);
            Log.d(TAG, "Kontrol edilen MAC adresi: " + currentMacAddress);

            // MAC adresinin doğru olup olmadığını kontrol et
            if (currentMacAddress.equalsIgnoreCase(newMacAddress)) {
                Log.d(TAG, "MAC adresi başarıyla değiştirildi: " + currentMacAddress);
            } else {
                Log.d(TAG, "MAC adresi değiştirilemedi. Yeni adres: " + currentMacAddress);
            }


        } catch (Exception e) {
            Log.e(TAG, "Hata oluştu: " + e.getMessage(), e);
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
