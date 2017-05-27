package sample.module.chiclaim.com.retrofit.custom_parameter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final int LO_BYTE = 15;
    private static final int MOVE_BIT = 4;
    private static final int HI_BYTE = 240;
    private static final String[] HEX_DIGITS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private MD5Util() {
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            buf.append(byteToHexString(b[i]));
        }

        return buf.toString();
    }

    private static String byteToHexString(byte b) {
        return HEX_DIGITS[(b & 240) >> 4] + HEX_DIGITS[b & 15];
    }

    public static String encode(String origin) {
        if(origin == null) {
            throw new IllegalArgumentException("MULTI_000523");
        } else {
            String resultString = null;
            resultString = new String(origin);

            try {
                MessageDigest e = MessageDigest.getInstance("MD5");

                try {
                    resultString = byteArrayToHexString(e.digest(resultString.getBytes("UTF-8")));
                } catch (UnsupportedEncodingException var4) {
                    var4.printStackTrace();
                }

                return resultString;
            } catch (NoSuchAlgorithmException var5) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(encode("的是非得失法"));
    }
}
