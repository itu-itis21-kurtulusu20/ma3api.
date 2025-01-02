package tr.gov.tubitak.uekae.esya.api.xmlsignature.document;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for different type of resolved data.
 *
 * @author ahmety
 * date: May 6, 2009
 */
public abstract class Document {
    protected static Logger logger = LoggerFactory.getLogger(Document.class);
    protected String mURI;
    protected String mMIMEType;
    protected String mEncoding;

    // used for getBytes 
    protected byte[] mBytesCached;


    protected Document(String aURI, String aMIMEType, String aEncoding)
    {
        mURI = aURI;
        mMIMEType = aMIMEType;
        mEncoding = aEncoding;
    }

    public String getURI()
    {
        return mURI;
    }

    public String getMIMEType()
    {
        return mMIMEType;
    }

    public String getEncoding()
    {
        return mEncoding;
    }

    public abstract DataType getType();

    /**
     * Get document content in most suitable form
     *
     * @return document content as InputStream or <code>{@link NodeList}</code>
     *      according to <code>{@link #getType}</code>
     * @throws XMLSignatureException if IOException occurs
     */
    public Object getData() throws XMLSignatureException {
        try {
            switch (getType()){
                case OCTETSTREAM: return getStream();
                case NODESET: return getNodeList();
            }
        }
        catch (UnsupportedOperationException e){
            // we shouldnt be here!
            throw new ESYARuntimeException(getClass() + "'es " + getType()
                    + " data returning method should have been supported.", e);
        }
        // shouldnt be here, no i18n
        throw new ESYARuntimeException("Unknown data type.");
    }

    /**
     * Get document content as bytes, which requires datatype conversion from
     *      InputStream or NodeList.
     *
     * @return document content as byte[]
     * @throws XMLSignatureException if any errors occur while datatype
     *      conversion
     */
    public byte[] getBytes() throws XMLSignatureException
    {
        if (mBytesCached==null)
        {
            switch (getType()){
                case NODESET:     mBytesCached = XmlUtil.outputDOM(getNodeList(), C14nMethod.INCLUSIVE); break;
                case OCTETSTREAM: mBytesCached = toByteArray(getStream()); break;
            }
        }
        // dummy return, we shouldnt be here!
        return mBytesCached;
    }

    /**
     * data return method for OCTETSTREAM data type documents
     *
     * @return data as Stream
     * @throws UnsupportedOperationException if implementing class is a NodeSet
     *      type document
     */
    public InputStream getStream() throws XMLSignatureException
    {
        throw new UnsupportedOperationException("unsupported.operation", getClass().getName()+".getStream()");
    }

    /**
     * data return method for NODESET data type documents
     *
     * @return data as NodeList
     * @throws UnsupportedOperationException if implementing class is a
     *      OctetStream type document
     */
    public NodeList getNodeList() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("unsupported.operation", getClass().getName()+".getNodeList()");
    }

    /**
     * Convert stream to byte[]
     *
     * @param aStream to be converted
     * @return Stream in byte[] format
     */
    public static byte[] toByteArray(InputStream aStream) {
        byte[] mBytes = null;
        try {
            //aStream.reset();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int avail, read;
            while ((avail = aStream.available())>0){
                byte[] bytes = new byte[avail];
                read = aStream.read(bytes);
                if (read!=-1){
                    bos.write(bytes, 0, read);
                }
            }
            mBytes = bos.toByteArray();
        } catch (IOException e){
            System.out.println("Stream->byte donusumunde hata");
            logger.error("Error in Document", e);
        }

        return mBytes;
    }

}
