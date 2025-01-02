using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class ESXLong1 : ESX1
    {
        public ESXLong1(BaseSignedData aSD)
            : base(aSD)
        {

            mSignatureType = ESignatureType.TYPE_ESXLong_Type1;
        }

        public ESXLong1(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {

            mSignatureType = ESignatureType.TYPE_ESXLong_Type1;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            base._addUnsignedAttributes(aParameters);
            _addESXLong1Attributes(aParameters);
        }

        public void _addESXLong1Attributes(Dictionary<String, Object> aParameters)
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


        /*protected*/
        internal override void _convert(ESignatureType aType, Dictionary<String, Object> aParamMap)
        {
            aParamMap[AllEParameters.P_SIGNER_INFO] = mSignerInfo;
            if (aType == ESignatureType.TYPE_BES || aType == ESignatureType.TYPE_EPES ||
                    aType == ESignatureType.TYPE_EST || aType == ESignatureType.TYPE_ESC)
            {
                base._convert(aType, aParamMap);
                _addESXLong1Attributes(aParamMap);
            }
            else if (aType == ESignatureType.TYPE_ESX_Type1)
            {
                _addESXLong1Attributes(aParamMap);
            }
            else if (aType == ESignatureType.TYPE_ESXLong_Type1)
            {
                throw new CMSSignatureException("Signature type is already ESXLong1.");
            }
            else
                throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to ES_XLong1");
        }

        /*@Override
        public ESignatureType getType() 
        {
            return ESignatureType.TYPE_ESXLong_Type1;
        }*/
    }
}
