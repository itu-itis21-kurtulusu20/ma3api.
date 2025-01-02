package dev.esya.api.xmlsignature.legacy.xmldsig.interop.iaik;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import gnu.crypto.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.Canonicalizer;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.AnonymousResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;

import java.io.File;

/**
 * @author ahmety
 * date: Jul 1, 2009
 */
public class IAIKCoreFeaturesTest extends XMLBaseTest
{

    static String IAIK_CORE_LOC = IAIKLOC + "coreFeatures\\signatures\\";


    public void testAnonymousReference() throws Exception
    {
        File file = new File(IAIKLOC + "coreFeatures/samples/anonymousReferenceContent.xml");
        validate("anonymousReferenceSignature.xml",
                 IAIK_CORE_LOC,
                 new AnonymousResolver(file,MIMETYPE_XML),
                 true);
    }

    public void testManifest() throws Exception
    {
        validate("manifestSignature.xml", IAIK_CORE_LOC, null, true);
    }

    public void testSignatureTypes() throws Exception
    {
        validate("signatureTypesSignature.xml",
                 IAIK_CORE_LOC,
                 new OfflineResolver("http://www.w3.org/TR/2000/REC-xml-20001006",
                                     BASELOC + "org/w3c/www/TR/2000/REC-xml-20001006",
                                     MIMETYPE_XML),
                 true);
    }

    /**
     2010-05-14 09:33:03,197 [main] DEBUG model.Reference           - Original digest: 9yMFLtkwiTVLvQaCOzE6+o4HrQE=
     2010-05-14 09:33:03,198 [main] DEBUG model.Reference           - Calculated digest: zhHBFuKaseNdidZUL7D7AaTwVys=

     * */
    public static void main(String[] args) throws Exception
    {
        String bytesStr =  "<DataItem xmlns=\"http://iaik.at#Envelope\" Id=\"DataItem\">\n" +
                        "    Some Data from IAIK\n" +
                        "  </DataItem>";
        byte[] bytes = Canonicalizer.getInstance(C14nMethod.INCLUSIVE.getURL()).canonicalize(bytesStr.getBytes());
        String digest = Base64.encode(DigestUtil.digest(DigestAlg.SHA1, bytes));
        System.out.println(""+ digest);
    }

}
