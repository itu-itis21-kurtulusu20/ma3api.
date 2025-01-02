package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.pqixqualified.EQCStatements;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificatePolicies;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.parser.BooleanExpressionParser;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.parser.QualifiedCertificateEvaluator;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyInformation;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyQualifierInfo;

public class QualifiedCertificateChecker extends CertificateSelfChecker {
    public static final String PARAM_STATEMENTOIDS = "statementoids";

    private static final Logger logger = LoggerFactory.getLogger(QualifiedCertificateChecker.class);

    private static final String AND_SEPERATOR = "AND";
    private static final String OR_SEPERATOR = "OR";


    public enum QualifiedCertificateCheckStatus implements CheckStatus {
        QCC_VALID,
        QCC_INVALID_KEYUSAGE,
        QCC_NO_STATEMENT_ID,
        QCC_NO_USER_NOTICE,
        WRONG_FORMAT_QCC_STATEMENT;

        public String getText() {
            switch (this) {
                case QCC_VALID:
                    return CertI18n.message(CertI18n.SERTIFIKA_NITELIKLI_KONTROLU_BASARILI);
                case QCC_INVALID_KEYUSAGE:
                    return CertI18n.message(CertI18n.SERTIFIKA_KEYUSAGE_HATALI);
                case QCC_NO_STATEMENT_ID:
                    return CertI18n.message(CertI18n.SERTIFIKA_NITELIKLI_IBARESI_YOK);
                case QCC_NO_USER_NOTICE:
                    return CertI18n.message(CertI18n.SERTIFIKA_KULLANICI_NOTU_YOK);
                case WRONG_FORMAT_QCC_STATEMENT:
                    return CertI18n.message(CertI18n.WRONG_FORMAT_QCC_STATEMENT);
                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }
	
	@Override
	protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo) 
	{
		ECertificate cert = aCertStatusInfo.getCertificate();
		
		if(cert.isCACertificate())
		{
			logger.debug("CA certificate.");
			aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
			return PathValidationResult.SUCCESS;
		}
		if(cert.isOCSPSigningCertificate())
		{
			logger.debug("OCSP Signing certificate.");
			aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
			return PathValidationResult.SUCCESS;
		}
		if(cert.isTimeStampingCertificate())
		{
			logger.debug("TimeStamping certificate.");
			aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
			return PathValidationResult.SUCCESS;
		}
		
		EKeyUsage keyUsage = cert.getExtensions().getKeyUsage();
		if(keyUsage == null || (!keyUsage.isNonRepudiation()))
		{
			logger.error("key usage is not non-repudiation.");
			aCertStatusInfo.addDetail(this,  QualifiedCertificateCheckStatus.QCC_INVALID_KEYUSAGE, false);
			return PathValidationResult.QCC_NO_STATEMENT_ID;
		}

        EQCStatements qc= cert.getExtensions().getQCStatements();
        if(qc == null)
        {
            logger.error("no qc statement is found.");
            aCertStatusInfo.addDetail(this,  QualifiedCertificateCheckStatus.QCC_NO_STATEMENT_ID, false);
            return PathValidationResult.QCC_NO_STATEMENT_ID;
        }

		String statementOids = mCheckParams.getParameterAsString(PARAM_STATEMENTOIDS);

        QualifiedCertificateEvaluator qualifiedCertificateEvaluator = new QualifiedCertificateEvaluator(cert);
        BooleanExpressionParser booleanExpressionParser = new BooleanExpressionParser();

        boolean parseResult;

        try {
            parseResult = booleanExpressionParser.parse(statementOids, qualifiedCertificateEvaluator);
        } catch (ESYAException e) {
            logger.error(e.toString());
            aCertStatusInfo.addDetail(this,  QualifiedCertificateCheckStatus.WRONG_FORMAT_QCC_STATEMENT, false);
            return PathValidationResult.WRONG_FORMAT_QCC_STATEMENT;
        }

        if (!parseResult)
        {
            logger.error("One of the OID is not found.");
            aCertStatusInfo.addDetail(this,  QualifiedCertificateCheckStatus.QCC_NO_STATEMENT_ID, false);
            return PathValidationResult.QCC_NO_STATEMENT_ID;
        }

        ///   DINDAR EKLEME  28.11.2012

        ECertificatePolicies cp = cert.getExtensions().getCertificatePolicies();
        boolean cpCheck = false;
        int c= cp.getPolicyInformationCount();
        for (int i = 0 ; i< c ; i++)
        {
            PolicyInformation pi = cp.getPolicyInformation(i);
            if(pi.policyQualifiers==null || pi.policyQualifiers.elements==null)
            	continue;
            int pkc = pi.policyQualifiers.elements.length;
            for (int j = 0 ; j < pkc ; j++)
            {
                PolicyQualifierInfo pqi = pi.policyQualifiers.elements[j];
                if (!pqi.policyQualifierId.equals(new Asn1ObjectIdentifier(new int[]{1,3,6,1,5,5,7,2,2})))
                    continue;

                if (!pqi.qualifier.toString().isEmpty()){
                    cpCheck = true;
                    break;
                }
            }
            if(cpCheck == true){
            	break;
            }

        }

        if (!cpCheck)
        {
            logger.error("no user notice is found.");
            aCertStatusInfo.addDetail(this,  QualifiedCertificateCheckStatus.QCC_NO_USER_NOTICE, false);
            return PathValidationResult.QCC_NO_USER_NOTICE;
        }

        ///   DINDAR EKLEME

        logger.debug("Qualified certificate control is succeeded.");
		aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
		return PathValidationResult.SUCCESS;
	}
	
	public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERTIFIKA_NITELIKLI_KONTROLU);
    }
}
