package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESigningCertificateV2;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificateV2;

public abstract class IMobileSigner implements BaseSigner
{
	protected String informativeText;
	protected FingerPrintInfo fingerPrintInfo;

	public IMobileSigner(String informativeText)
	{
		this.informativeText  = informativeText;
		this.fingerPrintInfo = new FingerPrintInfo(this);
	}

	public abstract ESignerIdentifier getSignerIdentifier();
	public abstract DigestAlg getDigestAlg();
	public abstract SigningCertificate getSigningCertAttr();
	public abstract ESigningCertificateV2 getSigningCertAttrv2();
	public abstract ECertificate getSigningCert() ;

	public String getInformativeText()
	{
		return informativeText;
	}

	public void setInformativeText(String informativeText)
	{
		this.informativeText = informativeText;
	}

	protected void calculateFingerPrint(DigestAlg digestAlg, byte [] dataToBeSigned) throws CryptoException {
		byte [] fingerPrintBytes = DigestUtil.digest(digestAlg, dataToBeSigned);
		String fingerPrint = StringUtil.toString(fingerPrintBytes);
		fingerPrintInfo.setFingerPrint(fingerPrint);
	}

	public FingerPrintInfo getFingerPrintInfo(){
		return fingerPrintInfo;
	}
}
