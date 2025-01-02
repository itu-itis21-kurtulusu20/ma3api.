using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.config;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;
using AlgorithmsConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.AlgorithmsConfig;
using Config = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
using HttpConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.HttpConfig;
using Parameters = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Parameters;
using TimestampConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.provider
{
    static public class XMLProviderUtil
    {
        /*
            <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IdResolver"/>
            <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.DOMResolver"/>
            <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.HttpResolver"/>
            <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.XPointerResolver"/>
            <resolver class="tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.FileResolver"/>
         */

        private static List<Type> defaultResolvers = new List<Type>();


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
        private static Dictionary<tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType, SignatureProfileValidationConfig> defaultTypeValidators
                = new Dictionary<SignatureType, SignatureProfileValidationConfig>();
        private static List<Type> xmldsigValidators  = new List<Type>();

        private static List<Type> esBesValidators    = new List<Type>( new []
                                                            {
                                                                typeof (AllDataObjectsTimeStampValidator),
                                                                typeof (DataObjectFormatValidator),
                                                                typeof (IndividualDataObjectsTimeStampValidator),
                                                                typeof (SigningCertificateValidator),
                                                                typeof (SigningTimeValidator),
                                                                typeof (TurkishESigProfileAttributeValidator),
                                                                typeof (TurkishESigProfileValidator)
                                                            });
        private static List<Type> esEpesValidators   = new List<Type>(new []{typeof(SignaturePolicyValidator)});
        private static List<Type> esTValidators      = new List<Type>(new []{typeof(SignatureTimeStampValidator)});
        private static List<Type> esCValidators      = new List<Type>(new []
                                                            {
                                                                typeof(CompleteCertificateRefsValidator),
                                                                typeof(CompleteRevocationRefsValidator),
                                                                typeof(AttributeCertificateRefsValidator),
                                                                typeof(AttributeRevocationRefsValidator)
                                                            });
        private static List<Type> esXValidators      = new List<Type>(new []
                                                            {
                                                                typeof(SigAndRefsTimestampValidator),
                                                                typeof(RefsOnlyTimestampValidator)
                                                            });
        private static List<Type> esXLValidators     = new List<Type>(new []
                                                            {
                                                                typeof(CertificateValuesValidator),
                                                                typeof(RevocationValuesValidator),
                                                                typeof(AttrAuthoritiesCertValuesValidator),
                                                                typeof(AttributeRevocationValuesValidator)
                                                            });
        private static List<Type> esAValidators      = new List<Type>(new []{typeof(ArchiveTimestampValidator)});

        static XMLProviderUtil() {
            try {
                esBesValidators.AddRange(xmldsigValidators);
                esEpesValidators.AddRange(esBesValidators);
                esTValidators.AddRange(esEpesValidators);
                esCValidators.AddRange(esTValidators);
                esXValidators.AddRange(esCValidators);
                esXLValidators.AddRange(esXValidators);
                esAValidators.AddRange(esXLValidators);

                SignatureProfileValidationConfig xmldsig= new SignatureProfileValidationConfig(SignatureType.XMLDSig,   null,                       xmldsigValidators);
                SignatureProfileValidationConfig bes    = new SignatureProfileValidationConfig(SignatureType.XAdES_BES, SignatureType.XMLDSig,      esBesValidators);
                SignatureProfileValidationConfig epes   = new SignatureProfileValidationConfig(SignatureType.XAdES_EPES,SignatureType.XAdES_BES,    esEpesValidators);
                SignatureProfileValidationConfig t      = new SignatureProfileValidationConfig(SignatureType.XAdES_T,   SignatureType.XAdES_EPES,   esTValidators);
                SignatureProfileValidationConfig c      = new SignatureProfileValidationConfig(SignatureType.XAdES_C,   SignatureType.XAdES_T,      esCValidators);
                SignatureProfileValidationConfig x      = new SignatureProfileValidationConfig(SignatureType.XAdES_X,   SignatureType.XAdES_C,      esXValidators);
                SignatureProfileValidationConfig xl     = new SignatureProfileValidationConfig(SignatureType.XAdES_X_L, SignatureType.XAdES_X,      esXLValidators);
                SignatureProfileValidationConfig a      = new SignatureProfileValidationConfig(SignatureType.XAdES_A,   SignatureType.XAdES_X_L,    esAValidators);

                defaultTypeValidators.Add(SignatureType.XMLDSig,   xmldsig);
                defaultTypeValidators.Add(SignatureType.XAdES_BES, bes);
                defaultTypeValidators.Add(SignatureType.XAdES_EPES,epes);
                defaultTypeValidators.Add(SignatureType.XAdES_T,   t);
                defaultTypeValidators.Add(SignatureType.XAdES_C,   c);
                defaultTypeValidators.Add(SignatureType.XAdES_X,   x);
                defaultTypeValidators.Add(SignatureType.XAdES_X_L, xl);
                defaultTypeValidators.Add(SignatureType.XAdES_A,   a);

                defaultResolvers.Add(typeof(IdResolver));
                defaultResolvers.Add(typeof(DOMResolver));
                defaultResolvers.Add(typeof(XPointerResolver));
                defaultResolvers.Add(typeof(FileResolver));
                defaultResolvers.Add(typeof(HttpResolver));

            } catch (Exception x){
                Console.WriteLine(x.StackTrace);
            }
        }


        public static tr.gov.tubitak.uekae.esya.api.signature.SignatureType? convert(SignatureType x, XMLSignature signature){
            // todo!
            switch (x){
                case SignatureType.XMLDSig : throw new SignatureRuntimeException("Expected BES, found XMLDSig!") ;
                case SignatureType.XAdES_BES: return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_BES;
                case SignatureType.XAdES_EPES: return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_EPES;
                case SignatureType.XAdES_T: return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_T;
                case SignatureType.XAdES_C: return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_C;

                // todo multiple X!
                case SignatureType.XAdES_X:
                    if (signature.QualifyingProperties.UnsignedSignatureProperties.SigAndRefsTimeStampCount > 0)
                        return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1;
                    else if (signature.QualifyingProperties.UnsignedSignatureProperties.RefsOnlyTimeStampCount > 0)
                        return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type2;
                    break;

                case SignatureType.XAdES_X_L:
                    if (signature.QualifyingProperties.UnsignedSignatureProperties.SigAndRefsTimeStampCount>0)
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type1;
                else if (signature.QualifyingProperties.UnsignedSignatureProperties.RefsOnlyTimeStampCount>0)
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL_Type2;
                    break;

                case SignatureType.XAdES_A:
                    return tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_A;
            }
            throw new SignatureRuntimeException("Could not convert signature type! " + x);
        }

        public static tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType
                convert(ValidationResultType aType)
        {
            switch (aType){
                case ValidationResultType.VALID  : 
                case ValidationResultType.WARNING: return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.VALID;
                case ValidationResultType.INVALID: return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.INVALID;
                case ValidationResultType.INCOMPLETE: return tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType.INCOMPLETE;
            }
            throw new XMLSignatureRuntimeException("Unkonwn validation result type "+aType);
        }



        public static Context convert(tr.gov.tubitak.uekae.esya.api.signature.Context c){
            try {
                tr.gov.tubitak.uekae.esya.api.xmlsignature.Context context =
                        c.getBaseURI().IsAbsoluteUri ?
                            new tr.gov.tubitak.uekae.esya.api.xmlsignature.Context(new Uri(c.getBaseURI().ToString()))
                            : new tr.gov.tubitak.uekae.esya.api.xmlsignature.Context(new FileInfo(c.getBaseURI().ToString()));

                context.Config = convert(c.getConfig());
                context.ValidateCertificates = true;
                context.setValidateCertificateBeforeSign(c.getConfig().getCertificateValidationConfig().isValidateCertificateBeforeSigning());
                context.ValidateTimeStamps = c.getConfig().getParameters().isValidateTimestampWhileSigning();
                // c.getData();
                // context.setValidationInfoResolver(c.getValidationInfoResolver()); todo
                return context;
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }

        public static Config convert(tr.gov.tubitak.uekae.esya.api.signature.config.Config c){
            Config config = Config.noInit();
            config.AlgorithmsConfig = new AlgorithmsConfig(c.getAlgorithmsConfig());
            config.HttpConfig = new HttpConfig(c.getHttpConfig());
            config.ResolversConfig = new ResolversConfig(new List<Type>(defaultResolvers));

            tr.gov.tubitak.uekae.esya.api.signature.config.Parameters parameters = c.getParameters();
            CertificateValidationConfig cvc = c.getCertificateValidationConfig();
            Parameters p = new Parameters();
            p.AddTimestampValidationData = true;
            p.WriteReferencedValidationDataToFileOnUpgrade = parameters.isWriteReferencedValidationDataToFileOnUpgrade();
            config.Parameters = p; //todo
            config.TimestampConfig = new TimestampConfig(c.getTimestampConfig());

            ValidationConfig vc = new ValidationConfig(cvc.getCertificateValidationPolicyFile(),
                                                       c.getCertificateValidationPolicies(),
                                                       cvc.getGracePeriodInSeconds(),
                                                       cvc.getLastRevocationPeriodInSeconds(),
                                                       cvc.getSigningTimeToleranceInSeconds(),
                                                       cvc.getValidationProfile(),
                                                       parameters.isForceStrictReferenceUse(),
                                                       cvc.isUseValidationDataPublishedAfterCreation(),
                                                       cvc.isValidateCertificateBeforeSigning(),
                                                       parameters.isTrustSigningTime(),
                                                       parameters.isCheckPolicyUri(),
                                                       defaultTypeValidators);

            config.ValidationConfig = vc; // todo

            return config;
        }
    }
}
