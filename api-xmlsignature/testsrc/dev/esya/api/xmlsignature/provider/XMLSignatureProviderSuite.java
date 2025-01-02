package dev.esya.api.xmlsignature.provider;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.signature.SignatureTestSuite;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;

/**
 * @author ayetgin
 */
@RunWith(Suite.class)
public class XMLSignatureProviderSuite extends SignatureTestSuite
{

    @BeforeClass
    public static void init(){
        SIGNATURE_FORMAT  = SignatureFormat.XAdES;
    }

}
