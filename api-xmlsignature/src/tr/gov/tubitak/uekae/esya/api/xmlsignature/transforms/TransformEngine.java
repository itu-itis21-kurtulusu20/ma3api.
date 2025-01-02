package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Class for applying system wide known transform algorithm, and making
 * necessary convertions if required any.
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.C14nTransformer
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XPathTransformer
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.EnvelopedSignatureTransformer
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.Base64Transformer
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.XSLTransformer
 *
 * @author ahmety
 * date: Jun 19, 2009
 */
public class TransformEngine
{
    private static Logger logger = LoggerFactory.getLogger(TransformEngine.class);

    private static List<Transformer> msTransformers = new ArrayList<Transformer>();

    static {
        // register predefined transforms...
        msTransformers.add(new C14nTransformer());
        msTransformers.add(new XPathTransformer());
        msTransformers.add(new EnvelopedSignatureTransformer());
        msTransformers.add(new Base64Transformer());
        msTransformers.add(new XSLTransformer());
    }

    /**
     * Method fo applying transform to given object according to specified
     * algorithm.
     *
     * @param aObject input to be transformed
     * @param aAlgorithm transform algorithm
     * @param aParams parameters needed by transform if any
     * @param aTransformElement xml element declaring the transform
     * @param aBaseURI where to find relative resourses
     * @return transformed input
     * @throws XMLSignatureException if anything goes wrong. This might be
     *      unknown transform algorithm, inapropriate input for transfrom, any
     *  I   OExcetion that might occur while transforming, missing transform
     *      parameter etc, etc...
     */
    public static Object transform(Object aObject,
                                   String aAlgorithm,
                                   Object[] aParams,
                                   Element aTransformElement,
                                   String aBaseURI)
            throws XMLSignatureException
    {
        if (logger.isDebugEnabled()){
            logger.debug("transform starting.");
            logger.debug("transform alg: "+aAlgorithm);
            logger.debug("transform obj: "+aObject);
        }

        for (int i = 0; i < msTransformers.size(); i++) {
            Transformer transformer = msTransformers.get(i);
            if (transformer.acceptsAlgorithm(aAlgorithm)){

                if (logger.isDebugEnabled())
                    logger.debug("Accepting transformer : "+transformer);

                List<DataType> dataTypes  = transformer.expectedDataTypes();

                Object transformInput = convertInputIfNecessary(aObject, dataTypes);

                return transformer.transform(transformInput, aAlgorithm, aParams, aTransformElement, aBaseURI);
            }
        }
        throw new UnsupportedOperationException("transform.isNotApplicableFor", aAlgorithm);
    }

    /*
    some transforms take an XPath node-set as input, while others require an
    octet stream. If the actual input matches the input needs of the transform,
    then the transform operates on the unaltered input. If the transform input
    requirement differs from the format of the actual input, then the input must
    be converted.
    */
    private static Object convertInputIfNecessary(Object aInput, List<DataType> aAcceptedDataTypes)
            throws XMLSignatureException
    {
        Object transformInput = null;

        Object input = aInput;

        // if input is document type then convert it to the most suitable one
        if (aInput instanceof Document){
            Document doc = (Document)input;
            input = doc.getData();
        }

        if (input instanceof NodeList){
            if (aAcceptedDataTypes.contains(DataType.NODESET)){
                transformInput = input;
            }
            else if (aAcceptedDataTypes.contains(DataType.OCTETSTREAM)){
                /*
                If the data object is a node-set and the next transform requires
                octets, the signature application MUST attempt to convert the
                node-set to an octet stream using Canonical XML [XML-C14N].
                */
                byte[] bytes = XmlUtil.outputDOM((NodeList)input, C14nMethod.INCLUSIVE);
                transformInput = new ByteArrayInputStream(bytes);
            }
        }
        else if (input instanceof InputStream){
            if (aAcceptedDataTypes.contains(DataType.OCTETSTREAM)){
                transformInput = input;
            }
            else if (aAcceptedDataTypes.contains(DataType.NODESET)){
                /*
                If the data object is an octet stream and the next transform
                requires a node-set, the signature application MUST attempt to
                parse the octets yielding the required node-set via [XML]
                well-formed processing.
                */
                try {
                    DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                    dfactory.setValidating(false);
                    dfactory.setNamespaceAware(true);
                    DocumentBuilder db = dfactory.newDocumentBuilder();
                    org.w3c.dom.Document doc = db.parse((InputStream)input);
                    XmlUtil.circumventBug2650(doc);
                    transformInput = XmlUtil.getNodeSet(doc, true);
                } catch (Exception ex) {
                    throw new XMLSignatureException(ex, "errors.convert", "Stream", "NodeSet");
                }

            }
        }
        return transformInput;
    }

}
