package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import com.objsys.asn1j.runtime.Asn1OpenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.security.PublicKey;
import java.util.Arrays;

/**
 * Checks whether the signature of the Certificate is valid. 
 */
public class CertificateSignatureChecker extends IssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(CertificateSignatureChecker.class);

    public enum CertificateSignatureCheckStatus implements CheckStatus {

        CERTIFICATE_NOT_SIGNED,
        CERTIFICATE_INVALID_STRUCTURE,
        NO_PUBLIC_KEY_IN_CERTIFICATE,
        INVALID_PUBLIC_KEY_IN_CERTIFICATE,
        KRIPTO_ERROR_ON_VERIFICATION,
        SIGNATURE_NOT_VERIFIED,
        SIGNATURE_VERIFIED;

        public String getText()
        {
            switch (this) {
                case CERTIFICATE_NOT_SIGNED:
                    return CertI18n.message(CertI18n.SERTIFIKA_IMZALI_DEGIL);
                case CERTIFICATE_INVALID_STRUCTURE:
                    return CertI18n.message(CertI18n.SERTIFIKA_YAPISI_BOZUK);
                case NO_PUBLIC_KEY_IN_CERTIFICATE:
                    return CertI18n.message(CertI18n.SERTIFIKADA_ACIK_ANAHTAR_YOK);
                case INVALID_PUBLIC_KEY_IN_CERTIFICATE:
                    return CertI18n.message(CertI18n.SERTIFIKADA_ACIK_ANAHTAR_BOZUK);
                case KRIPTO_ERROR_ON_VERIFICATION:
                    return CertI18n.message(CertI18n.DOGRULAMA_KRIPTO_HATASI);
                case SIGNATURE_NOT_VERIFIED:
                    return CertI18n.message(CertI18n.SERTIFIKA_IMZA_DOGRULANAMADI);
                case SIGNATURE_VERIFIED:
                    return CertI18n.message(CertI18n.SERTIFIKA_IMZA_DOGRULANDI);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * Sertifikanın üzerindeki imzanın doğruluğunu kontrol eder.
     */
    protected PathValidationResult _check(IssuerCheckParameters aConstraint,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        String subject = aCertificate.getSubject().stringValue();
        //String issuer = aUstSertifika.getSubject().stringValue();
        logger.debug("Sertifikanın imzası kontrol edilecek subject: " + subject);

        return checkSignature(aIssuerCertificate, aCertificate, aConstraint, aCertStatusInfo);
    }

    public static PathValidationResult checkSignature(ECertificate aIssuerCertificate, ECertificate aCertificate,
                                               IssuerCheckParameters aConstraint, CertificateStatusInfo aCertStatusInfo)
    {
        boolean sonuc;
        byte[] imzalanan;
        byte[] publicKeyBytes;
        byte[] imzali = aCertificate.getSignatureValue();
        PublicKey publicKey;

        if (imzali.length == 0) {
            logger.error("Sertifika imza değeri null");
            aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.CERTIFICATE_NOT_SIGNED.getText(),
                                                      CertificateSignatureCheckStatus.CERTIFICATE_NOT_SIGNED, false));

            return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
        }

        imzalanan = aCertificate.getTBSEncodedBytes();

        ESubjectPublicKeyInfo pki = aIssuerCertificate.getSubjectPublicKeyInfo();
        EAlgorithmIdentifier keyAlg = pki.getAlgorithm();

        try {
            if (Arrays.equals(keyAlg.getAlgorithm().value, AsymmetricAlg.DSA.getOID()))// dsa ise parametre kontrolü yapalım
            {
                Asn1OpenType params = keyAlg.getParameters();
                byte[] parameters = params !=null ? params.value : null;
                if (parameters == null || params.getLength()<=0 || Arrays.equals(parameters, EAlgorithmIdentifier.ASN_NULL)) {
                    Asn1OpenType oncekiParams = aConstraint.getPreviousDSAParams();
                    if (oncekiParams == null || oncekiParams.value.length <= 0 || Arrays.equals(oncekiParams.value, EAlgorithmIdentifier.ASN_NULL)) {
                        logger.error("DSA Parametreleri bulunamadı");
                        aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION.getText(),
                                                                  CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false));
                        return PathValidationResult.SIGNATURE_CONTROL_FAILURE; // Parametre yok ve üstten de gelmemiş
                    }
                    else {   // Parametreleri ust sertifikadan alıyoruz.
                        pki.getAlgorithm().setParameters(aConstraint.getPreviousDSAParams());
                    }
                }
                else {
                    // parametre varsa bundan sonraki sertifika kontrolleri için bu parametreleri kullan�yoruz
                    aConstraint.setPreviousDSAParams(keyAlg.getParameters());
                }
            }
            publicKeyBytes = pki.getEncoded();
            if (publicKeyBytes.length == 0) {
                logger.error("Sertifika public key alanı null.");
                aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.NO_PUBLIC_KEY_IN_CERTIFICATE.getText(),
                                                          CertificateSignatureCheckStatus.NO_PUBLIC_KEY_IN_CERTIFICATE, false));
                return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
            }
            Pair<SignatureAlg, AlgorithmParams>  pair= SignatureAlg.fromAlgorithmIdentifier(pki.getAlgorithm());

            AsymmetricAlg alg =  pair.first().getAsymmetricAlg();
            publicKey = KeyUtil.decodePublicKey(alg, publicKeyBytes);
        }
        catch (Exception x) {
            logger.error("Sertifika public key değeri alınırken hata oluştu.", x);
            aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.INVALID_PUBLIC_KEY_IN_CERTIFICATE.getText(),
                                                      CertificateSignatureCheckStatus.INVALID_PUBLIC_KEY_IN_CERTIFICATE, false));
            return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
        }

        try {

            EAlgorithmIdentifier algo = aCertificate.getSignatureAlgorithm();
            Pair<SignatureAlg, AlgorithmParams>  pair= SignatureAlg.fromAlgorithmIdentifier(algo);
            sonuc = SignUtil.verify(pair.first(),pair.second(), imzalanan, imzali, publicKey);
        }
        catch (Exception x) {
            logger.error("Sertifika imzası doğrulanırken hata oluştu.", x);

            aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION.getText(),
                                                      CertificateSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false));
            return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
        }
        if (sonuc) {
            logger.debug("Sertifika imza doğrulandı.");
            aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.SIGNATURE_VERIFIED.getText(),
                                                      CertificateSignatureCheckStatus.SIGNATURE_VERIFIED,true));
            return PathValidationResult.SUCCESS;
        }
        else {
            logger.error("Sertifika imza doğrulanamadı.");
            aCertStatusInfo.addDetail(new CheckResult(CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU), CertificateSignatureCheckStatus.SIGNATURE_NOT_VERIFIED.getText(),
                                                      CertificateSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false));
            return PathValidationResult.SIGNATURE_CONTROL_FAILURE;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERTIFIKA_IMZA_KONTROLU);
    }

}
