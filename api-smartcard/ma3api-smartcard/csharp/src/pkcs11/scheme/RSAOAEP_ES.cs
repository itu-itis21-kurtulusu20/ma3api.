using System;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme
{
    /* - Dolgusuz RSA (CKM_RSA_X_509) desteklenmiyor.  
       - 1.2.2 kartlarda deskteklenen mekanizmalar
          CKM_RSA_PKCS
       - 2.2.8 kartalarda ise
         CKM_RSA_PKCS
         CKM_RSA_PKCS_PSS
         CKM_RSA_PKCS_OAEP 
     */
    public class RSAOAEP_ES
    {

        private readonly OAEPPadding mParams;
        private readonly long[] mSupportedMechs;
        private bool mEncryption;

        public RSAOAEP_ES(OAEPPadding aParams, long[] supportedMechs)
        {
            mParams = aParams;
            mSupportedMechs = supportedMechs;
        }

        public void init(bool aEncryption)
        {
            mEncryption = aEncryption;
        }

        public byte[] getResult(byte[] aData)
        {       
            if (getMechanism().mechanism == PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP)
                return aData;

            throw new SmartCardException("Mechanism is not supported.");
        }

        public CK_MECHANISM getMechanism()
        {
            CK_MECHANISM mech = new CK_MECHANISM();
            if (SmartOp._in(PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP, mSupportedMechs))
            {
                mech.mechanism = PKCS11Constants_Fields.CKM_RSA_PKCS_OAEP;
                CK_RSA_PKCS_OAEP_PARAMS params_ = new CK_RSA_PKCS_OAEP_PARAMS();

                try
                {
                    params_.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(mParams.getDigestAlg().getName());
                    params_.mgf = ConstantsUtil.getMGFAlgorithm(params_.hashAlg);
                    params_.source = PKCS11Constants_Fields.CKZ_DATA_SPECIFIED;
                    params_.pSourceData = null;
                }
                catch (ESYAException e)
                {
                    throw new SmartCardException(e);
                }

                mech.pParameter = params_;
                return mech;

            }      
            throw new SmartCardException("Mechanism is not supported.");
        }
    }
}

