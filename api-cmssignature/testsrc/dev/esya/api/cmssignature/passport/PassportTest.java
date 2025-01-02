package dev.esya.api.cmssignature.passport;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.asn.passport.CscaMasterList;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

/**
 * Created by orcun.ertugrul on 19-Sep-17.
 */
public class PassportTest
{
    public static void main(String [] args) throws  Exception
    {
        MastertListCreateTest();


    //SmartCardTest();
    //MastertListParseTest();
    //MastertListCreateTest();
    //MasterListValidateTest();
    MastertListParseTest2();








        //passportMasterList.
    }



    private static void MastertListParseTest2() throws Exception
    {
        /*byte [] contentInfoBytes = AsnIO.dosyadanOKU("T:\\api-parent\\resources\\unit-test-resources\\passport\\masterlist\\DE.masterlist.bin");
        PassportMasterListSignedData masterListSignedData = new PassportMasterListSignedData(contentInfoBytes);
        ECertificate [] certs = masterListSignedData.getCertificates();*/



    }

    private static void MasterListValidateTest()
    {
        /*SignatureValidationResult svr = new SignatureValidationResult();
        boolean verified = true;
        Signer signer = signedData.getSignerList().get(0);

        ArrayList<Checker> checkers = new ArrayList<Checker>();
        checkers.add(new CryptoChecker());
        checkers.add(new MessageDigestAttrChecker());
        checkers.add(new SigningCertificateAttrChecker());

        for (Checker checker: checkers)
        {
            CheckerResult cresult = new CheckerResult();
            boolean result = checker.check(signer,cresult);
            svr.addCheckResult(cresult);
            if (verified) verified = result;
        }*/
    }

    private static void MastertListCreateTest()
    {
       /* CscaMasterList masterList = new CscaMasterList();

        ISignable masterListToBeSigned = new SignableByteArray(null);

        BaseSignedData bsd = new BaseSignedData();
        bsd.addContent();

        SignedData sd  = new SignedData();
        sd.version = new CMSVersion(CMSVersion.v3);*/


    }

    private static void MastertListParseTest()
    {
        try
        {
            byte [] contentInfoBytes = AsnIO.dosyadanOKU("C:\\Users\\orcun.ertugrul\\Desktop\\Pasaport\\Data\\DE.masterlist.bin");
            EContentInfo contentInfo = new EContentInfo(contentInfoBytes);

            ESignedData signedData = new ESignedData(contentInfo.getContent());

            //ESignedData signedData = new ESignedData(contentInfoBytes);

            BaseSignedData bsd = new BaseSignedData(contentInfoBytes);

            Signer s = bsd.getSignerList().get(0);


            byte [] masterListData = signedData.getEncapsulatedContentInfo().getContent();

            System.out.println("Signer Certificate: \n" + signedData.getCertificates().get(0).toString());

            Asn1DerDecodeBuffer decodeBuffer = new Asn1DerDecodeBuffer(masterListData);
            CscaMasterList masterList = new CscaMasterList();
            masterList.decode(decodeBuffer);

            Certificate []certs = masterList.certList.elements;

            for ( Certificate cert: certs)
            {
                ECertificate eCert = new ECertificate(cert);
                String str = eCert.toString();

                System.out.println(str);

                String subject = eCert.getSubject().stringValue().replaceAll(":","").replaceAll("/","");

                AsnIO.dosyayaz(eCert.getEncoded(), "C:\\Users\\orcun.ertugrul\\Desktop\\Pasaport\\Data\\DE\\certs\\" + subject + ".cer");
            }
        }
            catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
    }

    public static void SmartCardTest() throws Exception
    {
        /*SmartCard sc = new SmartCard(CardType.NCIPHER);


        long [] slotList = sc.getSlotList();


        for (long slot: slotList)
        {
            CK_SLOT_INFO slotInfo = sc.getSlotInfo(slot);
            System.out.println(slotInfo.slotDescription);

        }

        sc.openSession(slotList[0]);*/



    }
}
