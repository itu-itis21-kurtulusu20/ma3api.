package tr.gov.tubitak.uekae.esya.api.cades.example.pfx;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cades.example.CadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.cades.example.validation.CadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.signature.util.PfxSigner;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

public class PfxSignTest extends CadesSampleBase {

    @Test
    public void testBESSign() throws Exception {
        BaseSignedData baseSignedData = new BaseSignedData();
        ISignable content = new SignableByteArray("test".getBytes());
        baseSignedData.addContent(content);

        HashMap<String, Object> params = new HashMap<String, Object>();

        //if the user does not want certificate validation at generating signature,he can add
        //P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
        //params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        //necessary for certificate validation.By default,certificate validation is done
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        PfxSigner signer = getPfxSigner();
        ECertificate cert = signer.getSignersCertificate();


        baseSignedData.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params);
        byte[] signedDocument = baseSignedData.getEncoded();

        //write the contentinfo to file
        FileUtil.writeBytes(getTestDataFolder() + "BES-Pfx.p7s", signedDocument);

        CadesSignatureValidation signatureValidation = new CadesSignatureValidation();
        SignedDataValidationResult validationResult = signatureValidation.validate(signedDocument, null);
        System.out.println(validationResult);

        assertEquals(SignedData_Status.ALL_VALID, validationResult.getSDStatus());
    }
}
