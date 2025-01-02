package tr.gov.tubitak.uekae.esya.api.tsl;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.util.List;

public class Test extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	
	public void testDeneme(){
		try
        {
			long start=System.currentTimeMillis();
            //TSL tsl = TSL.parse("E:\\CC\\beytullah.yigit_ESYA_MA3API_int\\MA3\\api-tsl\\config\\tsl\\tsl_self_samples\\turkTSL\\tsl_signed.xml");
            TSL tsl = TSL.parse("C:\\Users\\beytullah.yigit\\.sertifikadeposu\\tsl.xml");
            long sure=System.currentTimeMillis()-start;
            System.out.println(sure);

            List<ECertificate> validCertList = tsl.getValidCertificates();
            System.out.println(validCertList.size());
            List<ECertificate> validCACertList = tsl.getValidCACertificates();
            System.out.println(validCACertList.size());
            logger.trace("acele etme");

            boolean validationRslt = tsl.validateTSL();
            System.out.println(validationRslt);
        }
        catch (TSLException e)
        {
            logger.error("Error in Test", e);
        }
        catch (Exception e)
        {
            logger.error("Error in Test", e);
        }
          
	}
/*
	public void testDeneme2(){
		String tslPath = "E:\\CC\\beytullah.yigit_ESYA_MA3API_int\\MA3\\api-tsl\\config\\tsl\\tsl_self_samples\\turkTSL\\tsl.xml";
		try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
			Document mDocument = db.parse(tslPath);

			boolean checkQCStatement =true;
            ECertificate signatureCertificate = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
            BaseSigner baseSigner = SmartCardManager.getInstance().getSigner("123456" , signatureCertificate);

            Context context = createContext();
            context.setDocument(mDocument);
            
            XMLSignature signature = new XMLSignature(context, false);
            mDocument.getDocumentElement().appendChild(signature.getElement());
            Transforms transforms = new Transforms(context);
            transforms.addTransform(new Transform(context,TransformType.ENVELOPED.getUrl()));
            signature.addDocument("#ID1", "text/xml", transforms, DigestMethod.SHA_256, false);
            signature.addKeyInfo(signatureCertificate);

            signature.sign(baseSigner);
            SmartCardManager.getInstance().logout();
            
            write(new FileOutputStream("E:\\CC\\beytullah.yigit_ESYA_MA3API_int\\MA3\\api-tsl\\config\\tsl\\tsl_self_samples\\turkTSL\\tsl_signed.xml"),mDocument);
        }
        catch (Exception exc)
        {
        	exc.printStackTrace();
        }
          
	}
    public Context createContext() {
    	String configPath="./config/xmlsignature-config.xml";
    	String contextPath="E:\\CC\\beytullah.yigit_ESYA_MA3API_int\\MA3\\api-tsl\\config\\tsl\\tsl_self_samples\\turkTSL\\";
    	Context context = null;
    	try {
			context = new Context(contextPath);
			context.setConfig(new Config(configPath));
		} catch (XMLSignatureException e) {
			e.printStackTrace();
		}
    	return context;
    }

    public void write(OutputStream aStream, Document mDocument) throws XMLSignatureException
    {
        write(new StreamResult(aStream), mDocument);
    }

    /**
     * Output xml signature to Result
     *
     * @param aResult signature to be transformed
     * @throws XMLSignatureException if xml conversion fails
     */
    public void write(Result aResult, Document mDocument) throws XMLSignatureException
    {
        try {
            Source s = new DOMSource(mDocument);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(s, aResult);
        }catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantOutputXml");
        }
    }
    
}
