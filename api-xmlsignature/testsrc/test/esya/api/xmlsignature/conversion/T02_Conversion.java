package test.esya.api.xmlsignature.conversion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.w3c.dom.Document;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class T02_Conversion extends XMLSignatureTestBase {
    private final String DIRECTORY = "T:\\api-xmlsignature\\test-output\\java\\ma3\\conversion\\";

    @Parameterized.Parameters(name = "{0}-To-{2}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"BES_Enveloping.xml",SignatureType.ES_T, "EST_Enveloping.xml"},
                {"BES_Enveloped.xml",SignatureType.ES_T, "EST_Enveloped.xml"},
                {"BES_Detached.xml",SignatureType.ES_T, "EST_Detached.xml"},

                {"BES_Enveloping.xml",SignatureType.ES_XL, "BES_ESXL_Enveloping.xml"},
                {"BES_Enveloped.xml",SignatureType.ES_XL, "BES_ESXL_Enveloped.xml"},
                {"BES_Detached.xml",SignatureType.ES_XL, "BES_ESXL_Detached.xml"},

                {"BES_Enveloping.xml",SignatureType.ES_A, "BES_ESA_Enveloping.xml"},
                {"BES_Enveloped.xml",SignatureType.ES_A, "BES_ESA_Enveloped.xml"},
                {"BES_Detached.xml",SignatureType.ES_A, "BES_ESA_Detached.xml"},

                {"EST_Enveloping.xml",SignatureType.ES_C, "ESC_Enveloping.xml"},
                {"EST_Enveloped.xml",SignatureType.ES_C, "ESC_Enveloped.xml"},
                {"EST_Detached.xml",SignatureType.ES_C, "ESC_Detached.xml"},

                {"ESC_Enveloping.xml",SignatureType.ES_X_Type1, "ESX_Enveloping.xml"},
                {"ESC_Enveloped.xml",SignatureType.ES_X_Type1, "ESX_Enveloped.xml"},
                {"ESC_Detached.xml",SignatureType.ES_X_Type1, "ESX_Detached.xml"},

                {"ESX_Enveloping.xml",SignatureType.ES_XL, "ESXL_Enveloping.xml"},
                {"ESX_Enveloped.xml",SignatureType.ES_XL, "ESXL_Enveloped.xml"},
                {"ESX_Detached.xml",SignatureType.ES_XL, "ESXL_Detached.xml"},

                {"ESXL_Enveloping.xml",SignatureType.ES_A, "ESA_Enveloping.xml"},
                {"ESXL_Enveloped.xml",SignatureType.ES_A, "ESA_Enveloped.xml"},
                {"ESXL_Detached.xml",SignatureType.ES_A, "ESA_Detached.xml"}
        });
    }

    String inputFile;
    SignatureType outputType;
    String outputFile;

    public T02_Conversion(String inputFile, SignatureType outputType, String outputFile){
        this.inputFile = inputFile;
        this.outputType = outputType;
        this.outputFile = outputFile;
    }

    @Test
    public void testConversion() throws Exception
    {
        Context context =  createContext(DIRECTORY);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        File f = new File(DIRECTORY + inputFile);

        // parse into DOM format
        Document document = db.parse(f);
        context.setDocument(document);

        XMLSignature signature = XMLSignature.parse(document, context);

        signature.upgrade(outputType);

        // writing enveloped XML to the file
        Source source = new DOMSource(document);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, new StreamResult(new FileOutputStream(DIRECTORY + outputFile)));

        XMLValidationUtil.checkSignatureIsValid(DIRECTORY, DIRECTORY + outputFile);
    }

    /*private void convertEnveloped(String inputFile, SignatureType outputType, String outputFile)throws Exception{

        Context context =  createContext();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        // open the document
        File f = new File(DIRECTORY + inputFile);

        // parse into DOM format
        Document document = db.parse(f);
        context.setDocument(document);

        Element signatureElement = ((Element)document.getDocumentElement().getElementsByTagName("ds:Signature").item(0));
        XMLSignature signature = new XMLSignature(signatureElement, context);


        signature.upgrade(outputType);

        // writing enveloped XML to the file
        Source source = new DOMSource(document);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, new StreamResult(new FileOutputStream(DIRECTORY + outputFile)));

        XMLValidationUtil.checkSignatureIsValid(DIRECTORY, DIRECTORY + outputFile);
    }

    public void conversion(String inputFile, SignatureType outputType, String outputFile)throws Exception{
        // create context with working directory
        Context context = createContext();

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(DIRECTORY + inputFile)),context);

        // upgrade to T
        signature.upgrade(outputType);

        signature.write(new FileOutputStream(DIRECTORY + outputFile));

        XMLValidationUtil.checkSignatureIsValid(DIRECTORY, DIRECTORY + outputFile);
    }*/


}
