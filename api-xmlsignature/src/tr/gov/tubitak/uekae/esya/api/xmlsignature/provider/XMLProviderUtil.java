package tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.config.CertificateValidationConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.AlgorithmsConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.HttpConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Parameters;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ResolversConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.SignatureProfileValidationConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ValidationConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.DOMResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.FileResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.HttpResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IdResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.XPointerResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AllDataObjectsTimeStampValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.ArchiveTimestampValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttrAuthoritiesCertValuesValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttributeCertificateRefsValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttributeRevocationRefsValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttributeRevocationValuesValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.CertificateValuesValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.CompleteCertificateRefsValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.CompleteRevocationRefsValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.DataObjectFormatValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.IndividualDataObjectsTimeStampValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.RefsOnlyTimestampValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.RevocationValuesValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SigAndRefsTimestampValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SignaturePolicyValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SignatureTimeStampValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SigningCertificateValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SigningTimeValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.TurkishESigProfileAttributeValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.TurkishESigProfileValidator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ayetgin
 */
public class XMLProviderUtil
{
    protected static Logger logger = LoggerFactory.getLogger(XMLProviderUtil.class);
    /*
        <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IdResolver"/>
        <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.DOMResolver"/>
        <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.HttpResolver"/>
        <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.XPointerResolver"/>
        <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.FileResolver"/>
     */
    private static List<Class<? extends IResolver>> defaultResolvers = new ArrayList<Class<? extends IResolver>>();


    /*
            <profile type="XMLDSig">
            </profile>
            <profile type="XAdES_BES" inherit-validators-from="XMLDSig">
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AllDataObjectsTimeStampValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.DataObjectFormatValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.IndividualDataObjectsTimeStampValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SigningCertificateValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SigningTimeValidator"/>
            </profile>
            <profile type="XAdES_EPES" inherit-validators-from="XAdES_BES" >
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SignaturePolicyValidator"/>
            </profile>
            <profile type="XAdES_T" inherit-validators-from="XAdES_EPES">
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SignatureTimeStampValidator"/>
            </profile>
            <profile type="XAdES_C" inherit-validators-from="XAdES_T">
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.CompleteCertificateRefsValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.CompleteRevocationRefsValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttributeCertificateRefsValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttributeRevocationRefsValidator"/>
            </profile>
            <profile type="XAdES_X" inherit-validators-from="XAdES_C">
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.SigAndRefsTimestampValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.RefsOnlyTimestampValidator"/>
            </profile>
            <profile type="XAdES_X_L" inherit-validators-from="XAdES_X">
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.CertificateValuesValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.RevocationValuesValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttrAuthoritiesCertValuesValidator"/>
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.AttributeRevocationValuesValidator"/>
            </profile>
            <profile type="XAdES_A" inherit-validators-from="XAdES_X_L">
                <validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.ArchiveTimestampValidator"/>
                <!--validator class="tr.gov.tubitak.uekae.esya.api.xmlsignature.validator."/-->
            </profile>
     */
    private static Map<tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType, SignatureProfileValidationConfig> defaultTypeValidators
            = new HashMap<SignatureType, SignatureProfileValidationConfig>();
    private static List<Class<? extends Validator>> xmldsigValidators = Arrays.asList();
    private static List<Class<? extends Validator>> esBesValidators = new ArrayList<Class<? extends Validator>>(Arrays.asList(AllDataObjectsTimeStampValidator.class,
                                                                                                      DataObjectFormatValidator.class,
                                                                                                      IndividualDataObjectsTimeStampValidator.class,
                                                                                                      SigningCertificateValidator.class,
                                                                                                      SigningTimeValidator.class,
                                                                                                      TurkishESigProfileAttributeValidator.class,
                                                                                                      TurkishESigProfileValidator.class));
    private static List<Class<? extends Validator>> esEpesValidators = new ArrayList<Class<? extends Validator>>(Arrays.<Class<? extends Validator>>asList(SignaturePolicyValidator.class));
    private static List<Class<? extends Validator>> esTValidators = new ArrayList<Class<? extends Validator>>(Arrays.<Class<? extends Validator>>asList(SignatureTimeStampValidator.class));
    private static List<Class<? extends Validator>> esCValidators = new ArrayList<Class<? extends Validator>>(Arrays.asList(CompleteCertificateRefsValidator.class,
                                                                                                    CompleteRevocationRefsValidator.class,
                                                                                                    AttributeCertificateRefsValidator.class,
                                                                                                    AttributeRevocationRefsValidator.class));
    private static List<Class<? extends Validator>> esXValidators = new ArrayList<Class<? extends Validator>>(Arrays.<Class<? extends Validator>>asList(SigAndRefsTimestampValidator.class,
                                                                                                        RefsOnlyTimestampValidator.class));
    private static List<Class<? extends Validator>> esXLValidators = new ArrayList<Class<? extends Validator>>(Arrays.asList(CertificateValuesValidator.class,
                                                                                                     RevocationValuesValidator.class,
                                                                                                     AttrAuthoritiesCertValuesValidator.class,
                                                                                                     AttributeRevocationValuesValidator.class));
    private static List<Class<? extends Validator>> esAValidators = new ArrayList<Class<? extends Validator>>(Arrays.<Class<? extends Validator>>asList(ArchiveTimestampValidator.class));

