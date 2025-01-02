package tr.gov.tubitak.uekae.esya.api.asic.core;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class SignableEntry implements Signable
{
    Signable internal;
    String pathInZip;

    public SignableEntry(Signable aInternal, String aPathInZip)
    {
        internal = aInternal;
        pathInZip = aPathInZip;
    }

    public InputStream getContent() throws SignatureException
    {
        return internal.getContent();
    }

    public byte[] getDigest(DigestAlg aDigestAlg) throws SignatureException
    {
        return internal.getDigest(aDigestAlg);
    }

    public String getURI()
    {
        return pathInZip;
    }

    public String getResourceName()
    {
        return internal.getResourceName();
    }

    public String getMimeType()
    {
        return internal.getMimeType();
    }
}
