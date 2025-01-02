package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnknownAlgorithmException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.Canonicalizer;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Canonicalization transform support for both xml and text based content.
 *
 * @author ahmety
 * date: Jun 19, 2009
 */
public class C14nTransformer implements Transformer
{

    private static final Logger logger = LoggerFactory.getLogger(C14nTransformer.class);

    private static List<DataType> msTypes = Arrays.asList( DataType.NODESET, DataType.OCTETSTREAM );

    public List<DataType> expectedDataTypes()
    {
        return msTypes;
    }

    public DataType returnType()
    {
        return DataType.OCTETSTREAM;
    }

    public boolean acceptsAlgorithm(String aAlgorithmURI)
    {
        return C14nMethod.isSupported(aAlgorithmURI);
    }

    public InputStream transform(Object aObject, String aAlg,
                            Object[] aParams, Element aElement, String aBaseURI)
            throws XMLSignatureException
    {
        try {
            Canonicalizer canon = Canonicalizer.getInstance(aAlg);
            if (aObject instanceof InputStream)
            {
                byte[] input = Document.toByteArray((InputStream)aObject);
                byte[] bytes = canon.canonicalize(input);
                return new ByteArrayInputStream(bytes);
            }
            else if (aObject instanceof NodeList)
            {
                NodeList nl = (NodeList)aObject;
                if (logger.isDebugEnabled())
                    logger.debug("canon input nodelist, size: "+nl.getLength());

                byte[] bytes = canon.canonicalizeXPathNodeSet(nl);
                if (logger.isDebugEnabled())
                    logger.debug("cononed: "+StringUtil.substring(bytes, 255));

                return new ByteArrayInputStream(bytes);
            }
        } catch (Exception e){
            throw new XMLSignatureException(e, "transform.errorApplyingTo", "C14n", aObject);
        }
        throw new XMLSignatureException("transform.isNotApplicableFor", "C14n", aObject.getClass());
    }
}
