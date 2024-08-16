package com.tanriverdi.adstormeterna.chrome.changer;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

//Tamamlandı

public class LocationChanger {

    private Context context;
    private LocationManager locationManager;
    private static final String TAG = "LocationChanger";

    public LocationChanger(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    // Konum sağlayıcıyı ekle
    public void addMockProvider() {
        try {
            // GPS sağlayıcısını sahte konumlar için ekliyoruz
            locationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                    false, false, false, false, true, true, true, 0, 5);
            locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            Log.d(TAG, "Sahte konum sağlayıcı eklendi ve etkinleştirildi");
        } catch (SecurityException e) {
            Log.e(TAG, "Sahte konum sağlayıcı eklenemedi: " + e.getMessage());
        }
    }

    // Sahte konumu ayarla
    public void setMockLocation(double latitude, double longitude) {
        try {
            Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
            mockLocation.setLatitude(latitude);
            mockLocation.setLongitude(longitude);
            mockLocation.setAltitude(0);  // Yükseklik sıfırlanıyor
            mockLocation.setAccuracy(1); // Konum doğruluğu
            mockLocation.setTime(System.currentTimeMillis());
            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            // Konum sağlayıcıya sahte konumu set ediyoruz
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation);

            Log.d(TAG, "Sahte konum ayarlandı: Enlem = " + latitude + ", Boylam = " + longitude);
        } catch (SecurityException e) {
            Log.e(TAG, "Konum ayarlama hatası: " + e.getMessage());
        }
    }

    // Konum değişimini kontrol etme ve devamlı olarak sahte konumu ayarlama
    public void keepMockLocation(double latitude, double longitude) {
        new Thread(() -> {
            while (true) {
                // Sahte konumu sürekli olarak ayarla
                setMockLocation(latitude, longitude);
                try {
                    // Konumu her 60 saniyede bir yeniden ayarla
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Konum yeniden ayarlama hatası: " + e.getMessage());
                }
            }
        }).start();
    }
}

/*private void setupLocationChanger() {
        try {
            // Sahte konum sağlayıcıyı ekleyin
            locationChanger.addMockProvider();

            // Sahte konumu ayarlayın
            double latitude = 37.7749;
            double longitude = -122.4194;
            locationChanger.setMockLocation(latitude, longitude);

            // Sahte konumu sürekli olarak güncelleyin
            locationChanger.keepMockLocation(latitude, longitude);
        } catch (Exception e) {
            Log.e(TAG, "Konum ayarlama hatası: " + e.getMessage());
        }
    }*/

//Play servis hizmetlerinde konum sağlayıcı iznini kapa


// LocationChanger sınıfını başlat
// LocationChanger locationChanger = new LocationChanger(this);
// locationChanger = new LocationChanger(this);
// locationChanger.addMockProvider(); // Konum sağlayıcıyı ekle

// Sahte konumu ayarla (örneğin, 40.7128 enlem ve -74.0060 )
// locationChanger.keepMockLocation(40.7128, -74.0060);

