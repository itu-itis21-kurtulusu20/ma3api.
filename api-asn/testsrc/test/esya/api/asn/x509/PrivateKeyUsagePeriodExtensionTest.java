package test.esya.api.asn.x509;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EPrivateKeyUsagePeriodExtension;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.Calendar;

/**
 * Created by ramazan.girgin on 26.12.2016.
 */
public class PrivateKeyUsagePeriodExtensionTest {
    @Test
    public void canSetNotBeforeAndNotAfter() {
        EPrivateKeyUsagePeriodExtension ePrivateKeyUsagePeriod = new EPrivateKeyUsagePeriodExtension();
        Calendar notBefore = Calendar.getInstance();
        Calendar notAfter = Calendar.getInstance();
        ePrivateKeyUsagePeriod.setNotBefore(notBefore);
        notAfter.add(Calendar.YEAR, 3);
        ePrivateKeyUsagePeriod.setNotAfter(notAfter);
        Assert.assertNotNull(ePrivateKeyUsagePeriod.getNotAfter());
        Assert.assertNotNull(ePrivateKeyUsagePeriod.getNotBefore());
    }

    @Test
    public void canEncodable() {
        EPrivateKeyUsagePeriodExtension ePrivateKeyUsagePeriod = new EPrivateKeyUsagePeriodExtension();
        Calendar notBefore = Calendar.getInstance();
        Calendar notAfter = Calendar.getInstance();
        ePrivateKeyUsagePeriod.setNotBefore(notBefore);
        notAfter.add(Calendar.YEAR, 3);
        byte[] encoded = ePrivateKeyUsagePeriod.getEncoded();
        Assert.assertNotNull(encoded);
    }

    @Test
    public void canDecodable() throws ESYAException {
        EPrivateKeyUsagePeriodExtension ePrivateKeyUsagePeriod = new EPrivateKeyUsagePeriodExtension();
        Calendar notBefore = Calendar.getInstance();
        Calendar notAfter = Calendar.getInstance();
        ePrivateKeyUsagePeriod.setNotBefore(notBefore);
        notAfter.add(Calendar.YEAR, 3);
        byte[] encoded = ePrivateKeyUsagePeriod.getEncoded();

        EPrivateKeyUsagePeriodExtension newPK = new EPrivateKeyUsagePeriodExtension(encoded);
        Assert.assertTrue(newPK != null);

    }

    @Test
    public void canEncodableAndGetDates() throws ESYAException {
        EPrivateKeyUsagePeriodExtension ePrivateKeyUsagePeriod = new EPrivateKeyUsagePeriodExtension();
        Calendar notBefore = Calendar.getInstance();
        Calendar notAfter = Calendar.getInstance();
        ePrivateKeyUsagePeriod.setNotBefore(notBefore);
        notAfter.add(Calendar.YEAR, 3);
        ePrivateKeyUsagePeriod.setNotAfter(notAfter);
        byte[] encoded = ePrivateKeyUsagePeriod.getEncoded();

        EPrivateKeyUsagePeriodExtension newPK = new EPrivateKeyUsagePeriodExtension(encoded);
        Assert.assertTrue(newPK != null);

        Calendar encodedNotBefore = newPK.getNotBefore();
        Calendar encodedNotAfter = newPK.getNotAfter();

        Assert.assertTrue(notBefore.getTime().toString().equals(encodedNotBefore.getTime().toString()));
        Assert.assertTrue(notAfter.getTime().toString().equals(encodedNotAfter.getTime().toString()));
    }
}
