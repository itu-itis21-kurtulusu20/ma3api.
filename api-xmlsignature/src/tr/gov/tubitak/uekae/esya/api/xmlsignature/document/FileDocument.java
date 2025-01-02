package tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Document for file.
 *
 * @author ahmety
 * date: May 14, 2009
 */
public class FileDocument extends Document
{
    private File mFile;

    public FileDocument(File aFile) throws XMLSignatureException
    {
        this(aFile, URLConnection.guessContentTypeFromName(aFile.getName()));
    }

    public FileDocument(File aFile, String aMIMEType) throws XMLSignatureException
    {
        this(aFile, aMIMEType, null);
    }

    public FileDocument(File aFile, String aMIMEType, String aEncoding) throws XMLSignatureException
    {
        super(aFile.getPath(), aMIMEType, aEncoding);
        if (!aFile.exists()){
            throw new XMLSignatureException("errors.cantFind", aFile.getAbsolutePath());
        }
        mFile = aFile;
    }

    public String getURI()
    {
        return mFile.getPath();
    }

    public DataType getType()
    {
        return DataType.OCTETSTREAM;
    }

    public InputStream getStream() throws XMLSignatureException
    {
        try {
            return new FileInputStream(mFile);
        } catch (Exception x){
            // shouldn't happen
            throw new XMLSignatureException(x, "I/O error on FileDocument.getStream");
        }
    }

}
