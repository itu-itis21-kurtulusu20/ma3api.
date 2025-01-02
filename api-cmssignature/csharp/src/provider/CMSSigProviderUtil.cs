using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;
using tr.gov.tubitak.uekae.esya.api.signature.config;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.provider
{
    public static class CMSSigProviderUtil
    {
        public static Dictionary<String, Object> toSignatureParameters(Context context)
        {
            Dictionary<String, Object> cmsParams = new Dictionary<String, Object>();

            cmsParams[EParameters.P_CERT_VALIDATION_POLICIES] = context.getConfig().getCertificateValidationPolicies();
            if (context.getData() != null){
            cmsParams[EParameters.P_EXTERNAL_CONTENT]= new SignableISignable(context.getData());
            }
            Config config = context.getConfig();
            cmsParams[EParameters.P_TSS_INFO]= config.getTimestampConfig().Settings;


            CertificateValidationConfig cvc = config.getCertificateValidationConfig();

            cmsParams[EParameters.P_FORCE_STRICT_REFERENCE_USE] = config.getParameters().isForceStrictReferenceUse();
            cmsParams[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = cvc.isValidateCertificateBeforeSigning();
            cmsParams[EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING] = config.getParameters().isValidateTimestampWhileSigning();
            cmsParams[EParameters.P_GRACE_PERIOD] = cvc.getGracePeriodInSeconds();
            cmsParams[EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] = cvc.getSigningTimeToleranceInSeconds();
            cmsParams[EParameters.P_IGNORE_GRACE] = cvc.getGracePeriodInSeconds() <= 0 || !cvc.isUseValidationDataPublishedAfterCreation();
            cmsParams[EParameters.P_VALIDATION_PROFILE] = cvc.getValidationProfile();
            cmsParams[EParameters.P_REVOCINFO_PERIOD]=cvc.getLastRevocationPeriodInSeconds();
            cmsParams[AllEParameters.P_VALIDATION_WITHOUT_FINDERS] = cvc.isValidateWithResourcesWithinSignature();
            cmsParams[EParameters.P_VALIDATION_INFO_RESOLVER] = context.getValidationInfoResolver();
            cmsParams[EParameters.P_TRUST_SIGNINGTIMEATTR] = config.getParameters().isTrustSigningTime();
            //cmsParams[EParameters.P_SIGNING_TIME] = context.getValidationTime();

            DigestAlg digestAlgForOCSP = config.getAlgorithmsConfig().getDigestAlgForOCSP();
            if (digestAlgForOCSP != null && digestAlgForOCSP != DigestAlg.SHA256)
            {
                cmsParams[AllEParameters.P_OCSP_DIGEST_ALG] = digestAlgForOCSP;
            }


            DigestAlg digestAlg = config.getAlgorithmsConfig().getDigestAlg();
            if (digestAlg != null && digestAlg != DigestAlg.SHA256)
            {
                cmsParams[AllEParameters.P_DIGEST_ALGORITHM] = digestAlg;
            }

            return cmsParams;
        }

        public static SignatureType convert(ESignatureType type)
        {

            if (type == ESignatureType.TYPE_BES)
                return SignatureType.ES_BES;
            else if (type == ESignatureType.TYPE_EPES)
                return SignatureType.ES_EPES;
            else if (type == ESignatureType.TYPE_EST)
                return SignatureType.ES_T;
            else if (type == ESignatureType.TYPE_ESC)
                return SignatureType.ES_C;
            else if (type == ESignatureType.TYPE_ESX_Type1)
                return SignatureType.ES_X_Type1;
            else if (type == ESignatureType.TYPE_ESX_Type2)
                return SignatureType.ES_X_Type2;
            else if (type == ESignatureType.TYPE_ESXLong)
                return SignatureType.ES_XL;
            else if (type == ESignatureType.TYPE_ESXLong_Type1)
                return SignatureType.ES_XL_Type1;
            else if (type == ESignatureType.TYPE_ESXLong_Type2)
                return SignatureType.ES_XL_Type2;
            else if (type == ESignatureType.TYPE_ESA)
                return SignatureType.ES_A;
            throw new SignatureRuntimeException("Unknown type " + type);
        }

        public static ESignatureType convert(SignatureType type)
        {
            switch (type)
            {
                case SignatureType.ES_BES: return ESignatureType.TYPE_BES;
                case SignatureType.ES_EPES: return ESignatureType.TYPE_EPES;
                case SignatureType.ES_T: return ESignatureType.TYPE_EST;
                case SignatureType.ES_C: return ESignatureType.TYPE_ESC;
                case SignatureType.ES_X_Type1: return ESignatureType.TYPE_ESX_Type1;
                case SignatureType.ES_X_Type2: return ESignatureType.TYPE_ESX_Type2;
                case SignatureType.ES_XL: return ESignatureType.TYPE_ESXLong;
                case SignatureType.ES_XL_Type1: return ESignatureType.TYPE_ESXLong_Type1;
                case SignatureType.ES_XL_Type2: return ESignatureType.TYPE_ESXLong_Type2;
                case SignatureType.ES_A: return ESignatureType.TYPE_ESA;
            }
            throw new SignatureRuntimeException("Unknown type " + type);
        }
        public static TimestampType? convertTimestampType(Asn1ObjectIdentifier timestampOID)
        {
            if (timestampOID == null)
                return null;

            if (timestampOID.Equals(AttributeOIDs.id_aa_signatureTimeStampToken))
                return TimestampType.SIGNATURE_TIMESTAMP;
            else if (timestampOID.Equals(AttributeOIDs.id_aa_ets_escTimeStamp))
                return TimestampType.SIG_AND_REFERENCES_TIMESTAMP;
            else if (timestampOID.Equals(AttributeOIDs.id_aa_ets_certCRLTimestamp))
                return TimestampType.REFERENCES_TIMESTAMP;
            else if (timestampOID.Equals(AttributeOIDs.id_aa_ets_archiveTimestamp))
                return TimestampType.ARCHIVE_TIMESTAMP;
            else if (timestampOID.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV2))
                return TimestampType.ARCHIVE_TIMESTAMP_V2;
            else if (timestampOID.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV3))
                return TimestampType.ARCHIVE_TIMESTAMP_V3;
            else if (timestampOID.Equals(AttributeOIDs.id_aa_ets_contentTimestamp))
                return TimestampType.CONTENT_TIMESTAMP;

            return null;
        }

        public static Asn1ObjectIdentifier convert(TimestampType type)
        {
            switch (type)
            {
                case TimestampType.CONTENT_TIMESTAMP: return AttributeOIDs.id_aa_ets_contentTimestamp;
                case TimestampType.SIGNATURE_TIMESTAMP: return AttributeOIDs.id_aa_signatureTimeStampToken;
                case TimestampType.SIG_AND_REFERENCES_TIMESTAMP: return AttributeOIDs.id_aa_ets_escTimeStamp;
                case TimestampType.REFERENCES_TIMESTAMP: return AttributeOIDs.id_aa_ets_certCRLTimestamp;
                case TimestampType.ARCHIVE_TIMESTAMP: return AttributeOIDs.id_aa_ets_archiveTimestamp;
                case TimestampType.ARCHIVE_TIMESTAMP_V2:
                case TimestampType.ARCHIVE_TIMESTAMP_V3: return AttributeOIDs.id_aa_ets_archiveTimestampV2;
            }
            return null;
        }
    }
}
