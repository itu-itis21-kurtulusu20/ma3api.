package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * The normative specification for base64 decoding transforms is [MIME]. The
 * base64 Transform element has no content. The input is decoded by the
 * algorithms. This transform is useful if an application needs to sign the raw
 * data associated with the encoded content of an element.
 * 
 * @author ahmety
 * date: Jul 27, 2009
 */
public class Base64Transformer implements Transformer
{
    private Logger logger = LoggerFactory.getLogger(Base64Transformer.class);

    private static final List<DataType> TYPES = Arrays.asList(DataType.NODESET, DataType.OCTETSTREAM);


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
        return TransformType.BASE64.getUrl().equals(aAlgorithmURI);
    }

    public InputStream transform(Object aObject,
                                 String aAlgorithmURI,
                                 Object[] aParams,
                                 Element aTransformElement,
                                 String aBaseURI)
            throws XMLSignatureException
    {
        if (logger.isDebugEnabled()){
            logger.debug("Object for base64 transform: "+aObject);
        }
        try {
            /*
            This transform requires an octet stream for input.
            */
            byte[] bytes;
            if (aObject instanceof InputStream){
                byte[] source = Document.toByteArray((InputStream)aObject);
                if (logger.isDebugEnabled()){
                    logger.debug("Transforming byte[]: "+ StringUtil.substring(source, 256));
                }
                bytes = Base64.decode(source, 0, source.length);
            }
            else if (aObject instanceof NodeList)
            {
                /*
                If an XPath node-set (or sufficiently functional alternative) is
                given as input, then it is converted to an octet stream by
                performing operations logically equivalent to
                    1) applying an XPath transform with expression self::text(),
                    then
                    2) taking the string-value of the node-set. Thus, if an XML
                    element is identified by a shortname XPointer in the Reference
                    URI, and its content consists solely of base64 encoded character
                    data, then this transform automatically strips away the start
                    and end tags of the identified element and any of its descendant
                    elements as well as any descendant comments and processing
                    instructions.
                */
                String text = XmlUtil.getText((NodeList)aObject);
                if (logger.isDebugEnabled()){
                    logger.debug("Transforming node text: "+ StringUtil.substring(text, 256));
                }
                bytes = Base64.decode(text);
            } else {
                throw new UnsupportedOperationException("transform.isNotApplicableFor", "Base64", aObject);
            }
            if (logger.isDebugEnabled()){
                logger.debug("Transformed data is : "+ StringUtil.substring(bytes, 256));
            }

            return new ByteArrayInputStream(bytes);

        } catch (Exception x){
            throw new XMLSignatureException(x,"transform.errorApplyingTo", "Base64", aObject);
        }
    }
}
