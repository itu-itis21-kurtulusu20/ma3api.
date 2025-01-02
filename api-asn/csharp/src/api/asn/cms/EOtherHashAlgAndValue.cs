using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EOtherHashAlgAndValue : BaseASNWrapper<OtherHashAlgAndValue>
    {
        public EOtherHashAlgAndValue(OtherHashAlgAndValue aObject)
            : base(aObject)
        {

        }

        public EOtherHashAlgAndValue(byte[] aBytes)
            : base(aBytes, new OtherHashAlgAndValue())
        {

        }

        public EAlgorithmIdentifier getHashAlg()
        {
            return new EAlgorithmIdentifier(mObject.hashAlgorithm);
        }

        public byte[] getHashValue()
        {
            return mObject.hashValue.mValue;
        }
    }
}
