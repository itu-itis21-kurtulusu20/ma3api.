package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.URI;

import java.io.File;
import java.io.IOException;

/**
 * @author ahmety
 * date: May 14, 2009
 */
public class FileResolver implements IResolver
{

    private static Logger logger = LoggerFactory.getLogger(FileResolver.class);

    public boolean isResolvable(String aURI, Context aBaglam)
    {
        if ((aURI == null) || aURI.equals("") || (aURI.charAt(0)=='#') || aURI.startsWith("http:"))
        {
            return false;
        }

        String baseURI = aBaglam.getBaseURIStr();
        if (logger.isDebugEnabled())
            logger.debug("Base uri is '"+baseURI+"'");

        try {
            if (logger.isDebugEnabled())
                logger.debug("I was asked whether I can resolve " + aURI);

            if (aURI.startsWith("file:") || baseURI.startsWith("file:")) {
                if (logger.isDebugEnabled())
                    logger.debug("I state that I can resolve " + aURI);

                return true;
            }

            File testFile = new File(aURI);
            if (testFile.exists())
                return true;

            testFile = new File(baseURI+File.separator+aURI);
            if (testFile.exists())
                return true;

            URI birlesikURI = new URI(aBaglam.getBaseURI(), aURI);
            testFile = new File(birlesikURI.toString());
            if (testFile.exists())
                return true;

        }
        catch (Exception e) {
            logger.warn("Warning in FileResolver", e);
        }

        logger.debug("But I can't");

        return false;
    }

    public Document resolve(String aURI, Context aContext)
            throws IOException
    {
        try {
            File testFile = new File(aURI);
            System.out.println(aContext.getBaseURI().getClass());
            if (!testFile.exists()){
                testFile = new File(aContext.getBaseURIStr()+File.separator+aURI);
            }
            if (!testFile.exists()){
                testFile = new File(aContext.getBaseURI().replaceAll("%20", " ")+File.separator+aURI);
            }
            if (!testFile.exists()){
                if(aContext.getBaseURI().startsWith("file:")){
                    String baseURI = aContext.getBaseURI().replaceFirst("file:", "");
                    testFile = new File(baseURI.replaceAll("%20", " ")+ File.separator + aURI);
                }
            }

            if (!testFile.exists()){

                URI birlesikURI = new URI(aContext.getBaseURI(), aURI);
                testFile = new File(birlesikURI.toString());
                if (!testFile.exists()){
                    testFile = new File(birlesikURI.getSchemeSpecificPart());
                    if (!testFile.exists()){
                        throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
                    }
                }
            }

            return new FileDocument(testFile, null);
        }
        catch (Exception e) {
            logger.error(I18n.translate("resolver.cantResolveUri", aURI), e);
            throw new IOException(I18n.translate("resolver.cantResolveUri", aURI));
        }
    }

    public static void main(String[] args) throws Exception
    {
        /*
        FileResolver bulucu = new FileResolver();
        Context baglam = new Context();
        baglam.setBaseURI(new URI("C://ahmet/prj/Sunucu/XMLSIG/xml-security-1_4_2/data/at/iaik/ixsil/coreFeatures/signatures/manifestSignature.xml"));

        bulucu.resolve("../samples/sampleXMLData.xml", baglam);
        */
    }
}
