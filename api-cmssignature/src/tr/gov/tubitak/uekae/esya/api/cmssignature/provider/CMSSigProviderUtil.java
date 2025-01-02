package tr.gov.tubitak.uekae.esya.api.cmssignature.provider;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.signature.config.CertificateValidationConfig;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class CMSSigProviderUtil
{

    public static Map<String, Object> toSignatureParameters(Context context){
        Map<String, Object> cmsParams = new HashMap<String, Object>();

        cmsParams.put(EParameters.P_CERT_VALIDATION_POLICIES,
                      context.getConfig().getCertificateValidationPolicies());

        if (context.getData()!=null)
            cmsParams.put(EParameters.P_EXTERNAL_CONTENT, new SignableISignable(context.getData()));

        Config config =context.getConfig();
        cmsParams.put(EParameters.P_TSS_INFO, config.getTimestampConfig().getSettings());


        CertificateValidationConfig cvc = config.getCertificateValidationConfig();

        cmsParams.put(EParameters.P_FORCE_STRICT_REFERENCE_USE,
                      config.getParameters().isForceStrictReferenceUse());
        cmsParams.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING,
                      cvc.isValidateCertificateBeforeSigning());
        cmsParams.put(EParameters.P_VALIDATE_TIMESTAMP_WHILE_SIGNING,
                      config.getParameters().isValidateTimestampWhileSigning());
        cmsParams.put(EParameters.P_GRACE_PERIOD,
                      cvc.getGracePeriodInSeconds());
        cmsParams.put(EParameters.P_VALIDATION_PROFILE,
                      cvc.getValidationProfile());
        cmsParams.put(EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS,
                      cvc.getSigningTimeToleranceInSeconds());
        cmsParams.put(EParameters.P_IGNORE_GRACE,
                      cvc.getGracePeriodInSeconds()<=0 || !cvc.isUseValidationDataPublishedAfterCreation());
        cmsParams.put(EParameters.P_REVOCINFO_PERIOD,
                      cvc.getLastRevocationPeriodInSeconds());
        cmsParams.put(AllEParameters.P_VALIDATION_WITHOUT_FINDERS,
                      cvc.isValidateWithResourcesWithinSignature());
        cmsParams.put(AllEParameters.P_VALIDATION_INFO_RESOLVER, context.getValidationInfoResolver());

        cmsParams.put(EParameters.P_TRUST_SIGNINGTIMEATTR,
                config.getParameters().isTrustSigningTime());

        if(context.getValidationTime() != null){
        	if(context.isValidationTimeSigningTime())
        		cmsParams.put(AllEParameters.P_SIGNING_TIME_ATTR, context.getValidationTime());
        	else
        		cmsParams.put(AllEParameters.P_SIGNING_TIME, context.getValidationTime());
        }

        if(context.isPAdES()){
        	cmsParams.put(AllEParameters.P_PADES_SIGNATURE, true);
        }
        
        DigestAlg digestAlgForOCSP = config.getAlgorithmsConfig().getDigestAlgForOCSP();
        if(digestAlgForOCSP != null && digestAlgForOCSP != DigestAlg.SHA256){
        	cmsParams.put(AllEParameters.P_OCSP_DIGEST_ALG, digestAlgForOCSP);
        }

        DigestAlg digestAlg = config.getAlgorithmsConfig().getDigestAlg();
        if(digestAlg != null){
            cmsParams.put(AllEParameters.P_DIGEST_ALGORITHM, digestAlg);
        }

        return cmsParams;
    }

    static SignatureType convert(ESignatureType type){
        switch (type){
            case TYPE_BES           : return SignatureType.ES_BES;
            case TYPE_EPES          : return SignatureType.ES_EPES;
            case TYPE_EST           : return SignatureType.ES_T;
            case TYPE_ESC           : return SignatureType.ES_C;
            case TYPE_ESX_Type1     : return SignatureType.ES_X_Type1;
            case TYPE_ESX_Type2     : return SignatureType.ES_X_Type2;
            case TYPE_ESXLong       : return SignatureType.ES_XL;
            case TYPE_ESXLong_Type1 : return SignatureType.ES_XL_Type1;
            case TYPE_ESXLong_Type2 : return SignatureType.ES_XL_Type2;
            case TYPE_ESA           : return SignatureType.ES_A;
        }
        throw new SignatureRuntimeException("Unknown type "+type);
    }

    static ESignatureType convert(SignatureType type){
        switch (type){
            case ES_BES             : return ESignatureType.TYPE_BES;
            case ES_EPES            : return ESignatureType.TYPE_EPES;
            case ES_T               : return ESignatureType.TYPE_EST;
            case ES_C               : return ESignatureType.TYPE_ESC;
            case ES_X_Type1         : return ESignatureType.TYPE_ESX_Type1;
            case ES_X_Type2         : return ESignatureType.TYPE_ESX_Type2;
            case ES_XL              : return ESignatureType.TYPE_ESXLong;
            case ES_XL_Type1        : return ESignatureType.TYPE_ESXLong_Type1;
            case ES_XL_Type2        : return ESignatureType.TYPE_ESXLong_Type2;
            case ES_A               : return ESignatureType.TYPE_ESA;
        }
        throw new SignatureRuntimeException("Unknown type "+type);
    }

    public static TimestampType convertTimestampType(Asn1ObjectIdentifier timestampOID){
        if (timestampOID==null)
            return null;
        if (timestampOID.equals(AttributeOIDs.id_aa_signatureTimeStampToken ))
            return TimestampType.SIGNATURE_TIMESTAMP;
        else if (timestampOID.equals(AttributeOIDs.id_aa_ets_escTimeStamp))
            return TimestampType.SIG_AND_REFERENCES_TIMESTAMP;
        else if (timestampOID.equals(AttributeOIDs.id_aa_ets_certCRLTimestamp))
            return TimestampType.REFERENCES_TIMESTAMP;
        else if (timestampOID.equals(AttributeOIDs.id_aa_ets_archiveTimestamp))
            return TimestampType.ARCHIVE_TIMESTAMP;
        else if (timestampOID.equals(AttributeOIDs.id_aa_ets_archiveTimestampV2))
            return TimestampType.ARCHIVE_TIMESTAMP_V2;
        else if (timestampOID.equals(AttributeOIDs.id_aa_ets_archiveTimestampV3))
            return TimestampType.ARCHIVE_TIMESTAMP_V3;
        else if (timestampOID.equals(AttributeOIDs.id_aa_ets_contentTimestamp))
            return TimestampType.CONTENT_TIMESTAMP;
        return null;
    }

    static Asn1ObjectIdentifier convert(TimestampType type ){
        switch (type){
        	case CONTENT_TIMESTAMP				: return AttributeOIDs.id_aa_ets_contentTimestamp;
            case SIGNATURE_TIMESTAMP            : return AttributeOIDs.id_aa_signatureTimeStampToken;
            case SIG_AND_REFERENCES_TIMESTAMP   : return AttributeOIDs.id_aa_ets_escTimeStamp;
            case REFERENCES_TIMESTAMP           : return AttributeOIDs.id_aa_ets_certCRLTimestamp;
            case ARCHIVE_TIMESTAMP              : return AttributeOIDs.id_aa_ets_archiveTimestamp;
            case ARCHIVE_TIMESTAMP_V2           : return AttributeOIDs.id_aa_ets_archiveTimestampV2;
            case ARCHIVE_TIMESTAMP_V3           : return AttributeOIDs.id_aa_ets_archiveTimestampV3;
        }
        return null;
    }

}