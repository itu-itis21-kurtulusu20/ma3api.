using System;
using System.Linq;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;
//using tr.gov.tubitak.uekae.esya.genel.kripto.hafizada;
//using tr.gov.tubitak.uekae.esya.genel.kripto;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer
{
    /**
     * Checks whether the signature of the CRL is valid. 
     */
    public class CRLSignatureChecker : CRLIssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        [Serializable]
        public class CRLSignatureCheckStatus : CheckStatus
        {
            public static readonly CRLSignatureCheckStatus CRL_NOT_SIGNED = new CRLSignatureCheckStatus(_enum.CRL_NOT_SIGNED);
            public static readonly CRLSignatureCheckStatus CRL_INVALID_STRUCTURE = new CRLSignatureCheckStatus(_enum.CRL_INVALID_STRUCTURE);
            public static readonly CRLSignatureCheckStatus NO_PUBLIC_KEY_IN_CERTIFICATE = new CRLSignatureCheckStatus(_enum.NO_PUBLIC_KEY_IN_CERTIFICATE);
            public static readonly CRLSignatureCheckStatus INVALID_PUBLIC_KEY_IN_CERTIFICATE = new CRLSignatureCheckStatus(_enum.INVALID_PUBLIC_KEY_IN_CERTIFICATE);
            public static readonly CRLSignatureCheckStatus KRIPTO_ERROR_ON_VERIFICATION = new CRLSignatureCheckStatus(_enum.KRIPTO_ERROR_ON_VERIFICATION);
            public static readonly CRLSignatureCheckStatus SIGNATURE_NOT_VERIFIED = new CRLSignatureCheckStatus(_enum.SIGNATURE_NOT_VERIFIED);
            public static readonly CRLSignatureCheckStatus SIGNATURE_VERIFIED = new CRLSignatureCheckStatus(_enum.SIGNATURE_VERIFIED);

            enum _enum
            {
                CRL_NOT_SIGNED,
                CRL_INVALID_STRUCTURE,
                NO_PUBLIC_KEY_IN_CERTIFICATE,
                INVALID_PUBLIC_KEY_IN_CERTIFICATE,
                KRIPTO_ERROR_ON_VERIFICATION,
                SIGNATURE_NOT_VERIFIED,
                SIGNATURE_VERIFIED
            }
            
            readonly _enum mValue;
            
            CRLSignatureCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.CRL_NOT_SIGNED:
                        return Resource.message(Resource.SIL_IMZALI_DEGIL);
                    case _enum.CRL_INVALID_STRUCTURE:
                        return Resource.message(Resource.SIL_YAPISI_BOZUK);
                    case _enum.NO_PUBLIC_KEY_IN_CERTIFICATE:
                        return Resource.message(Resource.SERTIFIKADA_ACIK_ANAHTAR_YOK);
                    case _enum.INVALID_PUBLIC_KEY_IN_CERTIFICATE:
                        return Resource.message(Resource.SERTIFIKADA_ACIK_ANAHTAR_BOZUK);
                    case _enum.KRIPTO_ERROR_ON_VERIFICATION:
                        return Resource.message(Resource.DOGRULAMA_KRIPTO_HATASI);
                    case _enum.SIGNATURE_NOT_VERIFIED:
                        return Resource.message(Resource.SIL_IMZA_DOGRULANAMADI);
                    case _enum.SIGNATURE_VERIFIED:
                        return Resource.message(Resource.SIL_IMZA_DOGRULANDI);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * SİL'in imzalayan sertifikası ile imzalanmış mı kontrol eder
         */
        protected override PathValidationResult _check(IssuerCheckParameters aConstraintCheckParams,
                                               ECRL aCRL, ECertificate aIssuerCertificate,
                                               CRLStatusInfo aCRLStatusInfo)
        {
            logger.Debug("Sil imzası kontrol edilecek");

            bool sonuc;
            //AsymmetricCryptoProvider dogrulayici = AsymmetricCrypto.getProvider();
            //AY_HafizadaTumKripto dogrulayici = HafizadaTumKripto.Instance;

            byte[] imzalanan;
            //PublicKey publickey;
            byte[] imzali = aCRL.getSignature();
            if (imzali == null)
            {
                logger.Error("Sertifika imza degeri null");
                aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.CRL_NOT_SIGNED, false);
                return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
            }
            try
            {
                imzalanan = aCRL.getTBSEncodedBytes();
            }
            catch (Exception aEx)
            {
                logger.Error("Sil degeri alınırken hata oluştu.", aEx);
                aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.CRL_INVALID_STRUCTURE, false);
                return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
            }
            try
            {
                ESubjectPublicKeyInfo spki = aIssuerCertificate.getSubjectPublicKeyInfo();
                EAlgorithmIdentifier keyAlg = spki.getAlgorithm();
                if (keyAlg.getAlgorithm().mValue.SequenceEqual(AsymmetricAlg.DSA.getOID()))// dsa ise parametre kontrolü yapalım
                {
                    if (keyAlg.getParameters() == null)
                    {
                        if (aConstraintCheckParams.getPreviousDSAParams() == null)
                        {
                            logger.Error("Key alg parametre yok ve ustten de gelmemis. ");
                            return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE; // Parametre yok ve üstten de gelmemiş
                        }
                        else
                        {   // Parametreleri ust sertifikadan alıyoruz.
                            keyAlg.setParameters(aConstraintCheckParams.getPreviousDSAParams());
                            spki.setAlgorithm(keyAlg);
                        }
                    }
                    else
                    {
                        // parametre varsa bundan sonraki sertifika kontrolleri için bu parametreleri kullanıyoruz
                        //aConstraintKontrolParam.setPreviousDSAParams(keyAlg.getParameters());
                    }
                }
                //byte[] key = spki.getBytes();

                //String spkiAlg = AsymmetricAlg.fromOID(aIssuerCertificate.getPublicKeyAlgorithm().getAlgorithm().mValue).getName();//aIssuerCertificate.getSubjectPublicKeyInfo().getAlgorithm().getAsymAlgName();
                //publickey = dogrulayici.pubDecode(key, spkiAlg);

                EAlgorithmIdentifier signatureAlgorithm = aCRL.getTBSSignatureAlgorithm();
                //String asymAlg =  signatureAlgorithm.getAsymAlgName();
                //String digestAlg = signatureAlgorithm.getDigestAlgName();

                PublicKey publicKey = new PublicKey(spki);
                //sonuc = dogrulayici.verify(imzalanan, imzali, publickey, spkiAlg, digestAlg);
                //SignatureAlg alg = SignatureAlg.fromAlgorithmIdentifier(signatureAlgorithm);
                //sonuc = SignUtil.verify(alg, imzalanan, imzali, publicKey);//dogrulayici.dogrula(imzalanan, imzali, spki, spkiAlg, digestAlg);
                Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(signatureAlgorithm);

                sonuc = SignUtil.verify(pair.first(), pair.second(), imzalanan, imzali, publicKey);

            }
            catch (Exception aEx)
            {
                logger.Error("Sertifika imzasi dogrulanirken hata olustu.", aEx);
                aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.KRIPTO_ERROR_ON_VERIFICATION, false);
                return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
            }

            if (sonuc)
            {
                logger.Debug("Sertifika imza dogrulandi.");
                aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.SIGNATURE_VERIFIED, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("Sertifika imza dogrulanamadi.");
                aCRLStatusInfo.addDetail(this, CRLSignatureCheckStatus.SIGNATURE_NOT_VERIFIED, false);
                return PathValidationResult.CRL_SIGNATURE_CONTROL_FAILURE;
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SIL_IMZA_KONTROLU);
        }
    }
}