    static {
        try {
            esBesValidators.addAll(xmldsigValidators);
            esEpesValidators.addAll(esBesValidators);
            esTValidators.addAll(esEpesValidators);
            esCValidators.addAll(esTValidators);
            esXValidators.addAll(esCValidators);
            esXLValidators.addAll(esXValidators);
            esAValidators.addAll(esXLValidators);

            SignatureProfileValidationConfig xmldsig = new SignatureProfileValidationConfig(SignatureType.XMLDSig, null, xmldsigValidators);
            SignatureProfileValidationConfig bes = new SignatureProfileValidationConfig(SignatureType.XAdES_BES, SignatureType.XMLDSig, esBesValidators);
            SignatureProfileValidationConfig epes = new SignatureProfileValidationConfig(SignatureType.XAdES_EPES, SignatureType.XAdES_BES, esEpesValidators);
            SignatureProfileValidationConfig t = new SignatureProfileValidationConfig(SignatureType.XAdES_T, SignatureType.XAdES_EPES, esTValidators);
            SignatureProfileValidationConfig c = new SignatureProfileValidationConfig(SignatureType.XAdES_C, SignatureType.XAdES_T, esCValidators);
            SignatureProfileValidationConfig x = new SignatureProfileValidationConfig(SignatureType.XAdES_X, SignatureType.XAdES_C, esXValidators);
            SignatureProfileValidationConfig xl = new SignatureProfileValidationConfig(SignatureType.XAdES_X_L, SignatureType.XAdES_X, esXLValidators);
            SignatureProfileValidationConfig a = new SignatureProfileValidationConfig(SignatureType.XAdES_A, SignatureType.XAdES_X_L, esAValidators);

            defaultTypeValidators.put(SignatureType.XMLDSig, xmldsig);
            defaultTypeValidators.put(SignatureType.XAdES_BES, bes);
            defaultTypeValidators.put(SignatureType.XAdES_EPES, epes);
            defaultTypeValidators.put(SignatureType.XAdES_T, t);
            defaultTypeValidators.put(SignatureType.XAdES_C, c);
            defaultTypeValidators.put(SignatureType.XAdES_X, x);
            defaultTypeValidators.put(SignatureType.XAdES_X_L, xl);
            defaultTypeValidators.put(SignatureType.XAdES_A, a);

            defaultResolvers.add(IdResolver.class);
            defaultResolvers.add(DOMResolver.class);
            defaultResolvers.add(XPointerResolver.class);
            defaultResolvers.add(FileResolver.class);
            defaultResolvers.add(HttpResolver.class);
        }
        catch (Exception e) {
            logger.error("Error in XMLProviderUtil", e);
        }
    }


