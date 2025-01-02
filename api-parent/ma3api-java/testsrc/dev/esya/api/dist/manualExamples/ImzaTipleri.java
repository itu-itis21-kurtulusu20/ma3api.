package dev.esya.api.dist.manualExamples;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.EST;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ImzaTipleri 
{
	private static final Object VALIDATION_POLICY = null;
	String CONTENT_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\test.dat";
	
	public void testBES() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();		
		bs.addContent(new SignableByteArray("test".getBytes())); 
				
		//Since SigningTime attribute is optional,add it to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
				
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, VALIDATION_POLICY);
				
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
	}

	EContentInfo input = null;
	
	public void testBeyanTarihi() throws Exception
	{
		BaseSignedData bs = new BaseSignedData(input);
		List<EAttribute>  attrs = bs.getSignerList().get(0).getSignedAttribute(SigningTimeAttr.OID);
		Calendar time = SigningTimeAttr.toTime(attrs.get(0));
		System.out.println(time.getTime().toString());
	}
	
	public void testESTTarihi() throws Exception
	{
		BaseSignedData bs = new BaseSignedData(input);
		EST estSign = (EST)bs.getSignerList().get(0);
		Calendar time = estSign.getTime();
		System.out.println(time.getTime().toString());
	}
	
	public void testEST() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();		
		bs.addContent(new SignableByteArray("test".getBytes()));
				
		HashMap<String, Object> params = new HashMap<String, Object>();		
		//ilerli for getting signaturetimestamp
		TSSettings tsSettings = new TSSettings("http://zd.ug.net", 21, "12345678".toCharArray());
		params.put(EParameters.P_TSS_INFO, tsSettings);
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);	
		params.put(EParameters.P_CERT_VALIDATION_POLICY, TestConstants.getPolicy());
				
		//add signer
		bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);
	}

	private BaseSigner getSignerInterface(SignatureAlg rsaSha1) {
		// TODO Auto-generated method stub
		return null;
	}

	private ECertificate getSignerCertificate() {
		// TODO Auto-generated method stub
		return null;
	}
}
