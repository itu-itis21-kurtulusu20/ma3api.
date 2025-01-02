package dev.esya.api.pades.pdfbox.ec.wrongalg;

import dev.esya.api.pades.pdfbox.PAdESBaseTest;
import dev.esya.api.pades.pdfbox.ec.T01_BES_ECSign;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class T01_BES_ECSign_WrongSigAlg extends PAdESBaseTest {

    private SignatureAlg ecSignatureAlg;

    public T01_BES_ECSign_WrongSigAlg(SignatureAlg ecSignatureAlg) {
        this.ecSignatureAlg = ecSignatureAlg;
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

    @Test(expected = SignatureException.class)
    public void T01_BESSignTest() throws Exception {
        new T01_BES_ECSign(ecSignatureAlg).T01_BESSignTest();
    }

    @Test(expected = SignatureException.class)
    public void T02_AddBEStoBESTest() throws Exception {
        new T01_BES_ECSign(ecSignatureAlg).T02_AddBEStoBESTest();
    }

    @Test(expected = SignatureException.class)
    public void T03_Two_BESSignTest() throws Exception {
        new T01_BES_ECSign(ecSignatureAlg).T03_Two_BESSignTest();
    }
}
