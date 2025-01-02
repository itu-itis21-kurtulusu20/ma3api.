package dev.esya.api.cmsenvelope.example;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.EEnvelopedData;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyAgreeRecipient;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyTransRecipient;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.KeyAgreeRecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.KeyTransRecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

public class CMSEnvelopeIssuerAndSerialNumberTest {

    @Test
    public void listRecipientIssuerAndSerialInEnvelope() throws Exception {

        byte[] encryptedContent = AsnIO.dosyadanOKU("...\\SifreliIcerik\\..");

        EContentInfo contentInfo = new EContentInfo(encryptedContent);
        EEnvelopedData mEnvelopeData = new EEnvelopedData(contentInfo.getContent());

        RecipientInfo[] aRecipientInfos = mEnvelopeData.getRecipientInfos().elements;
        for(int i = 0; i < aRecipientInfos.length; i++)
        {
            int choice = aRecipientInfos[i].getChoiceID();
            EIssuerAndSerialNumber isAndSerial = null;

            switch(choice)
            {
                case RecipientInfo._KTRI:
                    KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo) aRecipientInfos[i].getElement());
                    isAndSerial = ktr.getIssuerAndSerialNumber();

                    break;

                case RecipientInfo._KARI:
                    KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo) aRecipientInfos[i].getElement());
                    isAndSerial = kar.getIssuerAndSerialNumber();

                    break;
                default:
                    continue;
            }

            if(isAndSerial != null)
            {
                System.out.println("Recipient "+ i + " issuer name  : " + isAndSerial.getIssuer().stringValue());
                System.out.println("Recipient " + i +" serial number: " + StringUtil.toHexString(isAndSerial.getSerialNumber().toByteArray()));
            }
        }
    }
}
