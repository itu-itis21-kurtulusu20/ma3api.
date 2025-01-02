package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.PKCS11Exception;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PKCS11ExceptionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PKCS11ExceptionFactory.class);

    protected static final Constructor<?> constructor = PKCS11Exception.class.getDeclaredConstructors()[0];

    public static PKCS11Exception getPKCS11Exception(long errorCode, String message) {
        if (constructor.getParameterCount() > 1) {
            // in this case, expecting Java 17... PKCS11Exception (long, String)
            try {
                return (PKCS11Exception) constructor.newInstance(errorCode, message);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            // Java 8-16 PKCS11Exception (long)
            if (message != null) {
                LOGGER.warn("PKCS11Exception: ({}) {}", errorCode, message);
            }
            return new PKCS11Exception(errorCode);
        }
    }

    public static PKCS11Exception getPKCS11Exception(long errorCode) {
        return getPKCS11Exception(errorCode, null);
    }
}
