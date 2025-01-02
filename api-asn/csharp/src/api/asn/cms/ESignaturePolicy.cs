using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignaturePolicy : BaseASNWrapper<SignaturePolicy>
    {
        public ESignaturePolicy(SignaturePolicy aObject)
            : base(aObject)
        {

        }

        public ESignaturePolicy(byte[] aBytes)
            : base(aBytes, new SignaturePolicy())
        {

        }
        public ESignaturePolicy(ESignaturePolicyId policyId)
            : base(new SignaturePolicy())
        {
            mObject.Set_signaturePolicyId(policyId.getObject());
        }
        public ESignaturePolicyId getSignaturePolicyId()
        {
            if (mObject.ChoiceID == SignaturePolicy._SIGNATUREPOLICYID)
                return new ESignaturePolicyId((SignaturePolicyId)mObject.GetElement());

            return null;
        }
    }
}
