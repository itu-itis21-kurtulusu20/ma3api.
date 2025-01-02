using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class NCipherTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        public NCipherTemplate():base(CardType.NCIPHER)
        {            
        }
    
        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new NCipherOps();
                return mIslem;
            }
        }

        public override String[] getATRHashes()
        {
            return ATR_HASHES.ToArray();
        }

        public void addATR(String aATR)
        {
            ATR_HASHES.Add(aATR);
        }

    }
}
