package dev.esya.api.cmssignature.plugtest2010;

public class Plugtests2010Constants 
{
//	public static final SignableByteArray CONTENT = new SignableByteArray("aaa".getBytes());
//	public static ValidationPolicy POLICY_FILE ;
//	
//	private static final String pfxFilePath = CMSSignatureTest.INPUT_DIRECTORY_PATH + "creation//plugtests2010//RequestedKeyCert.p12";
//	private static final String counterSigFilePath = CMSSignatureTest.INPUT_DIRECTORY_PATH + "creation//plugtests2010//RequestedKeyCertCounterSign.p12";
//	
//	
//	
//	
//	private static ECertificate msCert;
//	private static PrivateKey msPriKey;
//	
//	private static PrivateKey msCounterPriKey;
//	private static ECertificate msCounterCert;
//
//	static
//	{
//		try 
//		{
//			//read policy.
//			POLICY_FILE = PolicyReader.readValidationPolicy(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests2010//policy.xml");
//			
//			
//			//parse sig pfx.
//			String password = "123456";
//			KeyStore pfx = KeyStore.getInstance("pkcs12");
//			pfx.load(new FileInputStream(pfxFilePath),password.toCharArray());
//			KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(password .toCharArray()) ;
//			Entry e = pfx.getEntry("Orcun Ertugrul",passwordProtection);
//			
//			KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)e;
//			
//			msPriKey = privateKeyEntry.getPrivateKey();
//			msCert = new ECertificate(privateKeyEntry.getCertificate().getEncoded());
//			
//			
//			//counter sig pfx
//			pfx = KeyStore.getInstance("pkcs12");
//			pfx.load(new FileInputStream(counterSigFilePath),password.toCharArray());
//			passwordProtection = new KeyStore.PasswordProtection(password .toCharArray()) ;
//			e = pfx.getEntry("Orcun Ertugrul CounterSign",passwordProtection);
//			
//			privateKeyEntry = (KeyStore.PrivateKeyEntry)e;
//			
//			msCounterPriKey = privateKeyEntry.getPrivateKey();
//			msCounterCert = new ECertificate(privateKeyEntry.getCertificate().getEncoded());
//			
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//			System.out.println("Initial settings can not load");
//			POLICY_FILE = null;
//		}
//	}
//	
//	public static BaseSigner getSignerInterface(SignatureAlg signAlg) throws CryptoException 
//	{
//		Signer signer = Crypto.getSigner(signAlg);
//		signer.init(msPriKey);
//		return signer;
//	}
//
//	public static ECertificate getSignerCertificate() 
//	{
//		return msCert;
//	}
//	
//	public static BaseSigner getCounterSignerInterface(SignatureAlg signAlg) throws CryptoException 
//	{
//		Signer signer = Crypto.getSigner(signAlg);
//		signer.init(msCounterPriKey);
//		return signer;
//	}
//
//	public static ECertificate getCounterSignerCertificate() 
//	{
//		return msCounterCert;
//	}
//	
//	
//	
//	public static TSSettings getTSSettings() 
//	{
//		return new TSSettings("http://xades-portal.etsi.org/protected/tsp/TspRequest");
//	}
}
