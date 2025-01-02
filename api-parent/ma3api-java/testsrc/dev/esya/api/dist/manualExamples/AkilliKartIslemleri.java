package dev.esya.api.dist.manualExamples;

import junit.framework.TestCase;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cades.example.validation.CadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.ObjectSessionInfo;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyID;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

public class AkilliKartIslemleri extends TestCase 
{
	String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() + "testdata\\support\\yasemin_sign.cer";
	
	public void testSmartCardManager() throws ESYAException
	{
		//Enable APDU usage
		SmartCardManager.useAPDU(true);
		//Connect a smartcard. If more than one smart card connected, user selects one of them
		SmartCardManager scm = SmartCardManager.getInstance();
		
		//Get qualified certificate. If more than one qualified certificate, user selects one of them.
		ECertificate cert = scm.getSignatureCertificate(true);
		//Create signer
		BaseSigner signer = scm.getSigner("12345", cert);
		
		/**
		 * Create signature
		 */
		
		//If not sign again with selected card logout.
		scm.logout();
		//To select new card and new certificate, call reset.
		scm.reset();
		
	}
	
	public void testAkilliKart1() throws Exception
	{
		SmartCard sc = new SmartCard(CardType.AKIS);
		long [] slots = sc.getSlotList();
		for (long slot : slots) 
		{
		   CK_SLOT_INFO slotInfo = sc.getSlotInfo(slot);
		   System.out.println(new String(slotInfo.slotDescription).trim());
		}
	}
	
	public void testKarttanSertifikaOkuma() throws Exception
	{
		SmartCard sc = new SmartCard(CardType.AKIS);
		long [] slots = sc.getSlotList();
		long slot = 0;
		if(slots.length == 1)
		   slot = slots[0];
		else
		   slot = selectSlot();
		long session = sc.openSession(slot);
		sc.login(session, "12345");
		List<byte[]> certBytes = sc.getSignatureCertificates(session);
		for (byte[] bs : certBytes) 
		{
			ECertificate cert = new ECertificate(bs);
			//cert.isQualifiedCertificate()
			System.out.println(cert.getSubject().getCommonNameAttribute());
		}
		sc.logout(session);
		sc.closeSession(session);
	}
	
	public void testAnahtarEtiketlerininOkunmasi() throws Exception
	{
		SmartCard sc = new SmartCard(CardType.AKIS);
		long slot = sc.getSlotList()[0];
		long session = sc.openSession(slot);
		sc.login(session, "12345");
		String [] labels = sc.getSignatureKeyLabels(session);
		for (String label : labels) 
		{
		   System.out.println(label);
		}
	}
	
	public void testSeriNoIleImzaciOlusturulmasi()throws Exception
	{
		SmartCard sc = new SmartCard(CardType.AKIS);
		long [] slots = sc.getSlotList();
		long session = sc.openSession(slots[0]);
		sc.login(session, "12345");
		ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
		
		BaseSigner signer = new SCSignerWithCertSerialNo (sc, session, slots[0], cert.getSerialNumber().toByteArray(), 
				SignatureAlg.RSA_SHA1.getName()); 
	}
	
	public void testLabelIleImzaciOlusturulmasi()throws Exception
	{
		SmartCard sc = new SmartCard(CardType.AKIS);
		long slot = sc.getSlotList()[0];
		long session = sc.openSession(slot);
		sc.login(session, "12345");		
		BaseSigner signer = new SCSignerWithKeyLabel(sc,session, slot,"yasemin.akturk#ug.netSIGN0",
				SignatureAlg.RSA_SHA1.getName());
	}

	public void testKeyIDIleImzaciOlusturulmasi() throws Exception {
		HSMSessionPool hsmSessionPool = new HSMSessionPool(CardType.DIRAKHSM, 89, "12345678");
		ObjectSessionInfo objectSessionInfo = hsmSessionPool.checkOutItem("test1@test.com_745418-sign");

		SmartCard sc = new SmartCard(CardType.DIRAKHSM);
		long slot = sc.getSlotList()[0];
		//sc.login(objectSessionInfo.getSession(), "12345678");

		BaseSigner signer = new SCSignerWithKeyID(sc, objectSessionInfo.getSession(), slot, objectSessionInfo.getObjectId(), SignatureAlg.RSA_SHA1.getName());

		//-------------------------

		BaseSignedData baseSignedData = new BaseSignedData();
		ISignable content = new SignableByteArray("test".getBytes());
		baseSignedData.addContent(content);

		ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream("C:/ma3api-java-bundle/config/certval-policy-test.xml"));

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

		ECertificate cert = new ECertificate(sc.getSignatureCertificates(objectSessionInfo.getSession()).get(0));

		baseSignedData.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params);

		byte[] signedDocument = baseSignedData.getEncoded();

		CadesSignatureValidation signatureValidation = new CadesSignatureValidation();
		SignedDataValidationResult validationResult = signatureValidation.validate(signedDocument, null);

		assertEquals(SignedData_Status.ALL_VALID, validationResult.getSDStatus());

		//-------------------------


		hsmSessionPool.offer(objectSessionInfo);
	}

	private long selectSlot() 
	{
		return 1;
	}
}
