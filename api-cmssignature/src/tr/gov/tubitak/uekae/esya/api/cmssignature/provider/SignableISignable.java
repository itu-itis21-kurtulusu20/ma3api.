package tr.gov.tubitak.uekae.esya.api.cmssignature.provider;

import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ayetgin
 */
public class SignableISignable implements ISignable
{
    Signable signable;

    public SignableISignable(Signable aSignable)
    {
        signable = aSignable;
    }

    public byte[] getContentData()
    {
        try {
            return AsnIO.streamOku(signable.getContent());
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

    public byte[] getMessageDigest(DigestAlg aDigestAlg) throws CryptoException, IOException
    {
        try {
        return signable.getDigest(aDigestAlg);
        } catch (Exception x){
            throw new IOException(x);
        }
    }

    public InputStream getAsInputStream() throws IOException
    {
        try {
            return signable.getContent();
        } catch (Exception x){
            throw new IOException(x);
        }

    }
}
