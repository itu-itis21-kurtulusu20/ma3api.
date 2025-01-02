using System.Collections.Generic;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using CheckResult = tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
	using PathValidationRecord = tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
	using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
	using Certificate = tr.gov.tubitak.uekae.esya.asn.x509.Certificate;


	/// <summary>
	/// @author ayetgin
	/// </summary>
	public static class XmlSignUtil
	{

		public static string verificationInfo(CertificateStatusInfo csi)
		{

			IList<CheckResult> details = csi.getDetails();
		    IList<PathValidationRecord> pathItems = csi.getValidationHistory();

			StringBuilder info = new StringBuilder();
		    ECertificate signingCert = csi.getCertificate();
			if (signingCert != null)
			{
				info.Append("Issuer: ").Append(signingCert.getIssuer().stringValue()).Append("\n");
				info.Append("Subject: ").Append(signingCert.getSubject().stringValue()).Append("\n");
				info.Append("Seri no: ").Append(signingCert.getSerialNumberHex()).Append("\n");
			}
			info.Append("Certificate status: ").Append(csi.getCertificateStatus()).Append("\n");
			info.Append("Certificate verification time: ").Append(csi.getValidationTime().Value).Append('\n');
			if (details != null)
			{
				foreach (CheckResult checkResult in details)
				{
					info.Append("- ").Append(checkResult.getResultText()).Append('\n');
				}
			}

			info.Append("\nPATH\n");
			if (pathItems != null)
			{
				foreach (PathValidationRecord pathItem in pathItems)
				{
					IList<ECertificate> path = pathItem.getPath();
					foreach (ECertificate cert in path)
					{
						info.Append("- '").Append(cert.getSubject().stringValue()).Append("'; serial: ").Append(cert.getSerialNumberHex()).Append('\n');
					}
				}
			}

			return info.ToString();
		}

		/*
		public static void addTimestampValidationData(XAdESTimeStamp aTimeStamp, XMLSignature aSignature) throws XMLSignatureException
		{
		    for (int i = 0; i < aTimeStamp.getEncapsulatedTimeStampCount(); i++) {
		        EncapsulatedTimeStamp ets = aTimeStamp.getEncapsulatedTimeStamp(i);
	
		        SignedDataValidationResult sdvr = XmlSignUtil.validateTimeStamp(ets);
	
		        CertificateValues certVals = new CertificateValues(aTimeStamp.getContext());
		        RevocationValues revVals = new RevocationValues(aTimeStamp.getContext());
	
		        for (SignatureValidationResult svr : sdvr.getSDValidationResults()) {
		            XmlSignUtil.fillValidationData(svr.getCertStatusInfo(), certVals, revVals);
		        }
	
		        List<ECertificate> tsCertificates = ets.getSignedData().getCertificates();
		        if (tsCertificates != null && tsCertificates.size() > 0) {
		            certVals = removeDuplicateValidationData(certVals, tsCertificates);
		        }
	
		        TimeStampValidationData tsvd = new TimeStampValidationData(aTimeStamp.getContext(), certVals, revVals);
		        tsvd.setURI("#" + aTimeStamp.getId());
	
		        aSignature.createOrGetQualifyingProperties().createOrGetUnsignedProperties()
		                .getUnsignedSignatureProperties().addTimeStampValidationData(tsvd, aTimeStamp);
		    }
	
		}
	
	
		public static void fillValidationData(CertificateStatusInfo csi,
		                                      CertificateValues aCertificateValues,
		                                      RevocationValues aRevocationValues)
		        throws XMLSignatureException
		{
		    int counter = 0;
		    while (csi != null) {
	
		        aCertificateValues.addCertificate(csi.getCertificate());
	
		        CRLStatusInfo crlStatus = csi.getCRLInfo();
		        if (crlStatus != null) {
		            aRevocationValues.addCRL(crlStatus.getCRL());
		        }
	
		        CRLStatusInfo deltaCrlStatus = csi.getDeltaCRLInfo();
		        if (deltaCrlStatus != null) {
		            aRevocationValues.addCRL(deltaCrlStatus.getCRL());
		        }
	
		        OCSPResponseStatusInfo ocspStatus = csi.getOCSPResponseInfo();
		        if (ocspStatus != null) {
		            aRevocationValues.addOCSPResponse(ocspStatus.getOCSPResponse());
		        }
	
		        csi = csi.getSigningCertficateInfo();
		        counter++;
		    }
		}
	
		// todo
	
		public static CertificateValues removeDuplicateValidationData(CertificateValues aCertificateValues,
		                                                              List<ECertificate> aCertificates)
		        throws XMLSignatureException
		{
		    CertificateValues result = new CertificateValues(aCertificateValues.getContext());
		    for (int i = 0; i < aCertificateValues.getCertificateCount(); i++) {
		        ECertificate current = aCertificateValues.getCertificate(i);
		        if (!aCertificates.contains(current)) {
		            result.addCertificate(current);
		        }
		    }
	
		    return result;
		}
	
		public static SignedDataValidationResult validateTimeStamp(EncapsulatedTimeStamp aTimeStamp)
		        throws XMLSignatureException
		{
		    byte[] input = aTimeStamp.getContentInfo().getEncoded();
	
		    Hashtable<String, Object> params = new Hashtable<String, Object>();
	
		    ValidationConfig validationConfig = aTimeStamp.getContext().getConfig().getValidationConfig();
		    String configFile = validationConfig.getCertificateValidationPolicyFile();
	
		    try {
	
		        ValidationPolicy policy = PolicyReader.readValidationPolicy(configFile);
	
		        params.put(AllEParameters.P_CERT_VALIDATION_POLICY, policy);
	
		        SignedDataValidation sd = new SignedDataValidation();
	
		        Context context = aTimeStamp.getContext();
		        CertValidationData validationData = context.getValidationData();
	
		        params.put(AllEParameters.P_INITIAL_CERTIFICATES, validationData.getCertificates());
		        params.put(AllEParameters.P_INITIAL_CRLS, validationData.getCrls());
		        params.put(AllEParameters.P_INITIAL_OCSP_RESPONSES, validationData.getOcspResponses());
	
		        params.put(AllEParameters.P_SIGNING_TIME, aTimeStamp.getTime());
		        params.put(AllEParameters.P_GRACE_PERIOD, new Long(validationConfig.getGracePeriodInSeconds()));
		        params.put(AllEParameters.P_REVOCINFO_PERIOD, new Long(validationConfig.getLastRevocationPeriodInSeconds()));
	
		        return sd.verify(input, params);
		    }
		    catch (Exception e) {
		        throw new XMLSignatureException(e, "Cant verify timestamp");
		    }
		}     */

	}

}