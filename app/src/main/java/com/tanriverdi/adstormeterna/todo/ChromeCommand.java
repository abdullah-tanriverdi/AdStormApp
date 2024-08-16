package com.tanriverdi.adstormeterna.todo;

import android.content.Context;
import android.util.Log;


import com.tanriverdi.adstormeterna.chrome.action.HomeBack;
import com.tanriverdi.adstormeterna.chrome.action.LockWakeScreen;
import com.tanriverdi.adstormeterna.chrome.action.OpenCloseChrome;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChromeCommand {

    private Context context;
    private LockWakeScreen lockWakeScreen ;
    private OpenCloseChrome openCloseChrome;
    private HomeBack homeBack;


    private static final String TAG = "ChromeCommand";

    private final Map<String , Runnable> commandMap;



    public ChromeCommand(Context context) {
        this.context = context;
        commandMap = new HashMap<>();
        initializeComponets();
        initializeCommand();

    }


    private void initializeCommand(){
        commandMap.put("wakeDevice" ,lockWakeScreen::wakeUpScreenAndGoHome);
        commandMap.put("lockDevice", lockWakeScreen::lockScreen);
        commandMap.put("openChrome", openCloseChrome::openChrome);
        commandMap.put("closeChrome", openCloseChrome::closeChrome);
        commandMap.put("back", homeBack::goBack);
        commandMap.put("home", homeBack::goToHome);






    }

    public JSONObject handleEvents(JSONObject data) {
        try {
            // "command" anahtarındaki diziyi al
            JSONArray commands = data.optJSONArray("command");
            if (commands != null) {
                for (int i = 0; i < commands.length(); i++) {
                    String command = commands.optString(i);
                    Runnable action = commandMap.get(command);
                    if (action != null) {
                        action.run();  // Komut için uygun işlemi çalıştır
                        Log.d(TAG, "Komut '" + command + "' işlendi.");
                    } else {
                        Log.w(TAG, "Komut '" + command + "' için tanımlı bir işlem bulunamadı.");
                    }
                }
            } else {
                Log.w(TAG, "'command' anahtarı eksik veya boş.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Olay işlenirken hata oluştu: " + e.getMessage(), e);
        }
        return data;
    }


    private  void initializeComponets(){
        lockWakeScreen = new LockWakeScreen(context);
        openCloseChrome = new OpenCloseChrome(context);
        homeBack= new HomeBack(context);


    }
}
