package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.TimeStampSignatureChecker;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;

import java.text.MessageFormat;
import java.util.Calendar;

/**
 * The signature-time-stamp attribute is a TimeStampToken computed on the signature value for a specific
 * signer; it is an unsigned attribute.
 * 
 * (etsi 101733v010801 6.1.1)
 * @author aslihan.kubilay
 *
 */

public class SignatureTimeStampAttr extends AttributeValue
{
	private static final DigestAlg OZET_ALG = DigestAlg.SHA256; 
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_signatureTimeStampToken;

	public SignatureTimeStampAttr ()
	{
		super();
	}

	public void setValue() throws CMSSignatureException
	{
		if(logger.isDebugEnabled())
			logger.debug("Adding signaturetimestamp attribute...");

		ESignerInfo signerInfo = (ESignerInfo) mAttParams.get(AllEParameters.P_SIGNER_INFO);

		Object digestAlgO = mAttParams.get(AllEParameters.P_TS_DIGEST_ALG);
		if (digestAlgO == null)
		{
			digestAlgO = OZET_ALG;
		}

		Object tssSpec = mAttParams.get(AllEParameters.P_TSS_INFO);
		if (tssSpec == null)
		{
			throw new NullParameterException("P_TSS_INFO parameter value is not set");
		}

		DigestAlg digestAlg = null;
		try
		{
			digestAlg = (DigestAlg) digestAlgO;
		} 
		catch (ClassCastException aEx)
		{
			throw new CMSSignatureException("P_TS_DIGEST_ALG parameter is not of type DigestAlg",aEx);
		}

		//timestamp server information
		TSSettings tsSettings = null;
		try
		{
			tsSettings = (TSSettings) tssSpec;
		} 
		catch (ClassCastException aEx)
		{
			throw new CMSSignatureException("P_TSS_INFO parameter is not of type TSSettings",aEx);
		}
		
        //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
        if(digestAlg==tsSettings.getDigestAlg() && digestAlgO != null)
        	logger.debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
        digestAlg=tsSettings.getDigestAlg();

		//The value of the messageImprint field within TimeStampToken shall be a hash of the value of the signature
		//field within SignerInfo for the signedData being time-stamped.
		ContentInfo token = new ContentInfo();
		try
		{
			TSClient tsClient = new TSClient();
			token = tsClient.timestamp(DigestUtil.digest(digestAlg, signerInfo.getSignature()), tsSettings).getContentInfo().getObject();
		} 
		catch (Exception aEx)
		{
			if(logger.isDebugEnabled())
				logger.error("Error while getting signaturetimestamp",aEx);
			throw new CMSSignatureException("Error while getting signaturetimestamp", aEx);
		}

		if(token == null)
		{
			if(logger.isDebugEnabled())
				logger.error("Timestamp response from server is null");
			throw new CMSSignatureException("Zaman Damgasi alinamadi");
		}
		else
		{
			if(logger.isDebugEnabled())
				logger.debug("Timestamp response for signaturetimestamp is received");
		}

		Object certObject =  mAttParams.get(AllEParameters.P_SIGNING_CERTIFICATE);
		if(certObject != null)
		{
			ECertificate cert = (ECertificate)certObject;
			Calendar time = null;
			ESignedData sd = null;
			try
			{
				sd = new ESignedData(token.content.value);
				ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
				time =  tstInfo.getTime();
			}
			catch(Exception ex)
			{
				logger.error("Asn1 decode error", ex);
				throw new CMSSignatureException("Asn1 decode error", ex);
			}

			if(!(time.compareTo(cert.getNotBefore()) > 0 && time.compareTo(cert.getNotAfter()) < 0)){
				throw new CertificateExpiredException(cert);
			}

			TimeStampSignatureChecker tsSignatureChecker = new TimeStampSignatureChecker(sd);
			CheckerResult cr = new CheckerResult();

			boolean result = tsSignatureChecker.check(null, cr);

			if(result == false)
				throw  new CMSSignatureException("Time Stamp Signature is invalid.");

		}
		

		_setValue(token);
	}
    /**
	 * Checks whether attribute is signed or not.
	 * @return false 
	 */ 
	public boolean isSigned() 
	{
		return false;
	}
	/**
	 * Returns AttributeOID of SignatureTimeStampAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	/**
	 * Returns  time of SignatureTimeStampAttr attribute
	 * @param aAttribute EAttribute
	 * @return Calendar
	 * @throws ESYAException
	 */
	public static Calendar toTime(EAttribute aAttribute) throws ESYAException
	{
		try 
		{
			EContentInfo contentInfo = new EContentInfo(aAttribute.getValue(0));
			ESignedData sd = new ESignedData(contentInfo.getContent());
			ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
			return tstInfo.getTime();
		} 
		catch (Exception e)
		{
			throw new ESYAException("Asn1 decode error", e);
		}
	}


}