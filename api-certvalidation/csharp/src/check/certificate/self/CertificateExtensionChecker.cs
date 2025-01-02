using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * Checks the extension information in the Certificate is valid
     * <ul>
     * <li>critical extensions must be known
     * <li>any particular extension must exist only once
     * </ul>
     */
    public class CertificateExtensionChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static List<Asn1ObjectIdentifier> taninanEklentiler = new List<Asn1ObjectIdentifier>();

        static CertificateExtensionChecker()
        {
            /*MUST*/
            taninanEklentiler.Add(EExtensions.oid_ce_keyUsage);             //key usage (section 4.2.1.3)
            taninanEklentiler.Add(EExtensions.oid_ce_certificatePolicies);  //certificate policies (section 4.2.1.5)
            taninanEklentiler.Add(EExtensions.oid_ce_subjectAltName);       //the subject alternative name (section 4.2.1.7)
            taninanEklentiler.Add(EExtensions.oid_ce_basicConstraints);     //basic constraints (section 4.2.1.10)
            taninanEklentiler.Add(EExtensions.oid_ce_nameConstraints);      //name constraints (section 4.2.1.11)
            taninanEklentiler.Add(EExtensions.oid_ce_policyConstraints);    //policy constraints (section 4.2.1.12)
            taninanEklentiler.Add(EExtensions.oid_ce_extKeyUsage);          //extended key usage (section 4.2.1.13)
            taninanEklentiler.Add(EExtensions.oid_ce_inhibitAnyPolicy);     //inhibit any-policy (section 4.2.1.15)

            /*SHOULD*/
            taninanEklentiler.Add(EExtensions.oid_ce_authorityKeyIdentifier);   //the authority identifier(section 4.2.1.1)
            taninanEklentiler.Add(EExtensions.oid_ce_subjectKeyIdentifier);     //subject key identifier (section 4.2.1.2)
            taninanEklentiler.Add(EExtensions.oid_ce_policyMappings);           //policy mapping (section 4.2.1.6)
            taninanEklentiler.Add(EExtensions.oid_pe_qcStatements);             //???
        }

        /**
         * Tanınmayan eklentileri kontrol eder. Kontrol sadece kritik eklentiler için
         * MUST ve SHOULD eklentileri içerir.
         * 4.2 Certificate Extensions: A certificate using system MUST reject the certificate if it encounters a critical extension it does not recognize
         * applications conforming to this profile MUST recognize the following extensions:
         * key usage (section 4.2.1.3),
         * certificate policies (section 4.2.1.5),
         * the subject alternative name (section 4.2.1.7),
         * basic constraints (section 4.2.1.10),
         * name constraints (section 4.2.1.11),
         * policy constraints (section 4.2.1.12),
         * extended key usage (section 4.2.1.13),
         * and inhibit any-policy (section 4.2.1.15).
         * In addition, applications conforming to this profile SHOULD recognize
         * the authority and subject key identifier (sections 4.2.1.1 and 4.2.1.2), and policy mapping (section 4.2.1.6) extensions.
         * Aynı eklentiden birden fazla bulunup bulunmad���n� kontrol eder.
         * @return boolean
         */
        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            logger.Debug("Extension kontrolü yapılacak.");

            ECertificate cert = aCertStatusInfo.getCertificate();
            EExtensions eklentiler = cert.getExtensions();
            if (eklentiler.getExtensionCount() == 0)
            {
                logger.Debug("Sertifikada extension yok, kontrolü yapılmayacak.");
                aCertStatusInfo.addDetail(this, ExtensionCheckStatus.NO_EXTENSION, true);
                return PathValidationResult.SUCCESS;
            }
            for (int i = 0; i < eklentiler.getExtensionCount(); i++)
            {
                EExtension eklenti = eklentiler.getExtension(i);
                if (eklenti.isCritical())
                {
                    if (!_tanimliEklentiMi(eklenti.getIdentifier()))
                    {
                        logger.Error("Extension tanımlı extensionlar içinde değil: " + eklenti.getIdentifier());
                        aCertStatusInfo.addDetail(this, ExtensionCheckStatus.INVALID_EXTENSION, false);
                        return PathValidationResult.CERTIFICATE_EXTENSIONS_FAILURE;
                    }
                }
                
                // A certificate MUST NOT include more than one instance of a particular extension
                for (int j = 0; j < eklentiler.getExtensionCount(); j++)
                {
                    EExtension diger = eklentiler.getExtension(j);

                    if ((i != j) && (eklenti.getIdentifier().Equals(diger.getIdentifier())))
                    {
                        logger.Error("Sertifikada aynı extension birden fazla defa tanımlanmış.");
                        aCertStatusInfo.addDetail(this, ExtensionCheckStatus.DUPLICATE_EXTENSION, false);
                        return PathValidationResult.CERTIFICATE_EXTENSIONS_FAILURE;
                    }
                }
            }
            aCertStatusInfo.addDetail(this, ExtensionCheckStatus.VALID_EXTENSIONS, true);
            return PathValidationResult.SUCCESS;
        }

        private bool _tanimliEklentiMi(Asn1ObjectIdentifier aOID)
        {
            foreach (Asn1ObjectIdentifier extension in taninanEklentiler)
            {
                if (aOID.Equals(extension))
                {
                    logger.Debug("Extension tanımlı extensionlar içinde bulundu:" + aOID);
                    return true;
                }
            }
            return false;
        }


        public override String getCheckText()
        {
            return Resource.message(Resource.SERTIFIKA_EKLENTI_KONTROLU);
        }
    }
}
