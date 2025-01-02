package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtendedKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.security.PublicKey;

/**
 * Checks the validity of signature the OCSP Response 
 *
 * @author IH
 */
public class OCSPSignatureChecker extends OCSPResponseChecker {
    
    private static final Logger logger = LoggerFactory.getLogger(OCSPSignatureChecker.class);

    public enum OCSPSignatureCheckStatus implements CheckStatus {

        RESPONSE_NOT_SIGNED,
        INVALID_RESPONSE_STRUCTURE,
        NO_CERTIFICATE_IN_RESPONSE,
        CERTIFICATE_IS_NOT_OCSP_CERTIFICATE,
        KRIPTO_ERROR_ON_VERIFICATION,
        SIGNATURE_NOT_VERIFIED,
        SIGNATURE_VERIFIED;

        public String getText()
        {
            switch (this) {
                case RESPONSE_NOT_SIGNED:
                    return CertI18n.message(CertI18n.CEVAP_IMZALI_DEGIL);
                case INVALID_RESPONSE_STRUCTURE:
                    return CertI18n.message(CertI18n.CEVAP_YAPISI_BOZUK);
                case NO_CERTIFICATE_IN_RESPONSE:
                    return CertI18n.message(CertI18n.CEVAPTA_SERTIFIKA_YOK);
                case CERTIFICATE_IS_NOT_OCSP_CERTIFICATE:
                    return CertI18n.message(CertI18n.SERTIFIKA_OCSP_SERTIFIKASI_DEGIL);
                case KRIPTO_ERROR_ON_VERIFICATION:
                    return CertI18n.message(CertI18n.DOGRULAMA_KRIPTO_HATASI);
                case SIGNATURE_NOT_VERIFIED:
                    return CertI18n.message(CertI18n.IMZA_DOGRULANAMADI);
                case SIGNATURE_VERIFIED:
                    return CertI18n.message(CertI18n.IMZA_DOGRULANDI);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }

        }
    }

    /**
     * OCSP Cevabının imzasının geçerliliğini kontrol eder.
     */
    protected PathValidationResult _check(EOCSPResponse aOCSPCevabi, OCSPResponseStatusInfo aOCSPCevapBilgisi)
    {
        byte[] imzalanan = aOCSPCevabi.getTbsResponseData();
        if (imzalanan == null) {
            logger.error("Cevap içinden imzalanan değer alınamadı");
            //aOCSPCevapBilgisi.addDetail(kontrolSonucuAl(OCSPSignatureCheckStatus.CEVAP_YAPISI_BOZUK, false));
            aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.INVALID_RESPONSE_STRUCTURE, false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }
        //imzayı ve imzalayan sertifikayı al
        byte[] imzali = aOCSPCevabi.getSignatureValue();
        if (imzali == null) {
            logger.error("Cevap içinden imzalı değer alınamadı");
            aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.RESPONSE_NOT_SIGNED, false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }

        EBasicOCSPResponse bor = aOCSPCevabi.getBasicOCSPResponse();
        //List<ECertificate> certs = aOCSPCevabi.getBasicOCSPResponse().getCerts();

        if (bor.getCertificateCount() == 0) {
            logger.error("Cevap içinden imzalayici sertifika alınamadı");
            aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.NO_CERTIFICATE_IN_RESPONSE, false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }


        ECertificate imzalayan = bor.getCertificate(0);

        // sertifikadaki ExtendedKeyUsage değerini kontrol et
        if(!_ocspSertifikasiMi(imzalayan))
        {
            logger.error("Imzalayici sertifika OCSP imzalayaıcı özelliğine sahip değil");
            aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.CERTIFICATE_IS_NOT_OCSP_CERTIFICATE,false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }

        // imzayı doğrulayalım
        boolean flag;

        try {
        	Pair<SignatureAlg, AlgorithmParams>  pair = SignatureAlg.fromAlgorithmIdentifier(aOCSPCevabi.getBasicOCSPResponse().getSignatureAlgorithm());
            PublicKey publicKey = KeyUtil.decodePublicKey(imzalayan.getSubjectPublicKeyInfo());
            flag = SignUtil.verify(pair.first(), pair.second(), imzalanan, imzali, publicKey);
            if (!flag) {
                // Java ile C arasında Little/Big Endian problemi olabilir
                // ters cevirerek deniyorum
                byte[] ters = new byte[imzali.length];
                int u = imzali.length - 1;
                for (int i = 0; i < imzali.length; i++) {
                    ters[i] = imzali[u - i];
                }
                flag = SignUtil.verify(pair.first(), pair.second(), imzalanan, ters, publicKey);

                if (!flag) {
                    aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false);
                }
                else {
                    aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.SIGNATURE_VERIFIED, true);
                }
            }
            else {
                aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.SIGNATURE_VERIFIED, true);
            }
        }
        catch (CryptoException aEx) {
            logger.error("Imza doğrulanırken hata oluştu", aEx);
            aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }
        logger.debug("Imza değeri dogrulama sonucu:" + flag);

        return (flag ? PathValidationResult.SUCCESS : PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE);
    }

    /**
     * Sertifikanın OCSP-Cevabı imzalama özelliği var mı kontrol eder
     */
    private boolean _ocspSertifikasiMi(ECertificate aSertifika)
    {
        EExtendedKeyUsage eku = aSertifika.getExtensions().getExtendedKeyUsage();
        return (eku == null) || eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
        // todo return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.OCSP_IMZA_KONTROLU);
    }


}
