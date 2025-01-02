using System;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class PBEAlg : CipherAlg
    {
        //todo bu oid dogru mu?? 05.08.2010
        public static readonly PBEAlg PBE_AES256_SHA256 = new PBEAlg(null/*_algorithmsValues.id_sha256*/, CipherAlg.AES256_CBC, DigestAlg.SHA256);

        private readonly DigestAlg mDigestAlg;
        private readonly CipherAlg mCipherAlg;

        private PBEAlg(int[] aOID, CipherAlg aCipherAlg, DigestAlg aDigestAlg)
            : base(aOID, /*aCipherAlg.getName()*/null, aCipherAlg.getBlockSize(), aCipherAlg.getMod(), aCipherAlg.getPadding())
        {
            mDigestAlg = aDigestAlg;
            mCipherAlg = aCipherAlg;
            String cipherName = aCipherAlg.getName().Split(new char[] { '/' })[0];
            //name değeri base classin constructor'unda null yapılıyor, gercek degeri simdi yaziliyor!!
            //mName = "PBEWith" + cipherName + "And" + aDigestAlg.getName();
            mName = "PBEWITH" + aDigestAlg.getName() + "AND" + cipherName;

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
}
