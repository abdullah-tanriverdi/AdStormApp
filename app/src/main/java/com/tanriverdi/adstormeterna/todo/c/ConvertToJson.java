package com.tanriverdi.adstormeterna.todo.c;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertToJson {

    private static final String TAG = "ConvertToJson";
    private Context context;

    public ConvertToJson(Context context) {
        this.context = context;
    }

    public void convertAndSavePreferencesFile() {
        // Kopyalanmış dosyanın yolu ve JSON dosyasının kaydedileceği yer
        String sourceFilePath = "/storage/emulated/0/AdStormEterna/Preferences";
        String destinationDirPath = "/storage/emulated/0/AdStormEterna/";
        String destinationFilePath = destinationDirPath + "Preferences1.json";

        // JSON nesnesi oluştur
        JSONObject jsonObject = new JSONObject();

        // Dosya içeriğini oku ve JSON nesnesine dönüştür
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath))) {
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            // JSON formatına dönüştür (basit bir format, ihtiyaca göre değiştirilebilir)
            jsonObject.put("fileContent", fileContent.toString());

            // JSON verisini dosyaya yaz
            try (FileWriter fileWriter = new FileWriter(destinationFilePath)) {
                fileWriter.write(jsonObject.toString());
            }

            String successMessage = "JSON dosyası başarıyla kaydedildi.";

            Log.i(TAG, successMessage);
        } catch (IOException e) {
            String errorMessage = "Dosya okuma/yazma hatası: " + e.getMessage();

            Log.e(TAG, errorMessage, e);
        } catch (Exception e) {
            String errorMessage = "JSON oluşturma hatası: " + e.getMessage();

            Log.e(TAG, errorMessage, e);
        }
    }
}
