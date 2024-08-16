package com.tanriverdi.adstormeterna.fingerprint;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

//Tamamlandı

public class AesCrypt {
    private static final String TAG = "AesCrypt";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"; //şifreleme dönüşüm algoritması
    private static final String SECRET_KEY = "1234567890123456"; // 16-byte anahtar (128 bit)
    private static final String INIT_VECTOR = "1234567890123456"; // 16-byte IV (128 bit)
    private final SecretKey secretKey;
    private final IvParameterSpec ivParameterSpec;
    private Context context;


    // Constructor, anahtar ve IV'yi başlatır
    public AesCrypt(Context context) {
        this.context= context;
        this.secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        this.ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
        Log.d(TAG, "AesCrypt nesnesi oluşturuldu");
    }


    // Veriyi şifreler Base64 formatında
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(encrypted);
            }
            Log.d(TAG, "Veri başarıyla şifrelendi");
            return data;
        } catch (Exception e) {
            Log.e(TAG, "Veri şifrelenemedi", e);
            throw new RuntimeException("Veri şifrelenemedi", e);
        }
    }

}

