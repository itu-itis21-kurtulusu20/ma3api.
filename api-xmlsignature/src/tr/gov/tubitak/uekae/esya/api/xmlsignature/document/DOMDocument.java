package tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.ATTR_ENCODING;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.ATTR_MIMETYPE;

/**
 * Document containing DOM Node.
 *
 * @see org.w3c.dom.Node
 *
 * @author ahmety
 * date: Jun 17, 2009
 */
public class DOMDocument extends Document
{
    private static final Logger logger = LoggerFactory.getLogger(DOMDocument.class);

    private NodeList mNodeList;
    private boolean mIncludeComments;

    /**
     * Constructs DOMDocument which holds its data as NodeList. Note that this
     * constructor strips comments from data. If you dont wish so, use other
     * constructor
     * 
     * @param aNode that holds xml data
     * @param aURI where data is obtained.
     */
    public DOMDocument(Node aNode, String aURI)
    {
        this(aNode, aURI, false);
    }

    public DOMDocument(Node aNode, String aURI, boolean aIncludeComments)
    {
        super(aURI, "text/xml", null);

        mIncludeComments = aIncludeComments;

        if (aNode==null){
            throw new ESYARuntimeException("invalid.argument");
        }
        if (logger.isDebugEnabled())
            logger.debug("Constructing DomDocument from node : " + aNode.getNodeName());

        if (aNode instanceof Element){
            String mime = ((Element)aNode).getAttributeNS(null, ATTR_MIMETYPE);
            if (mime!=null && mime.length()>0){
                mMIMEType = mime;
            }

            String encoding = ((Element)aNode).getAttributeNS(null, ATTR_ENCODING);
            if (encoding!=null && encoding.length()>0){
                mEncoding = encoding;
            }
        }

        try {

            //XmlUtil.circumventBug2650(XMLUtils.getOwnerDocument(aNode));
            mNodeList = XmlUtil.getNodeSet(aNode, aIncludeComments);

        } catch (Exception e){
            logger.error("Error in DOMDocument", e);
        }
    }

    public DOMDocument(NodeList aNodeList, String aURI, String aMIMEType, String aEncoding)
    {
        super(aURI, aMIMEType, aEncoding);
        mNodeList = aNodeList;
    }

    /**
     * Get document content as bytes, which requires datatype conversion from
     * InputStream or NodeList.
     * @return document content as byte[]
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          if any errors occur while datatype
     *          conversion
     */
    @Override
    public byte[] getBytes() throws XMLSignatureException
    {
        if (mBytesCached==null)
        {
            switch (getType()){
                case NODESET:     mBytesCached = XmlUtil.outputDOM(getNodeList(), mIncludeComments ?  C14nMethod.INCLUSIVE_WITH_COMMENTS : C14nMethod.INCLUSIVE); break;
                case OCTETSTREAM: mBytesCached = toByteArray(getStream()); break;
            }
        }
        // dummy return, we shouldnt be here!
        return mBytesCached;
    }

    public DataType getType()
    {
        return DataType.NODESET;
    }

    @Override
    public NodeList getNodeList() 
    {
        return mNodeList;
    }
}
