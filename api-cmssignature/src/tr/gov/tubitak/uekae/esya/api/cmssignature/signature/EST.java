package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SignatureTimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.SignatureTimeStampAttrChecker;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class EST extends BES {

	EST(BaseSignedData aSD) {
		super(aSD);
		mSignatureType = ESignatureType.TYPE_EST;
	}

	EST(BaseSignedData aSD, ESignerInfo aSigner) {
		super(aSD, aSigner);
		mSignatureType = ESignatureType.TYPE_EST;
	}

	@Override
	protected void _addUnsignedAttributes(Map<String, Object> aParameters)throws CMSSignatureException {
		_addESTAttributes(aParameters);
	}

	private void _addESTAttributes(Map<String, Object> aParameters)
			throws CMSSignatureException {

		aParameters.put(AllEParameters.P_SIGNER_INFO, mSignerInfo);

		// Add signaturetimestamp attribute
		SignatureTimeStampAttr attr = new SignatureTimeStampAttr();
		attr.setParameters(aParameters);
		attr.setValue();
		mSignerInfo.addUnsignedAttribute(attr.getAttribute());

        boolean isP4;
        try { //if it is P4, add signature timestamp validation data
            isP4 = getSignerInfo().getProfile() == TurkishESigProfile.P4_1;
            if(isP4){
                EAttribute signatureTimeStamp = attr.getAttribute();
                _addTSCertRevocationValues(signatureTimeStamp, aParameters, true);
            }
        } catch (ESYAException e) {
            throw new CMSSignatureException("Error in signature timestamp validation data addition", e);
        }

		Boolean validateTS = (Boolean) aParameters.get(EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING);

		if (validateTS && !isP4) {
			SignatureTimeStampAttrChecker signatureTimeStampAttrChecker = new SignatureTimeStampAttrChecker();
			Map<String, Object> params = new HashMap<String, Object>(aParameters);
			params.remove(AllEParameters.P_SIGNING_CERTIFICATE);
			signatureTimeStampAttrChecker.setParameters(params);
			CheckerResult aCheckerResult = new CheckerResult();
			boolean result = signatureTimeStampAttrChecker.check(this,aCheckerResult);
			if (!result) {
				logger.error(aCheckerResult.toString());
				throw new CMSSignatureException(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TIMESTAMP_INVALID));
			}
		}

		Boolean validateCert = (Boolean) aParameters.get(AllEParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING);

		// If signature type is above from EST, it must validate certificate to
		// collect validation data
		if (mSignatureType == ESignatureType.TYPE_EST && validateCert == false)
			return;
		else {
            ECertificate cer = (ECertificate) aParameters.get(AllEParameters.P_SIGNING_CERTIFICATE);
            // Get time information from timestamp
            Calendar c;
            try {
                c = getTime();
            } catch (ESYAException e) {
                throw new CMSSignatureException(e);
            }
			// Validate certificate for the time in signaturetimestamp
			if (logger.isDebugEnabled()) {
				logger.debug(cer.toString());
				logger.debug("Certificate will be validated according to the time in signaturetimestamp:"
						+ DateUtil.formatDateByDayMonthYear24hours(c.getTime()));
			}
			_validateCertificate(cer,aParameters,c,true);
		}
	}

	@Override
	protected void _convert( ESignatureType aType,
			Map<String, Object> aParameters) throws CMSSignatureException {
		if (aType.equals(ESignatureType.TYPE_BES)
				|| aType.equals(ESignatureType.TYPE_EPES)) {
			_addESTAttributes(aParameters);
		} else if(aType == ESignatureType.TYPE_EST) {
			throw new CMSSignatureException("Signature type is already ES_T.");
		} else
			throw new CMSSignatureException("Signature type:" + aType.name() + " can not be converted to ES_T");
	}

	/*
	 * @Override public ESignatureType getType() { return
	 * ESignatureType.TYPE_EST; }
	 */
}
