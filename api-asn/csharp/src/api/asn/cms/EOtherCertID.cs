
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EOtherCertID : BaseASNWrapper<OtherCertID>
    {
        public EOtherCertID(OtherCertID aObject)
            : base(aObject)
        {
        }

        public EGeneralNames getIssuerName()
        {

            return new EGeneralNames(mObject.issuerSerial.issuer);
        }

        public BigInteger getIssuerSerial()
        {
            return mObject.issuerSerial.serialNumber.mValue;
        }

        public byte[] getDigestValue()
        {
            OtherHash hash = mObject.otherCertHash;
            byte[] value = null;
            if (hash.ChoiceID == OtherHash._SHA1HASH)
            {
                value = ((Asn1OctetString) hash.GetElement()).mValue;
            }
            else
            {
                value = ((OtherHashAlgAndValue) hash.GetElement()).hashValue.mValue;
            }
            return value;
        }

        public int[] getDigestAlg()
        {
            OtherHash hash = mObject.otherCertHash;
            if (hash.ChoiceID == OtherHash._SHA1HASH)
            {
                return _algorithmsValues.sha_1;
            }
            else
            {
                return ((OtherHashAlgAndValue) hash.GetElement()).hashAlgorithm.algorithm.mValue;
            }
        }
    }
}