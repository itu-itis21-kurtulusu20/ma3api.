package test.esya.api.cmssignature.creation.ec;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SignerLocationAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.*;

/**
 * Created by sura.emanet on 18.12.2019.
 */

@RunWith(Parameterized.class)
public class T03_EST_EC extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T03_EST_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    //create signeddata with one est signature
    @Test
    public void test01_CreateEST() throws Exception {

        byte [] signature = createESTSignature(signatureAlg);

        ValidationUtil.checkSignatureIsValid(signature, null);
    }

    //create signeddata with one est signature with signingtime
    @Test
    public void test02_CreateESTWithSigningTime() throws Exception {

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        List<IAttribute> optionalAttrs = new ArrayList<IAttribute>();
        optionalAttrs.add(new SigningTimeAttr(Calendar.getInstance()));

        HashMap<String, Object> params = new HashMap<String, Object>();
        //necassary for getting signaturetimestamp
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        //necessary for validation of signer certificate according to time in signaturetimestamp attribute
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        //add signer
        bs.addSigner(ESignatureType.TYPE_EST, getECSignerCertificate(), getECSignerInterface(signatureAlg), optionalAttrs, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    //create signeddata with one est signature with signingtime and
    @Test
    public void test03_CreateESTWithSigningTimeAndSignerLocation() throws Exception {

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        List<IAttribute> optionalAttrs = new ArrayList<IAttribute>();
        optionalAttrs.add(new SigningTimeAttr(Calendar.getInstance()));
        optionalAttrs.add(new SignerLocationAttr(null, "Turkey", null));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_EST, getECSignerCertificate(), getECSignerInterface(signatureAlg), optionalAttrs, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    //Add another parallel est signature to signeddata
    @Test
    public void test04_AddSecondEST() throws Exception {

        byte [] inputSigedData = createESTSignature(signatureAlg);

        BaseSignedData bs = new BaseSignedData(inputSigedData);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    //add est counter signature to the first signer
    @Test
    public void test05_AddESTCounterSignatureToEST() throws Exception
    {
        byte [] inputSigedData = createESTSignature(signatureAlg);

        BaseSignedData bs = new BaseSignedData(inputSigedData);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        List<Signer> signers = bs.getSignerList();
        signers.get(0).addCounterSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    public byte [] createESTSignature(SignatureAlg signatureAlg)throws Exception {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_EST, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        return bs.getEncoded();
    }
}
