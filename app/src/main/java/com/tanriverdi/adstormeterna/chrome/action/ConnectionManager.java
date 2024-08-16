package com.tanriverdi.adstormeterna.chrome.action;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;


//Tamamlandı

public class ConnectionManager {

    private static final String TAG = "ConnectionManager";
    private Context context;
    public ConnectionManager(Context context) {
        this.context = context;
    }

    // Wi-Fi'yi  açma (Root gerekli)
    public void turnOnWifi() {
        Log.i(TAG, "Wi-Fi açma işlemi başlatılıyor");
        if (isRooted()) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                outputStream.writeBytes("svc wifi enable\n");
                outputStream.flush();
                outputStream.close();
                process.waitFor();
                Log.d(TAG, "Wi-Fi açıldı");
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Wi-Fi açma başarısız", e);
            }
        } else {
            Log.e(TAG, "Cihaz root'lanmış değil, Wi-Fi'yi açamıyor");
        }
    }

    // Wi-Fi'yi kapama (Root gerekli)
    public void turnOffWiFi() {
        Log.i(TAG, "Wi-Fi kapama işlemi başlatılıyor");
        if (isRooted()) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                outputStream.writeBytes("svc wifi disable\n");
                outputStream.flush();
                outputStream.close();
                process.waitFor();
                Log.d(TAG, "Wi-Fi kapandı");
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Wi-Fi kapama başarısız", e);
            }
        } else {
            Log.e(TAG, "Cihaz root'lanmış değil, Wi-Fi'yi kapatamıyor");
        }
    }


    // Bluetooth'u açma
    @SuppressLint("MissingPermission")
    public void turnOnBluetooth() {
        Log.i(TAG, "Bluetooth açma işlemi başlatılıyor");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
                Log.d(TAG, "Bluetooth açıldı");
            } else {
                Log.d(TAG, "Bluetooth zaten açık");
            }
        } else {
            Log.e(TAG, "Bluetooth adaptörü bulunamadı");
        }
    }


    // Bluetooth'u kapama
    @SuppressLint("MissingPermission")
    public void turnOffBluetooth() {
        Log.i(TAG, "Bluetooth kapama işlemi başlatılıyor");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
                Log.d(TAG, "Bluetooth kapandı");
            } else {
                Log.d(TAG, "Bluetooth zaten kapalı");
            }
        } else {
            Log.e(TAG, "Bluetooth adaptörü bulunamadı");
        }
    }

    // Mobil veriyi  açma (Root gerekli)
    public void turnOnMobileNet() {
        Log.i(TAG, "Mobil veri açma işlemi başlatılıyor");
        if (isRooted()) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                outputStream.writeBytes("svc data enable\n");
                outputStream.flush();
                outputStream.close();
                process.waitFor();
                Log.d(TAG, "Mobil veri açıldı");
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Mobil veri açma başarısız", e);
            }
        } else {
            Log.e(TAG, "Cihaz root'lanmış değil, mobil veriyi açamıyor");
        }
    }
    // Mobil veriyi kapama (Root gerekli)
    public void turnOffMobileNet() {
        Log.i(TAG, "Mobil veri kapama işlemi başlatılıyor");
        if (isRooted()) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                outputStream.writeBytes("svc data disable\n");
                outputStream.flush();
                outputStream.close();
                process.waitFor();
                Log.d(TAG, "Mobil veri kapandı");
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Mobil veri kapama başarısız", e);
            }
        } else {
            Log.e(TAG, "Cihaz root'lanmış değil, mobil veriyi kapatamıyor");
        }
    }


    // Uçak modunu açma (Root gerekli)
    public void turnOnAirplaneMode() {
        Log.i(TAG, "Uçak modu açma işlemi başlatılıyor");
        if (isRooted()) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                outputStream.writeBytes("settings put global airplane_mode_on 1\n");
                outputStream.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n");
                outputStream.flush();
                outputStream.close();
                process.waitFor();
                Log.d(TAG, "Uçak modu açıldı");
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Uçak modunu açma başarısız", e);
            }
        } else {
            Log.e(TAG, "Cihaz root'lanmış değil, uçak modunu açamıyor");
        }
    }

    // Uçak modunu kapama (Root gerekli)
    public void turnOffAirplaneMode() {
        Log.i(TAG, "Uçak modu kapama işlemi başlatılıyor");
        if (isRooted()) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                outputStream.writeBytes("settings put global airplane_mode_on 0\n");
                outputStream.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n");
                outputStream.flush();
                outputStream.close();
                process.waitFor();
                Log.d(TAG, "Uçak modu kapandı");
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Uçak modunu kapama başarısız", e);
            }
        } else {
            Log.e(TAG, "Cihaz root'lanmış değil, uçak modunu kapatamıyor");
        }
    }


    // Cihazın root'lanıp lanmadığını kontrol etme
    private boolean isRooted() {
        String[] paths = {
                "/sbin/su", "/system/bin/su", "/system/xbin/su",
                "/data/local/xbin/su", "/data/local/bin/su",
                "/system/sd/xbin/su", "/system/bin/failsafe/su",
                "/data/local/su"
        };
        for (String path : paths) {
            if (new java.io.File(path).exists()) {
                return true;
            }
        }
        return false;
    }
}