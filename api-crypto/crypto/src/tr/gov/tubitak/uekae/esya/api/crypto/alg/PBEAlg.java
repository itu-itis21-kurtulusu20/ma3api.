package tr.gov.tubitak.uekae.esya.api.crypto.alg;


/**
 * @author ayetgin
 */

public class PBEAlg extends CipherAlg
{

    public static final PBEAlg PBE_AES256_SHA256 = new PBEAlg(null, CipherAlg.AES256_CBC, DigestAlg.SHA256);

    private DigestAlg mDigestAlg;
    private CipherAlg mCipherAlg;

    public PBEAlg(int[] aOID, CipherAlg aCipherAlg, DigestAlg aDigestAlg)
    {
        super(aOID, null, aCipherAlg.getBlockSize(), aCipherAlg.getMod(), aCipherAlg.getPadding());
        mDigestAlg = aDigestAlg;
        mCipherAlg = aCipherAlg;

        String cipherName = aCipherAlg.getName().split("/")[0];
        mName = "PBEWith"+cipherName+"And"+aDigestAlg.getName();
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    public CipherAlg getCipherAlg()
    {
        return mCipherAlg;
    }

}
