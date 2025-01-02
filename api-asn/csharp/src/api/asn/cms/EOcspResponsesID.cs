using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EOcspResponsesID : BaseASNWrapper<OcspResponsesID>
    {
        public EOcspResponsesID(OcspResponsesID aObject)
            : base(aObject)
        {  
        }

        public EResponderID getOcspResponderID()
        {
            return new EResponderID(mObject.ocspIdentifier.ocspResponderID);
        }
        public DateTime? getProducedAt()
        {
            try
            {
                int year = mObject.ocspIdentifier.producedAt.Year;  //for Asn error
                return mObject.ocspIdentifier.producedAt.GetTime();
            }
            catch (Exception x)
            {
                Console.Out.WriteLine(x.ToString());
            }
            return null;
        }

        public byte[] getDigestValue()
        {
            OtherHash hash = mObject.ocspRepHash;
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
            OtherHash hash = mObject.ocspRepHash;
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
