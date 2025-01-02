package dev.esya.api.dist.manualExamples;

import com.objsys.asn1j.runtime.Asn1Exception;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETimeStampResponse;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class ZamanDamgasi extends TestCase
{
	public void testZamanDamgasi() throws Exception
	{
		HashMap<String, Object> params = new HashMap<String, Object>(); 
		
		TSSettings tsSettings = new TSSettings("http://zd.ug.net", 21, "12345678");
		params.put(EParameters.P_TSS_INFO, tsSettings);
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
	}
	
	public void testZamanDamgasiSunucusuTest() throws ESYAException, Asn1Exception, IOException
	{
		byte [] sha1Digest = new byte [20];
		Random rand = new Random();
		rand.nextBytes(sha1Digest);
		TSClient tsClient = new TSClient();
		TSSettings settings  = new TSSettings("http://zd.ug.net", 1, "12345678".
		toCharArray());
		tsClient.setDefaultSettings(settings);
		System.out.println("Remaining Credit: " + tsClient.requestRemainingCredit
		(settings));
		ETimeStampResponse response = tsClient.timestamp(sha1Digest, settings);
		ESignedData sd = new ESignedData(response.getContentInfo().getContent());
		ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
		//AsnIO.dosyayaz(tstInfo.getEncoded(),"C:\\Users\\beytullah.yigit\\Desktop\\tstInfo.txt");
		System.out.println("Time Stamp Time" + tstInfo.getTime().getTime());
		System.out.println("Remaining Credit: " + tsClient.requestRemainingCredit
		(settings));
		
	}
	
	public void testZamanDamgasiAlma() throws Exception
	{
		byte [] data = new byte [] {0,1,2,3,4,5,6,7,8,9};
		byte [] dataTbs = DigestUtil.digest(DigestAlg.SHA1, data);
		TSSettings settings = new TSSettings("http://zd.ug.net", 21, "12345678");
		TSClient tsClient = new TSClient();
		EContentInfo token = tsClient.timestamp(dataTbs, settings).getContentInfo();	
	}
}
