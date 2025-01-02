package dev.esya.api.dist.manualExamples.infra;

import dev.esya.api.dist.manualExamples.TestConstants;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampResponse;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampToken;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;

public class TSClientTest  extends TestCase 
{
	public void testGetTS() throws IOException, ESYAException
	{
		FileInputStream file = new FileInputStream("T:\\api-cmssignature\\testdata\\support\\policy.xml");
		byte [] digest = DigestUtil.digestStream(DigestAlg.SHA1, file);
		
		TSClient tsClient = new TSClient();
		
		//Test sistemi, kanuni bir geçerliliği yok.
		TSSettings settings = new TSSettings("http://tzd.kamusm.gov.tr", 1901, "12345678");
		ETimeStampResponse response = tsClient.timestamp(digest, settings);
		
		AsnIO.dosyayaz(response.getContentInfo().getEncoded(), "T:\\api-cmssignature\\testdata\\support\\policy.xml.zd");
	}
	
	public void testVerifyTS() throws Exception
	{
byte [] tsBytes = AsnIO.dosyadanOKU("T:\\api-cmssignature\\testdata\\support\\policy.xml.zd");

ETimeStampToken tsToken = new ETimeStampToken(tsBytes);
byte [] digestInTS = tsToken.getTSTInfo().getHashedMessage();

DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(tsToken.getTSTInfo().getHashAlgorithm());
FileInputStream file = new FileInputStream("T:\\api-cmssignature\\testdata\\support\\policy.xml");
byte [] digestOfFile = DigestUtil.digestStream(digestAlg, file);

//Zaman damgası doğru dosyaya mı ait kontrolü yapılıyor.
if(!Arrays.equals(digestInTS, digestOfFile))
	throw new Exception("Özetler uyuşmuyor. Zaman damgası bu dosyanın değil.");

//Zaman damgası imzası ve sertifikası doğrulanıyor.
Hashtable<String, Object> params = new Hashtable<String, Object>();
params.put(EParameters.P_CERT_VALIDATION_POLICY, TestConstants.getPolicy());
SignedDataValidation sdv = new SignedDataValidation();
SignedDataValidationResult sdvr = sdv.verify(tsBytes, params);
if(sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
	throw new Exception("Zaman damgası doğrulanamadı.");

//Zaman damgasını veren sertifikanın zaman damgası yetkisi kontrol ediliyor.
BaseSignedData bs = new BaseSignedData(tsBytes);
ECertificate tsCert = bs.getSignerList().get(0).getSignerCertificate();
if(!tsCert.isTimeStampingCertificate())
	throw new Exception("Zaman damgası veren sertifika zaman damgası vermeye yetkili değil.");

System.out.println("Zaman Damgası Doğrulandı.");
Calendar tsTime = tsToken.getTSTInfo().getTime();
System.out.println("Zaman Damgası Alınma Tarihi: " + tsTime.getTime());
	}
}

