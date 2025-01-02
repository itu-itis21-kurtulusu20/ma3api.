package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;

import java.text.MessageFormat;

public class CertificateExpiredException extends CMSSignatureException 
{
	ECertificate mCert;
	
	public CertificateExpiredException(ECertificate cert) {
		mCert = cert;
	}

	public String toString(){
		StringBuilder message = new StringBuilder();
		message.append(MessageFormat.format("Certificate: {0}. ", mCert.getSubject().getCommonNameAttribute()));
		message.append(CMSSignatureI18n.getMsg(E_KEYS.CERT_EXPIRED_ERROR_IN_TS));
		return message.toString();
	}

	@Override
	public String getMessage(){
		return toString();
	}
	
	public ECertificate getCertificate()
	{
		return mCert;
	}
}
