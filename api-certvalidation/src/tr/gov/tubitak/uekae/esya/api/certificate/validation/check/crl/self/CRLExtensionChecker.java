package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ExtensionCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;

import java.util.ArrayList;

/**
 * Checks the extension information in the CRL is valid 
 */
public class CRLExtensionChecker extends CRLSelfChecker {

    private static final Logger logger = LoggerFactory.getLogger(CRLExtensionChecker.class);

    private static ArrayList<Asn1ObjectIdentifier> knownExtensions = new ArrayList<Asn1ObjectIdentifier>();

    static {
        knownExtensions.add(EExtensions.oid_ce_authorityKeyIdentifier);//Authority Key Identifier
        knownExtensions.add(EExtensions.oid_ce_cRLNumber);//CRL Number
        knownExtensions.add(EExtensions.oid_ce_issuingDistributionPoint);//Issuing Distribution Point
    }

    /*RFC 3280de tanımlanan SIL extensionlar:
      5.2.1 Authority Key Identifier
      5.2.2 Issuer Alternative Name
      5.2.3 CRL Number
      5.2.4 Delta CRL Indicator
      5.2.5 Issuing Distribution Point
      5.2.6 Freshest CRL
      */

    /**
     * SIL eklentilerinin geçerli olup olmadığını kontrol eder
     */
    protected PathValidationResult _check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
    {
        EExtensions exts = aCRL.getCRLExtensions();
        if (exts.getExtensionCount() == 0) {
            logger.debug("CRL yapısında eklenti yok.");
            aCRLStatusInfo.addDetail(this, ExtensionCheckStatus.NO_EXTENSION, true);
            return PathValidationResult.SUCCESS;
        }

        for (int i = 0; i < exts.getExtensionCount(); i++) {
            EExtension ext = exts.getExtension(i);
            if (ext.isCritical()) {
                logger.debug("CRL yapısında eklenti var.");
                if (!_isKnownExtension(ext.getIdentifier())) {
                    logger.error("Extension tanımlı extensionlar içinde değil: " + ext.getIdentifier());
                    aCRLStatusInfo.addDetail(this, ExtensionCheckStatus.INVALID_EXTENSION, false);
                    return PathValidationResult.CRL_EXTENSIONS_CONTROL_FAILURE;
                }
            }
        }
        aCRLStatusInfo.addDetail(this, ExtensionCheckStatus.VALID_EXTENSIONS, true);
        return PathValidationResult.SUCCESS;
    }

    private boolean _isKnownExtension(Asn1ObjectIdentifier aOID)
    {
        for (Asn1ObjectIdentifier extension : knownExtensions) {
            if (aOID.equals(extension)) {
                if (logger.isDebugEnabled())
                    logger.debug("Extension tanımlı extensionlar içinde bulundu:" + aOID);
                return true;
            }
        }
        return false;
    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SIL_EKLENTI_KONTROLU);
    }

}
