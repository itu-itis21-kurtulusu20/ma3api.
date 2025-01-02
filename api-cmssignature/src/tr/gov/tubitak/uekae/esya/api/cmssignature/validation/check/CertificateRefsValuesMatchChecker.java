package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECertificateValues;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECompleteCertificateReferences;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherCertID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHash;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks if certificate references and certificate values match with each other
 * @author aslihan.kubilay
 *
 */
public class CertificateRefsValuesMatchChecker extends BaseChecker
{
	protected static Logger logger = LoggerFactory.getLogger(CertificateRefsValuesMatchChecker.class);

	private HashMap<Integer,HashMap<DigestAlg,byte[]>> mOzetTablo = new HashMap<Integer,HashMap<DigestAlg,byte[]>>();
	private HashMap<Integer,byte[]> mEncodedCertsMap = new HashMap<Integer, byte[]>();
 	
	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CERTIFICATE_REFERENCES_VALUES_MATCH_CHECKER), CertificateRefsValuesMatchChecker.class);
		
        if (!Boolean.TRUE.equals(getParameters().get(AllEParameters.P_FORCE_STRICT_REFERENCE_USE))) {
    		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CertificateRefsValuesMatchChecker_SUCCESSFUL)));
    		aCheckerResult.setResultStatus(SUCCESS);
    		return true;
        }
        
		List<EAttribute> refsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs);
		
		if(refsAttrs.isEmpty())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_COMPLETE_CERTIFICATE_REFERENCES_IN_SIGNEDDATA)));
			aCheckerResult.setResultStatus(NOTFOUND);
			return false;
		}
		
		EAttribute refsAttr = refsAttrs.get(0);
		ECompleteCertificateReferences certRefs = null;
		try
		{
			certRefs = new ECompleteCertificateReferences(refsAttr.getValue(0));			
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.COMPLETE_CERTIFICATE_REFERENCES_DECODE_ERROR),aEx));
			return false;
		}
		
		
		List<EAttribute> valuesAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certValues);
		
		if(valuesAttrs.isEmpty())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_CERTIFICATE_VALUES_ATTRIBUTE_IN_SIGNEDDATA)));
			aCheckerResult.setResultStatus(NOTFOUND);
			return false;
		}
		
		EAttribute valuesAttr = valuesAttrs.get(0);
		
		ECertificateValues values = null;
		try
		{
			values = new ECertificateValues(valuesAttr.getValue(0));
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CERTIFICATE_VALUES_ATTRIBUTE_DECODE_ERROR),aEx));
			return false;
		}
		
		boolean sonuc = _eslestir(certRefs,values);
		if(!sonuc)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CertificateRefsValuesMatchChecker_UNSUCCESSFUL)));
			return false;
		}
		
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CertificateRefsValuesMatchChecker_SUCCESSFUL)));
		aCheckerResult.setResultStatus(SUCCESS);
		return true;
			
	
	}
	
	
	private boolean _eslestir(ECompleteCertificateReferences aRefs,ECertificateValues aValues)
	{
		OtherCertID[] refs = aRefs.getObject().elements;
		Certificate[] values = aValues.getObject().elements;
		if(refs.length!=values.length){
			return false;
		}
		for(OtherCertID certID:refs)
		{
			try
			{
				if(_eslesenSertifikaBul(certID, values)==false)
					return false;
			}
			catch(Exception e)
			{
				logger.warn("Warning in CertificateRefsValuesMatchChecker", e);
				return false;
			}
		}
		
		return true;
	}
	
	//TODO yeni asn classlarini kullan
	private boolean _eslesenSertifikaBul(OtherCertID aCertID,Certificate[] aValues)
	throws ESYAException
	{
		Asn1OctetString ozet = null;
		DigestAlg alg = null;
		if(aCertID.otherCertHash.getChoiceID()==OtherHash._SHA1HASH)
		{
			ozet = (Asn1OctetString) aCertID.otherCertHash.getElement();
			alg = DigestAlg.SHA1;
		}
		else
		{
			OtherHashAlgAndValue other = (OtherHashAlgAndValue) aCertID.otherCertHash.getElement();
			alg = DigestAlg.fromOID(other.hashAlgorithm.algorithm.value);
			ozet = other.hashValue;
		}
		
		for(int i=0;i<aValues.length;i++)
		{
			byte[] encoded =mEncodedCertsMap.get(i);
			if(encoded==null)
			{
				encoded = _encodeCertificate(aValues[i]);
				mEncodedCertsMap.put(i, encoded);
			}
			
			HashMap<DigestAlg, byte[]> map= mOzetTablo.get(i);
			if(map==null)
			{
				
				byte[] cerOzet = DigestUtil.digest(alg, encoded);
				HashMap<DigestAlg, byte[]> yenimap = new HashMap<DigestAlg, byte[]>();
				yenimap.put(alg,cerOzet);
				mOzetTablo.put(i, yenimap);
				if(Arrays.equals(ozet.value, cerOzet))
					return true;
			}
			else
			{
				byte[] cerOzet = map.get(alg);
				if(cerOzet==null)
				{
					cerOzet = DigestUtil.digest(alg, encoded);
					map.put(alg, cerOzet);
					if(Arrays.equals(ozet.value, cerOzet))
						return true;
				}
				else
				{
					if(Arrays.equals(ozet.value, cerOzet))
						return true;
				}
				
			}
		}
		
		return false;
		
	}
	
	private byte[] _encodeCertificate(Certificate aCer)
	throws Asn1Exception
	{
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		aCer.encode(encBuf);
		return encBuf.getMsgCopy();
	}
}
