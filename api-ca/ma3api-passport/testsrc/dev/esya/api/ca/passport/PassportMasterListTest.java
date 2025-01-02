package dev.esya.api.ca.passport;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.ca.passport.masterlist.PassportMasterListSignedData;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.util.Calendar;

/**
 * Created by orcun.ertugrul on 04-Oct-17.
 */
public class PassportMasterListTest
{
    @Test
    public void testCreateMasterList() throws Exception
    {
        PassportMasterListSignedData masterListSignedData = new PassportMasterListSignedData();

        ECertificate cert = ECertificate.readFromFile("T:\\api-parent\\resources\\test-root-cert\\RootA.crt");

        masterListSignedData.addCertificate(cert);

        //SmartCardManager.getInstance().getSigner();


        P11SmartCard sc = new P11SmartCard(CardType.AKIS);
        ECertificate signerCert = new ECertificate(sc.getSignatureCertificates().get(0));
        sc.login("12345");
        BaseSigner signer = sc.getSigner(signerCert.asX509Certificate(), CipherAlg.RSA_PKCS1.toString());


        masterListSignedData.sign(signer, signerCert);

        byte [] encodedMasterList = masterListSignedData.getEncoded();

        AsnIO.dosyayaz(encodedMasterList, "T:\\api-parent\\resources\\unit-test-resources\\passport\\masterlist\\TR.masterlist.bin");

    }

    @Test
    public void parseMasterList() throws Exception
    {
        //Download from icaopkd, and copy "CscaMasterListData::" field data to another file.

        byte [] base64Bytes = AsnIO.dosyadanOKU("D:\\Projeler\\Pasaport\\Data\\icaopkd-002-ml-000098.bin");
        byte [] masterListBytes= Base64.decode(base64Bytes, 0, base64Bytes.length);

        PassportMasterListSignedData masterList = new PassportMasterListSignedData(masterListBytes);


        ECertificate []certs =  masterList.getCertificates();

        for (ECertificate aCert:certs) {


            String filePath = "D:\\Projeler\\Pasaport\\Data\\MasterList\\" + aCert.getSubject().getCountryNameAttribute() + "-" +
                    aCert.getNotBefore().get(Calendar.YEAR)  + "-" + aCert.getSerialNumberHex() + ".cer";
            if(new File(filePath).exists())
                throw new Exception("File Exist: " + filePath);


            AsnIO.dosyayaz(aCert.getEncoded(), filePath);

            System.out.println(aCert.toString());



        }

    }
}
