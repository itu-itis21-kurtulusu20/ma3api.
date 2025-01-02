package tr.gov.tubitak.uekae.esya.api.asic.model;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.impl.BaseSignable;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author ayetgin
 */
public class SignableZipEntry extends BaseSignable
{
    private ZipFile file;
    private ZipEntry entry;

    public SignableZipEntry(ZipFile aFile, ZipEntry aEntry)
    {
        file = aFile;
        entry = aEntry;
    }

    public InputStream getContent() throws SignatureException
    {
        try {
            return file.getInputStream(entry);
        } catch (Exception x){
            throw new SignatureException(x);
        }
    }

    public String getURI()
    {
        return entry.getName();
    }

    public String getMimeType()
    {
        return null;
    }
}
