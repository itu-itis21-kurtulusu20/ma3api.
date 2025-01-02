using System;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme
{
    public class Rsa_SS : ISignatureScheme
    {
        private readonly String _signatureAlg;
        private readonly long[] _mechanisms;

        public Rsa_SS(String aSigningAlg, long[] aMechanismList)
        {
            _signatureAlg = aSigningAlg;
            _mechanisms = aMechanismList;
        }

        public CK_MECHANISM getMechanism()
        {

            CK_MECHANISM mech = new CK_MECHANISM();
            if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_RAW))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_RSA_X_509;
                return mech;
            }

            if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA256)
                && SmartOp._in(PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS, _mechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS;
                return mech;
            }

            if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA384)
                && SmartOp._in(PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS, _mechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS;
                return mech;
            }

            if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA512)
                && SmartOp._in(PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS, _mechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS;
                return mech;
            }

            if ((_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA1)
                 || _signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA256)
                 || _signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA384)
                 || _signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA512)
                 || _signatureAlg.Equals(Algorithms.SIGNATURE_RSA))
                && SmartOp._in(PKCS11Constants_Fields.CKM_RSA_PKCS, _mechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS;
                return mech;
            }

            if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_SHA1)
                && SmartOp._in(PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS, _mechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS;
                return mech;
            }

            if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA_MD5)
                && SmartOp._in(PKCS11Constants_Fields.CKM_MD2_RSA_PKCS, _mechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_MD2_RSA_PKCS;
                return mech;
            }

            throw new SmartCardException("Mechanism is not supported.");
        }

        public byte[] getSignatureInput(byte[] aMessage)
        {
            CK_MECHANISM mech = getMechanism();
            if (mech.mechanism == PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS
                || mech.mechanism == PKCS11Constants_Fields.CKM_MD2_RSA_PKCS
                || mech.mechanism == PKCS11Constants_Fields.CKM_RSA_X_509 
                || mech.mechanism == PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS
                || mech.mechanism == PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS
                || mech.mechanism == PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS)
            {
                return aMessage;
            }
            else if (mech.mechanism == PKCS11Constants_Fields.CKM_RSA_PKCS)
            {
                if (_signatureAlg.Equals(Algorithms.SIGNATURE_RSA) || _signatureAlg.Equals(Algorithms.SIGNATURE_RSA_RAW))
                    return aMessage;

                //MessageDigest ozetci = null;
                byte[] hashPrefix = null;
                String digestAlg = null;
                try
                {
                    digestAlg = Algorithms.getDigestAlgOfSignatureAlg(_signatureAlg);
                    //ozetci = MessageDigest.getInstance(digestAlg);                
                    hashPrefix = getPrefixForDigestAlg(digestAlg);
                }

                catch (ESYAException e)
                {
                    throw new SmartCardException("UnKnown Digest Algorithm", e);
                }
                catch (Exception aEx)
                {
                    throw new SmartCardException(digestAlg + " algorithm is not supported");
                }

                //ozetci.update(aMessage);
                //byte[] messageHash = ozetci.digest();
                byte[] messageHash = DigestUtil.digest(DigestAlg.fromName(digestAlg), aMessage);

                byte[] realHashstruct = new byte[hashPrefix.Length + messageHash.Length];
                Array.Copy(hashPrefix, 0, realHashstruct, 0,
                           hashPrefix.Length);
                Array.Copy(messageHash, 0, realHashstruct, hashPrefix.Length,
                           messageHash.Length);

                return realHashstruct;
            }    
            else
            {
                return null;
            }
        }


        private byte[] getPrefixForDigestAlg(String aDigestAlg)
        {
            if (aDigestAlg.Equals(Algorithms.DIGEST_SHA1))
                return sha1Prefix;
            else if (aDigestAlg.Equals(Algorithms.DIGEST_SHA256))
                return sha256Prefix;
            if (aDigestAlg.Equals(Algorithms.DIGEST_SHA384))
                return sha384Prefix;
            if (aDigestAlg.ToUpper().Equals(Algorithms.DIGEST_SHA512))
                return sha512Prefix;

            throw new ESYAException(aDigestAlg + " UnKnown digest algorithm");
        }

        private static readonly byte[] sha1Prefix = new[]
                                                        {
                                                            (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09,
                                                            (byte) 0x06, (byte) 0x05, (byte) 0x2B,
                                                            (byte) 0x0E, (byte) 0x03, (byte) 0x02, (byte) 0x1A,
                                                            (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14
                                                        };

        private static readonly byte[] sha256Prefix = new[]
                                                          {
                                                              (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x0d,
                                                              (byte) 0x06, (byte) 0x09, (byte) 0x60,
                                                              (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x65,
                                                              (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x01,
                                                              (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x20
                                                          };

        private static readonly byte[] sha384Prefix = new[]
                                                          {
                                                              (byte) 0x30, (byte) 0x41, (byte) 0x30, (byte) 0x0d,
                                                              (byte) 0x06, (byte) 0x09, (byte) 0x60,
                                                              (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x65,
                                                              (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x02,
                                                              (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x30
                                                          };

        private static readonly byte[] sha512Prefix = new[]
                                                          {
                                                              (byte) 0x30, (byte) 0x51, (byte) 0x30, (byte) 0x0d,
                                                              (byte) 0x06, (byte) 0x09, (byte) 0x60,
                                                              (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x65,
                                                              (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x03,
                                                              (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x40
                                                          };
    }
}