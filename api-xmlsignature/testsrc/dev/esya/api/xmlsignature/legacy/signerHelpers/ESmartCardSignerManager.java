package dev.esya.api.xmlsignature.legacy.signerHelpers;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class ESmartCardSignerManager implements ISignerManager {


    protected static ArrayList<P11SmartCard> smartCardList = new ArrayList<P11SmartCard>();



    String _getPinForSlot(int slot)
    {
        String retPin = "12345";
        return retPin;
    }

    protected P11SmartCard getSmartCardForSlot(int slotNo) throws SmartCardException, PKCS11Exception, IOException, LoginException {
        for (P11SmartCard aSC: smartCardList) {
            if(aSC.getSlotNo() == slotNo)
                return aSC;
        }

        String [] terminals = SmartOp.getCardTerminals();
        Pair<Long, CardType> slotAndCardType = SmartOp.getSlotAndCardType(terminals[slotNo-1]);

        P11SmartCard sc = new P11SmartCard(slotAndCardType.getObject2());
        sc.openSession(slotAndCardType.getObject1());
        String PIN = _getPinForSlot(slotAndCardType.getObject1().intValue());
        sc.login(PIN);
        smartCardList.add(sc);

        return sc;
    }

    public BaseSigner getSigner(int signSlotNo, int certSlotNo)
    {
        try {
            P11SmartCard signKeySmartCard = getSmartCardForSlot(signSlotNo);
            P11SmartCard certificateSmartCard = getSmartCardForSlot(certSlotNo);
            byte [] certBytes = certificateSmartCard.getSignatureCertificates().get(0);
            ECertificate signatureCertificate = new ECertificate(certBytes);
            return signKeySmartCard.getSigner(signatureCertificate.asX509Certificate(), Algorithms.SIGNATURE_RSA_SHA256);
        }
        //Önceden exception fırlatılmadığı için burada yakalandı.
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public ECertificate getSigningCertificate(int slot)
    {
        try
        {
            P11SmartCard sc = getSmartCardForSlot(slot);
            byte [] certBytes = sc.getSignatureCertificates().get(0);
            return new ECertificate(certBytes);
        }
        //Önceden exception fırlatılmadığı için burada yakalandı.
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void logout(int slot)
    {
        try
        {
            P11SmartCard signKeySmartCard = getSmartCardForSlot(slot);
            signKeySmartCard.logout();
        }
        //Önceden exception fırlatılmadığı için burada yakalandı.
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
