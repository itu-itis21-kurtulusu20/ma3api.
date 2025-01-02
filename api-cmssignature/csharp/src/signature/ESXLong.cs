using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class ESXLong : ESC
    {
        public ESXLong(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_ESXLong;
        }

        public ESXLong(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {
            mSignatureType = ESignatureType.TYPE_ESXLong;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            base._addUnsignedAttributes(aParameters);
            _addESXLongAttributes(aParameters);
        }

        private void _addESXLongAttributes(Dictionary<String, Object> aParameters)
        {
            CertValuesAttr certValues = new CertValuesAttr();
            RevocationValuesAttr revValues = new RevocationValuesAttr();

            certValues.setParameters(aParameters);
            revValues.setParameters(aParameters);

            certValues.setValue();
            revValues.setValue();

            mSignerInfo.addUnsignedAttribute(certValues.getAttribute());
            mSignerInfo.addUnsignedAttribute(revValues.getAttribute());
        }

        override /*protected*/internal void _convert(ESignatureType aType, Dictionary<String, Object> aParameters)
        {
            if (aType == ESignatureType.TYPE_BES || aType == ESignatureType.TYPE_EPES ||
                    aType == ESignatureType.TYPE_EST)
            {
                base._convert(aType, aParameters);
                _addESXLongAttributes(aParameters);
            }
            else if (aType == ESignatureType.TYPE_ESC)
            {
                aParameters[AllEParameters.P_SIGNER_INFO] = mSignerInfo;
                _addESXLongAttributes(aParameters);
            }
            else if (aType == ESignatureType.TYPE_ESXLong)
            {
                throw new CMSSignatureException("Signature type is already ESXLong.");
            }
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to ESXLong");
        }

        /*@Override
        public ESignatureType getType() 
        {
            return ESignatureType.TYPE_ESXLong;
        }*/
    }
}
