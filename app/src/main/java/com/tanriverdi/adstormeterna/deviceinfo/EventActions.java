package com.tanriverdi.adstormeterna.deviceinfo;


import android.util.Log;

import com.tanriverdi.adstormeterna.deviceinfo.DeviceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//Tamamlandı

public class EventActions {

    private final DeviceInfo deviceInfo;
    private final Map<String, Supplier<JSONObject>> actionsMap; // Event türleri ile ilgili metodları içeren HashMap
    private static final String TAG = "EventActions";


    // Constructor, DeviceInfo nesnesini alır ve actionsMap'i başlatır
    public EventActions(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        actionsMap = new HashMap<>();
        initializeActions(); // actionsMap'i başlatır.
    }

    //Hashmap başlatılır
    private void initializeActions() {
        // Event türleri ile metodları eşleştirir
        actionsMap.put("name", deviceInfo::sendDeviceNameToServer);
        actionsMap.put("battery", deviceInfo::sendBatteryStatusToServer);
        actionsMap.put("charging", deviceInfo::sendChargingStatusToServer);
        actionsMap.put("wifiOrMobile", deviceInfo::sendNetworkStatusToServer);
        actionsMap.put("bssid", deviceInfo::sendBSSIDToServer);
        actionsMap.put("location", deviceInfo::sendLocationToServer);
        actionsMap.put("os", deviceInfo::sendOsToServer);
        actionsMap.put("storage", deviceInfo::sendStorageToServer);
        actionsMap.put("installedApp", deviceInfo::sendInstalledApplicationsToServer);
        actionsMap.put("cpu", deviceInfo::sendCpuInfoToServer);
        actionsMap.put("screenStatus", deviceInfo::sendScreenStatusToServer);
        actionsMap.put("bluetooth", deviceInfo::sendBluetoothStatusToServer);
        actionsMap.put("wifiMac", deviceInfo::sendWifiMacAddressToServer);
        actionsMap.put("screenReso", deviceInfo::sendScreenResolutionToServer);
        actionsMap.put("ip", deviceInfo::sendIpAddressToServer);
        actionsMap.put("ram", deviceInfo::sendRamToServer);
        actionsMap.put("androidId", deviceInfo::sendAndroidIdToServer);
       //actionsMap.put("aescrypt", deviceInfo::aesCrypty);
        actionsMap.put("persistentData", deviceInfo::persistentAllData);
        actionsMap.put("cacheData", deviceInfo::cacheAllData);
        actionsMap.put("info",deviceInfo::getAllDataAsJSONObject);


    }


    // Gelen verileri işler ve sonuçları JSON formatında döner
    public JSONObject handleEvents(JSONObject data) throws JSONException {
        JSONObject combinedData = new JSONObject();
        JSONArray events = data.getJSONArray("events");

        // Her bir event üzerinde döngü
        for (int i = 0; i < events.length(); i++) {
            String event = events.getString(i); // Event türünü al
            if (actionsMap.containsKey(event)) {  // Eğer event türü actionsMap'te mevcutsa
                JSONObject result = null;
                // Android 24 (Nougat) ve üzeri sürümler için
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = actionsMap.get(event).get(); // İlgili metoddan JSON nesnesini al
                }
                // Gelen sonuçları birleştir
                combinedData = mergeJSONObjects(combinedData, result);
                Log.d(TAG, "Event işleme tamamlandı: " + event);
            }
        }
        return combinedData;
    }


    // İki JSON nesnesini birleştirir
    private JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) throws JSONException {
        JSONObject merged = new JSONObject();

        // İlk JSON nesnesinin anahtarlarını al ve birleştir
        JSONArray names = json1.names();
        if (names != null) {
            for (int i = 0; i < names.length(); i++) {
                String name = names.getString(i);
                merged.put(name, json1.get(name));
            }
        }

        // İkinci JSON nesnesinin anahtarlarını al ve birleştir
        names = json2.names();
        if (names != null) {
            for (int i = 0; i < names.length(); i++) {
                String name = names.getString(i);
                merged.put(name, json2.get(name));
            }
        }

        Log.d(TAG, "JSON nesneleri birleştirildi.");
        return merged;
    }
}
