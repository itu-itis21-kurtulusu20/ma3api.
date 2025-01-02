package dev.esya.api.cmssignature.ts;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

public class TSTest {

    public DigestAlg digestAlg = DigestAlg.SHA256;

    public String tsAddress = "http://zdsA1.test3.kamusm.gov.tr";
    public int customerID = 1;
    public String password = "12345678";


    @Test
    public void getTSCertificate()throws  Exception{
        TSSettings tsSettings = new TSSettings(tsAddress, customerID, password, digestAlg);

        byte [] digestValue = RandomUtil.generateRandom(digestAlg.getDigestLength());

        TSClient tsClient = new TSClient();
        tsClient.setDefaultSettings(tsSettings);
        ETimeStampResponse timeStampResponse = tsClient.timestamp(digestValue);

        BaseSignedData signedData = new BaseSignedData(timeStampResponse.getContentInfo().getEncoded());

        ECertificate certificate = signedData.getSignerList().get(0).getSignerCertificate();

        AsnIO.dosyayaz(certificate.getEncoded(),"C:/a/ts.cer");

        System.out.println(certificate);


    }

}
