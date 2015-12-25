package cmei.java.random;
import java.security.SecureRandom;
import java.util.UUID;
/***
 * uuid 产生的随机128big 16 byte的字符在时空上重复的概率很小。
 * 里面包含了当前系统时间，MAC地址等。
 * @author cmei
 *
 *
 *# salt secureRandom generated 16 bytes.
String hashedPassword = DigestUtils.md5Hex(password.getValue()+salt);
profiles.put(AccountProfileKey.LOGIN_SECRET, hashedPassword);
profiles.put(AccountProfileKey.LOGIN_SEED, salt);
 */
public class TokenGenerator {

    //seed:auid,aaid for anonymous
    public static String generateToken(String prefix, long seed) {
        String value = String.valueOf(seed);
        int length = value.length();
        String base = value.substring(length - 2);
        String random = UUID.randomUUID().toString().replace("-", "");
        return prefix + random + base;
    }

    public static String generateToken(String prefix) {
        String random = UUID.randomUUID().toString().replace("-", "");
        return prefix + random ;
    }

    //length 50
    public static String genCode(int length) {
        SecureRandom sr = new SecureRandom();
        byte[] binaryCode = new byte[length];
        sr.nextBytes(binaryCode);
        //return Util.base64UrlEncode(binaryCode);
        return null;
    }

}

