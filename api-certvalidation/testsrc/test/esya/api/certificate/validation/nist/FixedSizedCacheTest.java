package test.esya.api.certificate.validation.nist;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.cache.FixedSizedCache;

import java.time.Duration;

public class FixedSizedCacheTest
{
    @Test
    public void addSecondTime() throws Exception
    {
        FixedSizedCache<String, ECertificate> cache = new FixedSizedCache(20, Duration.ofHours(1));

        String ADDRESS = "address";

        ECertificate certificate1 = ECertificate.readFromFile("T:\\api-parent\\resources\\ug\\trusted-certs\\RootC.crt");
        ECertificate certificate2 = ECertificate.readFromFile("T:\\api-parent\\resources\\ug\\trusted-certs\\RootA.crt");


        cache.put(ADDRESS, certificate1);
        cache.put(ADDRESS, certificate2);


        ECertificate cachedCertificate = cache.getItem(ADDRESS);

        Assert.assertTrue(cachedCertificate.equals(certificate2));
    }


    @Test
    public void notAddedCertificateMustBeNull() throws Exception
    {
        String ADDRESS = "address";

        FixedSizedCache<String, ECertificate> cache = new FixedSizedCache(20, Duration.ofHours(1));

        ECertificate cachedCertificate = cache.getItem(ADDRESS);

        Assert.assertNull(cachedCertificate);
    }


    @Test
    public void testStaleTime() throws Exception
    {
        FixedSizedCache<String, ECertificate> cache = new FixedSizedCache(20, Duration.ofMillis(1));

        String ADDRESS = "address";

        ECertificate certificate1 = ECertificate.readFromFile("T:\\api-parent\\resources\\ug\\trusted-certs\\RootC.crt");

        cache.put(ADDRESS, certificate1);

        Thread.currentThread().sleep(10);

        ECertificate cachedCertificate = cache.getItem(ADDRESS);

        Assert.assertNull(cachedCertificate);
    }

    @Test
    public void testReducingCache() throws Exception
    {
        FixedSizedCache<String, ECertificate> cache = new FixedSizedCache(15, Duration.ofMillis(2));

        ECertificate certificate1 = ECertificate.readFromFile("T:\\api-parent\\resources\\ug\\trusted-certs\\RootC.crt");

        for(int i = 0; i < 100; i++)
            cache.put(Integer.toString(i), certificate1);

        Assert.assertTrue(cache.getEntryCount() <= cache.getMaxSize() );
    }

}
