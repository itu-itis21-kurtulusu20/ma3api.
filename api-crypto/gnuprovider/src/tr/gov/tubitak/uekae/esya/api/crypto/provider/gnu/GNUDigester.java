package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.Digester;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;

/**
 * @author ayetgin
 */
public class GNUDigester implements Digester
{
    IMessageDigest mDigest;

    public GNUDigester(DigestAlg aAlg) throws UnknownElement
    {
        mDigest = HashFactory.getInstance(GNUProviderUtil.resolveDigestName(aAlg));
    }

    public void update(byte[] aData)
    {
        mDigest.update(aData, 0, aData.length);
    }

    public void update(byte[] aData, int aOffset, int aLength)
    {
        mDigest.update(aData, aOffset, aLength);
    }

    public byte[] digest()
    {
        return mDigest.digest();
    }


}
