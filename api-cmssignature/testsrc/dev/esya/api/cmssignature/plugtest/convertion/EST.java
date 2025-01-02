package dev.esya.api.cmssignature.plugtest.convertion;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.HashMap;

public class EST extends CMSSignatureTest
{
	private final String OUTPUT_DIR = getDirectory() +"convertion\\plugtests\\est\\";
	
	/**
	 * Signature with MessageDigest, ESSSigningCertificate V1, ContentType, 
	 * SigningTime, SignatureTimeStamp attributes.
	 * Possible input data Signature-C-BES-2.p7s
	 */
	public void testConvertToESTFromBES()
	throws Exception
	{
		byte[] inputBES = AsnIO.dosyadanOKU(getDirectory() +"creation\\plugtests\\bes\\Signature-C-BES-2.p7s");
		
		BaseSignedData sd = new BaseSignedData(inputBES);
		
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting signaturetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		
		//necessary for validating signer certificate according to time of signaturetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		sd.getSignerList().get(0).convert(ESignatureType.TYPE_EST, params);
		
		AsnIO.dosyayaz(sd.getEncoded(), OUTPUT_DIR+"Signature-C-T-1.p7s");
		
	}

}
