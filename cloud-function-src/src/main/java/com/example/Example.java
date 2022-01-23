package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Example implements HttpFunction {
    
    private static final String encryptionKey = "jidkF$HUc2A#S%kd";

  @Override
  public void service(HttpRequest request, HttpResponse response) throws Exception {
    BufferedWriter writer = response.getWriter();
    Map<String, List<String>> map = request.getQueryParameters();
    
    String pswd = map.get("pswd").get(0);
    String type = map.get("type").get(0);
    
    String output = "";
    
    if(type.equals("encrypt")) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] key = encryptionKey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(pswd.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            output = encoder.encodeToString(cipherText);
            
        } catch (Exception E) {
             System.err.println("Encrypt Exception : "+E.getMessage());
        }
    }
    else if(type.equals("decrypt")) {
    	try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            byte[] key = encryptionKey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(pswd.getBytes("UTF8"));
            output = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : "+E.getMessage());
        }
    }
    
    //output += pswd+" "+type+" "+encryptionKey;
    writer.write(output);
  }
}