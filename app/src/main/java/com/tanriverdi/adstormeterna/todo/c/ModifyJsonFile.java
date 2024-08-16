package com.tanriverdi.adstormeterna.todo.c;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModifyJsonFile {

    private static final String TAG = "ModifyJsonFile";
    private Context context;

    public ModifyJsonFile(Context context) {
        this.context = context;
    }

    public boolean modifyAndSaveJsonFile() {
        // Kaynak ve hedef dosya yolları
        String sourceFilePath = "/storage/emulated/0/AdStormEterna/Preferences";
        String destinationFilePath = "/storage/emulated/0/AdStormEterna/Preferences1";

        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);

        // Dosyayı JSON formatına dönüştür ve üzerinde değişiklik yap
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // JSON objesine dönüştür
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());

            // JSON objesinde değişiklik yap
            // Net ayarlarını kontrol et ve güncelle
            if (jsonObject.has("net")) {
                JSONObject net = jsonObject.getJSONObject("net");
                net.put("network_prediction_options", 2);
            } else {
                JSONObject net = new JSONObject();
                net.put("network_prediction_options", 2);
                jsonObject.put("net", net);
            }

            // Hesap bilgilerini kontrol et ve güncelle
            if (jsonObject.has("account_info") && jsonObject.getJSONArray("account_info").length() > 0) {
                JSONObject account = jsonObject.getJSONArray("account_info").getJSONObject(0);
                String accountId = account.optString("account_id");
                String email = account.optString("email");
                String gaiaId = account.optString("gaia");

                JSONObject googleServices = jsonObject.optJSONObject("google").optJSONObject("services");
                if (googleServices == null) {
                    googleServices = new JSONObject();
                    jsonObject.put("google", new JSONObject().put("services", googleServices));
                }
                googleServices.put("account_id", accountId);
                googleServices.put("last_gaia_id", gaiaId);
                googleServices.put("last_username", email);
                googleServices.put("consented_to_sync", true);
            }

            // Diğer ayarları güncelle
            jsonObject.put("enable_do_not_track", true);
            jsonObject.put("https_only_mode_enabled", true);
            jsonObject.put("https_only_mode_auto_enabled", false);
            jsonObject.put("privacy_guide", new JSONObject().put("viewed", true));

            JSONObject profile = jsonObject.optJSONObject("profile");
            if (profile == null) {
                profile = new JSONObject();
                jsonObject.put("profile", profile);
            }
            JSONObject contentSettings = profile.optJSONObject("content_settings");
            if (contentSettings == null) {
                contentSettings = new JSONObject();
                profile.put("content_settings", contentSettings);
            }
            JSONObject exceptions = contentSettings.optJSONObject("exceptions");
            if (exceptions == null) {
                exceptions = new JSONObject();
                contentSettings.put("exceptions", exceptions);
            }

            // Permission autoblocking ve geolocation ayarlarını güncelle
            exceptions.put("permission_autoblocking_data", new JSONObject());
            JSONObject geolocation = new JSONObject();
            geolocation.put("https://www.google.com:443,*", new JSONObject().put("last_modified", System.currentTimeMillis()).put("setting", 1));
            exceptions.put("geolocation", geolocation);

            JSONObject permissionActions = new JSONObject();
            permissionActions.put("geolocation", new JSONObject().put("action", 0).put("prompt_disposition", 4).put("time", System.currentTimeMillis() + (10000 + (int) (Math.random() * 10000))));
            exceptions.put("permission_actions", permissionActions);

            // Sync ayarlarını güncelle
            JSONObject sync = jsonObject.optJSONObject("sync");
            if (sync == null) {
                sync = new JSONObject();
                jsonObject.put("sync", sync);
            }
            sync.put("feature_status_for_sync_to_signin", 1);
            sync.put("autofill_wallet_import_enabled_migrated", true);
            sync.put("encryption_bootstrap_token_per_account_migration_done", true);
            sync.put("has_setup_completed", true);
            sync.put("keep_everything_synced", false);
            sync.put("local_data_out_of_sync", false);
            sync.put("passwords", false);
            sync.put("payments", false);
            sync.put("preferences", false);
            sync.put("reading_list", false);
            sync.put("tabs", false);
            sync.put("typed_urls", false);
            sync.put("autofill", false);
            sync.put("bookmarks", false);

            // Data type status
            JSONObject dataTypeStatus = new JSONObject();
            dataTypeStatus.put("app_list", false);
            dataTypeStatus.put("app_settings", false);
            dataTypeStatus.put("apps", false);
            dataTypeStatus.put("arc_package", false);
            dataTypeStatus.put("autofill", false);
            dataTypeStatus.put("autofill_profiles", false);
            dataTypeStatus.put("autofill_wallet", false);
            dataTypeStatus.put("autofill_wallet_credential", false);
            dataTypeStatus.put("autofill_wallet_metadata", false);
            dataTypeStatus.put("autofill_wallet_offer", false);
            dataTypeStatus.put("autofill_wallet_usage", false);
            dataTypeStatus.put("bookmarks", false);
            dataTypeStatus.put("collaboration_group", false);
            dataTypeStatus.put("compare", false);
            dataTypeStatus.put("contact_info", false);
            dataTypeStatus.put("device_info", false);
            dataTypeStatus.put("dictionary", false);
            dataTypeStatus.put("extension_settings", false);
            dataTypeStatus.put("extensions", false);
            dataTypeStatus.put("history", false);
            dataTypeStatus.put("history_delete_directives", false);
            dataTypeStatus.put("incoming_password_sharing_invitation", false);
            dataTypeStatus.put("managed_user_settings", false);
            dataTypeStatus.put("nigori", false);
            dataTypeStatus.put("os_preferences", false);
            dataTypeStatus.put("os_priority_preferences", false);
            dataTypeStatus.put("outgoing_password_sharing_invitation", false);
            dataTypeStatus.put("passwords", false);
            dataTypeStatus.put("plus_address", false);
            dataTypeStatus.put("power_bookmark", false);
            dataTypeStatus.put("preferences", false);
            dataTypeStatus.put("printers", false);
            dataTypeStatus.put("printers_authorization_servers", false);
            dataTypeStatus.put("priority_preferences", false);
            dataTypeStatus.put("reading_list", false);
            dataTypeStatus.put("saved_tab_group", false);
            dataTypeStatus.put("search_engines", false);
            dataTypeStatus.put("security_events", false);
            dataTypeStatus.put("segmentation", false);
            dataTypeStatus.put("send_tab_to_self", false);
            dataTypeStatus.put("sessions", false);
            dataTypeStatus.put("shared_tab_group_data", false);
            dataTypeStatus.put("sharing_message", false);
            dataTypeStatus.put("themes", false);
            dataTypeStatus.put("user_consent", false);
            dataTypeStatus.put("user_events", false);
            dataTypeStatus.put("web_apks", false);
            dataTypeStatus.put("web_apps", false);
            dataTypeStatus.put("webauthn_credential", false);
            dataTypeStatus.put("wifi_configurations", false);
            dataTypeStatus.put("workspace_desk", false);

            sync.put("data_type_status_for_sync_to_signin", dataTypeStatus);

            // Yeni JSON objesini dosyaya yaz
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile))) {
                writer.write(jsonObject.toString(4)); // Pretty print JSON
            }

            String successMessage = "JSON dosyası başarıyla değiştirildi ve kaydedildi.";
            Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show();
            Log.i(TAG, successMessage);

        } catch (IOException | JSONException e) {
            String errorMessage = "JSON dosyası işleme hatası: " + e.getMessage();
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMessage, e);
        }
        return true;
    }
}
