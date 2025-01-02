using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.src.pkcs11.scheme
{
    public class ECDSA_SS : ISignatureScheme
    {
        String mSignatureAlg;
        long[] mMechanism;
        bool mIsSigning;

        public ECDSA_SS(String aSigningAlg, long[] aMechanismList)
        {
            mSignatureAlg = aSigningAlg;
            mMechanism = aMechanismList;
        }

        public CK_MECHANISM getMechanism()
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            if (mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA_SHA1) && SmartOp._in(PKCS11Constants_Fields.CKM_ECDSA_SHA1, mMechanism))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_ECDSA_SHA1;
                return mech;
            }

            else if (mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA) || mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA_SHA1) || mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA_SHA224)
                     || mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA_SHA256)
                     || mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA_SHA384)
                     || mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA_SHA512))
            {
                if (SmartOp._in(PKCS11Constants_Fields.CKM_ECDSA, mMechanism))
                {
                    mech.mechanism = PKCS11Constants_Fields.CKM_ECDSA;
                    return mech;
                }
            }

            throw new SmartCardException("Mechanism is not supported.");
        }

        public byte[] getSignatureInput(byte[] aMessage)
        {
            CK_MECHANISM mech = getMechanism();

            if (mech.mechanism == PKCS11Constants_Fields.CKM_ECDSA_SHA1)
            {
                return aMessage;
            }
            else
            {
                if (mSignatureAlg.Equals(Algorithms.SIGNATURE_ECDSA))
                    return aMessage;
                String digestAlgStr = null;
                try
                {
                    digestAlgStr = Algorithms.getDigestAlgOfSignatureAlg(mSignatureAlg);
                }
                catch (ESYAException aException)
                {
                    throw new SmartCardException(aException);
                }
   
                try
                {     
                    DigestAlg digestAlg = DigestAlg.fromName(digestAlgStr);
                    return DigestUtil.digest(digestAlg, aMessage);                   
                }
                catch (NoSuchAlgorithmException aEx)
                {
                    throw new SmartCardException("Unknown digest algorithm", aEx);
                }

            }
        }

        public void init(bool aIsSigning)
        {
            mIsSigning = aIsSigning;
        }
    }
}
