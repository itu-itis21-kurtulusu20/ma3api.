package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESigningCertificateV2;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

public class MobileSigner extends IMobileSigner
{
	MSSPClientConnector mConnector;
	UserIdentifier mUserIden;
	ECertificate mSigningCert;

	String mSigningAlg;
	AlgorithmParameterSpec mParams;

	public MobileSigner(MSSPClientConnector connector, UserIdentifier aUserIden, ECertificate aSigningCert,
	String informativeText,String aSigningAlg, AlgorithmParameterSpec aParams)
	{
		super(informativeText);
		mConnector = connector;
		mUserIden = aUserIden;
		mSigningCert = aSigningCert;
		mSigningAlg = aSigningAlg;
		mParams = aParams;
	}

	public byte[] sign(byte[] aData) throws ESYAException
	{
		DigestAlg digestAlg = SignatureAlg.fromName(getSignatureAlgorithmStr()).getDigestAlg();
		calculateFingerPrint(digestAlg, aData);
		return mConnector.sign(aData, SigningMode.SIGNHASH, mUserIden,
				mSigningCert, informativeText, mSigningAlg, mParams);
	}

	public ArrayList<MultiSignResult> sign(ArrayList<byte[]> aData, ArrayList<String> informativeText) throws ESYAException
	{
		return mConnector.sign(aData, SigningMode.SIGNHASH, mUserIden,
				mSigningCert, informativeText, mSigningAlg, mParams);
	}

	public String getSignatureAlgorithmStr()
	{
		return mSigningAlg;
	}

	public ECertificate getSigningCert()
	{
		return mConnector.getSigningCert();
	}

	public ESignerIdentifier getSignerIdentifier()
	{
		return mConnector.getSignerIdentifier();
	}
	public SigningCertificate getSigningCertAttr(){
		return mConnector.getSigningCertAttr();
	}
	public ESigningCertificateV2 getSigningCertAttrv2(){
		return mConnector.getSigningCertAttrv2();
	}
	public DigestAlg getDigestAlg(){
		return mConnector.getDigestAlg();
	}
	public AlgorithmParameterSpec getAlgorithmParameterSpec()
	{
		return mParams;
	}

	public MSSPClientConnector getmConnector() {
		return mConnector;
	}

	public UserIdentifier getmUserIden() {
		return mUserIden;
	}
}
