package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.security.PublicKey;
import java.util.Arrays;

/**
 * Checks whether the signature of the CRL is valid. 
 */
public class CRLSignatureChecker extends CRLIssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(CRLSignatureChecker.class);

    public enum CRLSignatureCheckStatus implements CheckStatus {

        CRL_NOT_SIGNED,
        CRL_INVALID_STRUCTURE,
        NO_PUBLIC_KEY_IN_CERTIFICATE,
        INVALID_PUBLIC_KEY_IN_CERTIFICATE,
        KRIPTO_ERROR_ON_VERIFICATION,
        SIGNATURE_NOT_VERIFIED,
        SIGNATURE_VERIFIED;

        public String getText()
        {
            switch (this) {
                case CRL_NOT_SIGNED:
                    return CertI18n.message(CertI18n.SIL_IMZALI_DEGIL);
                case CRL_INVALID_STRUCTURE:
                    return CertI18n.message(CertI18n.SIL_YAPISI_BOZUK);
                case NO_PUBLIC_KEY_IN_CERTIFICATE:
                    return CertI18n.message(CertI18n.SERTIFIKADA_ACIK_ANAHTAR_YOK);
                case INVALID_PUBLIC_KEY_IN_CERTIFICATE:
                    return CertI18n.message(CertI18n.SERTIFIKADA_ACIK_ANAHTAR_BOZUK);
                case KRIPTO_ERROR_ON_VERIFICATION:
                    return CertI18n.message(CertI18n.DOGRULAMA_KRIPTO_HATASI);
                case SIGNATURE_NOT_VERIFIED:
                    return CertI18n.message(CertI18n.SIL_IMZA_DOGRULANAMADI);
                case SIGNATURE_VERIFIED:
                    return CertI18n.message(CertI18n.SIL_IMZA_DOGRULANDI);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * SİL'in imzalayan sertifikası ile imzalanmış mı kontrol eder
     */
    protected PathValidationResult _check(IssuerCheckParameters aConstraintCheckParams,
                                          ECRL aCRL, ECertificate aIssuerCertificate,
                                          CRLStatusInfo aCRLStatusInfo)
    {
        logger.debug("Sil imzası kontrol edilecek");

        boolean sonuc;
        //AsymmetricCryptoProvider dogrulayici = AsymmetricCrypto.getProvider();

        byte[] imzalanan;
        PublicKey publickey;
        byte[] imzali = aCRL.getSignature();
        if (imzali == null) {
            logger.error("Sil imza değeri null");
            aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.CRL_NOT_SIGNED, false);
            return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
        }
        try {
            imzalanan = aCRL.getTBSEncodedBytes();
        }
        catch (Exception aEx) {
            logger.error("Sil değeri alınırken hata oluştu.", aEx);
            aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.CRL_INVALID_STRUCTURE, false);
            return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
        }
        try {
            ESubjectPublicKeyInfo spki = aIssuerCertificate.getSubjectPublicKeyInfo();
            EAlgorithmIdentifier keyAlg = spki.getAlgorithm();
            if (Arrays.equals(keyAlg.getAlgorithm().value, AsymmetricAlg.DSA.getOID()))// dsa ise parametre kontrolü yapalım
            {
                if (keyAlg.getParameters() == null) {
                    if (aConstraintCheckParams.getPreviousDSAParams() == null) {
                        logger.error("Key alg parametre yok ve üstten de gelmemiş. ");
                        return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE; // Parametre yok ve üstten de gelmemiş
                    }
                    else {   // Parametreleri ust sertifikadan alıyoruz.
                        keyAlg.setParameters(aConstraintCheckParams.getPreviousDSAParams());
                        spki.setAlgorithm(keyAlg);
                    }
                }
                else {
                    // parametre varsa bundan sonraki sertifika kontrolleri için bu parametreleri kullanıyoruz
                    //aConstraintKontrolParam.setPreviousDSAParams(keyAlg.getParameters());
                }
            }
            //byte[] key = spki.getEncoded();

            String spkiAlg = AsymmetricAlg.fromOID(aIssuerCertificate.getPublicKeyAlgorithm().getAlgorithm().value).getName();
            publickey = KeyUtil.decodePublicKey(spki);

            EAlgorithmIdentifier signatureAlgorithm = aCRL.getTBSSignatureAlgorithm();
            //String asymAlg =  signatureAlgorithm.getAsymAlgName();
            //String digestAlg =  signatureAlgorithm.getDigestAlgName();

            Pair<SignatureAlg, AlgorithmParams>  pair = SignatureAlg.fromAlgorithmIdentifier(signatureAlgorithm);

            if (pair.getObject1()==null){
                logger.error("CRL signature algorithm could not be detected "+new OID(signatureAlgorithm.getAlgorithm().value));
            }


            sonuc = SignUtil.verify(pair.first(), pair.second(), imzalanan, imzali, publickey);
        }
        catch (Exception aEx) {
            logger.error("Sil imzası doğrulanırken hata oluştu.", aEx);
            aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false);
            return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
        }
        
        if (sonuc) {
            logger.debug("Sil imzası doğrulandı.");
            aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.SIGNATURE_VERIFIED, true);
            return PathValidationResult.SUCCESS;
        }
        else {
            logger.error("Sil imzası doğrulanamadı.");
            aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false);
            return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SIL_IMZA_KONTROLU);
    }
}
