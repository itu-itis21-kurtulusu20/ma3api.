package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * <p>The normative specification for XSL Transformations is [XSLT].
 * Specification of a namespace-qualified stylesheet element, which MUST be the
 * sole child of the Transform element, indicates that the specified style sheet
 * should be used. Whether this instantiates in-line processing of local XSLT
 * declarations within the resource is determined by the XSLT processing model;
 * the ordered application of multiple stylesheet may require multiple
 * Transforms. No special provision is made for the identification of a remote
 * stylesheet at a given URI because it can be communicated via an xsl:include
 * or xsl:import within the stylesheet child of the Transform.</p>
 * <p>
 * <p>This transform requires an octet stream as input. If the actual input is
 * an XPath node-set, then the signature application should attempt to convert
 * it to octets (apply Canonical XML]) as described in the Reference Processing
 * Model</p>
 *
 * @author ahmety
 * date: Aug 7, 2009
 */
public class XSLTransformer implements tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.Transformer
{

    private static Logger logger = LoggerFactory.getLogger(XSLTransformer.class);


    static final String NS_XSLT = "http://www.w3.org/1999/XSL/Transform";

    // check for secure processing feature
    private static Class xClass = null;

    static {
        try {
            xClass = Class.forName("javax.xml.XMLConstants");
        }
        catch (Exception e) {
            logger.warn("Warning in XSLTransformer", e);
        }
    }


    private static List<DataType> TYPES = Arrays.asList(
            DataType.OCTETSTREAM
    );

    public List<DataType> expectedDataTypes()
    {
        return TYPES;
    }

    public DataType returnType()
    {
        return DataType.OCTETSTREAM;
    }

    public boolean acceptsAlgorithm(String aAlgorithmURI)
    {
        return TransformType.XSLT.getUrl().equals(aAlgorithmURI);
    }

    public InputStream transform(Object aObject,
                                 String aAlgorithmURI,
                                 Object[] aParams,
                                 Element aTransformElement,
                                 final String aBaseURI)
            throws XMLSignatureException
    {
        if (logger.isDebugEnabled())
            logger.debug("XSLT transform o: " + aObject
                           + ", a: " + aAlgorithmURI
                           + ", p " + (aParams != null ? Arrays.asList(aParams) : "''")
                           + ", baseURI: "+aBaseURI);

        if (xClass == null) {
            throw new XMLSignatureException("SECURE_PROCESSING_FEATURE not supported");
        }

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            // Process XSLT stylesheets in a secure manner
            tFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE);

            /*
             This transform requires an octet stream as input. If the actual
             input is an XPath node-set, then the signature application should
             attempt to convert it to octets (apply Canonical XML]) as
             described in the Reference Processing Model (section 4.3.3.2).
             */
            Source xmlSource = new StreamSource((InputStream)aObject);
            xmlSource.setSystemId(aBaseURI);

            Source stylesheet;

            /*
             This complicated transformation of the stylesheet itself is
             necessary because of the need to get the pure style sheet. If we
             simply say Source stylesheet = new DOMSource(this._xsltElement);
             whereby this._xsltElement is not the rootElement of the Document,
             this causes problems; so we convert the stylesheet to byte[] and
             use this as input stream
             */
            {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                javax.xml.transform.Transformer transformerForNode = tFactory.newTransformer();

                Element xsltElement = XmlUtil.selectFirstElement(
                                            aTransformElement.getFirstChild(),
                                            NS_XSLT, "stylesheet");

                logger.debug("xsl element: " + xsltElement);

                DOMSource source = new DOMSource(xsltElement);
                StreamResult result = new StreamResult(os);
                source.setSystemId(aBaseURI);
                transformerForNode.transform(source, result);

                stylesheet = new StreamSource(new ByteArrayInputStream(os.toByteArray()));
                stylesheet.setSystemId(aBaseURI);

                if (logger.isDebugEnabled()){
                    logger.debug("stylesheet :" + StringUtil.substring(os.toByteArray(), 255));
                    logger.debug("stylesheet :" + stylesheet);
                }
            }


            Transformer transformer = tFactory.newTransformer(stylesheet);

            /*
             Force Xalan to use \n as line separator on all OSes. This
             avoids OS specific signature validation failures due to line
             separator differences in the transformed output. Unfortunately,
             this is not a standard JAXP property so will not work with
             non-Xalan implementations.
             */
            try {
                transformer.setOutputProperty("{http://xml.apache.org/xalan}line-separator", "\n");
            }
            catch (Exception e) {
                logger.warn("Unable to set Xalan line-separator property: " + e.getMessage(), e);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult outputTarget = new StreamResult(baos);

            transformer.transform(xmlSource, outputTarget);
            return new ByteArrayInputStream(baos.toByteArray());

        }
        catch (Exception e) {
            throw new XMLSignatureException(e, "transform.errorApplyingTo", "XSLT", aObject);
        }

    }
}
