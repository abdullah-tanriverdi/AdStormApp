package com.tanriverdi.adstormeterna.deviceinfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;;
import java.util.List;
import android.Manifest;
import com.tanriverdi.adstormeterna.fingerprint.AesCrypt;


//Tamamlandı
public class DeviceInfo {


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private  static final String TAG = "DeviceInfo";
    private Context context;


    public DeviceInfo(Context context) {
        this.context = context;
    }


    // Cihaz adını sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendDeviceNameToServer() {
        String deviceName = getDeviceName();
        JSONObject deviceNameData = new JSONObject();
        try {
            deviceNameData.put("name", deviceName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceNameData;
    }

    public String getDeviceName() {
        return Build.MODEL;
    }


    // Pil durumunu sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendBatteryStatusToServer() {
        String batteryStatus = getBatteryStatus();
        JSONObject batteryData = new JSONObject();
        try {
            batteryData.put("battery", batteryStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return batteryData;

    }

    public String getBatteryStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;
            return batteryPct + "%";
        }
        return "Unable to retrieve battery status";
    }


    // Şarj durumunu sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendChargingStatusToServer() {
        boolean isCharging = getChargingStatus();
        JSONObject chargingData = new JSONObject();
        try {
            chargingData.put("Charging Status: ", isCharging);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chargingData;
    }

