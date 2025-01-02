package tr.gov.tubitak.uekae.esya.api.cmssignature.provider;

import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;

/**
 * @author ayetgin
 */
public class CMSSignableImpl implements Signable
{
    private ISignable internal;

    public CMSSignableImpl(ISignable aInternal)
    {
        internal = aInternal;
    }

    public InputStream getContent() throws SignatureException
    {
        try {
        return internal.getAsInputStream();
        } catch (Exception x){
            throw new SignatureException(x);
        }
    }

    public byte[] getDigest(DigestAlg aDigestAlg) throws SignatureException
    {
        try {
        return internal.getMessageDigest(aDigestAlg);
        } catch (Exception x){
            throw new SignatureException(x);
        }
    }

    public String getURI()
    {
        return null;  // todo
    }

    public String getResourceName()
    {
        return null;  // todo
    }

    public String getMimeType()
    {
        return null;  // todo
    }
}
