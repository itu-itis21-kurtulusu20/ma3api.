using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECrlValidatedID : BaseASNWrapper<CrlValidatedID>
    {

        public ECrlValidatedID(CrlValidatedID aObject)
            : base(aObject)
        {
        }

        public EName getCrlissuer()
        {
            return new EName(mObject.crlIdentifier.crlissuer);
        }
        public DateTime? getCrlIssuedTime()
        {
            try
            {
                int year = mObject.crlIdentifier.crlIssuedTime.Year;  //for Asn error
                return mObject.crlIdentifier.crlIssuedTime.GetTime();
            }
            catch (Exception x)
            {
                Console.Out.WriteLine(x.ToString());
            }
            return null;
        }
        public BigInteger getCrlNumber()
        {
            if (mObject.crlIdentifier.crlNumber != null)
                return mObject.crlIdentifier.crlNumber.mValue;
            return null;
        }

        public byte[] getDigestValue()
        {
            OtherHash hash = mObject.crlHash;
            byte[] value = null;
            if (hash.ChoiceID == OtherHash._SHA1HASH)
            {
                value = ((Asn1OctetString)hash.GetElement()).mValue;
            }
            else
            {
                value = ((OtherHashAlgAndValue)hash.GetElement()).hashValue.mValue;
            }
            return value;
        }

        public int[] getDigestAlg()
        {
            OtherHash hash = mObject.crlHash;
            if (hash.ChoiceID == OtherHash._SHA1HASH)
            {
                return _algorithmsValues.sha_1;
            }
            else
            {
                return ((OtherHashAlgAndValue)hash.GetElement()).hashAlgorithm.algorithm.mValue;
            }
        }

    }
}
