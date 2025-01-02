package test.esya.api.cmssignature.attributes;

import junit.framework.TestCase;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import test.esya.api.cmssignature.testconstants.TestConstants;
import test.esya.api.cmssignature.testconstants.UGTestConstants;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CommitmentType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.CommitmentTypeIndicationAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestBESCommitmentTypeAttr extends CMSSignatureTest
{
    TestConstants msTestConstants = new UGTestConstants();

    @Test
    public void testSignatureCreaterToolAttrWriteReadTest() throws Exception
    {
        BaseSignedData baseSignedData = new BaseSignedData();

        ISignable content = new SignableByteArray("test".getBytes());

        //add content which will be signed
        baseSignedData.addContent(content);

        //Specified attributes are optional,add them to optional attributes list
        List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();

        optionalAttributes.add(new CommitmentTypeIndicationAttr(CommitmentType.TEST));

        //create parameters necessary for signature creation
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EParameters.P_CERT_VALIDATION_POLICY, msTestConstants.getPolicy());

        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        params.put(EParameters.P_TSS_INFO, msTestConstants.getTSSettings());

        //add signer
        baseSignedData.addSigner(ESignatureType.TYPE_BES, msTestConstants.getSignerCertificate(), msTestConstants.getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);

        byte[] encoded = baseSignedData.getEncoded();

        BaseSignedData bsController = new BaseSignedData(encoded);

        Signer aSigner = bsController.getSignerList().get(0);

        List<EAttribute> attrs = aSigner.getAttribute(CommitmentTypeIndicationAttr.OID);

        CommitmentType ct = CommitmentTypeIndicationAttr.toCommitmentType(attrs.get(0));

        TestCase.assertEquals(CommitmentType.TEST, ct);
    }
}
