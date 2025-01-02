package tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Document for in memory byte[]
 *
 * @author ahmety
 * date: May 14, 2009
 */
public class InMemoryDocument extends Document
{
    private byte[] mBytes;
    private InputStream mStream;

    public InMemoryDocument(byte[] aBytes, String aURI, String aMIMEType, String aEncoding)
    {
        super(aURI, aMIMEType, aEncoding);
        mBytes = aBytes;
    }

    public DataType getType()
    {
        return DataType.OCTETSTREAM;
    }

    @Override
    public byte[] getBytes() throws XMLSignatureException
    {
        return mBytes;
    }

    public InputStream getStream()
    {
        if (mStream==null){
            mStream = new ByteArrayInputStream(mBytes);
        }
        return mStream;
    }

}
