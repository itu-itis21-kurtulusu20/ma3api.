package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteCertificateReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteRevocationReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ValueFinderFromElsewhere;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.ArchiveTimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponseBytes;

import java.util.*;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.*;

public class ESA extends BES {

	protected static Logger logger = LoggerFactory.getLogger(ESA.class);

	ESA(BaseSignedData aSD)
	{
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESA;
	}
	
	ESA(BaseSignedData aSD,ESignerInfo aSigner)
	{
		super(aSD,aSigner);
		mSignatureType = ESignatureType.TYPE_ESA;
	}
	
	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) 
	throws CMSSignatureException 
	{
		super._addUnsignedAttributes(aParameters);
		_addValidationData(aParameters, Calendar.getInstance());
		//_addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken);
		_addArchiveAttributes(aParameters);
		
	}
	
	@SuppressWarnings("unchecked")
	private void _addValidationData(Map<String, Object> aParameters, Calendar signTime)
			throws CMSSignatureException {
		Object list = aParameters.get(AllEParameters.P_CERTIFICATE_REVOCATION_LIST);
		List<CertRevocationInfo> certRevList = null;
		if (list != null) {
			try {
				certRevList = (List<CertRevocationInfo>) list;
			} catch (ClassCastException aEx) {
				throw new CMSSignatureException("P_CERTIFICATE_REVOCATION_LIST parameter is not of type List<CertRevocationInfo>",aEx);
			}

		} else {
			ECertificate cer = (ECertificate) aParameters.get(AllEParameters.P_SIGNING_CERTIFICATE);
			CertRevocationInfoFinder finder = new CertRevocationInfoFinder(true);
			
			CertificateStatusInfo csi = finder.validateCertificate(cer,aParameters, signTime);
			certRevList = finder.getCertRevRefs(csi);
			aParameters.put(AllEParameters.P_CERTIFICATE_REVOCATION_LIST, list);
		}
		_addCertRevocationValuesToSignedData(certRevList);
	}
	
	private void _addArchiveAttributes(Map<String, Object> aParameters)
			throws CMSSignatureException {
		aParameters.put(AllEParameters.P_SIGNED_DATA, mSignedData.getSignedData());
		aParameters.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		ArchiveTimeStampAttr timestampAttr = new ArchiveTimeStampAttr();
		timestampAttr.setParameters(aParameters);
		timestampAttr.setValue();
		mSignerInfo.addUnsignedAttribute(timestampAttr.getAttribute());
		
		byte[] preCalculatedHash = timestampAttr.getCalculatedMessageDigest();
		Boolean validateTS = (Boolean) aParameters.get(EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING);
		if (validateTS) {
			Map<String, Object> params = new HashMap<String, Object>(aParameters);
			params.remove(AllEParameters.P_SIGNING_CERTIFICATE);
			params.put(AllEParameters.P_PRE_CALCULATED_TIMESTAMP_HASH, preCalculatedHash);

			SignedDataValidation sdv = new SignedDataValidation();
			SignatureValidationResult svr = sdv.verifyByGivenSigner(this.mSignedData.getEncoded(), this, params);
			if(svr.getSignatureStatus() != Types.Signature_Status.VALID){
				logger.error(svr.toString());
				throw new CMSSignatureException(svr.toString());
			}
		}
				
	}

	private void _addAllArchiveValues(Map<String,Object> aParamMap, Asn1ObjectIdentifier aTSOID) throws CMSSignatureException{
		_addTSCertRevocationValues(aParamMap, aTSOID, mSignedData.checkIfAnyESAv2Exist());
		_addArchiveAttributes(aParamMap);		
	}
	protected void _convert(ESignatureType aType,Map<String,Object> aParamMap)
	throws CMSSignatureException
	{
		boolean containsESAv2 = mSignedData.checkIfAnyESAv2Exist(); 
		aParamMap.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		
		if(aType == TYPE_BES ||aType == TYPE_EPES)
		{
			// always validate certificate, fill P_CERTIFICATE_REVOCATION_LIST
			aParamMap.put(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, true);
			if(containsESAv2){
				ESXLong esxlong = new ESXLong(mSignedData,mSignerInfo);
				esxlong._convert(aType,aParamMap);
				_addAllArchiveValues(aParamMap, AttributeOIDs.id_aa_signatureTimeStampToken);
			}
			else{
				_addValidationData(aParamMap, Calendar.getInstance());
				_addArchiveAttributes(aParamMap);
			}
		}
		else if(aType == TYPE_EST){
			if(containsESAv2){
				ESXLong esxlong = new ESXLong(mSignedData,mSignerInfo);
				esxlong._convert(aType,aParamMap);				
			}
			else{
				Calendar signTime;
				try {
					signTime = getTime();
				} catch (ESYAException e) {
					throw new CMSSignatureException(e);
				}
				_addValidationData(aParamMap, signTime);
			}
			_addAllArchiveValues(aParamMap , AttributeOIDs.id_aa_signatureTimeStampToken);
		}
		else if(aType == TYPE_ESXLong || aType == TYPE_ESXLong_Type1 || 
				aType == TYPE_ESXLong_Type2 || aType == TYPE_ESA)
		{
			Asn1ObjectIdentifier tsOID = null;
			
			switch(aType)
			{
				case TYPE_ESXLong_Type1:
					tsOID = AttributeOIDs.id_aa_ets_escTimeStamp;break;
				case TYPE_ESXLong_Type2:
					tsOID = AttributeOIDs.id_aa_ets_certCRLTimestamp;break;
				case TYPE_ESXLong:
					tsOID = AttributeOIDs.id_aa_signatureTimeStampToken;break;
				case TYPE_ESA:
					tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV2;break;
				default://do nothing
			}
			if(tsOID == AttributeOIDs.id_aa_ets_archiveTimestampV2 && _checkIfSignerIsESAV3()){
				tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV3;
			}
			_addAllArchiveValues(aParamMap,tsOID);
		}

		else if (aType == TYPE_ESC || aType == TYPE_ESX_Type1 || aType == TYPE_ESX_Type2) {
			try {
				_addValidationDataFromReferences(aParamMap);
			} catch (Exception e) {
				logger.error("Error in ESA", e);
				throw new CMSSignatureException(e);
			}
			if (aType == TYPE_ESC)
				_addAllArchiveValues(aParamMap,AttributeOIDs.id_aa_signatureTimeStampToken);
			else if (aType == TYPE_ESX_Type1)
				_addAllArchiveValues(aParamMap,AttributeOIDs.id_aa_ets_escTimeStamp);
			else if (aType == TYPE_ESX_Type2)
				_addAllArchiveValues(aParamMap,AttributeOIDs.id_aa_ets_certCRLTimestamp);
		}
	}
	
	private void _addValidationDataFromReferences(Map<String, Object> aParamMap) throws ESYAException {

		try {

			List<EAttribute> attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs);
			ECompleteCertificateReferences certRefs = null;
			certRefs = new ECompleteCertificateReferences(attrs.get(0).getValue(0));

			ValidationInfoResolver vir = (ValidationInfoResolver) aParamMap.get(EParameters.P_VALIDATION_INFO_RESOLVER);
			ValueFinderFromElsewhere vfe = new ValueFinderFromElsewhere(vir);
			vfe.findCertValues(certRefs).getObject();

			attrs = mSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs);
			ECompleteRevocationReferences revRefs = new ECompleteRevocationReferences(attrs.get(0).getValue(0));
			vfe.findRevocationValues(revRefs).getObject();

			List<EOCSPResponse> ocsps = new ArrayList<EOCSPResponse>();
			for (EBasicOCSPResponse basicresponse : vfe.getOCSPs()) {
				EOCSPResponse eocsp = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1, 2}, basicresponse.getEncoded())));
				ocsps.add(eocsp);
			}

			_addCertRevocationValuesToSignedData(vfe.getCertificates(), vfe.getCRLs(), ocsps);

		}  catch (Exception e){
			throw new ESYAException(e);
		}

	}
}