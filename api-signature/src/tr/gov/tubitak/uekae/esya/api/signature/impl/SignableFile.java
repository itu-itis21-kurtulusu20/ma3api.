package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 * Signable file which its content will be added to a signature.
 *
 * @see tr.gov.tubitak.uekae.esya.api.signature.Signable
 * @see tr.gov.tubitak.uekae.esya.api.signature.Signature#addContent(tr.gov.tubitak.uekae.esya.api.signature.Signable, boolean)
 *
 * @author ayetgin
 */
public class SignableFile extends BaseSignable
{

    private File mFile;
    private String mMime;

    // todo mime type detection?

    public SignableFile(String aPath, String aMimeType)
    {
        super();
        mFile = new File(aPath);
        if (mFile.isDirectory())
            throw new SignatureRuntimeException("Directory cant be signed!");
        if (!mFile.exists())
            throw new SignatureRuntimeException("Signable file "+aPath+" not exists!");
        mMime = aMimeType;
    }

    public SignableFile(File aFile, String aMimeType)
    {
        super();
        mFile = aFile;
        mMime = aMimeType;
    }

    public InputStream getContent() throws SignatureException
    {
        try {
            return new FileInputStream(mFile);
        } catch (Exception x){
            throw new SignatureException(x);
        }
    }


    public String getURI()
    {
        return mFile.toURI().toASCIIString();
    }

    public String getMimeType()
    {
        return mMime;
    }
}
