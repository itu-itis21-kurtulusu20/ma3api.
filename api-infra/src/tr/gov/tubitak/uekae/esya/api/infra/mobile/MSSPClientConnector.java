package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESigningCertificateV2;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificate;
import tr.gov.tubitak.uekae.esya.asn.cms.SigningCertificateV2;

import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;


public interface MSSPClientConnector
{
	public void setCertificateInitials(UserIdentifier aUserID)  throws ESYAException;
	public ECertificate getSigningCert();
	public SigningCertificate getSigningCertAttr();
	public ESigningCertificateV2 getSigningCertAttrv2();
	public ESignerIdentifier getSignerIdentifier();
	public DigestAlg getDigestAlg();
	public boolean isMultipleSignSupported();
	public byte [] sign(byte [] dataToBeSigned, SigningMode aMode,
						UserIdentifier aUserID, ECertificate aSigningCert,
						String informativeText,	String aSigningAlg, AlgorithmParameterSpec aParams) throws ESYAException;
	public ArrayList<MultiSignResult> sign(ArrayList<byte[]> dataToBeSigned, SigningMode aMode, UserIdentifier aUserID,
                                           ECertificate eCertificate, ArrayList<String> informativeText, String aSigningAlg, AlgorithmParameterSpec aParams)
			throws ESYAException;
}
