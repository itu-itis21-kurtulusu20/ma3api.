package dev.esya.api.cmssignature.performans.ocsp;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.OCSPClient;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

public class OCSPRequestTest {

    @Test
    public void OCSPTest() throws Exception
    {
        long REQ_COUNT = 10;
        int notNice = 0;
        long total = 0;

        //byte[] certBytes = AsnIO.dosyadanOKU("C:\\Users\\ORCUN\\Downloads\\EImzaTR\\58102214610NES0.cer");

        String certFilePath = "";
        String subCACertFilePath = "";

        byte[] certBytes = AsnIO.dosyadanOKU("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");
        ECertificate certificate = new ECertificate(certBytes);

        byte[] subCABytes = AsnIO.dosyadanOKU("T:\\api-parent\\resources\\unit-test-resources\\certificate\\SubRoot A1.cer");
        ECertificate subCACertificate = new ECertificate(subCABytes);

        System.out.println("OCSP Adress: " + certificate.getOCSPAdresses().get(0));

        for(int i=0; i < REQ_COUNT; i++)
        {
            long start = 0;
            try
            {
                start = System.currentTimeMillis();

                OCSPClient ocspClient = new OCSPClient(certificate.getOCSPAdresses().get(0));
                //ocspClient.setCheckResponseStatus(false);
                ocspClient.openConnection();
                ocspClient.queryCertificate(certificate, subCACertificate);
                //System.out.println(ocspClient.getOCSPResponse().getResponseStatus());

                ocspClient.closeConnection();

                long interval = System.currentTimeMillis() - start;
                //System.out.println(interval);

                System.out.println(interval);

                if (interval > 500)
                {
                    notNice++;
                }

                total = total + interval;
            }
            catch (Exception ex)
            {
                long interval = System.currentTimeMillis() - start;
                total = total + interval;
                System.out.println("Alınamadı! " + ex.getMessage());
            }
        }

        System.out.println("Average: " + total / REQ_COUNT);
        System.out.println("NotNice: " + notNice);
    }

}
