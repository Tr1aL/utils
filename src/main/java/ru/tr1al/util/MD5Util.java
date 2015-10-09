package ru.tr1al.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

public class MD5Util {

    public static String getRandomGUID() {
        String s_id = "";
        try {
            s_id = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        StringBuilder sbValueBeforeMD5 = new StringBuilder(s_id);
        sbValueBeforeMD5.append(":");
        sbValueBeforeMD5.append(Long.toString(System.currentTimeMillis()));
        sbValueBeforeMD5.append(":");
        sbValueBeforeMD5.append(Long.toString(new Random(new SecureRandom().nextLong()).nextLong()));
        return MD5Util.getMD5(sbValueBeforeMD5.toString());
    }

    public static String getMD5(String str) {
        try {
            if (str == null) {
                return null;
            }
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(str.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            String result = hash.toString(16);
            while (result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
