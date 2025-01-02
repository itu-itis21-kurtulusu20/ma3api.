using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class ESC : EST
    {
        public ESC(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_ESC;
        }

        public ESC(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {
            mSignatureType = ESignatureType.TYPE_ESC;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            base._addUnsignedAttributes(aParameters);
            _addESCAttributes(aParameters);
        }

        private void _addESCAttributes(Dictionary<String, Object> aParameters)
        {
            CompleteCertRefAttr certRefs = new CompleteCertRefAttr();
            CompleteRevRefAttr revRefs = new CompleteRevRefAttr();

            certRefs.setParameters(aParameters);
            revRefs.setParameters(aParameters);

            certRefs.setValue();
            revRefs.setValue();
            mSignerInfo.addUnsignedAttribute(certRefs.getAttribute());
            mSignerInfo.addUnsignedAttribute(revRefs.getAttribute());
        }

        //@Override
        override /*protected*/internal void _convert(ESignatureType aType, Dictionary<String, Object> aParameters)
        {
            if (aType.Equals(ESignatureType.TYPE_BES) || aType.Equals(ESignatureType.TYPE_EPES))
            {
                base._convert(aType, aParameters);
                _addESCAttributes(aParameters);
            }
            else if (aType == ESignatureType.TYPE_EST)
            {
                Object cer = null;
                aParameters.TryGetValue(AllEParameters.P_SIGNING_CERTIFICATE, out cer);

                DateTime? d = _getTimeFromSignatureTS(mSignerInfo);
                //CertificateStatusInfo csi = _validateCertificate(cer, aParameters, d);

                CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
                CertificateStatusInfo csi = finder.validateCertificate((ECertificate)cer, aParameters, d);
                List<CertRevocationInfoFinder.CertRevocationInfo> list = finder.getCertRevRefs(csi);
                aParameters[AllEParameters.P_CERTIFICATE_REVOCATION_LIST] = list;

                _addESCAttributes(aParameters);
            }
            else if (aType == ESignatureType.TYPE_ESC)
                throw new CMSSignatureException("Signature is already ES_C.");
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to ES_C");

        }

        /*@Override
        public ESignatureType getType() 
        {
            return ESignatureType.TYPE_ESC;
        }*/
    }
}
