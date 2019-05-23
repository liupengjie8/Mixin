package main.java.mixin_labs.java.boot;
import main.java.mixin_labs.java.sdk.MixinUtil;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import main.java.mixin_labs.java.sdk.PrivateKeyReader;
public class Config {

public static final String CLIENT_ID     = "6accf13d-26bb-4af9-9402-41d8651aa815";
public static final String CLIENT_SECRET = "c62cfb6e4981506a0593c0c53a1934713bb95a82b6143eab1c6c7154279abb2d";
public static final String PIN           = "427812";
public static final String SESSION_ID    = "ceaa1fea-1f5f-47d1-9cff-b657bdaf6dac";
public static final String PIN_TOKEN     = "ciL5DelhhPpvTfea2wo4QePqTSnbUeSTVoePUZggehxGDP3pmuctSNzE54KLI+U7CyNUXpxX0GaHFKykBqi9JQTZbfMbQlkuuzCleJ89r5/WKWh8oiHxIq6GnaSMaTxhh3AdaTM5DJouN9+D8toSqVKSfMwzqNnOpCJTdBByauU=";

  private static RSAPrivateKey loadPrivateKey() {
    try {

      PrivateKey key =
        new PrivateKeyReader(Config.class.getClassLoader().getResourceAsStream("rsa_private_key.txt"))
          .getPrivateKey();
      System.out.println(key);
      return (RSAPrivateKey) key;
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
      return null;
    }
  }

  public static final RSAPrivateKey RSA_PRIVATE_KEY = loadPrivateKey();
  public static final byte[] PAY_KEY = MixinUtil.decrypt(RSA_PRIVATE_KEY, PIN_TOKEN, SESSION_ID);
}