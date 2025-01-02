using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.provider
{
    public class SDValidationData : SignedDataValidation
    {
        readonly ESignedData signedData;

        public SDValidationData(ESignedData aSignedData)
        {
            signedData = aSignedData;
            collectValidationData();
        }
        public SDValidationData(ESignedData aSignedData, Context context)
        {
            signedData = aSignedData;
            collectValidationData(context);
        }
        void collectValidationData()
        {
            try
            {
                base._fillCertRevocationLists(signedData, null);
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }
        void collectValidationData(Context context)
        {
            try
            {
                base._fillCertRevocationLists(signedData, CMSSigProviderUtil.toSignatureParameters(context));
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }
        public List<ECertificate> getCerts()
        {
            return mCerts;
        }
        public List<ECRL> getCRLs()
        {
            return mCRLs;
        }
        public List<EBasicOCSPResponse> getOCSPResponses()
        {
            return mOCSPs;
        }

    }
}
