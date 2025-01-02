using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.pfxsigner;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers
{
    public class ESmartCardSignerManager : ISignerManager
    {
        protected static ArrayList smartCardList = new ArrayList();

        public CardType cardType = CardType.AKIS;

        String _getPinForSlot(int slotNo)
        {
            String retPin = "12345";
            return retPin;
        }

        protected P11SmartCard getSmartCardForSlot(int slotNo)
        {
            foreach (P11SmartCard aSC in smartCardList)
            {
                if (aSC.getSlotNo() == slotNo)
                    return aSC;
            }

            String[] terminals = SmartOp.getCardTerminals();
            Pair<long, CardType> slotAndCardType = SmartOp.getSlotAndCardType(terminals[slotNo - 1]);

            P11SmartCard sc = new P11SmartCard(slotAndCardType.second());
            sc.openSession(slotAndCardType.first());
            String PIN = _getPinForSlot((int)slotAndCardType.first());
            sc.login(PIN);
            smartCardList.Add(sc);

            return sc;
    }

public BaseSigner getSigner(int slot)
        {
            throw new NotImplementedException();
        }

        public BaseSigner getSigner(int signSlotNo, int certSlotNo)
        {
            P11SmartCard signKeySmartCard = getSmartCardForSlot(signSlotNo);
            P11SmartCard certificateSmartCard = getSmartCardForSlot(certSlotNo);
            byte[] certBytes = certificateSmartCard.getSignatureCertificates()[0];

            ECertificate signatureCertificate = new ECertificate(certBytes);
            return signKeySmartCard.getSigner(signatureCertificate, Algorithms.SIGNATURE_RSA_SHA256);
        }

        public ECertificate getSigningCertificate(int slot)
        {
            P11SmartCard sc = getSmartCardForSlot(slot);
            byte[] certBytes = sc.getSignatureCertificates()[0];
            return new ECertificate(certBytes);
        }

        public void logout(int slot)
        {
            P11SmartCard signKeySmartCard = getSmartCardForSlot(slot);
            signKeySmartCard.logout();
        }
    }
}
