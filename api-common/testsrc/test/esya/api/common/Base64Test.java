package test.esya.api.common;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static junit.framework.TestCase.assertNotNull;

public class Base64Test {

    private static final String testCerString = "MIIGBDCCBOygAwIBAgICCgIwDQYJKoZIhvcNAQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDES" +
            "MBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQKDAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0w" +
            "KwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUgU2VydGlmaWthIE1ha2FtxLEwHhcNMTEwNjI0MTE0" +
            "ODQ3WhcNMTQwMzIwMTI0ODQ3WjBCMQswCQYDVQQGEwJUUjEUMBIGA1UEBRMLNzg5NDU2MTIzMTIx" +
            "HTAbBgNVBAMMFMOWemfDvHIgTXVzdGFmYSBTdWN1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB" +
            "CgKCAQEAr+itKHM+V4Qdac8d3CU52kt+i0r1v50kJNhnCofnVcemMJhWygQc4L49UfTnFBIXXH04" +
            "5KYxMEss6IY53uaGvl130mrYXWhrByNUrHd6jhkL1C1GuT2vBheRtWMXihsIMkeDZS06VK1Ha7on" +
            "OhNQbH+WO8dlql2bbYuMSws/BYchIHg6piPUoYUyfVxnbA2f9guHwe2f/wf8Bh4z275wv8UAGipm" +
            "8HQDNUbgFXwvow1e+36aXuy7zjjyuoSNMrcNYCPN4LiRN3vv+sr/f/mivq8s3UYXCUj9NidYCLcr" +
            "IfP82PYG3I54vAGzvVhGbY4EEP9do/jw8+IOt5wbTV/GaQIDAQABo4ICyDCCAsQwHwYDVR0jBBgw" +
            "FoAU/OhOzZyRByyhQdk8YXJ/lRAv96swHQYDVR0OBBYEFC/uqb//5KF4Ipk9bnMYag092gKSMA4G" +
            "A1UdDwEB/wQEAwIGwDCCATQGA1UdIASCASswggEnMIIBIwYLYIYYAQIBAQUHAQEwggESMC8GCCsG" +
            "AQUFBwIBFiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNX1NVRTCB3gYIKwYBBQUHAgIw" +
            "gdEegc4AQgB1ACAAcwBlAHIAdABpAGYAaQBrAGEALAAgAC4ALgAuAC4AIABzAGEAeQExAGwBMQAg" +
            "AEUAbABlAGsAdAByAG8AbgBpAGsAIAEwAG0AegBhACAASwBhAG4AdQBuAHUAbgBhACAAZwD2AHIA" +
            "ZQAgAG8AbAB1AV8AdAB1AHIAdQBsAG0AdQFfACAAbgBpAHQAZQBsAGkAawBsAGkAIABlAGwAZQBr" +
            "AHQAcgBvAG4AaQBrACAAcwBlAHIAdABpAGYAaQBrAGEAZAExAHIALjB0BgNVHR8EbTBrMCygKqAo" +
            "hiZodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRhcDovL2Rp" +
            "emluLnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsGAQUFBwEB" +
            "BIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNLmNydDA+" +
            "BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5uZXQudHIvQz1UUixPPVRFU1RTTSxDTj1U" +
            "RVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6Ly9vY3NwLnRlc3RzbS5uZXQudHIvMBgGCCsGAQUFBwED" +
            "BAwwCjAIBgYEAI5GAQEwDQYJKoZIhvcNAQEFBQADggEBADOM7PirdSHjZBRsxmX0shdaSTl2DACB" +
            "z2wlb/Y2RyVEapzc4ji9CCJEIhTs7q9812of5FRXlsItT+PFRvuiLNRArLQPxxgAZwOTtowMWIzH" +
            "tw+el8bcqMXSmt7/G/YhN82vO7MZJ//3fzFk1vBWL2Uf38rtfR2PmeWGhiyQoL8xSQo0dBDuD649" +
            "gKSmk0NupuLuSCG8/Y36EovWHJLxw0RT8RBLHZcb0/PRzCrIKDCwTKunK7bENqmz6cyk5fhhxA6d" +
            "wTWqDg6Hw1Cnt66ZDZPSFuUeHj6cgIbgNY2temEhwj6fSE59CGR8WwHOAwVEnZRbi3fsoz7KIDv0" +
            "IZBmMvc=";

    @Test
    public void testBase64Decode() throws CertificateException {

        byte[] decode = Base64.decode(testCerString);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
        assertNotNull(certificate);
    }
}
