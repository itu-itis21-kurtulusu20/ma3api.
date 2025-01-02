using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;


namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class AladdinTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        static AladdinTemplate()
	    {
	        ATR_HASHES.Add("3BD5180081313A7D8073C8211030");
            ATR_HASHES.Add("3BD518008131FE7D8073C82110F4");
            ATR_HASHES.Add("3B7F96000080318065B0846160FB120FFD829000");
        }

        public AladdinTemplate(CardType aCardType)
            : base(aCardType)
        {

        }

        public override IPKCS11Ops getPKCS11Ops() 
	    {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new AladdinOps();
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
