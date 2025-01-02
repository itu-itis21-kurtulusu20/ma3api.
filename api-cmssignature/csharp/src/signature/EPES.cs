using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class EPES : BES
    {
        public EPES(BaseSignedData aSD)
            : base(aSD)
        {
            //super(aSD);
            mSignatureType = ESignatureType.TYPE_EPES;
        }

        public EPES(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {
            //super(aSD, aSigner);
            mSignatureType = ESignatureType.TYPE_EPES;
        }

        //@Override
        protected override List<IAttribute> _getMandatorySignedAttributes(bool aIsCounter, DigestAlg aAlg)
        {
            List<IAttribute> attributes = new List<IAttribute>();
            attributes.Add(new MessageDigestAttr());
            if (!aIsCounter)
                attributes.Add(new ContentTypeAttr());
            if (aAlg.Equals(DigestAlg.SHA1))
                attributes.Add(new SigningCertificateAttr());
            else
                attributes.Add(new SigningCertificateV2Attr());

            return attributes;
        }

        //@Override
        override /*protected*/internal void _convert(ESignatureType aType, Dictionary<String, Object> aParameters)
        {
            if (aType.Equals(ESignatureType.TYPE_EPES))
                throw new CMSSignatureException("Signature is already TYPE_EPES.");
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to EPES");

        }
    }
}
