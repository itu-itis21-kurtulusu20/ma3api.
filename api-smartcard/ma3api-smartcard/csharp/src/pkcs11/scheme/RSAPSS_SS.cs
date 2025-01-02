using System;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme
{
    public class RSAPSS_SS : ISignatureScheme
    {
        private static long[] pssMechanimsWithHash = {
            PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS_PSS,
            PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS_PSS,
            PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS_PSS,
            PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS_PSS};

        private readonly int mModBits;
        private readonly RSAPSSParams mParams = null;     

        private readonly long[] mSupportedMechanisms;

        public RSAPSS_SS(RSAPSSParams aParams, int aModBits, long[] aSupportedMechanisms)
        {
            mModBits = aModBits;
            mParams = aParams;
            mSupportedMechanisms = aSupportedMechanisms;
        }

        public CK_MECHANISM getMechanism()
        {
            CK_MECHANISM mech = new CK_MECHANISM();

            DigestAlg pssDigestAlg = mParams.getDigestAlg();

            //Check Mechanisms with hash.
            CK_MECHANISM defaultMechanismForPSS = getDefaultMechanismForPSS(pssDigestAlg);
            if (SmartOp._in(defaultMechanismForPSS.mechanism, mSupportedMechanisms))
            {
                mech.mechanism = defaultMechanismForPSS.mechanism;
                mech.pParameter = GetSCPssParameters(mParams);
            }
            else if (SmartOp._in(PKCS11Constants_Fields.CKM_RSA_PKCS_PSS, mSupportedMechanisms))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_PSS;
                mech.pParameter = GetSCPssParameters(mParams);
            }
            else
                throw new SmartCardException("Smartcard does NOT support RSAPSS Algorithm !");

            return mech;
        }

        private CK_RSA_PKCS_PSS_PARAMS GetSCPssParameters(RSAPSSParams rsapssParams)
        {
            CK_RSA_PKCS_PSS_PARAMS pkcs11PssParams = new CK_RSA_PKCS_PSS_PARAMS();

            String digestAlg = rsapssParams.getDigestAlg().getName();

            try
            {
                pkcs11PssParams.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(digestAlg);
                if(!SmartOp._in(pkcs11PssParams.hashAlg, mSupportedMechanisms))
                	throw new SmartCardException("Smartcard does NOT support digest algorithm: " + digestAlg);
                pkcs11PssParams.mgf = ConstantsUtil.getMGFAlgorithm(pkcs11PssParams.hashAlg);

            }
             catch (SmartCardException e)
            {
                throw e;
            }
            catch (ESYAException e)
            {
                throw new SmartCardException(e);
            }

            pkcs11PssParams.sLen = rsapssParams.getSaltLength();

            return pkcs11PssParams;
        }


        public byte[] getSignatureInput(byte[] aTobeSigned)
        {
            CK_MECHANISM mech = getMechanism();

            if (IsHashIncludedInMechanism(mech.mechanism))
            {
                return aTobeSigned;
            }
            else if (mech.mechanism == PKCS11Constants_Fields.CKM_RSA_PKCS_PSS)
            {
                String digestAlg = mParams.getDigestAlg().getName();
                try
                {
                    byte[] digest = DigestUtil.digest(DigestAlg.fromName(digestAlg), aTobeSigned);
                    return digest;
                }
                catch (Exception aEx)
                {
                    throw new SmartCardException(digestAlg + " algorithm is not supported", aEx);
                }

            }
            else if (mech.mechanism == PKCS11Constants_Fields.CKM_RSA_X_509)
            {
                throw new SmartCardException("Desteklenmeyen Mekanizma -> imzalama algoritmasi:RSAPSS");
            }

            throw new SmartCardException("Desteklenmeyen imzalama algoritmasi:RSAPSS");
        }

        private bool IsHashIncludedInMechanism(long mechanism)
        {
            if (SmartOp._in(mechanism, pssMechanimsWithHash))
                return true;
            else
                return false;
        }

        public static CK_MECHANISM getDefaultMechanismForPSS(DigestAlg digestAlg)
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            CK_RSA_PKCS_PSS_PARAMS _params = new CK_RSA_PKCS_PSS_PARAMS();

            _params.sLen = digestAlg.getDigestLength();

            if (digestAlg.Equals(DigestAlg.SHA1))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA1_RSA_PKCS_PSS;
                _params.hashAlg = PKCS11Constants_Fields.CKM_SHA_1;
                _params.mgf = PKCS11Constants_Fields.CKG_MGF1_SHA1;
            }
            else if (digestAlg.Equals(DigestAlg.SHA256))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA256_RSA_PKCS_PSS;
                _params.hashAlg = PKCS11Constants_Fields.CKM_SHA256;
                _params.mgf = PKCS11Constants_Fields.CKG_MGF1_SHA256;
            }
            else if (digestAlg.Equals(DigestAlg.SHA384))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA384_RSA_PKCS_PSS;
                _params.hashAlg = PKCS11Constants_Fields.CKM_SHA384;
                _params.mgf = PKCS11Constants_Fields.CKG_MGF1_SHA384;
            }
            else if (digestAlg.Equals(DigestAlg.SHA512))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_SHA512_RSA_PKCS_PSS;
                _params.hashAlg = PKCS11Constants_Fields.CKM_SHA512;
                _params.mgf = PKCS11Constants_Fields.CKG_MGF1_SHA512;
            }
            else
                throw new ESYARuntimeException("Unknown DigestAlg: " + digestAlg);

            mech.pParameter = _params;

            return mech;
        }
    }
}

