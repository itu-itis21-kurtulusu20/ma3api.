package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/17/11
 * Time: 8:45 AM
 */
public class Iso9796_2_SC1 implements ISignatureScheme {
    String _signatureAlg;
    int _keyLength;
    boolean _isSigning;

    public Iso9796_2_SC1(String aSignatureAlg, int aKeyLength) 
    {
        _signatureAlg = aSignatureAlg;
        _keyLength = aKeyLength;
    }

    private CK_MECHANISM getMechanism()
    {
    	CK_MECHANISM mech = new CK_MECHANISM(0L);
    	mech.mechanism = PKCS11Constants.CKM_RSA_X_509; 
        return mech;
    }


    public P11SignParameters getSignParameters(byte[] aTobeSigned) throws SmartCardException, PKCS11Exception {

        CK_MECHANISM mech = getMechanism();

        int Ni = _keyLength;
        if (Ni % 128 != 0) {
            throw new SmartCardException("Key length:" + Ni + ", must be multiple of 128");
        }

        byte[] M = aTobeSigned;

        byte[] hashM = null;
        MessageDigest ozetci = null;
        
        String digestAlg;
		try 
		{
			digestAlg = Algorithms.getDigestAlgOfSignatureAlg(_signatureAlg);
		} 
		catch (ESYAException e) 
		{
			throw new SmartCardException("Unkown algorithm", e);
		}

        //DigestAlg digestAlg = DigestAlg.fromName(_signatureHash);
        try {
            //hashM = DigestUtil.digest(digestAlg, M);
            ozetci = MessageDigest.getInstance(digestAlg);
            ozetci.update(M);
            hashM = ozetci.digest();

        } catch (NoSuchAlgorithmException ex) {
            throw new SmartCardException("Error in digest calculation!", ex);
        }

        int t = 0;
        byte[] block = new byte[Ni];
        int delta = 0;
        t = 8;
        //delta = Ni - digestAlg.getDigestLength() - 1;
        delta = Ni - ozetci.getDigestLength() - 1;
        //digest.doFinal(block, delta);
        System.arraycopy(hashM, 0, block, delta, hashM.length);
        block[block.length - 1] = (byte) 0xBC;


        byte header = 0;
        //int x = (digestAlg.getDigestLength() + M.length) * 8 + t + 4 - Ni * 8;
        int x = (ozetci.getDigestLength() + M.length) * 8 + t + 4 - Ni * 8;

        if (x > 0) {
            int mR = M.length - ((x + 7) / 8);
            header = 0x60;

            delta -= mR;

            System.arraycopy(M, 0, block, delta, mR);
        } else {
            header = 0x40;
            delta -= M.length;

            System.arraycopy(M, 0, block, delta, M.length);
        }

        if ((delta - 1) > 0) {
            for (int i = delta - 1; i != 0; i--) {
                block[i] = (byte) 0xbb;
            }
            block[delta - 1] ^= (byte) 0x01;
            block[0] = (byte) 0x0b;
            block[0] |= header;
        } else {
            block[0] = (byte) 0x0a;
            block[0] |= header;
        }
        return new P11SignParameters(mech, block);
    }

	public void init(boolean aIsSigning) 
	{ _isSigning = aIsSigning;}
}
