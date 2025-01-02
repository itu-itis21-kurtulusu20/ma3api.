using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignaturePolicyId : BaseASNWrapper<SignaturePolicyId>
    {
        public ESignaturePolicyId(SignaturePolicyId aObject)
            : base(aObject)
        {

        }

        public ESignaturePolicyId(byte[] aBytes)
            : base(aBytes, new SignaturePolicyId())
        {

        }
        public ESignaturePolicyId(int[] oid, int[] digestOid, byte[] digestValue)
            : base(new SignaturePolicyId())
        {
            mObject.sigPolicyHash = new OtherHashAlgAndValue(new AlgorithmIdentifier(digestOid), digestValue);
            mObject.sigPolicyId = new Asn1ObjectIdentifier(oid);
        }
        public Asn1ObjectIdentifier getPolicyObjectIdentifier()
        {
            return mObject.sigPolicyId;
        }

        public EOtherHashAlgAndValue getHashInfo()
        {
            return new EOtherHashAlgAndValue(mObject.sigPolicyHash);
        }

        public void addPolicyQualifier(ESigPolicyQualifierInfo qualifier)
        {
            if (mObject.sigPolicyQualifiers == null)
                mObject.sigPolicyQualifiers = new SignaturePolicyId_sigPolicyQualifiers();
            mObject.sigPolicyQualifiers.elements = extendArray(mObject.sigPolicyQualifiers.elements, qualifier.getObject());
        }

        public ESigPolicyQualifierInfo[] getPolicyQualifiers()
        {
            if (mObject.sigPolicyQualifiers == null)
                return new ESigPolicyQualifierInfo[0];
            return wrapArray<ESigPolicyQualifierInfo, SigPolicyQualifierInfo>(mObject.sigPolicyQualifiers.elements, typeof(ESigPolicyQualifierInfo));
        }

    }
}
