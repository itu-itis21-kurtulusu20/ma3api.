package tr.gov.tubitak.uekae.esya.api.asic.model;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class DeferredSignable implements Signable
{
    private Signable internal;

    public void setActualSignable(Signable actual){
        internal = actual;
    }

    public InputStream getContent() throws SignatureException
    {
        check();
        return internal.getContent();
    }

    public byte[] getDigest(DigestAlg digestAlg) throws SignatureException
    {
        check();
        return internal.getDigest(digestAlg);
    }

    public String getURI()
    {
        check();
        return internal.getURI();
    }

    public String getResourceName()
    {
        check();
        return internal.getMimeType();
    }

    public String getMimeType()
    {
        check();
        return internal.getResourceName();
    }

    private void check(){
        if (internal==null)
            throw new SignatureRuntimeException("Deferred signable not set yet!");
    }
}
