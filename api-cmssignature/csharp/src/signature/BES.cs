using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class BES : Signer
    {
        public BES(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_BES;
        }

        public BES(BaseSignedData aSD, ESignerInfo aSigner)
        {
            mSignedData = aSD;
            mSignerInfo = aSigner;
            mSignatureType = ESignatureType.TYPE_BES;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {

            Object validateCert = null;
            aParameters.TryGetValue(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, out validateCert);
            Object cer = null;
            aParameters.TryGetValue(AllEParameters.P_SIGNING_CERTIFICATE, out cer);
            if (mSignatureType != ESignatureType.TYPE_BES && mSignatureType != ESignatureType.TYPE_EPES)
            {
                _validateCertificate((ECertificate)cer, aParameters, getCurrentTime(aParameters), true);
            }
            else if ((Boolean)validateCert)
            {
                _validateCertificate((ECertificate)cer, aParameters, getCurrentTime(aParameters), false);
            }

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
            if (aType.Equals(ESignatureType.TYPE_BES))
                throw new CMSSignatureException("Signature is already BES.");
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to BES");

        }
        /**
     * Returns  time of signature time stamp
     * @return Calendar
     * @throws ESYAException
     */
        public override DateTime? getTime()
        {
            try
            {
                EAttribute attr = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken)[0];
                EContentInfo contentInfo = new EContentInfo(attr.getValue(0));
                ESignedData sd = new ESignedData(contentInfo.getContent());
                ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                return tstInfo.getTime();
            }
            catch (Asn1Exception ex)
            {
                throw new ESYAException("Cant decode timestamp time", ex);
            }
        }

        private DateTime? getCurrentTime(Dictionary<String, Object> aParameters)
        {
            if (aParameters.ContainsKey(EParameters.P_CURRENT_TIME))
                return ((DateTime?)aParameters[EParameters.P_CURRENT_TIME]);
            return DateTime.UtcNow;
        }
    }
}