    public boolean getChargingStatus() {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, iFilter);

        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        }

        return false;
    }


    // Ağ durumunu sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendNetworkStatusToServer() {
        String networkStatus = getNetworkStatus();
        JSONObject networkData = new JSONObject();
        try {
            networkData.put("network", networkStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return networkData;
    }

    public String getNetworkStatus() {
        ConnectivityManager cm;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String wifiName = wifiInfo.getSSID();
                return "Wi-Fi-> " + wifiName;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return "Mobile Data";

            } else {
                return "No Connection";
            }
        }
        return "Unable to determine network status";
    }


    // Wi-Fi BSSID bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendBSSIDToServer() {
        String bssid = getBSSID();
        JSONObject bssidData = new JSONObject();
        try {
            bssidData.put("bssid", bssid);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return bssidData;
    }

    public String getBSSID() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo != null ? wifiInfo.getBSSID() : "BSSID not available";
        }
        return "Unable to retrieve BSSID";
    }


    // Konum bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendLocationToServer() {
        Location location = (Location) getLocation();
        JSONObject locationObject = new JSONObject();
        JSONObject locationInfo = new JSONObject();

        try {

            if (location != null) {
                double latitude = location.getLatitude();
                String latitude1= String.valueOf(latitude);
                double longitude = location.getLongitude();
                String longitude1= String.valueOf(longitude);
               // String locationInfo = "Latitude: " + latitude + "Longitude: " + longitude;

                locationInfo.put("latitude",latitude1);
                locationInfo.put("longitude", longitude1);
                locationObject.put("location", locationInfo);
                //  locationObject.put("Longitude", longitude);

            } else {
                locationObject.put("ERROR", "Location not available");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationObject;
    }

    public Location getLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        if (locationManager != null) {
            try {
                // Konum izinlerini kontrol et
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // Eğer context bir Activity değilse, izin isteği gönderilemez
                    if (context instanceof Activity) {
                        // İzinler verilmemiş, izin iste
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION);
                    }
                    return null;
                }

                // Son bilinen konumu al
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return location;
    }


    // İzin sonuçları
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.toString();
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Location location = getLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                }
            } else {
                Log.d("Location", "Location permission denied");
            }
        }
    }


    // İşletim sistemi bilgilerini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendOsToServer() {
        String osVersion = getOsVersion();
        JSONObject osData = new JSONObject();
        try {

            osData.put("osversion" , osVersion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return osData;
    }

    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }


    // RAM bilgilerini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendRamToServer() {
        String memoryUsage = getRAMUsage();
        JSONObject ramData = new JSONObject();
        try {
            ramData.put("Total RAM: ", memoryUsage.split(";")[0]);
            ramData.put("Available RAM: ", memoryUsage.split(";")[1]);
            ramData.put("Used RAM: ", memoryUsage.split(";")[2]);
            ramData.put("Used RAM Percentage: ", memoryUsage.split(";")[3]);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ramData;
    }

    public String getRAMUsage() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long totalMemory = memoryInfo.totalMem;
        long availableMemory = memoryInfo.availMem;
        long usedMemory = totalMemory - availableMemory;

        float usedMemoryPercentage = (usedMemory / (float) totalMemory) * 100;

        return formatSize(totalMemory) + ";"
                + formatSize(availableMemory) + ";"
                + formatSize(usedMemory) + ";"
                + String.format("%.2f%%", usedMemoryPercentage);
    }

    // Cihaz alan boyutlarını formatlamak için kullanılan metod
    public String formatSize(long size) {
        String suffix = null;
        float fSize = size;

        if (fSize >= 1024) {
            suffix = "KB";
            fSize /= 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
                if (fSize >= 1024) {
                    suffix = "GB";
                    fSize /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Float.toString(fSize));

        int commaOffset = resultBuffer.indexOf(".");
        if (commaOffset >= 0) {
            int decimalDigits = resultBuffer.length() - commaOffset - 1;
            if (decimalDigits > 2) {
                resultBuffer.setLength(commaOffset + 3);
            }
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


    // Depolama kullanımını sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendStorageToServer() {
        String storageUsage = getStorageUsage();
        JSONObject storageData = new JSONObject();
        JSONObject storageDataFinal = new JSONObject();
        try {

            String total = storageUsage.split(";")[0];
            String available = storageUsage.split(";")[1];
            String used= storageUsage.split(";")[2];
            String percentege = storageUsage.split(";")[3];

            storageData.put("Total Storage", total);
            storageData.put("Available Storage", available);
            storageData.put("Used Storage", used);
            storageData.put("Used Storage Percentage", percentege);
            storageDataFinal.put("storage", storageData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storageDataFinal;
    }

    public String getStorageUsage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();

        long totalStorage = totalBlocks * blockSize;
        long availableStorage = availableBlocks * blockSize;
        long usedStorage = totalStorage - availableStorage;

        float usedStoragePercentage = (usedStorage / (float) totalStorage) * 100;

        return formatSize(totalStorage) + ";"
                + formatSize(availableStorage) + ";"
                + formatSize(usedStorage) + ";"
                + String.format("%.2f%%", usedStoragePercentage);
    }


    // Yüklü uygulamaların bilgilerini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendInstalledApplicationsToServer() {
        List<String> appList = getInstalledApplications();
        JSONObject appData = new JSONObject();
        JSONArray appArray = new JSONArray();

        try {
            for (String appName : appList) {
                appArray.put(appName);
            }


            appData.put("installedapps", appArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return appData;
    }

    public List<String> getInstalledApplications() {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        List<String> appList = new ArrayList<>();

        for (PackageInfo packageInfo : packages) {
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String packageName = packageInfo.packageName;
            if (isValidPackageName(packageName)) {
                appList.add(appName);
            }
        }

        return appList;
    }

    // Paket adının geçerli olup olmadığını kontrol eden metod
    public boolean isValidPackageName(String packageName) {
        // Paket adının geçerli olup olmadığını kontrol eden basit bir kontrol
        return packageName != null && !packageName.trim().isEmpty() && packageName.matches("^[a-zA-Z0-9._-]+$");
    }


    // CPU bilgilerini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendCpuInfoToServer() {
        JSONObject cpuData = new JSONObject();
        try {
            JSONObject cpuInfo = getCpuInfo();
            cpuData.put("cpuinfo", cpuInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cpuData;
    }

    public JSONObject getCpuInfo() {
        JSONObject cpuInfoJson = new JSONObject();
        try {
            Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    cpuInfoJson.put(key, value);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                cpuInfoJson.put("Error", "Error retrieving CPU info.");
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }

        return cpuInfoJson;
    }


    // Ekranın On/Off bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendScreenStatusToServer() {
        String screenStatus = getScreenStatus();
        JSONObject screenStatusData = new JSONObject();
        try {

            screenStatusData.put("Screen Status: ", screenStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return screenStatusData;
    }

    public String getScreenStatus() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = powerManager.isInteractive(); // API level 20 ve üstü için
        if (isScreenOn) {
            return "On";
        } else {
            return "Off";
        }
    }

    // Bluetooth On/Off bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendBluetoothStatusToServer() {
        String bluetoothStatus = getBluetoothStatus();
        JSONObject bluetoothStatusData = new JSONObject();
        try {
            bluetoothStatusData.put("bluetooth", bluetoothStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bluetoothStatusData;
    }

    public String getBluetoothStatus() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                return "Enabled";
            } else {
                return "Disabled";
            }
        }
        return "Not Supported";
    }


    // Wi-Fi Mac bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendWifiMacAddressToServer() {
        String wifiMacAddress = getWifiMacAddress();
        JSONObject wifiData = new JSONObject();
        if (wifiMacAddress != null) {
            try {

                wifiData.put("macaddress", wifiMacAddress);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
        }
        return wifiData;
    }

    public String getWifiMacAddress() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getBSSID();
            }
        }
        return null;
    }


    //Ekran boyut bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendScreenResolutionToServer() {
        String screenResolution = getScreenResolution();
        JSONObject screenResolutionData = new JSONObject();
        try {


            screenResolutionData.put("screenresolution", screenResolution);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return screenResolutionData;
    }

    public String getScreenResolution() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            return width + "x" + height;
        }
        return "Unable to retrieve screen resolution";
    }

    //IP bilgilerini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendIpAddressToServer() {
        JSONObject ipAddressData = new JSONObject();
        JSONObject ipAdrresDataFinal = new JSONObject();
        String wifiIpAddress = getWifiIpAddress();
        String mobileIpAddress = getMobileIpAddress();




        try {
            ipAddressData.put("Wi-Fi IP Address: ", wifiIpAddress);
            ipAddressData.put("Mobile IP Address: ", mobileIpAddress);
            ipAdrresDataFinal.put("ipv4" , ipAddressData);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ipAdrresDataFinal;
    }

    public String getMobileIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "No mobile connection";
    }

    public String getWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return String.format("%d.%d.%d.%d",
                    (ipAddress & 0xff),
                    (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff),
                    (ipAddress >> 24 & 0xff));
        }
        return "No WiFi connection";
    }

    //Android ID bilgisini sunucuya göndermek için JSON nesnesi oluşturur
    public JSONObject sendAndroidIdToServer() {
        String androidId = getAndroidId();
        JSONObject androidIdData = new JSONObject();
        try {
            androidIdData.put("Android Id: ", androidId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return androidIdData;
    }

    public String getAndroidId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    //Çeşitli bilgileri şifreleyip sunucuya göndermek için JSON nesnesi oluşturur
    public String aesCrypty() {
          String encryptedJson = null;
        AesCrypt aesCrypt = new AesCrypt(context);
        JSONArray jsonArray = new JSONArray();
        JSONObject resultJson = new JSONObject();

        try {
            jsonArray.put(sendDeviceNameToServer());
            jsonArray.put(sendOsToServer());
          //  jsonArray.put(sendCpuInfoToServer());
            jsonArray.put(sendScreenResolutionToServer());
           jsonArray.put(sendAndroidIdToServer());

            String jsonArrayString = jsonArray.toString();
            encryptedJson = aesCrypt.encrypt(jsonArrayString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  encryptedJson;
    }

    //tüm bilgiler
    public JSONObject getAllDataAsJSONObject() {
        JSONObject result = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            //JSONObject maualData = new JSONObject();
       //     maualData.put("FingerPrint:", aesCrypty());
            // JSON verilerini al
            JSONObject persistentData = persistentAllData();
            JSONObject cacheData = cacheAllData();

         //   jsonArray.put(maualData);
            // Verileri JSON dizisine ekle
            jsonObject.put("fingerprint", aesCrypty());
            jsonObject.put("cache",cacheData);
            jsonObject.put("persistent",persistentData);
            // JSON dizisini "All Data" anahtarı ile JSON objesi içine ekle
            result.put("all", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    //kalıcı bilgiler
    public JSONObject  persistentAllData() {
        // JSON döndüren metodlar
        JSONObject deviceNameData = sendDeviceNameToServer();
        JSONObject osVersion = sendOsToServer();
        JSONObject cpu = sendCpuInfoToServer();
        JSONObject screenReso = sendScreenResolutionToServer();
        JSONObject storage = sendStorageToServer();
        JSONObject mac = sendWifiMacAddressToServer();

        // JSONObject serialNumber = sendSerialNumberToServer(); // Yorum satırı ile işaretlenmiş

        // Birden fazla JSON objesini birleştirme
        JSONObject mergedData;
        try {
            mergedData = mergeJSONObjects(deviceNameData, osVersion, cpu, screenReso, storage, mac);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Yeni JSON objesi oluşturma ve 'PersistentAllData' anahtarını ekleme
        JSONObject finalData = new JSONObject();
        try {
           finalData.put("persistent", mergedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

       return mergedData;
    }

    //geçici bilgiler
    public JSONObject cacheAllData() {
        // JSON döndüren metodlar
        JSONObject battery = sendBatteryStatusToServer();
        JSONObject wifiOrMobile = sendNetworkStatusToServer();
        JSONObject bssid = sendBSSIDToServer();
        JSONObject location = sendLocationToServer();
        JSONObject bluetooth = sendBluetoothStatusToServer();
        JSONObject ip = sendIpAddressToServer();
        JSONObject installedApps = sendInstalledApplicationsToServer();

        // JSON objelerini birleştirme
        JSONObject mergedData;
        try {
            mergedData = mergeJSONObjects(battery, wifiOrMobile, bssid, location, bluetooth, ip,installedApps);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Yeni JSON objesi oluşturma ve 'CacheData' anahtarını ekleme
        JSONObject finalData = new JSONObject();
        try {
            finalData.put("cache", mergedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mergedData;
    }


    // Birden fazla JSON objesini birleştiren yardımcı metot
    private JSONObject mergeJSONObjects(JSONObject... jsonObjects) throws JSONException {
        JSONObject merged = new JSONObject();

        for (JSONObject jsonObject : jsonObjects) {
            JSONArray names = jsonObject.names();
            if (names != null) {
                for (int i = 0; i < names.length(); i++) {
                    String name = names.getString(i);
                    merged.put(name, jsonObject.get(name));
                }
            }
        }

        return merged;
    }


}