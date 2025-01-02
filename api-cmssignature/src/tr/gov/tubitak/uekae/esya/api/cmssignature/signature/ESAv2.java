package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_BES;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_EPES;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESA;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESC;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_EST;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESXLong;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESXLong_Type1;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESXLong_Type2;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESX_Type1;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType.TYPE_ESX_Type2;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;

import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.*;

import java.util.HashMap;
import java.util.Map;

public class ESAv2 extends ESXLong {

	ESAv2(BaseSignedData aSD) {
		super(aSD);
		mSignatureType = ESignatureType.TYPE_ESAv2;
	}

	ESAv2(BaseSignedData aSD, ESignerInfo aSigner) {
		super(aSD, aSigner);
		mSignatureType = ESignatureType.TYPE_ESAv2;
	}

	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters) throws CMSSignatureException {
		
		super._addUnsignedAttributes(aParameters);
		_addTSCertRevocationValues(aParameters, AttributeOIDs.id_aa_signatureTimeStampToken, true);
		_addArchiveAttributes(aParameters);
	}

	private void _addArchiveAttributes(Map<String, Object> aParameters) throws CMSSignatureException {
		aParameters.put(AllEParameters.P_SIGNED_DATA, mSignedData.getSignedData());
		aParameters.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		ArchiveTimeStampV2Attr timestamp = new ArchiveTimeStampV2Attr();
		timestamp.setParameters(aParameters);
		timestamp.setValue();
		mSignerInfo.addUnsignedAttribute(timestamp.getAttribute());

		byte[] preCalculatedHash = timestamp.getCalculatedMessageDigest();
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

	protected void _convert(ESignatureType aType, Map<String, Object> aParamMap) throws CMSSignatureException {

		if(_checkIfSignerIsESAV3()){
			throw new CMSSignatureException("Signer has ArchiveTimeStampV3Attr, ArchiveTimeStampV2Attr cannot be taken");
		}
		
		aParamMap.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);
		if (aType == TYPE_BES || aType == TYPE_EPES || aType == TYPE_EST || aType == TYPE_ESC) {
			super._convert(aType, aParamMap);
			_addTSCertRevocationValues(aParamMap, AttributeOIDs.id_aa_signatureTimeStampToken, true);
			_addArchiveAttributes(aParamMap);
		} 
		else if (aType == TYPE_ESXLong || aType == TYPE_ESXLong_Type1 || aType == TYPE_ESXLong_Type2 || aType == TYPE_ESA || aType == ESignatureType.TYPE_ESAv2) {
			
			Asn1ObjectIdentifier tsOID = null;
			switch (aType) {
			case TYPE_ESXLong_Type1:
				tsOID = AttributeOIDs.id_aa_ets_escTimeStamp;
				break;
			case TYPE_ESXLong_Type2:
				tsOID = AttributeOIDs.id_aa_ets_certCRLTimestamp;
				break;
			case TYPE_ESXLong:
				tsOID = AttributeOIDs.id_aa_signatureTimeStampToken;
				break;
			case TYPE_ESA:
				tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV2;
				break;
			case TYPE_ESAv2: //TODO if ve burası nasıl olacak gerek var mı?
				tsOID = AttributeOIDs.id_aa_ets_archiveTimestampV2;
				break;	
			default:// do nothing
			}

			_addTSCertRevocationValues(aParamMap, tsOID, true);
			_addArchiveAttributes(aParamMap);

		} 
		else if (aType == TYPE_ESX_Type1) {
			ESXLong1 esxlong1 = new ESXLong1(mSignedData, mSignerInfo);
			esxlong1._convert(aType, aParamMap);
			_addTSCertRevocationValues(aParamMap, AttributeOIDs.id_aa_ets_escTimeStamp, true);
			_addArchiveAttributes(aParamMap);
		} 
		else if (aType == TYPE_ESX_Type2) {
			ESXLong2 esxlong2 = new ESXLong2(mSignedData, mSignerInfo);
			esxlong2._convert(aType, aParamMap);
			_addTSCertRevocationValues(aParamMap, AttributeOIDs.id_aa_ets_certCRLTimestamp, true);
			_addArchiveAttributes(aParamMap);
		}
	}

}
