package test.esya.api.cmssignature.creation.ec.wrongalg;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import test.esya.api.cmssignature.creation.ec.T01_BES_EC;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class T01_BES_EC_WrongSigAlg extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    public T01_BES_EC_WrongSigAlg(SignatureAlg signatureAlg) {
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

    @Test(expected = CMSSignatureException.class)
    public void T01_createSign() throws Exception {
        new T01_BES_EC(signatureAlg).createSign();
    }

    @Test(expected = CMSSignatureException.class)
    public void T02_createParallelSignature() throws Exception {
        new T01_BES_EC(signatureAlg).createParallelSignature();
    }

    @Test(expected = CMSSignatureException.class)
    public void T03_createSerialSignature() throws Exception {
        new T01_BES_EC(signatureAlg).createSerialSignature();
    }

    @Test(expected = CMSSignatureException.class)
    public void T04_createDetachedSignature() throws Exception {
        new T01_BES_EC(signatureAlg).createDetachedSignature();
    }
}
