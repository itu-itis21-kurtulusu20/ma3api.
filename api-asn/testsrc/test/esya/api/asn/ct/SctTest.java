package test.esya.api.asn.ct;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Created by omer.dural on 16.03.2018.
 */
public class SctTest {

    @Test
    public void testSctValue() throws Exception {

        final String sslSctCert = "-----BEGIN CERTIFICATE-----\n" +
                "MIIE3zCCA8egAwIBAgIIBsB/djrjFy0wDQYJKoZIhvcNAQELBQAwXDEqMCgGA1UE\n" +
                "AwwhVMOcQsSwVEFLIFRlc3QgQ1QgSW50ZXJtZWRpYXRlIENBMRIwEAYDVQQKDAlU\n" +
                "w5xCxLBUQUsxDTALBgNVBAsTBFRlc3QxCzAJBgNVBAYTAlRSMB4XDTEzMDQxNzA3\n" +
                "MjY0OFoXDTIzMDQxNzA3MTIwNFowMDEOMAwGA1UECgwFVmFsaWQxETAPBgNVBAsT\n" +
                "CFNTTCBBMSAyMQswCQYDVQQGEwJUUjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC\n" +
                "AQoCggEBAIA2eEUdV4ErKFvQeOmLOm6IfXogBjoy+21CClX+Dp2ooocb5Y/j9IU2\n" +
                "mNJibbLwbbbhkGwdbijB1lSMOsxygmD0MtU8yvIa32Y8XIEb2Wpg05h+CCXATVTN\n" +
                "gXYR6ttqajUbD93fjZr+wP6/FbkfM2Y4QnellmaH3CEkiv8HxsOb24zo84Y5xBUD\n" +
                "+cRLgPuDppcKGmFss51uMoHB5LRA2CPJAK5Ve3GszW0dxh3+PUqY+iTnxTGKGJ7a\n" +
                "Oqn91a+970+4LVV+T9tff9Imz38a9Vc7czrJuio+vMbUHUVbNhZm5jEct9f+oXlr\n" +
                "0O87gY9gXRArbFcXhILCmb5yLhmbkn0CAwEAAaOCAc8wggHLMH4GCCsGAQUFBwEB\n" +
                "BHIwcDA9BggrBgEFBQcwAoYxaHR0cDovL2RlcG8udGVzdDIua2FtdXNtLmdvdi50\n" +
                "ci9DVFRlc3RTdWJSb290LmNydDAvBggrBgEFBQcwAYYjaHR0cDovL29jc3BzQTEx\n" +
                "LnRlc3QyLmthbXVzbS5nb3YudHIwHQYDVR0OBBYEFL6wL57wi1teWEDPNF+c77Re\n" +
                "pH0UMAkGA1UdEwQCMAAwHwYDVR0jBBgwFoAUBNLNr3gau/uSmRvg2s29StIAE0Qw\n" +
                "KQYDVR0RBCIwIIIed3d3LnNzbGExLTIudGVzdC5rYW11c20uZ292LnRyMB0GA1Ud\n" +
                "JQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjAYBgNVHSAEETAPMA0GC2CGGAECAQEF\n" +
                "BwEJMA4GA1UdDwEB/wQEAwIGwDCBiQYKKwYBBAHWeQIEAgR7BHkAdwB1ALDMg+Wl\n" +
                "+X1rr3wJzChJBIcqx+iLEyxjULfG/SbhbGx3AAABUVih7ZIAAAQDAEYwRAIgOhMk\n" +
                "4KM9BRFixcvWSYeJQLW8MfXukrZTiSpns1Dm6lYCICTX79NAkRKVEsl9arfsz9Fn\n" +
                "8ZYn9Do1PNUvHl5W3T3rMA0GCSqGSIb3DQEBCwUAA4IBAQC55JRjOSECkqCThB76\n" +
                "MiQdCIvRDKXlB+jUAO5BiVf7pjIRZ6UIIqXSPbX8Xkokd8qeILp2XyZM1qkmny/b\n" +
                "nNzrpBp0alzfI9VDFs6z5HQ+ga65x1B8kUlc0CebTL5IFCT6badcpp2ZaUSnC5eq\n" +
                "5OV+XgmRiObt8DXmo8Uf1fxJHmeoSrUGd0IF0av1yi/tSQ53jMyjCNonLJLh+Mis\n" +
                "osRWkWIYu64Pj+HJHg/nO1GxwD+yJ7TzKwn2rPCaoJ1dLXUcKVCoGigRw+xJZnIY\n" +
                "mmHgq8VC0vV5Pt4bZu8horMei4fUm5m9qnWY9AhbpzkzTWlHHWJRGjdVeM62ZAdA\n" +
                "Kgva\n" +
                "-----END CERTIFICATE-----\n";

        ECertificate certificate = new ECertificate(sslSctCert.getBytes("ASCII"));
        byte[] sctValue = certificate.getSCTValue();
        Assert.assertNotNull(sctValue);
    }
}
