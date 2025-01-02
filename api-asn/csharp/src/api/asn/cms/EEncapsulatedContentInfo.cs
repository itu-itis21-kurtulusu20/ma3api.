using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.cms;
using Com.Objsys.Asn1.Runtime;
using System.IO;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EEncapsulatedContentInfo : BaseASNWrapper<EncapsulatedContentInfo>
    {
        public EEncapsulatedContentInfo(EncapsulatedContentInfo aObject)
            : base(aObject)
        {
        }

        public EEncapsulatedContentInfo(Asn1ObjectIdentifier aContentType, Asn1OctetString aContent)
            : base(new EncapsulatedContentInfo())
        {
            setContentType(aContentType);
            setContent(aContent);
        }

        public EEncapsulatedContentInfo(Stream aIns)
            : base(aIns, new EncapsulatedContentInfo())
        {
        }


        public Asn1ObjectIdentifier getContentType()
        {
            return mObject.eContentType;
        }

        public void setContentType(Asn1ObjectIdentifier aContentType)
        {
            mObject.eContentType = aContentType;
        }

        public byte[] getContent()
        {
            if (mObject.eContent != null)
                return mObject.eContent.mValue;

            return null;
        }

        public void setContent(Asn1OctetString aContent)
        {
            mObject.eContent = aContent;
        }
    }
}
