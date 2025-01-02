using System;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
//using tr.gov.tubitak.uekae.esya.genel.kripto;
//using tr.gov.tubitak.uekae.esya.genel.kripto.hafizada;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks whether the signature of the Certificate is valid. 
     */
    public class CertificateSignatureChecker : IssuerChecker
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [Serializable]
        public class CertificateSignatureCheckStatus : CheckStatus
        {
            public static readonly CertificateSignatureCheckStatus CERTIFICATE_NOT_SIGNED = new CertificateSignatureCheckStatus(_enum.CERTIFICATE_NOT_SIGNED);
            public static readonly CertificateSignatureCheckStatus CERTIFICATE_INVALID_STRUCTURE = new CertificateSignatureCheckStatus(_enum.CERTIFICATE_INVALID_STRUCTURE);
            public static readonly CertificateSignatureCheckStatus NO_PUBLIC_KEY_IN_CERTIFICATE = new CertificateSignatureCheckStatus(_enum.NO_PUBLIC_KEY_IN_CERTIFICATE);
            public static readonly CertificateSignatureCheckStatus INVALID_PUBLIC_KEY_IN_CERTIFICATE = new CertificateSignatureCheckStatus(_enum.INVALID_PUBLIC_KEY_IN_CERTIFICATE);
            public static readonly CertificateSignatureCheckStatus KRIPTO_ERROR_ON_VERIFICATION = new CertificateSignatureCheckStatus(_enum.KRIPTO_ERROR_ON_VERIFICATION);
            public static readonly CertificateSignatureCheckStatus SIGNATURE_NOT_VERIFIED = new CertificateSignatureCheckStatus(_enum.SIGNATURE_NOT_VERIFIED);
            public static readonly CertificateSignatureCheckStatus SIGNATURE_VERIFIED = new CertificateSignatureCheckStatus(_enum.SIGNATURE_VERIFIED);

            enum _enum
            {
                CERTIFICATE_NOT_SIGNED,
                CERTIFICATE_INVALID_STRUCTURE,
                NO_PUBLIC_KEY_IN_CERTIFICATE,
                INVALID_PUBLIC_KEY_IN_CERTIFICATE,
                KRIPTO_ERROR_ON_VERIFICATION,
                SIGNATURE_NOT_VERIFIED,
                SIGNATURE_VERIFIED
            }
            readonly _enum mValue;
            CertificateSignatureCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.CERTIFICATE_NOT_SIGNED:
                        return Resource.message(Resource.SERTIFIKA_IMZALI_DEGIL);
                    case _enum.CERTIFICATE_INVALID_STRUCTURE:
                        return Resource.message(Resource.SERTIFIKA_YAPISI_BOZUK);
                    case _enum.NO_PUBLIC_KEY_IN_CERTIFICATE:
                        return Resource.message(Resource.SERTIFIKADA_ACIK_ANAHTAR_YOK);
                    case _enum.INVALID_PUBLIC_KEY_IN_CERTIFICATE:
                        return Resource.message(Resource.SERTIFIKADA_ACIK_ANAHTAR_BOZUK);
                    case _enum.KRIPTO_ERROR_ON_VERIFICATION:
                        return Resource.message(Resource.DOGRULAMA_KRIPTO_HATASI);
                    case _enum.SIGNATURE_NOT_VERIFIED:
                        return Resource.message(Resource.SERTIFIKA_IMZA_DOGRULANAMADI);
                    case _enum.SIGNATURE_VERIFIED:
                        return Resource.message(Resource.SERTIFIKA_IMZA_DOGRULANDI);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * Sertifikanın üzerindeki imzanın doğruluğunu kontrol eder.
         */
        protected override PathValidationResult _check(IssuerCheckParameters aConstraint,
                                              ECertificate aIssuerCertificate, ECertificate aCertificate,
                                              CertificateStatusInfo aCertStatusInfo)
        {
            String subject = aCertificate.getSubject().stringValue();
            //String issuer = aUstSertifika.getSubject().stringValue();
            logger.Debug("Sertifikanın imzası kontrol edilecek subject: " + subject);

            return checkSignature(aIssuerCertificate, aCertificate, aConstraint, aCertStatusInfo);
        }

        public static PathValidationResult checkSignature(ECertificate aIssuerCertificate, ECertificate aCertificate,
                                               IssuerCheckParameters aConstraint, CertificateStatusInfo aCertStatusInfo)
        {
            bool sonuc;
            byte[] imzalanan;
            byte[] publicKeyBytes;
            byte[] imzali = aCertificate.getSignatureValue();
            PublicKey publicKey;

            if (imzali.Length == 0)
            {
                logger.Error("Sertifika imza değeri null");
                //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.CERTIFICATE_NOT_SIGNED, false);
                aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.CERTIFICATE_NOT_SIGNED.getText(),
                    CertificateSignatureCheckStatus.CERTIFICATE_NOT_SIGNED, false));

                return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
            }

            imzalanan = aCertificate.getTBSEncodedBytes();

            ESubjectPublicKeyInfo pki = aIssuerCertificate.getSubjectPublicKeyInfo();
            EAlgorithmIdentifier keyAlg = pki.getAlgorithm();
            try
            {

                if (keyAlg.getAlgorithm().mValue.SequenceEqual<int>(AsymmetricAlg.DSA.getOID()))// dsa ise parametre kontrolü yapalım
                {
                    Asn1OpenType params_ = keyAlg.getParameters();
                    byte[] parameters = params_ != null ? params_.mValue : null;
                    if (parameters == null || params_.Length <= 0 || parameters.SequenceEqual<byte>(EAlgorithmIdentifier.ASN_NULL))
                    {
                        Asn1OpenType oncekiParams = aConstraint.getPreviousDSAParams();
                        if (oncekiParams == null || oncekiParams.mValue.Length <= 0 || oncekiParams.mValue.SequenceEqual<byte>(EAlgorithmIdentifier.ASN_NULL))
                        {
                            logger.Error("DSA Parametreleri bulunamadı");
                            //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false);
                            aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION.getText(),
                                CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false));
                            return PathValidationResult.SIGNATURE_CONTROL_FAILURE; // Parametre yok ve üstten de gelmemiş
                        }
                        else
                        {   // Parametreleri ust sertifikadan alıyoruz.
                            pki.getAlgorithm().setParameters(aConstraint.getPreviousDSAParams());
                        }
                    }
                    else
                    {
                        // parametre varsa bundan sonraki sertifika kontrolleri için bu parametreleri kullan�yoruz
                        aConstraint.setPreviousDSAParams(keyAlg.getParameters());
                    }
                }
                publicKeyBytes = pki.getBytes();
                if (publicKeyBytes.Length == 0)
                {
                    logger.Error("Sertifika public key alanı null.");
                    //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.NO_PUBLIC_KEY_IN_CERTIFICATE, false);
                    aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.NO_PUBLIC_KEY_IN_CERTIFICATE.getText(),
                        CertificateSignatureCheckStatus.NO_PUBLIC_KEY_IN_CERTIFICATE, false));
                    return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
                }
                //publicKey = dogrulayici.pubDecode(publicKeyBytes, pki.getAlgorithm().getAsymAlgName());
                publicKey = new PublicKey(pki);
            }
            catch (Exception x)
            {
                logger.Error("Sertifika public key değeri alınırken hata oluştu.", x);
                //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.INVALID_PUBLIC_KEY_IN_CERTIFICATE, false);
                aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.INVALID_PUBLIC_KEY_IN_CERTIFICATE.getText(),
                    CertificateSignatureCheckStatus.INVALID_PUBLIC_KEY_IN_CERTIFICATE, false));
                return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
            }

            try
            {
                EAlgorithmIdentifier algo = aCertificate.getSignatureAlgorithm();
                //SignatureAlg signAlg = SignatureAlg.fromAlgorithmIdentifier(algo);
                Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(algo);

                sonuc = SignUtil.verify(pair.first(), pair.second(), imzalanan, imzali, publicKey);
            }
            catch (Exception x)
            {
                logger.Error("Sertifika imzası doğrulanırken hata oluştu.", x);

                //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false);
                aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION.getText(),
                    CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false));
                return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
            }
            if (sonuc)
            {
                logger.Debug("Sertifika imza doğrulandı.");
                //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.SIGNATURE_VERIFIED, true);
                aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.SIGNATURE_VERIFIED.getText(),
                    CertificateSignatureCheckStatus.SIGNATURE_VERIFIED, true));
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("Sertifika imza doğrulanamadı.");
                //aCertStatusInfo.addDetail(this, CertificateSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false);
                aCertStatusInfo.addDetail(new CheckResult(Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.SIGNATURE_NOT_VERIFIED.getText(),
                    CertificateSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false));
                return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SERTIFIKA_IMZA_KONTROLU);
        }
    }
}
