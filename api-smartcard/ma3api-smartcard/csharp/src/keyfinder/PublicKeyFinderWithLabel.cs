using System;
using System.Collections.Generic;
using System.Linq;
using Org.BouncyCastle.Math;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;


namespace smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder
{
    public class PublicKeyFinderWithLabel : KeyFinder
    {
        readonly SmartCard mSC;
        readonly long mSessionId;
        readonly String mLabel;

        public PublicKeyFinderWithLabel(SmartCard aSC, long aSessionId, String aLabel)
        {
            mSC = aSC;
            mSessionId = aSessionId;
            mLabel = aLabel;
        }

        public int getKeyLenght()
        {
            ERSAPublicKey publicKeySpec = mSC.readRSAPublicKey(mSessionId, mLabel);
            return publicKeySpec.getModulus().Length*8;
        }
    }
}
