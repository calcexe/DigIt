package pl.calc_exe.wykop.extras;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    public static String toMd5(String text) {

        try {
            MessageDigest md5;
            md5 = MessageDigest.getInstance("MD5");
            md5.update(text.getBytes(), 0, text.length());
            byte[] digest = md5.digest();
            StringBuilder sb = new StringBuilder();

            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
