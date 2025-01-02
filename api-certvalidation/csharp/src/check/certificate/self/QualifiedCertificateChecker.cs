using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using log4net;
using log4net.Repository.Hierarchy;
using tr.gov.tubitak.uekae.esya.api.asn.pqixqualified;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.parser;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    public class QualifiedCertificateChecker : CertificateSelfChecker 
    {
	    public static readonly String PARAM_STATEMENTOIDS = "statementoids";
	
	    protected ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
	
	    private static readonly String AND_SEPERATOR = "AND";
	    private static readonly String OR_SEPERATOR = "OR";
	
	
	public class QualifiedCertificateCheckStatus : CheckStatus
	{
	    public static readonly QualifiedCertificateCheckStatus QCC_VALID = new QualifiedCertificateCheckStatus();
	    public static readonly QualifiedCertificateCheckStatus QCC_INVALID_KEYUSAGE = new QualifiedCertificateCheckStatus();
		public static readonly QualifiedCertificateCheckStatus QCC_NO_STATEMENT_ID = new QualifiedCertificateCheckStatus();
        public static readonly QualifiedCertificateCheckStatus QCC_NO_USER_NOTICE = new QualifiedCertificateCheckStatus();
        public static readonly QualifiedCertificateCheckStatus WRONG_FORMAT_QCC_STATEMENT = new QualifiedCertificateCheckStatus();

        private QualifiedCertificateCheckStatus()
        {
            
        }

        public String getText()
        {

            if (this.Equals(QCC_VALID))
                return Resource.message(Resource.SERTIFIKA_NITELIKLI_KONTROLU_BASARILI);
            else if (this.Equals(QCC_INVALID_KEYUSAGE))
                return Resource.message(Resource.SERTIFIKA_KEYUSAGE_HATALI);
            else if (this.Equals(QCC_NO_STATEMENT_ID))
                return Resource.message(Resource.SERTIFIKA_NITELIKLI_IBARESI_YOK);
            else if (this.Equals(QCC_NO_USER_NOTICE))
                return Resource.message(Resource.SERTIFIKA_KULLANICI_NOTU_YOK);
            else if (this.Equals(WRONG_FORMAT_QCC_STATEMENT))
                return Resource.message(Resource.WRONG_FORMAT_QCC_STATEMENT);
            else
                return Resource.message(Resource.KONTROL_SONUCU);
        }
    }
	

	protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo) 
	{
		
		ECertificate cert = aCertStatusInfo.getCertificate();
		
		if(cert.isCACertificate())
		{
			logger.Debug("CA certificate.");
			aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
			return PathValidationResult.SUCCESS;
		}
        if (cert.isOCSPSigningCertificate() == true)
        {
            logger.Debug("OCSP Signing certificate.");
            aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
            return PathValidationResult.SUCCESS;
        }
        if (cert.isTimeStampingCertificate() == true)
        {
            logger.Debug("TimeStamping certificate.");
            aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
            return PathValidationResult.SUCCESS;
        }	 
		
		EKeyUsage keyUsage = cert.getExtensions().getKeyUsage();
		
		if(keyUsage == null || (!keyUsage.isNonRepudiation()))
		{
			logger.Error("key usage is not non-repudiation.");
			aCertStatusInfo.addDetail(this,  QualifiedCertificateCheckStatus.QCC_INVALID_KEYUSAGE, false);
			return PathValidationResult.QCC_NO_STATEMENT_ID;
		}


        EQCStatements qc = cert.getExtensions().getQCStatements();
        if (qc == null)
        {
            logger.Error("no qc statement is found.");
            aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_NO_STATEMENT_ID, false);
            return PathValidationResult.QCC_NO_STATEMENT_ID;
        }

        String statementOids = mCheckParams.getParameterAsString(PARAM_STATEMENTOIDS);

        QualifiedCertificateEvaluator qualifiedCertificateEvaluator = new QualifiedCertificateEvaluator(cert);
        BooleanExpressionParser booleanExpressionParser = new BooleanExpressionParser();

        bool parseResult;

        try
        {
            parseResult = booleanExpressionParser.parse(statementOids, qualifiedCertificateEvaluator);
        }
        catch (ESYAException e)
        {
            logger.Error(e.ToString());
            aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.WRONG_FORMAT_QCC_STATEMENT, false);
            return PathValidationResult.WRONG_FORMAT_QCC_STATEMENT;
        }

        if (!parseResult)
        {
            logger.Error("One of the OID is not found.");
            aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_NO_STATEMENT_ID, false);
            return PathValidationResult.QCC_NO_STATEMENT_ID;
        }

        // dindar ekleme 28/11/12

        ECertificatePolicies cp = cert.getExtensions().getCertificatePolicies();
        bool cpCheck = false;
        int c = cp.getPolicyInformationCount();
        for (int i = 0; i < c; i++)
        {
            PolicyInformation pi = cp.getPolicyInformation(i);
            if (pi.policyQualifiers == null || pi.policyQualifiers.elements == null)
                continue;
            int pkc = pi.policyQualifiers.elements.Length;
            for (int j = 0; j < pkc; j++)
            {
                PolicyQualifierInfo pqi = pi.policyQualifiers.elements[j];
                if (!pqi.policyQualifierId.Equals(new Asn1ObjectIdentifier(new int[] { 1, 3, 6, 1, 5, 5, 7, 2, 2 })))
                    continue;

                if (pqi.qualifier.ToString().Length != 0)
                {
                    cpCheck = true;
                    break;
                }
            }
            if (cpCheck == true)
            {
                break;
            }

        }

        if (!cpCheck)
        {
            logger.Error("no user notice is found.");
            aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_NO_USER_NOTICE, false);
            return PathValidationResult.QCC_NO_USER_NOTICE;
        }
        // dindar son
		
		logger.Debug("Qualified certificate control is succeeded.");
		aCertStatusInfo.addDetail(this, QualifiedCertificateCheckStatus.QCC_VALID, true);
		return PathValidationResult.SUCCESS;
	}

    public override String getCheckText()
	{
	    return Resource.message(Resource.SERTIFIKA_NITELIKLI_KONTROLU);
    }
}

}
