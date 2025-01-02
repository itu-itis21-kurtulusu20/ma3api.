package test.esya.api.cmssignature.creation;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@RunWith(Parameterized.class)
public class T01_BES extends CMSSignatureTest
{
	private SignatureAlg signatureAlg;
	private RSAPSSParams rsaPSSParams;

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data(){
		return Arrays.asList(new Object[][]{
				{SignatureAlg.RSA_SHA1,   null},
				{SignatureAlg.RSA_SHA256, null},
				{SignatureAlg.RSA_SHA384, null},
				{SignatureAlg.RSA_SHA512, null},

				{SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1)},
				{SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256)},
				{SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384)},
				{SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512)}
		});
	}

	public T01_BES(SignatureAlg signatureAlg, RSAPSSParams rsapssParams) {
		this.signatureAlg = signatureAlg;
		this.rsaPSSParams = rsapssParams;
	}

	@Test
	public void createSign()throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent());

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPSSParams), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

   @Test
	public void createSignWithCRL()throws Exception{

		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent());

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

		bs.addSigner(ESignatureType.TYPE_BES, getSecondSignerCertificate(), getSecondSignerInterface(signatureAlg,rsaPSSParams), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

    @Test
	public void createParallelSignature() throws Exception
	{
		//first signature
		byte [] signatureBytes = createBasicCMSSignature();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		//second signature
		BaseSignedData bs = new BaseSignedData(signatureBytes);
		bs.addSigner(ESignatureType.TYPE_BES, getSecondSignerCertificate(), getSecondSignerInterface(signatureAlg,rsaPSSParams), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

	@Test
	public void createSerialSignature() throws Exception
	{
		//first signature
		byte [] signatureBytes = createBasicCMSSignature();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		//second signature
		BaseSignedData bs = new BaseSignedData(signatureBytes);
		bs.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, getSecondSignerCertificate(), getSecondSignerInterface(signatureAlg,rsaPSSParams), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

    @Test
	public void createDetachedSignature() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent(), false);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPSSParams), null, params);
		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
	}

	@Test
	public void createSignatureInTwoSteps() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent(), false);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		byte [] dtbs = bs.initAddingSigner(ESignatureType.TYPE_BES, getSignerCertificate(), signatureAlg, rsaPSSParams, null, params);
		byte[] bsdBytes = bs.getEncoded();

		BaseSigner signer = getSignerInterface(signatureAlg, rsaPSSParams);
		byte[] signature = signer.sign(dtbs);

		finishSigning(bsdBytes, signature);
	}

	private void finishSigning(byte[] bsdBytes, byte [] signature) throws Exception
	{
		BaseSignedData bs = new BaseSignedData(bsdBytes);
		bs.finishAddingSigner(signature);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
	}
}
