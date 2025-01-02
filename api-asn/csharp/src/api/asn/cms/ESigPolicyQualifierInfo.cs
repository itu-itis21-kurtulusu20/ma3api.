using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESigPolicyQualifierInfo : BaseASNWrapper<SigPolicyQualifierInfo>
    {
        public ESigPolicyQualifierInfo(SigPolicyQualifierInfo aObject)
            : base(aObject)
        {
        }
        public ESigPolicyQualifierInfo(int[] oid, Asn1Type openType)
            : base(new SigPolicyQualifierInfo())
        {
            mObject.sigPolicyQualifierId = new Asn1ObjectIdentifier(oid);
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                openType.Encode(encBuf);
            }
            catch (Asn1Exception ex)
            {

                throw new ESYARuntimeException("Cant convert to open type : " + openType, ex);
            }
            mObject.qualifier = new Asn1OpenType(encBuf.MsgCopy);
        }
        public OID getObjectIdentifier()
        {
            return new OID(mObject.sigPolicyQualifierId.mValue);
        }

        public void decodeQualifier(Asn1Type aObject)
        {
            try
            {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mObject.qualifier.mValue);
                aObject.Decode(decBuf);
            }
            catch (Exception ex)
            {
                throw new ESYARuntimeException("Cant convert to open type : " + aObject, ex);
            }
        }


    }
}
