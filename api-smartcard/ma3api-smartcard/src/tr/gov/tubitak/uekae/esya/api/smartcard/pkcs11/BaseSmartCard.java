package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;

public interface BaseSmartCard 
{
	public void openSession(long aSlotID)
	throws SmartCardException;
	
	public List<byte[]> getSignatureCertificates()
	throws SmartCardException;
	
	public List<byte[]> getEncryptionCertificates()
	throws SmartCardException;
	
	public void login(String aCardPIN)
	throws SmartCardException, LoginException;
	
	public void logout()
	throws SmartCardException;
	
	public void closeSession()
	throws SmartCardException;
	
	public String getSerial()
	throws SmartCardException;
	
	public String getSerial(long aSlotID)
	throws SmartCardException;
	
	public BaseSigner getSigner(X509Certificate aCert, String aSigningAlg)
	throws SmartCardException;
	
	public BaseSigner getSigner(X509Certificate aCert, String aSigningAlg, AlgorithmParameterSpec aParams)
	throws SmartCardException;
	
	public boolean isSessionActive();
}
