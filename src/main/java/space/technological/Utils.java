package space.technological;

import com.google.common.hash.Hashing;


import java.nio.charset.StandardCharsets;

public class Utils {
    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String sha256(String tohash) {
        String sha256hex = Hashing.sha256()
                .hashString(tohash, StandardCharsets.UTF_8)
                .toString();
        return sha256hex;
    }

    public static String md5(String tohash) {
        String md5 = Hashing.md5()
                .hashString(tohash, StandardCharsets.UTF_8)
                .toString();
        return md5;
    }

    public static void info(String text) {
        System.out.println("[INFO]" + text);
    }

    public static void error(String text) {
        System.out.println("[ERROR]" + text);
    }
}
