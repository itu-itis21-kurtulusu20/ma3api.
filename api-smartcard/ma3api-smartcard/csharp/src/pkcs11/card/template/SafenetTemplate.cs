using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    public class SafenetTemplate : CardTemplate
    {
        protected static readonly List<String> ATR_HASHES = new List<String>();

        public SafenetTemplate(CardType aCardType)
            : base(aCardType)
        {
        }

        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new SafenetOps();
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