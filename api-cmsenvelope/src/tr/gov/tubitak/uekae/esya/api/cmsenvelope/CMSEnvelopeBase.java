package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.IEnvelopeData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KEKRecipient;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyAgreeRecipient;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyTransRecipient;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.PasswordRecipient;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateChoices;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoice;

public class CMSEnvelopeBase 
{
	private static Logger logger = LoggerFactory.getLogger(CMSEnvelopeBase.class);
	
	protected IEnvelopeData mEnvelopeData = null;
	protected EContentInfo mContentInfo = new EContentInfo(new ContentInfo());

	protected CryptoProvider cryptoProvider;
	
	protected CMSEnvelopeBase()
	{
		try
    	{
    		LV.getInstance().checkLD(Urunler.CMSZARF);
    	}
    	catch(LE ex)
    	{
    		logger.error("Lisans kontrolu basarisiz.",ex.getMessage());
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz.");
    	}
	}
	
	
	protected long _getVersion() {
		boolean originatorInfoPresent = false;
		boolean certsWithTypeOtherPresent = false;
		boolean crlWithTypeOtherPresent = false;
		boolean certsWithTypeAttributePresent = false;
		boolean pwriPresent = false;
		boolean oriPresent = false;
		boolean unProtectedAttributesPresent = false;
		boolean allRecipientInfoVersionIsZero = true; 
		
		long version = 0;
		//check recipient infos versions
		for(int i=0; i < mEnvelopeData.getRecipientInfoCount(); i++)
		{
			RecipientInfo ri = mEnvelopeData.getRecipientInfo(i);
			if(ri instanceof KeyTransRecipient)
			{
				version = ((KeyTransRecipient)ri).getCMSVersion().value;
			}else if(ri instanceof KeyAgreeRecipient)
			{
				version = ((KeyAgreeRecipient)ri).getCMSVersion().value;
			}else if(ri instanceof KEKRecipient)
			{
				version = ((KEKRecipient)ri).getCMSVersion().value;
			}else if(ri instanceof PasswordRecipient)
			{
				version = ((PasswordRecipient)ri).getCMSVersion().value;
				pwriPresent = true;
			}else
			{
				oriPresent = true;
			}
				
			if(version != 0)
			{
				allRecipientInfoVersionIsZero = false;
				break;
			}
		}
		
		//check originator info parameters
		if(mEnvelopeData.getOriginatorInfo() != null)
		{
			originatorInfoPresent = true;
			CertificateChoices[] certChoice = mEnvelopeData.getOriginatorInfo().certs.elements;
			for(int i=0; i<certChoice.length; i++)
			{
				if(certChoice[i].getChoiceID() != CertificateChoices._CERTIFICATE)
					certsWithTypeOtherPresent = true;
				if(certChoice[i].getChoiceID() == CertificateChoices._V1ATTRCERT || certChoice[i].getChoiceID() == CertificateChoices._V2ATTRCERT)
					certsWithTypeAttributePresent = true;
			}
			RevocationInfoChoice [] crlChoice = mEnvelopeData.getOriginatorInfo().crls.elements;
			for(int i=0; i<crlChoice.length; i++)
			{
				if(crlChoice[i].getChoiceID() != RevocationInfoChoice._CRL)
					crlWithTypeOtherPresent = true;
			}
		}
		
		//check unprotectedAttributes
		if(mEnvelopeData.getUnprotectedAttributes() != null)
		{
			unProtectedAttributesPresent = true;
		}
		
		if(originatorInfoPresent && (certsWithTypeOtherPresent || crlWithTypeOtherPresent))
		{
			version = 4;
		}else if((originatorInfoPresent && certsWithTypeAttributePresent) || pwriPresent || oriPresent)
		{
			version = 3;
		}else if((originatorInfoPresent == false) || (unProtectedAttributesPresent == false) || allRecipientInfoVersionIsZero)
		{
			version = 0;
		}else
		{
			version = 2;
		}
		return version;
	}
	
	protected void checkLicense(ECertificate aCer)
	{
		try
    	{
    		boolean isTest = LV.getInstance().isTL(Urunler.CMSZARF);
    		if(isTest)
    			if(!aCer.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
    			{
    				logger.error("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    				throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    			}

    		String issuerName = LV.getInstance().getCertificateIssuer(Urunler.CMSZARF);
    		if(!StringUtil.isNullorEmpty(issuerName) && !aCer.getIssuer().getCommonNameAttribute().contains(issuerName)){
    			   logger.error("Certificate issuer and the issuer name field in license must match");
				   throw new ESYARuntimeException("Certificate issuer and the issuer name field in license must match");
    		}
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz." + ex.getMessage());
    	}
	}

	public CryptoProvider getCryptoProvider() {
		if(cryptoProvider != null)
			return cryptoProvider;
		else
			return Crypto.getProvider();
	}

	public void setCryptoProvider(CryptoProvider cryptoProvider) {
		this.cryptoProvider = cryptoProvider;
	}
}
