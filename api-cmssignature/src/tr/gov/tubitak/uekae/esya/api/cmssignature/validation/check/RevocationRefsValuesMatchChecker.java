package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteRevocationReferences;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ERevocationValues;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.CRLListID;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlOcspRef;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlValidatedID;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspListID;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspResponsesID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHash;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;
import tr.gov.tubitak.uekae.esya.asn.ocsp.BasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponderID;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1UTCTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks if revocation references and revocation values match each other
 * @author aslihan.kubilay
 *
 */
public class RevocationRefsValuesMatchChecker extends BaseChecker
{

	protected static Logger logger = LoggerFactory.getLogger(RevocationRefsValuesMatchChecker.class);
	/*private HashMap<Integer,HashMap<DigestAlg,byte[]>> mCrlHashTable = new HashMap<Integer,HashMap<DigestAlg,byte[]>>();
	private HashMap<Integer,byte[]> mEncodedCrlsMap = new HashMap<Integer, byte[]>();
 	
	private HashMap<Integer,HashMap<DigestAlg,byte[]>> mOCSPHashTable = new HashMap<Integer,HashMap<DigestAlg,byte[]>>();
	private HashMap<Integer,byte[]> mEncodedOcspMap = new HashMap<Integer, byte[]>();*/
 	
	
	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFERENCES_AND_VALUES_MATCH_CHECKER), RevocationRefsValuesMatchChecker.class);
		
        if (!Boolean.TRUE.equals(getParameters().get(AllEParameters.P_FORCE_STRICT_REFERENCE_USE))) {
    		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL)));
    		aCheckerResult.setResultStatus(SUCCESS);
    		return true;
        }
        
		List<EAttribute> refsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs);
		
		if(refsAttrs.isEmpty())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFERENCES_ATTRIBUTE_NOT_FOUND)));
			aCheckerResult.setResultStatus(NOTFOUND);
			return false;
		}
		
		EAttribute refsAttr = refsAttrs.get(0);
		
		ECompleteRevocationReferences revRefs = null;
		try
		{
			revRefs = new ECompleteRevocationReferences(refsAttr.getValue(0));
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFERENCES_ATTRIBUTE_DECODE_ERROR),aEx));
			return false;
		}
		
		
		List<EAttribute> valuesAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationValues);
		
		if(valuesAttrs.isEmpty())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_VALUES_ATTRIBUTE_NOT_FOUND)));
			aCheckerResult.setResultStatus(NOTFOUND);
			return false;
		}
		
		EAttribute valuesAttr = valuesAttrs.get(0);
		
		ERevocationValues values = null;
		try
		{
			values = new ERevocationValues(valuesAttr.getValue(0));
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_VALUES_DECODE_ERROR),aEx));
			return false;
		}

		if(revRefs.getRefCount()!=values.getBasicOCSPResponseCount()+values.getCRLCount())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL)));
			return false;
		}
		
		if(!_eslestir(revRefs, values))
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL)));
			return false;
		}
		
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL)));
		aCheckerResult.setResultStatus(SUCCESS);
		return true;
	}
	
	//TODO yeni asn classlari ile yap
	private boolean _eslestir(ECompleteRevocationReferences aRefs,ERevocationValues aValues)
	{
		CrlOcspRef[] refs = aRefs.getObject().elements;
		ArrayList<CRLListID>  crlRefs = new ArrayList<CRLListID>();
		ArrayList<OcspListID>  ocspRefs = new ArrayList<OcspListID>();
		
		for(CrlOcspRef ref:refs)
		{
			if(ref.crlids!=null && ref.crlids.crls.elements.length != 0)
				crlRefs.add(ref.crlids);
			
			if(ref.ocspids!=null && ref.ocspids.ocspResponses.elements.length!=0)
				ocspRefs.add(ref.ocspids);
		}
		
		boolean crlSonuc = true;
		if(aValues.getObject().crlVals!=null)
		{
			CertificateList[] crls = aValues.getObject().crlVals.elements;
			crlSonuc = _crlEslestir(crlRefs, crls);
		}
			
		boolean ocspSonuc = true;
		if(aValues.getObject().ocspVals!=null)
		{
			BasicOCSPResponse[] ocsps = aValues.getObject().ocspVals.elements;
			ocspSonuc = _ocspEslestir(ocspRefs, ocsps);
		}
		
		return crlSonuc && ocspSonuc;
		
	}
	
	private boolean _crlEslestir(ArrayList<CRLListID> crlRefs,CertificateList[] aCRLs)
	{
		for(CRLListID crlLID:crlRefs)
		{
			CrlValidatedID[] crlVIDList = crlLID.crls.elements;
			for(CrlValidatedID vid:crlVIDList)
			{
				OtherHash hash = vid.crlHash;
				CrlIdentifier id = vid.crlIdentifier;
				try
				{
					if(!_crlkontrol(hash,id,aCRLs)) 
						return false;
				}
				catch(Exception aEx)
				{
					logger.warn("Warning in RevocationRefsValuesMatchChecker", aEx);
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean _ocspEslestir(ArrayList<OcspListID>  aOcspRefs,BasicOCSPResponse[] aOCSPs)
	{
		for(OcspListID ocspLID:aOcspRefs)
		{
			OcspResponsesID[] ocspRIDList = ocspLID.ocspResponses.elements;
			for(OcspResponsesID orid:ocspRIDList)
			{
				OtherHash hash = orid.ocspRepHash;
				OcspIdentifier id = orid.ocspIdentifier;
				try
				{
					if(!_ocspkontrol(hash,id,aOCSPs)) 
						return false;
				}
				catch(Exception aEx)
				{
					logger.warn("Warning in RevocationRefsValuesMatchChecker", aEx);
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean _crlkontrol(OtherHash aHash,CrlIdentifier aId,CertificateList[] aCRLs)
	throws ESYAException
	{
		Asn1OctetString ozet = null;
		DigestAlg alg = null;
		if(aHash.getChoiceID()==OtherHash._SHA1HASH)
		{
			ozet = (Asn1OctetString) aHash.getElement();
			alg = DigestAlg.SHA1;
		}
		else
		{
			OtherHashAlgAndValue other = (OtherHashAlgAndValue) aHash.getElement();
			alg = DigestAlg.fromOID(other.hashAlgorithm.algorithm.value);
			ozet = other.hashValue;
		}
		
		String issuedTime = null;
		Name crlIssuer = null;
		BigInteger crlNumber = null;
		if(aId!=null)
		{
			issuedTime = aId.crlIssuedTime.value;
			crlIssuer = aId.crlissuer;
			
			if(aId.crlNumber!=null)
				crlNumber = aId.crlNumber.value;
		}
		
		for(int i=0;i<aCRLs.length;i++)
		{
			if(aId!=null)
			{
				String thisUpdate = ((Asn1UTCTime)aCRLs[i].tbsCertList.thisUpdate.getElement()).value;//TODO burda generalizedtime da olabilir
				if(!issuedTime.equals(thisUpdate)) continue;
				Name issuer = aCRLs[i].tbsCertList.issuer;
				if(!UtilEsitlikler.esitMi(crlIssuer, issuer)) continue;
				if(crlNumber!=null)
				{
					BigInteger cn = new ECRL(aCRLs[i]).getCRLNumber();
					if(cn==null || !crlNumber.equals(cn)) continue;
				}
			}
			
			byte[] encoded = _encodeCRL(aCRLs[i]);

			byte[] crlOzet = DigestUtil.digest(alg, encoded);
			if(Arrays.equals(ozet.value, crlOzet))
				return true;
		}
		
		return false;
		
	}
	
	private boolean _ocspkontrol(OtherHash aHash,OcspIdentifier aId,BasicOCSPResponse[] aOCSPs)
	throws ESYAException
	{
		Asn1OctetString ozet = null;
		DigestAlg alg = null;
		if(aHash.getChoiceID()==OtherHash._SHA1HASH)
		{
			ozet = (Asn1OctetString) aHash.getElement();
			alg = DigestAlg.SHA1;
		}
		else
		{
			OtherHashAlgAndValue other = (OtherHashAlgAndValue) aHash.getElement();
			alg = DigestAlg.fromOID(other.hashAlgorithm.algorithm.value);
			ozet = other.hashValue;
		}
		
		String producedAt = aId.producedAt.value;
		ResponderID rid = aId.ocspResponderID;
		
		
		for(int i=0;i<aOCSPs.length;i++)
		{
			String time = aOCSPs[i].tbsResponseData.producedAt.value;
			if(!time.equals(producedAt)) continue;
			
			ResponderID id = aOCSPs[i].tbsResponseData.responderID;
			if(!UtilEsitlikler.esitMi(rid, id)) continue;
			
			byte[] encoded = _encodeOCSP(aOCSPs[i]);
			byte[] ocspOzet = DigestUtil.digest(alg, encoded);
			if(Arrays.equals(ozet.value, ocspOzet))
				return true;
		}
		
		return false;
		
	}
	
	private byte[] _encodeCRL(CertificateList aCRL)
	throws Asn1Exception
	{
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		aCRL.encode(encBuf);
		return encBuf.getMsgCopy();
	}
	
	private byte[] _encodeOCSP(BasicOCSPResponse aOCSP)
	throws Asn1Exception
	{
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		aOCSP.encode(encBuf);
		return encBuf.getMsgCopy();
	}

}
