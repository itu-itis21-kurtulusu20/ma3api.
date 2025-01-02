package test.esya.api.xmlsignature.creation.ec.wrongalg;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.creation.ec.T01_SignWithBES_EC;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class T01_SignWithBES_EC_WrongSigAlg extends XMLSignatureTestBase {

    private SignatureAlg signatureAlg;

    public T01_SignWithBES_EC_WrongSigAlg(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {SignatureAlg.ECDSA_SHA1},
            {SignatureAlg.ECDSA_SHA224},
            {SignatureAlg.ECDSA_SHA256},
            {SignatureAlg.ECDSA_SHA512}
        });
    }

    @Test(expected = XMLSignatureException.class)
    public void T01_testCreateEnveloping() throws Exception {
        new T01_SignWithBES_EC(signatureAlg).testCreateEnveloping();
    }

    @Test(expected = XMLSignatureException.class)
    public void T02_testCreateEnveloped() throws Exception {
        new T01_SignWithBES_EC(signatureAlg).testCreateEnveloped();
    }

    @Test(expected = XMLSignatureException.class)
    public void T03_testCreateDetached() throws Exception {
        new T01_SignWithBES_EC(signatureAlg).testCreateDetached();
    }

    @Test(expected = XMLSignatureException.class)
    public void T04_createCounterSignature() throws Exception {
        new T01_SignWithBES_EC(signatureAlg).createCounterSignature();
    }
}
