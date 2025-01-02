package dev.esya.api.cmssignature.plugtest;

public class PlugtestsConstants
{
//	public static ValidationPolicy POLICY_FILE = null;
//	public static ValidationPolicy POLICY_FILE_CRL = null;
//	public static ValidationPolicy POLICY_FILE_OCSP = null;
//	
//	public static final SignableByteArray CONTENT = new SignableByteArray("aaa".getBytes());
//	
//	static
//	{
//		try
//		{
//			POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml"));
//			POLICY_FILE_CRL = PolicyReader.readValidationPolicy(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policyCRL.xml");
//			POLICY_FILE_OCSP  = PolicyReader.readValidationPolicy(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policyOCSP.xml");
//		
//		}
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	public static TSSettings getTSSettings()
//	{
//		return new TSSettings("http://zd.ug.net", 21, "12345678".toCharArray());
//	}
//	
//	public static BaseSigner getSignerInterface(SignatureAlg signAlg) throws Exception 
//	{
//		SmartCard sc = new SmartCard(CardType.AKIS);
//		long slot = sc.getSlotList()[0];
//		long session = sc.openSession(slot);
//		sc.login(session, "12345");
//		return new SCSignerWithKeyLabel(sc, session, slot, "yasemin.akturk#ug.netSIGN0",signAlg.getAsymmetricAlg().getName(),
//				signAlg.getDigestAlg().getName() );
//	}
//
//	public static ECertificate getSignerCertificate() throws Exception
//	{
//		return new ECertificate(AsnIO.dosyadanOKU( CMSSignatureTest.INPUT_DIRECTORY_PATH +  "creation//plugtests//yasemin_sign.cer"));
//	}
}
