package dev.esya.api.smartcard;

import org.junit.Test;
import sun.security.x509.X509Key;

public class SunSecurityTest {

    @Test
    public void testSunSecurity(){
        X509Key key = new X509Key();
        System.out.println(key.getClass().getName().equals("sun.security.x509.X509Key"));
    }


}
