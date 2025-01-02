using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * Checks the validity of signature the OCSP Response 
     *
     * @author IH
     */
    public class OCSPSignatureChecker : OCSPResponseChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public class OCSPSignatureCheckStatus : CheckStatus
        {
            public static readonly OCSPSignatureCheckStatus RESPONSE_NOT_SIGNED = new OCSPSignatureCheckStatus(_enum.RESPONSE_NOT_SIGNED);
            public static readonly OCSPSignatureCheckStatus INVALID_RESPONSE_STRUCTURE = new OCSPSignatureCheckStatus(_enum.INVALID_RESPONSE_STRUCTURE);
            public static readonly OCSPSignatureCheckStatus NO_CERTIFICATE_IN_RESPONSE = new OCSPSignatureCheckStatus(_enum.NO_CERTIFICATE_IN_RESPONSE);
            public static readonly OCSPSignatureCheckStatus CERTIFICATE_IS_NOT_OCSP_CERTIFICATE = new OCSPSignatureCheckStatus(_enum.CERTIFICATE_IS_NOT_OCSP_CERTIFICATE);
            public static readonly OCSPSignatureCheckStatus KRIPTO_ERROR_ON_VERIFICATION = new OCSPSignatureCheckStatus(_enum.KRIPTO_ERROR_ON_VERIFICATION);
            public static readonly OCSPSignatureCheckStatus SIGNATURE_NOT_VERIFIED = new OCSPSignatureCheckStatus(_enum.SIGNATURE_NOT_VERIFIED);
            public static readonly OCSPSignatureCheckStatus SIGNATURE_VERIFIED = new OCSPSignatureCheckStatus(_enum.SIGNATURE_VERIFIED);

            enum _enum
            {
                RESPONSE_NOT_SIGNED,
                INVALID_RESPONSE_STRUCTURE,
                NO_CERTIFICATE_IN_RESPONSE,
                CERTIFICATE_IS_NOT_OCSP_CERTIFICATE,
                KRIPTO_ERROR_ON_VERIFICATION,
                SIGNATURE_NOT_VERIFIED,
                SIGNATURE_VERIFIED
            }
            
            readonly _enum mValue;
            
            OCSPSignatureCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }

            public String getText()
            {
                switch (mValue)
                {
                    case _enum.RESPONSE_NOT_SIGNED:
                        return Resource.message(Resource.CEVAP_IMZALI_DEGIL);
                    case _enum.INVALID_RESPONSE_STRUCTURE:
                        return Resource.message(Resource.CEVAP_YAPISI_BOZUK);
                    case _enum.NO_CERTIFICATE_IN_RESPONSE:
                        return Resource.message(Resource.CEVAPTA_SERTIFIKA_YOK);
                    case _enum.CERTIFICATE_IS_NOT_OCSP_CERTIFICATE:
                        return Resource.message(Resource.SERTIFIKA_OCSP_SERTIFIKASI_DEGIL);
                    case _enum.KRIPTO_ERROR_ON_VERIFICATION:
                        return Resource.message(Resource.DOGRULAMA_KRIPTO_HATASI);
                    case _enum.SIGNATURE_NOT_VERIFIED:
                        return Resource.message(Resource.IMZA_DOGRULANAMADI);
                    case _enum.SIGNATURE_VERIFIED:
                        return Resource.message(Resource.IMZA_DOGRULANDI);
                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * OCSP Cevabının imzasının geçerliliğini kontrol eder.
         */
        protected override PathValidationResult _check(EOCSPResponse aOCSPCevabi, OCSPResponseStatusInfo aOCSPCevapBilgisi)
        {
            byte[] imzalanan = aOCSPCevabi.getTbsResponseData();
            if (imzalanan == null)
            {
                logger.Error("Cevap icinden imzalanan deger alinamadi");
                //aOCSPCevapBilgisi.addDetail(kontrolSonucuAl(OCSPSignatureCheckStatus.CEVAP_YAPISI_BOZUK, false));
                aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.INVALID_RESPONSE_STRUCTURE, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }
            // imzayı ve imzalayan sertifikayı al
            byte[] imzali = aOCSPCevabi.getSignatureValue();
            if (imzali == null)
            {
                logger.Error("Cevap icinden imzali deger alinamadi");
                aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.RESPONSE_NOT_SIGNED, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }

            EBasicOCSPResponse bor = aOCSPCevabi.getBasicOCSPResponse();
            //List<ECertificate> certs = aOCSPCevabi.getBasicOCSPResponse().getCerts();

            if (bor.getCertificateCount() == 0)
            {
                logger.Error("Cevap icinden imzalayici sertifika alinamadi");
                aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.NO_CERTIFICATE_IN_RESPONSE, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }

            ECertificate imzalayan = bor.getCertificate(0);

            // sertifikadaki ExtendedKeyUsage degerini kontrol et
            if (!_ocspSertifikasiMi(imzalayan))
            {
                logger.Error("Imzalayici sertifika OCSP imzalayici ozelligine sahip degil");
                aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.CERTIFICATE_IS_NOT_OCSP_CERTIFICATE, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }

            // imzayi dogrulayalim
            bool flag;
            //AY_HafizadaTumKripto haf = HafizadaTumKripto.Instance;

            try
            {
                //SignatureAlg signAlg = SignatureAlg.fromAlgorithmIdentifier(aOCSPCevabi.getBasicOCSPResponse().getSignatureAlgorithm());
                Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(aOCSPCevabi.getBasicOCSPResponse().getSignatureAlgorithm());
                PublicKey publicKey = new PublicKey(imzalayan.getSubjectPublicKeyInfo());//KeyUtil.decodePublicKey(imzalayan.getSubjectPublicKeyInfo());
                //flag = SignUtil.verify(signAlg, imzalanan, imzali, publicKey);
                flag = SignUtil.verify(pair.first(), pair.second(), imzalanan, imzali, publicKey);
                if (!flag)
                {
                    // Java ile C arasında Little/Big Endian problemi olabilir
                    // ters cevirerek deniyorum
                    byte[] ters = new byte[imzali.Length];
                    int u = imzali.Length - 1;
                    for (int i = 0; i < imzali.Length; i++)
                    {
                        ters[i] = imzali[u - i];
                    }                    
                    //flag = SignUtil.verify(signAlg, imzalanan, ters, publicKey);
                    flag = SignUtil.verify(pair.first(), pair.second(), imzalanan, ters, publicKey);
                    if (!flag)
                    {
                        aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false);
                    }
                    else
                    {
                        aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.SIGNATURE_VERIFIED, true);
                    }
                }
                else
                {
                    aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.SIGNATURE_VERIFIED, true);
                }
            }
            catch (CryptoException aEx)
            {
                logger.Error("Imza dogrulanirken hata olustu", aEx);
                aOCSPCevapBilgisi.addDetail(this, OCSPSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }
            logger.Debug("Imza degeri dogrulama sonucu:" + flag);

            return (flag ? PathValidationResult.SUCCESS : PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE);
        }

        /**
         * Sertifikanin OCSP-Cevabi imzalama ozelligi var mi kontrol eder
         */
        private bool _ocspSertifikasiMi(ECertificate aSertifika)
        {
            EExtendedKeyUsage eku = aSertifika.getExtensions().getExtendedKeyUsage();
            //return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
            return (eku != null) || eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.OCSP_IMZA_KONTROLU);
        }
    }
}
