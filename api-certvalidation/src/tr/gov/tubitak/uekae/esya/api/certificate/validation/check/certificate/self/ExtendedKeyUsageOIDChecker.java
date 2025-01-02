package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtendedKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ExtensionCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;


public class ExtendedKeyUsageOIDChecker extends CertificateSelfChecker
{
    private static final Logger logger = LoggerFactory.getLogger(ExtendedKeyUsageOIDChecker.class);

    private static final String PARAM_EKUOID = "oid";

    enum ExtendedKeyUsageCheckStatus implements CheckStatus{
        NO_EXTENDED_KEY_USAGE,
        INVALID_EXTENDED_KEY_USAGE,
        VALID;

        public String getText()
        {
            switch (this){
                case NO_EXTENDED_KEY_USAGE :
                    return CertI18n.message(CertI18n.EXTENDEDKEY_USAGE_YOK);
                case INVALID_EXTENDED_KEY_USAGE:
                    return CertI18n.message(CertI18n.EXTENDEDKEY_USAGE_BOZUK);
                case VALID  :
                    return CertI18n.message(CertI18n.EXTENDEDKEY_USAGE_GECERLI);
            }
            return null;
        }
    }

    @Override
    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
    {
        logger.debug("Extended Key Usage kontrolü yapılacak.");

        ECertificate certificate = aCertStatusInfo.getCertificate();
        if(certificate.isCACertificate())
        {
            logger.debug("CA certificate.");
            aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
            return PathValidationResult.SUCCESS;
        }
        if(certificate.isOCSPSigningCertificate())
        {
            logger.debug("OCSP Signing certificate.");
            aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
            return PathValidationResult.SUCCESS;
        }
        if(certificate.isTimeStampingCertificate())
        {
            logger.debug("TimeStamping certificate.");
            aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
            return PathValidationResult.SUCCESS;
        }


        String oidParam = mCheckParams.getParameterAsString(PARAM_EKUOID);
        Asn1ObjectIdentifier oid = new Asn1ObjectIdentifier(OIDUtil.parse(oidParam));

        EExtendedKeyUsage eku = certificate.getExtensions().getExtendedKeyUsage();

        if (eku==null){
            aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.NO_EXTENDED_KEY_USAGE, false);
            return PathValidationResult.EXTENDED_KEYUSAGE_CONTROL_FAILURE;
        }

        if (!eku.hasElement(oid)){
            aCertStatusInfo.addDetail(this, ExtensionCheckStatus.INVALID_EXTENSION, false);
            return PathValidationResult.EXTENDED_KEYUSAGE_CONTROL_FAILURE;
        }
        aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
        return PathValidationResult.SUCCESS;
    }

    @Override
    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU);
    }

}
