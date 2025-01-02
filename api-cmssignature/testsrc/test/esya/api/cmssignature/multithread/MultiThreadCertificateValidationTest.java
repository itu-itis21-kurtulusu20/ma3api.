package test.esya.api.cmssignature.multithread;

import org.junit.Assert;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MultiThreadCertificateValidationTest extends CMSSignatureTest {

    public static boolean errorOccured = false;

    @Test
    public void multiThreadCertificateValidationTest() throws Exception {
        ECertificate eCertificate = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");
        ValidationPolicy validationPolicy = getPolicy();

        final int numberOfThreads = 50;

        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new MyRunnable(eCertificate, validationPolicy));
            threads.add(thread);
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads.get(i).join();
        }

        Assert.assertFalse(errorOccured);
    }

    static class MyRunnable implements Runnable {
        ECertificate eCertificate;
        ValidationPolicy validationPolicy;

        MyRunnable(ECertificate eCertificate, ValidationPolicy validationPolicy) {
            this.eCertificate = eCertificate;
            this.validationPolicy = validationPolicy;
        }

        @Override
        public void run() {
            try {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);
                params.put(EParameters.P_GRACE_PERIOD, 86400L);

                for (int i = 0; i < 50; i++) {
                    CertRevocationInfoFinder certRevocationInfoFinder = new CertRevocationInfoFinder(true);
                    certRevocationInfoFinder.validateCertificate(eCertificate, params, Calendar.getInstance());
                }
            } catch (Exception ex) {
                errorOccured = true;
            }
        }
    }
}