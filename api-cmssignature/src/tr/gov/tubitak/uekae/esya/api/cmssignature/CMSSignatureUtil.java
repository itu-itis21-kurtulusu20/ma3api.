package tr.gov.tubitak.uekae.esya.api.cmssignature;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECertificateChoices;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponseBytes;
import tr.gov.tubitak.uekae.esya.asn.ocsp._ocspValues;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.List;

public class CMSSignatureUtil {
	
	private static Logger logger =LoggerFactory.getLogger(CMSSignatureUtil.class);
	/**
	 * Add certificate to signed data if signed data does not contain certificate already
	 * @param aSD
	 * @param aCer
	 */
	public static void addCerIfNotExist(ESignedData aSD,ECertificate aCer)
	{
		checkLicense();
    	
		List<ECertificate> cerList = aSD.getCertificates();
		if(cerList!=null)
		{
			if(!cerList.contains(aCer))
			{
				aSD.addCertificateChoices(new ECertificateChoices(aCer));
				if(logger.isDebugEnabled())
					logger.debug("Signer certificate is added to SignedData:"+aCer);
			}
		}
		else
		{
			aSD.addCertificateChoices(new ECertificateChoices(aCer));
			if(logger.isDebugEnabled())
				logger.debug("Signer certificate is added to SignedData:"+aCer);
		}
	}
	
	/**
	 * Add digest algorithm of signer to signed data if signed data does not contain it already
	 * @param aSD
	 * @param aDigestAlg
	 */
	public static void addDigestAlgIfNotExist(ESignedData aSD,EAlgorithmIdentifier aDigestAlg)
	{
		checkLicense();
		
		List<EAlgorithmIdentifier> digAlgs = aSD.getDigestAlgorithmList();
		
		if(!digAlgs.isEmpty())
		{
			if(!digAlgs.contains(aDigestAlg))
			{
				aSD.addDigestAlgorithmIdentifier(aDigestAlg);
				if(logger.isDebugEnabled())
					logger.debug("Signer's digest algorithm is added to SignedData:"+aDigestAlg);
			}
		}
		else
		{
			aSD.addDigestAlgorithmIdentifier(aDigestAlg);
			if(logger.isDebugEnabled())
				logger.debug("Signer's digest algorithm is added to SignedData:"+aDigestAlg);
		}
	}
	/**
	 * Convert basic OCSP response to OCSP response
	 * @param aBOR
	 */
	public static EOCSPResponse convertBasicOCSPRespToOCSPResp(EBasicOCSPResponse aBOR)
	{
		checkLicense();
		
		OCSPResponseStatus respStatus = OCSPResponseStatus.successful();
		ResponseBytes respBytes = new ResponseBytes(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic),new Asn1OctetString(aBOR.getEncoded()));
		EOCSPResponse resp = new EOCSPResponse(new OCSPResponse(respStatus,respBytes));
		return resp;
	}

	public static void checkLicense()
	{
		try
    	{
    		LV.getInstance().checkLD(Urunler.CMSIMZA);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
	}

	public static DigestAlg getDigestAlgFromParameters(DigestAlg definedDigestAlg, AlgorithmParameterSpec spec) throws CMSSignatureException {
		if(spec != null) {
			DigestAlg paramDigestAlg = null;
			if(spec instanceof RSAPSSParams)
				spec = ((RSAPSSParams)spec).toPSSParameterSpec();

			if (spec instanceof PSSParameterSpec) {
				PSSParameterSpec pssSpec = (PSSParameterSpec) spec;
				String digestAlgName = pssSpec.getDigestAlgorithm();
				paramDigestAlg = DigestAlg.fromName(digestAlgName);
			}

			if (definedDigestAlg != null && paramDigestAlg != null && !definedDigestAlg.equals(paramDigestAlg))
				throw new CMSSignatureException("SignatureAlg digest algorithm and AlgorithmParameters digest algorithm is not same");

			if (definedDigestAlg == null && paramDigestAlg != null)
				return paramDigestAlg;
		}

		return definedDigestAlg;
	}

}
