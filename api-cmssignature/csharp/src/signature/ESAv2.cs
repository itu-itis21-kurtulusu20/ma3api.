using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.src.validation.check;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class ESAv2 : ESXLong
    {
        public ESAv2(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_ESA;
        }

        public ESAv2(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {
            mSignatureType = ESignatureType.TYPE_ESA;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            base._addUnsignedAttributes(aParameters);
            _addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken, true);
            _addArchiveAttributes(aParameters);
        }

        private void _addArchiveAttributes(Dictionary<String, Object> aParameters)
        {
            aParameters[AllEParameters.P_SIGNED_DATA] = mSignedData.getSignedData();
            aParameters[AllEParameters.P_SIGNER_INFO] = mSignerInfo;
            ArchiveTimeStampV2Attr timestamp = new ArchiveTimeStampV2Attr();
            timestamp.setParameters(aParameters);
            timestamp.setValue();
            mSignerInfo.addUnsignedAttribute(timestamp.getAttribute());

            byte[] preCalculatedHash = timestamp.getCalculatedMessageDigest();
            Boolean validateTS = (Boolean) aParameters[EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING];
            if (validateTS)
            {
                Dictionary<String, Object> paramaters = new Dictionary<String, Object>(aParameters);
                paramaters.Remove(AllEParameters.P_SIGNING_CERTIFICATE);
                paramaters[AllEParameters.P_PRE_CALCULATED_TIMESTAMP_HASH] = preCalculatedHash;

                SignedDataValidation sdv = new SignedDataValidation();
                SignatureValidationResult svr = sdv.verifyByGivenSigner(this.mSignedData.getEncoded(), this, paramaters);
                if (svr.getSignatureStatus() != Types.Signature_Status.VALID)
                {
                    logger.Error(svr.ToString());
                    throw new CMSSignatureException(svr.ToString());
                }
            }
        }

        override /*protected*/internal void _convert(ESignatureType aType, Dictionary<String, Object> aParamMap)
        {
            if (_checkIfSignerIsESAV3())
            {
                throw new CMSSignatureException("Signer has ArchiveTimeStampV3Attr, ArchiveTimeStampV2Attr cannot be taken");
            }

            aParamMap[AllEParameters.P_SIGNER_INFO] = mSignerInfo;
            if (aType == ESignatureType.TYPE_BES || aType == ESignatureType.TYPE_EPES ||
                    aType == ESignatureType.TYPE_EST || aType == ESignatureType.TYPE_ESC)
            {
                base._convert(aType, aParamMap);
                _addTSCertRevocationValues(aParamMap, AttributeOIDs.id_aa_signatureTimeStampToken, true);
                _addArchiveAttributes(aParamMap);
            }
            else if (aType == ESignatureType.TYPE_ESXLong || aType == ESignatureType.TYPE_ESXLong_Type1 ||
                    aType == ESignatureType.TYPE_ESXLong_Type2 || aType == ESignatureType.TYPE_ESA || aType == ESignatureType.TYPE_ESAv2)
            {
                Asn1ObjectIdentifier tsOID = null;

                if (aType == ESignatureType.TYPE_ESXLong_Type1)
                {
                    tsOID = AttributeOIDs.id_aa_ets_escTimeStamp;
                }
                else if (aType == ESignatureType.TYPE_ESXLong_Type2)
                {
                    tsOID = AttributeOIDs.id_aa_ets_certCRLTimestamp;
                }
                else if (aType == ESignatureType.TYPE_ESXLong)
                {
                    tsOID = AttributeOIDs.id_aa_signatureTimeStampToken;
                } if (aType == ESignatureType.TYPE_ESA || aType == ESignatureType.TYPE_ESAv2)
                {
                    tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV2;
                }

                _addTSCertRevocationValues(aParamMap, tsOID, true);
                _addArchiveAttributes(aParamMap);

            }
            else if (aType == ESignatureType.TYPE_ESX_Type1)
            {
                ESXLong1 esxlong1 = new ESXLong1(mSignedData, mSignerInfo);
                esxlong1._convert(aType, aParamMap);
                _addTSCertRevocationValues(aParamMap, AttributeOIDs.id_aa_ets_escTimeStamp, true);
                _addArchiveAttributes(aParamMap);
            }
            else if (aType == ESignatureType.TYPE_ESX_Type2)
            {
                ESXLong2 esxlong2 = new ESXLong2(mSignedData, mSignerInfo);
                esxlong2._convert(aType, aParamMap);
                _addTSCertRevocationValues(aParamMap, AttributeOIDs.id_aa_ets_certCRLTimestamp, true);
                _addArchiveAttributes(aParamMap);
            }
        }
    }
}
