package dev.esya.api.cmssignature.smartcard.atr;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

public class ATRTest {

    @Test
    public void getATR() throws SmartCardException {

        String[] cardATRs = SmartOp.getCardATRs();

        for (String atr : cardATRs)
        {
            System.out.println(atr);
        }

    }

}
