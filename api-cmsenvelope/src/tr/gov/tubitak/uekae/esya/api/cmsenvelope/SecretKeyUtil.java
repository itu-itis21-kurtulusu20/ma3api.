package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by sura.emanet on 16.01.2018.
 */
public class SecretKeyUtil {

    public static void eraseSecretKey(SecretKey key) {
        try {
            //clear the underlying key from memory
            //it was done due to meet the requirements of crypto analysis
            //this is so ugly
            if (key instanceof SecretKeySpec) {
                SecretKeySpec keySpec = (SecretKeySpec) key;
                Field f = SecretKeySpec.class.getDeclaredField("key");
                f.setAccessible(true);
                byte[] keybytes = (byte[]) f.get(keySpec);
                Arrays.fill(keybytes, (byte) 0XCC);
                Arrays.fill(keybytes, (byte) 0XCC);
            }
        }
        catch (Exception ex){
            throw new ESYARuntimeException("Anahtar silinirken hata oluştu.", ex);
        }
    }
}
