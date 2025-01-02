using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    class TKartTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        static TKartTemplate()
	    {
	        //ATR_HASHES.Add("3BF2180002C10A31FE58C80874");//Net-ID kartlarının ATR'si ile aynı
	    }
        public TKartTemplate()
            : base(CardType.TKART)
        {
        }        

        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new TKartOps();
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
