package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.util.HashMap;

/**
 * @author ayetgin
 */
public abstract class BaseSignable implements Signable
{
    private static final int DEFAULT_BUF_SIZE = 32 * 1024;

    private HashMap<DigestAlg, byte[]> mDigestTable = new HashMap<DigestAlg, byte[]>(1);

    private final byte[] mBuffer;

    protected BaseSignable()
    {
        mBuffer = new byte[DEFAULT_BUF_SIZE];
    }

    public byte[] getDigest(DigestAlg aDigestAlg) throws SignatureException
    {
        byte[] ozet = mDigestTable.get(aDigestAlg);
        if (ozet == null)
        {
            try {
                ozet = DigestUtil.digestStream(aDigestAlg, getContent(), mBuffer.length);
                mDigestTable.put(aDigestAlg, ozet);
            }
            catch (Exception x){
                throw new SignatureException("Cant digest signable!", x);
            }
        }

        return ozet;
    }

    public String getResourceName() {
        String uri = getURI();
        int lastSlashIndex = uri.lastIndexOf("/");
        return uri.substring(lastSlashIndex+1, uri.length());
    }
}
