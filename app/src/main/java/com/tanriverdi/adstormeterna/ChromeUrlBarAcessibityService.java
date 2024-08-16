package com.tanriverdi.adstormeterna;


// Android Accessibility Service sınıfını içe aktarma kütüphaneleri
import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChromeUrlBarAcessibityService extends AccessibilityService {
    private static final String TAG = "ChromeUrlBarAccessibilityService";
    private List<AccessibilityNodeInfo> sponsoredAdsList = new ArrayList<>();
    @Override // erişilebilirlik olayı meydana geldiğinde tetiklenir.
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) { // yeni bir ekran açıldığında bu olayı yakalıyoruz.

            // Olayın tetiklendiği paketin adı
            String packageName = event.getPackageName().toString();
            if (packageName.equals("com.android.chrome")) {
                performActionOnURLBar();

            }
        }
    }

    @Override
    public void onInterrupt() {
        // Servis kesintiye uğradığında yapılacak işlemler
    }

    // URL çubuğunda işlem yapacak metod.
    public void performActionOnURLBar() {
        // Ekrandaki en üst seviyedeki aktif pencerenin kök düğümünü alma
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            // URL çubuğunu ID kullanarak bulma
            List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar");
            if (!nodes.isEmpty()) {

                // İlk URL çubuğu düğümünü alma
                AccessibilityNodeInfo urlBarNode = nodes.get(0);

                // URL çubuğuna tıklayarak odaklanma (klavyeyi açar)
                urlBarNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);

                // URL çubuğuna metin yazma
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "anahtarcı");
                urlBarNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                forceEnterKeyPress();
                // Gecikmeli olarak "Enter" tuşuna basma
                Log.d(TAG, "e1");    //  new Handler().postDelayed(this::forceEnterKeyPress, 2000); // 2 saniye gecikme
                try {
                    Thread.sleep(5000);
                    Log.d(TAG, "e11");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d(TAG, "e111");
                new Handler().postDelayed(() -> {
                    Log.d(TAG, "e1111");
                    // Sayfa yüklendiğinde ilk bağlantıyı tıklama

                    findSponsoredAds();

                    new Handler().postDelayed(() -> {
                        // Tüm reklamlar bulunduktan sonra tıklama işlemi yapılır
                        if (!sponsoredAdsList.isEmpty()) {
                            clickSponsoredAdAtIndex(1); // İlk sponsorlu reklama tıklama
                        } else {
                            Log.d(TAG, "No sponsored ads found to click.");
                        }
                    }, 5000); // 5 saniye gecikme ekliyoruz
                }, 2000); // İlk gecikme


            } else {
                Log.d(TAG, "URL çubuğu bulunamadı");
            }
        } else {
            Log.d(TAG, "Kök düğüm null");
        }
    }

    // Root yetkisiyle "Enter" tuşuna basmayı simüle eden metot
    private void forceEnterKeyPress() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            // "input keyevent KEYCODE_ENTER" komutunu gönderiyoruz. Bu komut, cihazda "Enter" tuşuna basmayı simüle eder.
            os.writeBytes("input keyevent " + KeyEvent.KEYCODE_ENTER + "\n");
            os.flush();
            os.close();
            process.waitFor();
            Log.d(TAG, "Root ile Enter tuşuna basma işlemi gerçekleştirildi.");
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Root ile Enter tuşuna basma işlemi sırasında hata oluştu.", e);
        }
    }




    // Tüm "ücretli sponsorlu reklam" yazılarını bul ve bir listeye ekle
    private void findSponsoredAds() {
        sponsoredAdsList.clear();  // Önceki listeyi temizle

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodes = getAllNodes(rootNode);

            for (AccessibilityNodeInfo node : nodes) {
                if (node != null) {
                    CharSequence text = node.getText();
                    if (text != null && text.toString().trim().toLowerCase().startsWith("ücretli sponsorlu reklam")) {
                        sponsoredAdsList.add(node);  // Bulunan sponsorlu reklamı listeye ekle
                        Log.d(TAG, "Sponsored ad found: " + text.toString());
                    }

                }
            }
            Log.d(TAG, "Total sponsored ads found: " + sponsoredAdsList.size());
        } else {
            Log.d(TAG, "Root node is not available");
        }
    }

    // Verilen sıradaki "ücretli sponsorlu reklam" yazısına ve hemen altındaki düğüme tıklama
    private void clickSponsoredAdAtIndex(int index) {
        if (index >= 0 && index < sponsoredAdsList.size()) {
            AccessibilityNodeInfo adNode = sponsoredAdsList.get(index);
            if (adNode != null) {
                // İlk olarak belirtilen index'teki sponsorlu reklama tıklama işlemi yap
                performClickAction(adNode);
                Log.d(TAG, "Clicked on sponsored ad at index: " + index);

                // Hemen altındaki düğümleri tıklanabilir bulana kadar ara ve tıkla
                AccessibilityNodeInfo parentNode = adNode.getParent();
                if (parentNode != null) {
                    int childCount = parentNode.getChildCount();
                    int adNodeIndex = -1;

                    // Mevcut düğümün index'ini bul
                    for (int i = 0; i < childCount; i++) {
                        if (parentNode.getChild(i).equals(adNode)) {
                            adNodeIndex = i;
                            break;
                        }
                    }

                    // Sonraki tıklanabilir düğümü bulana kadar döngü ile devam et
                    for (int i = adNodeIndex + 1; i < childCount; i++) {
                        AccessibilityNodeInfo nextNode = parentNode.getChild(i);
                        if (nextNode != null && nextNode.isClickable()) {
                            performClickAction(nextNode);
                            Log.d(TAG, "Clicked on the next clickable element below the sponsored ad at index: " + index);
                            return; // Tıklama işlemi gerçekleştirildi, döngüden çık
                        }
                    }

                    Log.d(TAG, "No clickable elements found after the sponsored ad.");
                }
            } else {
                Log.d(TAG, "Sponsored ad at index " + index + " is not available.");
            }
        } else {
            Log.d(TAG, "Index " + index + " is out of bounds. Total sponsored ads found: " + sponsoredAdsList.size());
        }
    }

    // Tıklama işlemini güvenilir hale getirmek için yardımcı metod
    private void performClickAction(AccessibilityNodeInfo node) {
        if (node != null) {
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);  // İlk olarak odaklanma işlemi
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);  // Ardından tıklama işlemi
        }
    }




    // Yardımcı metod: Tüm düğümleri almak için
    private List<AccessibilityNodeInfo> getAllNodes(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> allNodes = new ArrayList<>();
        if (node == null) return allNodes;

        allNodes.add(node);

        for (int i = 0; i < node.getChildCount(); i++) {
            allNodes.addAll(getAllNodes(node.getChild(i)));
        }

        return allNodes;
    }
}





   /* // Verilen sıradaki web sitesine tıklama
    private void clickLinkAtIndex(int index) {
        Log.d(TAG, "Clicking link at index: " + index);
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            Log.d(TAG, "Root node is available");

            // Sayfadaki tüm düğmeleri ve bağlantıları tarama
            List<AccessibilityNodeInfo> nodes = getAllNodes(rootNode);
            List<AccessibilityNodeInfo> httpLinks = new ArrayList<>();

            for (AccessibilityNodeInfo node : nodes) {
                if (node != null && node.getClassName().equals("android.widget.TextView")) {
                    CharSequence linkText = node.getText();
                    if (linkText != null && linkText.toString().startsWith("http")) {
                        Log.d(TAG, "Link found: " + linkText.toString());
                        httpLinks.add(node);
                    }
                }
            }

            // Verilen sıradaki bağlantıya tıklama
            if (index >= 0 && index < httpLinks.size()) {
                AccessibilityNodeInfo linkNode = httpLinks.get(index);
                if (linkNode != null) {
                    linkNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d(TAG, "Clicked on: " + linkNode.getText().toString());
                } else {
                    Log.d(TAG, "Link node at index " + index + " is not available");
                }
            } else {
                Log.d(TAG, "Index " + index + " is out of bounds. Total links found: " + httpLinks.size());
            }
        } else {
            Log.d(TAG, "Root node is not available");
        }
    }

    // Yardımcı metod: Tüm düğümleri almak için
    private List<AccessibilityNodeInfo> getAllNodes(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> allNodes = new ArrayList<>();
        if (node == null) return allNodes;

        // Kök düğümü ekleyin
        allNodes.add(node);

        // Alt düğümleri ekleyin
        for (int i = 0; i < node.getChildCount(); i++) {
            allNodes.addAll(getAllNodes(node.getChild(i)));
        }

        return allNodes;
    }*/





   /* private void checkAndPrintLinks() {
        Log.d(TAG, "Checking for links");
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            Log.d(TAG, "Root node is available");

            // Sayfadaki tüm düğmeleri ve bağlantıları tarama
            List<AccessibilityNodeInfo> nodes = getAllNodes(rootNode);


            boolean found = false;
            for (AccessibilityNodeInfo node : nodes) {
                if (node != null && node.getClassName().equals("android.widget.TextView")) {
                    CharSequence linkText = node.getText();
                    if (linkText != null && linkText.toString().startsWith("http")) {
                        Log.d(TAG, "Link found: " + linkText.toString());

                        // İlk bağlantıya tıklama
                        if (!found) {
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Log.d(TAG, "Clicked on: " + linkText.toString());
                            found = true;
                        }
                    }
                }
            }
            if (!found) {
                Log.d(TAG, "No HTTP links found");
            }
        } else {
            Log.d(TAG, "Root node is not available");
        }
    }

    // Yardımcı metod: Tüm düğümleri almak için
    private List<AccessibilityNodeInfo> getAllNodes(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> allNodes = new ArrayList<>();
        if (node == null) return allNodes;

        // Kök düğümü ekleyin
        allNodes.add(node);

        // Alt düğümleri ekleyin
        for (int i = 0; i < node.getChildCount(); i++) {
            allNodes.addAll(getAllNodes(node.getChild(i)));
        }

        return allNodes;
    }*/





