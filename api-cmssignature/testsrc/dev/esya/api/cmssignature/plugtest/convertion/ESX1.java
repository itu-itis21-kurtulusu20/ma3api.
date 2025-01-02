package dev.esya.api.cmssignature.plugtest.convertion;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreOCSPOps;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.util.HashMap;

public class ESX1 extends CMSSignatureTest
{
	private final String INPUT_DIR = getDirectory() +"convertion\\plugtests\\esc\\";
	private final String OUTPUT_DIR = getDirectory() +"convertion\\plugtests\\esx1\\";
	
	
	
		
	public void testAddToCertStore()
	throws Exception
	{
		CertStore cs = new CertStore();
		CertStoreCRLOps crlOps = new CertStoreCRLOps(cs);
		
		crlOps.writeCRL(AsnIO.dosyadanOKU("E:\\test\\imza\\creation\\plugtests\\certscrls\\UGSIL.crl"), 1L);
		
		CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(cs);
		ocspOps.writeOCSPResponseAndCertificate(new EOCSPResponse(AsnIO.dosyadanOKU("C://basicocspresp.dat")), ECertificate.readFromFile("E:\\test\\imza\\creation\\plugtests\\yasemin_sign.cer"));
		
		CertStoreCertificateOps cerOps = new CertStoreCertificateOps(cs);
		cerOps.writeCertificate(AsnIO.dosyadanOKU("E:\\test\\imza\\creation\\plugtests\\certscrls\\root\\UGKOK.crt"), 1);
		cerOps.writeCertificate(AsnIO.dosyadanOKU("E:\\test\\imza\\creation\\plugtests\\certscrls\\root\\UGOCSP.cer"), 1);
		
	}
	
	/**
	 * Create ESX1 signature with just CRL revocation references
	 * Possible input: Signature-C-C-1.p7s
	 */
	public void testConvertESX1FromESC_1()
	throws Exception
	{
		byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-C-1.p7s");
		BaseSignedData bs = new BaseSignedData(inputESC);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting esctimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESX_Type1, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-X-1.p7s");
		
	}
	
	
	/**
	 * Create ESX1 signature with just OCSP revocation references
	 * Possible input: Signature-C-C-2.p7s
	 */
	public void testConvertESX1FromESC_3()
	throws Exception
	{
		byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-C-2.p7s");
		BaseSignedData bs = new BaseSignedData(inputESC);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting esctimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESX_Type1, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-X-3.p7s");
		
	}

}
