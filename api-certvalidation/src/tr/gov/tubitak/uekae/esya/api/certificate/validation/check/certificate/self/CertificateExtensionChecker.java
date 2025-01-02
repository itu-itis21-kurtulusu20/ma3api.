package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ExtensionCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

import java.util.ArrayList;

/**
 * Checks the extension information in the Certificate is valid
 * <ul>
 * <li>critical extensions must be known
 * <li>any particular extension must exist only once
 * </ul>
 */
public class CertificateExtensionChecker extends CertificateSelfChecker
{
    private static Logger logger = LoggerFactory.getLogger(CertificateExtensionChecker.class);


    private static ArrayList<Asn1ObjectIdentifier> taninanEklentiler = new ArrayList<Asn1ObjectIdentifier>();

    static{
        /*MUST*/
        taninanEklentiler.add(EExtensions.oid_ce_keyUsage);             //key usage (section 4.2.1.3)
        taninanEklentiler.add(EExtensions.oid_ce_certificatePolicies);  //certificate policies (section 4.2.1.5)
        taninanEklentiler.add(EExtensions.oid_ce_subjectAltName);       //the subject alternative name (section 4.2.1.7)
        taninanEklentiler.add(EExtensions.oid_ce_basicConstraints);     //basic constraints (section 4.2.1.10)
        taninanEklentiler.add(EExtensions.oid_ce_nameConstraints);      //name constraints (section 4.2.1.11)
        taninanEklentiler.add(EExtensions.oid_ce_policyConstraints);    //policy constraints (section 4.2.1.12)
        taninanEklentiler.add(EExtensions.oid_ce_extKeyUsage);          //extended key usage (section 4.2.1.13)
        taninanEklentiler.add(EExtensions.oid_ce_inhibitAnyPolicy);     //inhibit any-policy (section 4.2.1.15)

        /*SHOULD*/
        taninanEklentiler.add(EExtensions.oid_ce_authorityKeyIdentifier);   //the authority identifier(section 4.2.1.1)
        taninanEklentiler.add(EExtensions.oid_ce_subjectKeyIdentifier);     //subject key identifier (section 4.2.1.2)
        taninanEklentiler.add(EExtensions.oid_ce_policyMappings);           //policy mapping (section 4.2.1.6)
        taninanEklentiler.add(EExtensions.oid_pe_qcStatements);             //???
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
    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
    {
        logger.debug("Extension kontrolü yapılacak.");

        ECertificate cert = aCertStatusInfo.getCertificate();
        EExtensions eklentiler = cert.getExtensions();
        if (eklentiler.getExtensionCount() == 0) {
            logger.debug("Sertifikada extension yok, kontrolü yapılmayacak.");
            aCertStatusInfo.addDetail(this, ExtensionCheckStatus.NO_EXTENSION, true);
            return PathValidationResult.SUCCESS;
        }
        for (int i = 0; i < eklentiler.getExtensionCount(); i++) {
            EExtension eklenti = eklentiler.getExtension(i);
            if (eklenti.isCritical()) {
                if (!_tanimliEklentiMi(eklenti.getIdentifier())) {
                    logger.error("Extension tanımlı extensionlar içinde değil: " + eklenti.getIdentifier());
                    aCertStatusInfo.addDetail(this, ExtensionCheckStatus.INVALID_EXTENSION, false);
                    return PathValidationResult.CERTIFICATE_EXTENSIONS_FAILURE;
                }
            }

            // A certificate MUST NOT include more than one instance of a particular extension
            for (int j = 0; j < eklentiler.getExtensionCount(); j++) {
                EExtension diger = eklentiler.getExtension(j);

                if ((i != j) && (eklenti.getIdentifier().equals(diger.getIdentifier()))) {
                    logger.error("Sertifikada aynı extension birden fazla defa tanımlanmış.");
                    aCertStatusInfo.addDetail(this, ExtensionCheckStatus.DUPLICATE_EXTENSION, false);
                    return PathValidationResult.CERTIFICATE_EXTENSIONS_FAILURE;
                }
            }
        }
        aCertStatusInfo.addDetail(this, ExtensionCheckStatus.VALID_EXTENSIONS, true);
        return PathValidationResult.SUCCESS;
    }

    private boolean _tanimliEklentiMi(Asn1ObjectIdentifier aOID)
    {
        for (Asn1ObjectIdentifier extension : taninanEklentiler) {
            if (aOID.equals(extension)) {
                logger.debug("Extension tanımlı extensionlar içinde bulundu:" + aOID);
                return true;
            }
        }
        return false;
    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERTIFIKA_EKLENTI_KONTROLU);
    }
}
