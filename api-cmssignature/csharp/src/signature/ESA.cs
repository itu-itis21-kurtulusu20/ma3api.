using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.src.validation.check;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.signature
{
    public class ESA : BES
    {
        public ESA(BaseSignedData aSD)
            : base(aSD)
        {
            mSignatureType = ESignatureType.TYPE_ESA;
        }

        public ESA(BaseSignedData aSD, ESignerInfo aSigner)
            : base(aSD, aSigner)
        {
            mSignatureType = ESignatureType.TYPE_ESA;
        }

        //@Override
        protected override void _addUnsignedAttributes(Dictionary<String, Object> aParameters)
        {
            base._addUnsignedAttributes(aParameters);
            _addValidationData(aParameters, DateTime.Now);
            //_addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken);
            _addArchiveAttributes(aParameters);

        }
        private void _addValidationData(Dictionary<String, Object> aParameters, DateTime? signTime)
        {
            Object list = null;
            aParameters.TryGetValue(AllEParameters.P_CERTIFICATE_REVOCATION_LIST, out list);
            List<CertRevocationInfoFinder.CertRevocationInfo> certRevList = null;
            if (list != null)
            {
                try
                {
                    certRevList = (List<CertRevocationInfoFinder.CertRevocationInfo>)list;
                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("P_CERTIFICATE_REVOCATION_LIST parameter is not of type List<CertRevocationInfo>", aEx);
                }
            }
            else
            {
                ECertificate cer = (ECertificate)aParameters[AllEParameters.P_SIGNING_CERTIFICATE];
                CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
                CertificateStatusInfo csi = finder.validateCertificate(cer, aParameters, signTime);

                certRevList = finder.getCertRevRefs(csi);
                aParameters[AllEParameters.P_CERTIFICATE_REVOCATION_LIST] = certRevList;
            }

            _addCertRevocationValuesToSignedData(certRevList);
	}

        private void _addArchiveAttributes(Dictionary<String, Object> aParameters)
        {
            aParameters[AllEParameters.P_SIGNED_DATA] = mSignedData.getSignedData();
            aParameters[AllEParameters.P_SIGNER_INFO] = mSignerInfo;

            ArchiveTimeStampAttr timestampAttr = new ArchiveTimeStampAttr();
            timestampAttr.setParameters(aParameters);
            timestampAttr.setValue();
            mSignerInfo.addUnsignedAttribute(timestampAttr.getAttribute());

            byte[] preCalculatedHash = timestampAttr.getCalculatedMessageDigest();
            Boolean validateTS = (Boolean)aParameters[EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING];

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


        

        private void _addAllArchiveValues(Dictionary<String, Object> aParamMap, Asn1ObjectIdentifier aTSOID)
        {
            _addTSCertRevocationValues(aParamMap, aTSOID, mSignedData.checkIfAnyESAv2Exist());
            _addArchiveAttributes(aParamMap);
        }
        override /*protected*/internal void _convert(ESignatureType aType, Dictionary<String, Object> aParamMap)
        {
            bool containsESAv2 = mSignedData.checkIfAnyESAv2Exist(); 
            aParamMap[AllEParameters.P_SIGNER_INFO] = mSignerInfo;

            if (aType == ESignatureType.TYPE_BES || aType == ESignatureType.TYPE_EPES)
            {
                // always validate certificate, fill P_CERTIFICATE_REVOCATION_LIST
                aParamMap[AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = true;
                if (containsESAv2)
                {
                    ESXLong esxlong = new ESXLong(mSignedData, mSignerInfo);
                    esxlong._convert(aType, aParamMap);
                    _addAllArchiveValues(aParamMap, AttributeOIDs.id_aa_signatureTimeStampToken);
                }
                else
                {
                    _addValidationData(aParamMap, DateTime.Now);
                    _addArchiveAttributes(aParamMap);
                }
            }
            else if (aType == ESignatureType.TYPE_EST)
            {
                if (containsESAv2)
                {
                    ESXLong esxlong = new ESXLong(mSignedData, mSignerInfo);
                    esxlong._convert(aType, aParamMap);
                }
                else
                {
                    DateTime? signTime;
                    try
                    {
                        signTime = getTime();
                    }
                    catch (ESYAException e)
                    {
                        throw new CMSSignatureException(e);
                    }
                    _addValidationData(aParamMap, signTime);
                }
                _addAllArchiveValues(aParamMap, AttributeOIDs.id_aa_signatureTimeStampToken);
            }
            else if (aType == ESignatureType.TYPE_ESXLong || aType == ESignatureType.TYPE_ESXLong_Type1 ||
                    aType == ESignatureType.TYPE_ESXLong_Type2 || aType == ESignatureType.TYPE_ESA)
            {
                Asn1ObjectIdentifier tsOID = null;

                //switch (aType)
                //{
                //    case TYPE_ESXLong_Type1:
                //        tsOID = AttributeOIDs.id_aa_ets_escTimeStamp; break;
                //    case TYPE_ESXLong_Type2:
                //        tsOID = AttributeOIDs.id_aa_ets_certCRLTimestamp; break;
                //    case TYPE_ESXLong:
                //        tsOID = AttributeOIDs.id_aa_signatureTimeStampToken; break;
                //    case TYPE_ESA:
                //        tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV2; break;
                //    default://do nothing
                //}

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
                } if (aType == ESignatureType.TYPE_ESA)
                {
                    tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV2;
                }
                if (tsOID == AttributeOIDs.id_aa_ets_archiveTimestampV2 && _checkIfSignerIsESAV3())
                {
                    tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV3;
                }
                _addAllArchiveValues(aParamMap, tsOID);
            }
            else if (aType == ESignatureType.TYPE_ESC || aType == ESignatureType.TYPE_ESX_Type1 || aType == ESignatureType.TYPE_ESX_Type2)
            {
                try
                {
                    _addValidationDataFromReferences(aParamMap);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.ToString());
                    throw new CMSSignatureException(e);
                }
                if (aType == ESignatureType.TYPE_ESC)
                    _addAllArchiveValues(aParamMap, AttributeOIDs.id_aa_signatureTimeStampToken);
                else if (aType == ESignatureType.TYPE_ESX_Type1)
                    _addAllArchiveValues(aParamMap, AttributeOIDs.id_aa_ets_escTimeStamp);
                else if (aType == ESignatureType.TYPE_ESX_Type2)
                    _addAllArchiveValues(aParamMap, AttributeOIDs.id_aa_ets_certCRLTimestamp);
            }
        }
       private void _addValidationDataFromReferences(Dictionary<String, Object> aParamMap) {
		
		List<EAttribute> attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs);
		ECompleteCertificateReferences certRefs;
		certRefs = new ECompleteCertificateReferences(attrs[0].getValue(0));
		
		ValidationInfoResolver vir = (ValidationInfoResolver) aParamMap[EParameters.P_VALIDATION_INFO_RESOLVER];
		ValueFinderFromElsewhere vfe = new ValueFinderFromElsewhere(vir);
		vfe.findCertValues(certRefs).getObject();
        
		attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs);		
		ECompleteRevocationReferences revRefs = new ECompleteRevocationReferences(attrs[0].getValue(0));
		vfe.findRevocationValues(revRefs).getObject();
		
		List<EOCSPResponse> ocsps = new List<EOCSPResponse>();
		foreach (EBasicOCSPResponse basicresponse in vfe.getOCSPs()) {
			EOCSPResponse eocsp = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[] { 1, 2 }, basicresponse.getEncoded())));
			ocsps.Add(eocsp);
		}
		
		_addCertRevocationValuesToSignedData(vfe.getCertificates(),vfe.getCRLs(), ocsps);
	}
    }
}
