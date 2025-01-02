using System;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme
{
    public class Iso9796_2_SC1 : ISignatureScheme
    {
        private readonly String _signatureAlg;
        private readonly long _sessionId;
        private readonly int _keyLength;

        public Iso9796_2_SC1(String aSignatureAlg, int aKeyLength)
        {
            _signatureAlg = aSignatureAlg;
            _keyLength = aKeyLength;
        }

        public CK_MECHANISM getMechanism()
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            mech.mechanism = PKCS11Constants_Fields.CKM_RSA_X_509;
            return mech;            
        }


        public byte[] getSignatureInput(byte[] aTobeSigned)
        {
            int Ni = _keyLength;
            if (Ni%128 != 0)
            {
                throw new SmartCardException("Key length:" + Ni + ", must be multiple of 128");
            }

            byte[] M = aTobeSigned;

            byte[] hashM = null;
            //MessageDigest ozetci = null;

            String digestAlg;
            try
            {
                digestAlg = Algorithms.getDigestAlgOfSignatureAlg(_signatureAlg);
            }
            catch (ESYAException e)
            {
                throw new SmartCardException("Unkown algorithm", e);
            }

            DigestAlg _digestAlg = DigestAlg.fromName(digestAlg);
            try
            {
                hashM = DigestUtil.digest(_digestAlg, M);
                //ozetci = MessageDigest.getInstance(digestAlg);
                //ozetci.update(M);
                //hashM = ozetci.digest();
            }
            catch (Exception ex)
            {
                throw new SmartCardException("Error in digest calculation!", ex);
            }

            int t;
            byte[] block = new byte[Ni];
            int delta;
            t = 8;
            //delta = Ni - digestAlg.getDigestLength() - 1;
            delta = Ni - _digestAlg.getDigestLength() - 1;
            //digest.doFinal(block, delta);
            Array.Copy(hashM, 0, block, delta, hashM.Length);
            block[block.Length - 1] = 0xBC;


            byte header = 0;
            //int x = (digestAlg.getDigestLength() + M.length) * 8 + t + 4 - Ni * 8;
            int x = (_digestAlg.getDigestLength() + M.Length)*8 + t + 4 - Ni*8;

            if (x > 0)
            {
                int mR = M.Length - ((x + 7)/8);
                header = 0x60;

                delta -= mR;

                Array.Copy(M, 0, block, delta, mR);
            }
            else
            {
                header = 0x40;
                delta -= M.Length;

                Array.Copy(M, 0, block, delta, M.Length);
            }

            if ((delta - 1) > 0)
            {
                for (int i = delta - 1; i != 0; i--)
                {
                    block[i] = 0xbb;
                }
                block[delta - 1] ^= 0x01;
                block[0] = 0x0b;
                block[0] |= header;
            }
            else
            {
                block[0] = 0x0a;
                block[0] |= header;
            }
            return block;
        }
    }
}