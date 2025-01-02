package tr.gov.tubitak.uekae.esya.api.smartcard.apdu.akis;

import gnu.crypto.key.rsa.GnuRSAPrivateKey;
import gnu.crypto.key.rsa.GnuRSAPublicKey;
import tubitak.akis.cif.dataStructures.RSAKey;
import tubitak.akis.cif.dataStructures.SDOHeader;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;

/**
 * Created by selami.ozbey on 2/26/2015.
 */
public class RSAKeyFactory {
    public RSAKeyFactory() {
    }

    public static RSAKey createRSAPublicKey(byte[] key, SDOHeader sdo, byte[] chr, byte cha) throws CryptoException {
        GnuRSAPublicKey gnuRSAPublicKey = (GnuRSAPublicKey) KeyUtil.decodePublicKey(AsymmetricAlg.RSA, key);
        byte[] mod = normalize(gnuRSAPublicKey.getModulus().toByteArray());
        byte[] exp = gnuRSAPublicKey.getPublicExponent().toByteArray();
        return new RSAKey(sdo, mod, exp, chr, cha);
    }

    public static RSAKey createRSAPrivateKey(byte[] priKey, SDOHeader sdo) throws CryptoException {
        GnuRSAPrivateKey privKey = (GnuRSAPrivateKey)KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, priKey);
        byte[] prime1 = normalize(privKey.getPrimeP().toByteArray());
        byte[] prime2 = normalize(privKey.getPrimeQ().toByteArray());
        byte[] exp1 = normalize(privKey.getPrimeExponentP().toByteArray());
        byte[] exp2 = normalize(privKey.getPrimeExponentQ().toByteArray());
        byte[] inv2 = normalize(privKey.getCrtCoefficient().toByteArray());
        byte[] mod = normalize(privKey.getModulus().toByteArray());
        return new RSAKey(sdo, prime1, prime2, exp1, exp2, inv2, mod);
    }

    public static RSAKey createRSAPrivateKey(GnuRSAPrivateKey privKey, SDOHeader sdo)  {
        byte[] prime1 = normalize(privKey.getPrimeP().toByteArray());
        byte[] prime2 = normalize(privKey.getPrimeQ().toByteArray());
        byte[] exp1 = normalize(privKey.getPrimeExponentP().toByteArray());
        byte[] exp2 = normalize(privKey.getPrimeExponentQ().toByteArray());
        byte[] inv2 = normalize(privKey.getCrtCoefficient().toByteArray());
        byte[] mod = normalize(privKey.getModulus().toByteArray());
        return new RSAKey(sdo, prime1, prime2, exp1, exp2, inv2, mod);
    }

    private static byte[] normalize(byte[] comp) {
        if(comp[0] == 0) {
            int len = comp.length - 1;
            byte[] truncComp = new byte[len];
            System.arraycopy(comp, 1, truncComp, 0, len);
            return truncComp;
        } else {
            return comp;
        }
    }
}

