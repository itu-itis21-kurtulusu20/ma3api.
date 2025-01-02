package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.UNSUCCESS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EATSHashIndex;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SignatureTimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

public class ATSHashIndexAttrChecker extends BaseChecker
{
	protected static Logger logger = LoggerFactory.getLogger(ATSHashIndexAttrChecker.class);
	private ESignedData mSignedData = null;
    private List<Asn1OctetString> allUnsignedAttrHash = new ArrayList<Asn1OctetString>();
	public ATSHashIndexAttrChecker(ESignedData aSignedData)
	{
		mSignedData = aSignedData;
	}
	
	@Override
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.ATS_HASH_INDEX_ATTRIBUTE_CHECKER), ATSHashIndexAttrChecker.class);
		
		EATSHashIndex atsHashIndex;
		try {	
		EAttribute atsHashIndexAttr = mSignedData.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex).get(0);			
		atsHashIndex =  new EATSHashIndex(atsHashIndexAttr.getValue(0));

		} catch (Exception e) {
			logger.warn("Warning in ATSHashIndexAttrChecker", e);
            aCheckerResult.addMessage(new ValidationMessage("Error while getting ats-hash-index attribute"));
            aCheckerResult.setResultStatus(UNSUCCESS);
			return false;
			//throw new ESYARuntimeException("Error while getting ats-hash-index attribute", e);
		}
		
		List<Asn1OctetString> unsignedAttrHash = Arrays.asList(atsHashIndex.getUnsignedAttrsHashIndex());
		DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());
		
		List<Attribute> unsignedAttr = Arrays.asList(aSigner.getSignerInfo().getUnsignedAttributes().getObject().elements);
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		try {
        for(Attribute attr :unsignedAttr){
        	if(attr !=null){
			if (!attr.type.equals(AttributeOIDs.id_countersignature)
					&& !attr.type.equals(AttributeOIDs.id_aa_ets_archiveTimestampV3)) {
	        	attr.encode(encBuf);
	        	Asn1OctetString digest=new Asn1OctetString(DigestUtil.digest(digestAlg,encBuf.getMsgCopy()));
	        	allUnsignedAttrHash.add(digest);
	        	encBuf.reset();
				if(!unsignedAttrHash.contains(digest)){
		            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.UNSIGNED_ATTRIBUTE_NOT_INCLUDED)));
		            aCheckerResult.setResultStatus(UNSUCCESS);
					return false;
				}
			}
			
			}
	        }
		} catch (ESYAException e) {
			logger.warn("Warning in ATSHashIndexAttrChecker", e);
            aCheckerResult.addMessage(new ValidationMessage("Error while calculating digest of unsigned attributes"));
            aCheckerResult.setResultStatus(UNSUCCESS);
			return false;
			//throw new ESYARuntimeException("Error while calculating digest of unsigned attributes", e);
		}
        if(!_checkArchiveTSAttr(aSigner,unsignedAttrHash,digestAlg) || !_checkCounterSignatureAttr(aSigner, digestAlg)){
            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.UNSIGNED_ATTRIBUTE_NOT_INCLUDED)));
            aCheckerResult.setResultStatus(UNSUCCESS);
			return false;
        }
        for(Asn1OctetString hash : unsignedAttrHash){
        	if(!allUnsignedAttrHash.contains(hash)){
	            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.UNSIGNED_ATTRIBUTE_MISSING)));
	            aCheckerResult.setResultStatus(UNSUCCESS);
				return false;
        	}
        }
        
        aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ATS_HASH_INDEX_ATTRIBUTE_CHECKER_SUCCESSFUL)));
        aCheckerResult.setResultStatus(SUCCESS);   
        return true;

	}
	private boolean _checkArchiveTSAttr(Signer aSigner, List<Asn1OctetString> hashList,DigestAlg aDigestAlg){		
		try {
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			ETSTInfo tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
			Calendar tsTime = tstInfo.getTime();
			
			List<EAttribute> atsv3Attributes = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);			
			for (EAttribute attr : atsv3Attributes) {
				Calendar time = SignatureTimeStampAttr.toTime(attr);
				if (tsTime.after(time)) {
		        	attr.getObject().encode(encBuf);
		        	Asn1OctetString digest=new Asn1OctetString(DigestUtil.digest(aDigestAlg,encBuf.getMsgCopy()));
		        	allUnsignedAttrHash.add(digest);
		        	encBuf.reset();
					if(!hashList.contains(digest)){
						return false;
					}
				}
			}
		} catch (ESYAException e) {
			logger.warn("Warning in ATSHashIndexAttrChecker", e);
			return false;
		}
		return true;
	}
	
	private boolean _checkCounterSignatureAttr(Signer aSigner,DigestAlg aDigestAlg){		
		try {			
			List<EAttribute> counterAttributes = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_countersignature);
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			
			for (EAttribute attr : counterAttributes) {
	        	attr.getObject().encode(encBuf);
	        	Asn1OctetString digest=new Asn1OctetString(DigestUtil.digest(aDigestAlg,encBuf.getMsgCopy()));
	        	allUnsignedAttrHash.add(digest);
	        	encBuf.reset();
			}
		} catch (ESYAException e) {
			logger.warn("Warning in ATSHashIndexAttrChecker", e);
			return false;
		}
		return true;
	}	
}


/*
private EATSHashIndex getATSHashIndex(EAttribute tsAttr){
try {	
EContentInfo ci = new EContentInfo(tsAttr.getValue(0));
ESignedData sd = new ESignedData(ci.getContent());
EAttribute atsHashIndexAttr = sd.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex).get(0);			
return  new EATSHashIndex(atsHashIndexAttr.getValue(0));
} catch (ESYAException e) {
throw new ESYARuntimeException("Error while getting ats-hash-index attribute", e);
}
}
*/