    public static tr.gov.tubitak.uekae.esya.api.signature.SignatureType convert(SignatureType x, XMLSignature signature)
    {
        // todo!
        switch (x) {
            case XMLDSig:
                throw new SignatureRuntimeException("Expected BES, found XMLDSig!");
            case XAdES_BES:
                return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_BES;
            case XAdES_EPES:
                return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_EPES;
            case XAdES_T:
                return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_T;
            case XAdES_C:
                return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_C;

            // todo multiple X!
            case XAdES_X:
                if (signature.getQualifyingProperties().getUnsignedSignatureProperties().getSigAndRefsTimeStampCount()>0)
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1;
                else if (signature.getQualifyingProperties().getUnsignedSignatureProperties().getRefsOnlyTimeStampCount()>0)
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type2;
            case XAdES_X_L:
                if (signature.getQualifyingProperties().getUnsignedSignatureProperties().getSigAndRefsTimeStampCount()>0)
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type1;
                else if (signature.getQualifyingProperties().getUnsignedSignatureProperties().getRefsOnlyTimeStampCount()>0)
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type2;
                return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL;
            case XAdES_A:
                return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_A;
        }
        throw new SignatureRuntimeException("Could not convert signature type! "+x);
    }

    public static tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType
    convert(ValidationResultType aType)
    {
        switch (aType) {
            case VALID:
                return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.VALID;
            case WARNING:
                return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.VALID;
            case INVALID:
                return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.INVALID;
            case INCOMPLETE:
                return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.INCOMPLETE;
        }
        throw new XMLSignatureRuntimeException("Unkonwn validation result type " + aType);
    }


    public static Context convert(tr.gov.tubitak.uekae.esya.api.signature.Context c)
    {
        try {
            tr.gov.tubitak.uekae.esya.api.xmlsignature.Context context =
                    c.getBaseURI().isAbsolute() ?
                            new tr.gov.tubitak.uekae.esya.api.xmlsignature.Context(c.getBaseURI().toString())
                            : new tr.gov.tubitak.uekae.esya.api.xmlsignature.Context(new File(c.getBaseURI().toString()));

            context.setConfig(convert(c.getConfig()));
            context.setValidateCertificates(true);
            context.setValidateCertificateBeforeSign(c.getConfig().getCertificateValidationConfig().isValidateCertificateBeforeSigning());
            context.setValidateTimeStamps(c.getConfig().getParameters().isValidateTimestampWhileSigning());
            // c.getData();
            // context.setValidationInfoResolver(c.getValidationInfoResolver()); todo
            return context;
        }
        catch (Exception x) {
            throw new SignatureRuntimeException(x);
        }
    }

    public static Config convert(tr.gov.tubitak.uekae.esya.api.signature.config.Config c)
    {
        Config config = Config.noInit();
        config.setAlgorithmsConfig(new AlgorithmsConfig(c.getAlgorithmsConfig()));
        config.setHttpConfig(new HttpConfig(c.getHttpConfig()));
        config.setResolversConfig(new ResolversConfig(new ArrayList(defaultResolvers)));

        tr.gov.tubitak.uekae.esya.api.signature.config.Parameters params = c.getParameters();
        CertificateValidationConfig cvc = c.getCertificateValidationConfig();
        Parameters p = new Parameters();
        p.setAddTimestampValidationData(true);
        p.setWriteReferencedValidationDataToFileOnUpgrade(params.isWriteReferencedValidationDataToFileOnUpgrade());
        config.setParameters(p); //todo
        config.setTimestampConfig(new TimestampConfig(c.getTimestampConfig()));

        ValidationConfig vc = new ValidationConfig(cvc.getCertificateValidationPolicyFile(),
                                                   c.getCertificateValidationPolicies(),
                                                   cvc.getGracePeriodInSeconds(),
                                                   cvc.getLastRevocationPeriodInSeconds(),
                                                   cvc.getSigningTimeToleranceInSeconds(),
                                                   cvc.getValidationProfile(),
                                                   params.isForceStrictReferenceUse(),
                                                   cvc.isUseValidationDataPublishedAfterCreation(),
                                                   cvc.isValidateCertificateBeforeSigning(),
                                                   params.isTrustSigningTime(),
                                                   params.isCheckPolicyUri(),
                                                   defaultTypeValidators);

        config.setValidationConfig(vc); // todo

        return config;
    }


}
