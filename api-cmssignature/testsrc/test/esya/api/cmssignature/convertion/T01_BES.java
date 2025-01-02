package test.esya.api.cmssignature.convertion;

import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import test.esya.api.cmssignature.validation.ValidationUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class T01_BES extends CMSSignatureTest
{
	private final String DIRECTORY = "T:\\api-cmssignature\\test-output\\java\\ma3\\conversion\\";
	
	@Test
	public void testCreateBES() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent());

		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);

		AsnIO.dosyayaz(bs.getEncoded(), DIRECTORY+"BES.p7s");

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

	@Test
	public void testCreateP1() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent());

		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate() , getSignerInterface(SignatureAlg.RSA_SHA256), optionalAttributes, params);

		AsnIO.dosyayaz(bs.getEncoded(),DIRECTORY + "EPES.p7s");

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}
}
