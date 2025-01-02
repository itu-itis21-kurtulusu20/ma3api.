package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevocationCheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.RevokeCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The data structure for storing the output of certificate validation
 *
 * CertificateStatusInfo stores the status of the certificate with the
 * validation details such as the status info of the issuer certificates and the
 * CRLs. If any OCSP query is performed the related information about that OCSP
 * query is also stored in this structure. 
 */
public class CertificateStatusInfo extends StatusInfo implements Cloneable, Serializable
{

    private ECertificate mCertificate;
    private Calendar mValidationTime;

    private CertificateStatus mCertificateStatus;
    private RevocationStatusInfo mRevocationInfo;
    private List<RevocationCheckResult> mRevocationCheckDetails = new ArrayList<RevocationCheckResult>(0);

    private List<CRLStatusInfo> mCRLInfoList = new ArrayList<CRLStatusInfo>();
    private List<CRLStatusInfo> mDeltaCRLInfoList = new ArrayList<CRLStatusInfo>();

    private List<OCSPResponseStatusInfo> mOCSPResponseInfo = new ArrayList<OCSPResponseStatusInfo>(); 

    private List<PathValidationRecord> mValidationHistory = new ArrayList<PathValidationRecord>();
    private List<List<ECertificate> > mIncompletePathList = new ArrayList<List<ECertificate>>();

	private List<ECertificate> mTrustedCertificates;

    /**
     * \brief
     * SertifikaDurumBilgisi constructoru
     */
    public CertificateStatusInfo()
    {
    }

    /**
     * \brief
     * SertifikaDurumBilgisi constructoru
     * <p>
     * \param const ECertificate{@literal &}
     * Sertifika
     */
    public CertificateStatusInfo(ECertificate aCertificate, Calendar aValidationTime)
    {
        mCertificate = aCertificate;
        mValidationTime = aValidationTime;
    }
    /**
   	 * Convert certificate validation result  to string
   	 */
    public String getDetailedMessage()
    {
    	StringBuilder sb = new StringBuilder();
    	
    	if(mCertificateStatus == CertificateStatus.VALID)
		{
    		sb.append(CertI18n.message(CertI18n.CERTIFICATE_VALIDATION_SUCCESSFUL));
		}
		else
		{
			sb.append(mCertificateStatus.textAl() + " ");
			
			if(mCertificateStatus == CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE)
			{
				List<CheckResult> checkerResults = getDetails();
				for (CheckResult checkResult : checkerResults) 
				{
					if(!checkResult.isSuccessful()){
						sb.append(CertI18n.message(CertI18n.CERTIFICATE_CHECKER_FAIL, checkResult.getCheckText()));
						sb.append(checkResult.getResultText());
					}

				}
			}
			
			if(mCertificateStatus == CertificateStatus.PATH_VALIDATION_FAILURE)
			{
				List<PathValidationRecord> pathValidations = getValidationHistory();
				
				if(pathValidations == null || pathValidations.size() == 0)
					sb.append(CertI18n.message(CertI18n.CERTIFICATE_NO_PATH_FOUND) + " ");
				
				if(pathValidations != null)
					for (PathValidationRecord pathValidationRecord : pathValidations) 
						sb.append(pathValidationRecord.getResultCode().getMessage() + " ");
			}
			
//			if(mCertificateStatus == CertificateStatus.NO_TRUSTED_CERT_FOUND)
//			{
//				
//			}
		}
    	return sb.toString();
    }

    /**
     * \brief
     * Iptal Kontrol Sonucu Detay Bilgilerini doner.
     * <p>
     * <p>
     * /return const QList{@literal <RevocationCheckResult> &}
     * Iptal Kontrol Sonucu Detay Bilgileri
     */
    public List<RevocationCheckResult> getRevocationCheckDetails()
    {
        return mRevocationCheckDetails;
    }

    /**
     * \brief
     * İptal Kontrol Sonucu Detay Bilgilerini belirler.
     * <p>
     * \param const QList{@literal < RevocationCheckResult> &} iIptalKontrolDetaylari
     * Sonuç Detayı istenen kontrol işlemi
     */
    public void setRevocationCheckDetails(List<RevocationCheckResult> aIptalKontrolDetaylari)
    {
        mRevocationCheckDetails = aIptalKontrolDetaylari;
    }

    /**
     * İptal Kontrol Sonucu Detay Bilgisi döner.
     *
     * @param aKontrolText Sonuç Detayı istenen kontrol işlemi
     * @return IptalKontrolSonucu İptal Kontrol Sonucu
     */
    public RevocationCheckResult getRevocationCheckDetail(String aKontrolText)
    {
        for (RevocationCheckResult checkResult : mRevocationCheckDetails) {
            if (checkResult.getCheckText().equals(aKontrolText))
                return checkResult;
        }
        return null;
    }


    /**
     * \brief
     * Iptal Kontrol Sonucu Detay Bilgisi ekler.
     * <p>
     * \param const RevocationCheckResult {@literal &} iIKS
     * Iptal Kontrol Sonucu
     */
    public void addRevocationCheckDetail(RevocationCheckResult aIKS)
    {
        mRevocationCheckDetails.add(aIKS);
    }
    /**
   	 * Add revocation check detail 
   	 * @param aChecker RevocationChecker
   	 * @param aResult CheckStatus
   	 * @param aCheckResult RevokeCheckStatus
   	 */
    public void addRevocationCheckDetail(RevocationChecker aChecker,
                                 CheckStatus aResult,
                                 RevokeCheckStatus aCheckResult){
        mRevocationCheckDetails.add(new RevocationCheckResult(aChecker.getCheckText(),
                                                              aResult.getText(),
                                                              aResult, aCheckResult));
    }
    /**
   	 * Returns certificate's revocation info
   	 * @return 
   	 */
    public RevocationStatusInfo getRevocationInfo()
    {
        return mRevocationInfo;
    }
    /**
   	 * Set revocation info
   	 * @param iptalDurumu
   	 */
    public void setRevocationInfo(RevocationStatusInfo iptalDurumu)
    {
        mRevocationInfo = iptalDurumu;
    }
    /**
   	 * Returns OCSP response info list
   	 * @return 
   	 */
    public List<OCSPResponseStatusInfo> getOCSPResponseInfoList()
    {
    	return mOCSPResponseInfo;
    }
    /**
   	 * Add OCSP response info to OCSP response info list
   	 * @param aStatusInfo
   	 */
    public void addOCSPResponseInfo(OCSPResponseStatusInfo aStatusInfo)
    {
    	mOCSPResponseInfo.add(aStatusInfo);
    }

//    public OCSPResponseStatusInfo getOCSPResponseInfo()
//    {
//        return mOCSPResponseInfo;
//    }

//    public void setOCSPResponseInfo(OCSPResponseStatusInfo cevabiDurumu)
//    {
//        mOCSPResponseInfo = cevabiDurumu;
//    }

    /**
   	 * Returns certificate which has this certificate status info
   	 * @return certificate
   	 */
    public ECertificate getCertificate()
    {
        return mCertificate;
    }

    /**
   	 * Set certificate which has this certificate status info
   	 * @param certificate to be set
   	 */
    public void setCertificate(ECertificate certificate)
    {
        mCertificate = certificate;
    }

    /**
   	 * Returns certificate status(VALID,REVOCATION_CHECK_FAILURE,CERTIFICATE_SELF_CHECK_FAILURE and etc)
   	 * @return 
   	 */
    public CertificateStatus getCertificateStatus()
    {
        return mCertificateStatus;
    }

    /**
	 * Set certificate status(VALID,REVOCATION_CHECK_FAILURE,CERTIFICATE_SELF_CHECK_FAILURE and etc)
	 * @param certificateStatus
	 */
    public void setCertificateStatus(CertificateStatus certificateStatus)
    {
        mCertificateStatus = certificateStatus;
    }

    /**
   	 * Returns validation time which certificate is validated in this time
   	 * @return Calendar
   	 */
    public Calendar getValidationTime()
    {
        return mValidationTime;
    }

    /**
	 * Set validation time which certificate is validated in this time
	 * @param aValidationTime
	 */
    public void setValidationTime(Calendar aValidationTime)
    {
        mValidationTime = aValidationTime;
    }

    /**
   	 * Returns CRL info list
   	 * @return
   	 */
    public List<CRLStatusInfo> getCRLInfoList()
    {
	    return mCRLInfoList;
    }

    /**
	 * Add   CRL info to  CRL info list
	 * @param aCRLInfo
	 */
    public void addCRLInfo(CRLStatusInfo aCRLInfo)
    {
        mCRLInfoList.add(aCRLInfo);
    }

//    public CRLStatusInfo getCRLInfo()
//    {
//        if (mCRLInfoList.isEmpty())
//            return null;
//        else
//            return mCRLInfoList.get(0);
//    }

    /**
	 * Set CRL info list
	 * @param aCRLInfoList
	 */
    public void setCRLInfoList(List<CRLStatusInfo> aCRLInfoList)
    {
        mCRLInfoList = aCRLInfoList;
    }

    /**
   	 * Returns delta CRL info list
   	 * @return
   	 */
    public List<CRLStatusInfo> getDeltaCRLInfoList()
    {
        return mDeltaCRLInfoList;
    }

//    public CRLStatusInfo getDeltaCRLInfo()
//    {
//        if (mDeltaCRLInfoList.isEmpty())
//            return null;
//        else
//            return mDeltaCRLInfoList.get(0);
//    }

    /**
	 * Add  delta CRL info to delta CRL info list
	 * @param aDeltaSilDurumu
	 */
    public void addDeltaCRLInfo(CRLStatusInfo aDeltaSilDurumu)
    {
        mDeltaCRLInfoList.add(aDeltaSilDurumu);
    }

    /**
	 * Set delta CRL info list
	 * @param aCRLInfoList
	 */
    public void setDeltaCRLInfoList(List<CRLStatusInfo> aCRLInfoList)
    {
        mDeltaCRLInfoList = aCRLInfoList;
    }

    /**
	 * Returns validation history of certificate
	 * @return
	 */
    public List<PathValidationRecord> getValidationHistory()
    {
        return mValidationHistory;
    }

    /**
   	 * Set validation history
   	 * @param aValidationHistory
   	 */
    public void setValidationHistory(List<PathValidationRecord> aValidationHistory)
    {
        mValidationHistory = aValidationHistory;
    }

    /**
	 * Add  validation record to validation history
	 * @param aPathValidationRecord
	 */
    public void addToValidationHistory(PathValidationRecord aPathValidationRecord){
        mValidationHistory.add(aPathValidationRecord);
    }

    /**
	 * Returns trusted certificates
	 * @return
	 */
    public List<ECertificate> getTrustedCertificates()
    {
    	return mTrustedCertificates;
    }

    /**
	 * Set trusted certificates
	 * @param aCerts List{@literal <ECertificate>}
	 */
    public void setTrustedCertificates(List<ECertificate> aCerts)
    {
    	mTrustedCertificates = aCerts;
    }

    /**
	 * Checks whether certificate is revoked or not.
	 * @return True if any path validation result is CERTIFICATE_REVOKED in validation history, false otherwise
	 */   
    public boolean isRevoked(){
        for (int i = mValidationHistory.size()-1; i>=0;i--)
        {
            if (mValidationHistory.get(i).getResultCode() == PathValidationResult.CERTIFICATE_REVOKED)
                return true;
        }
        return false;
    }

    /**
   	 * Convert validation history result to string
   	 */
    public String validationHistory2String()
    {
        if (mValidationHistory.isEmpty())
            return	CertI18n.PVR_NO_PATHFOUND;

        if (isRevoked())
            return CertI18n.PVR_CERTIFICATE_REVOKED;

        PathValidationResult res = mValidationHistory.get(0).getResultCode();
        int vhSize = mValidationHistory.size();

        boolean allSame = true;
        for (int i = 1; i<vhSize;i++)
        {
            if (mValidationHistory.get(i).getResultCode()!=res)
            {
                allSame = false;
                break;
            }
        }

        if (!allSame)
            return CertI18n.ZINCIR_SORUNLU; // Different results found then return generic failure
        else
            return res.getMessage();// All results are the same then return the result

    }

    /**
    * Converts CertificateStatusInfo object to string
    */
    public String toString()
    {    	
        switch (mCertificateStatus)
        {
	        case VALID				                : return CertI18n.message(CertI18n.GECERLI);
	        case PATH_VALIDATION_FAILURE			: return validationHistory2String();
	        case REVOCATION_CHECK_FAILURE           : return CertI18n.message(CertI18n.IPTAL_KONTROLU_SORUNLU);
	        case CERTIFICATE_SELF_CHECK_FAILURE		: return CertI18n.message(CertI18n.SERTIFIKA_SORUNLU);
	        case NO_TRUSTED_CERT_FOUND		        : return CertI18n.message(CertI18n.NOTRUSTEDCERTFOUND);
	        default						            : return CertI18n.message(CertI18n.UNKNOWN);
        }
    }

    @Override
    public CertificateStatusInfo clone()
    {
        try {
            return (CertificateStatusInfo) super.clone();
        }
        catch (CloneNotSupportedException x) {
            throw new ESYARuntimeException(x);
        }
    }
    
    public List<List<ECertificate>> getIncompletePathList()
    {
        return mIncompletePathList;
    }

    void setIncompletePathList(List<List<ECertificate>> aIPL)
    {
        mIncompletePathList = aIPL;
    }

    /**
   	 * Convert self control check result to string
   	 */
    public String checkResultsToString()
    {
        String res = "";
        for (int i = 0; i < mDetails.size();i++)
        {
            res += "  [-] "+mDetails.get(i).toString()+"\n";
        }
        //res.chop(1);
        return res;
    }

    /**
	 * Convert all revocation check result to string
	 */
    public String revocationCheckResultsToString()
    {
        String res = "";
        for (int i = 0; i < mRevocationCheckDetails.size();i++)
        {
            res += "  [-] " + mRevocationCheckDetails.get(i).toString() + "\n" ;
        }
        //res.chop(1);
        return res;
    }


    /**
	 * Convert all validation result to string
	 */
    // todo
    public String printDetailedValidationReport()
    {
        String report;

        String subjectLine = "Sertifika Sahibi: "+ mCertificate.getSubject().stringValue(); //.toTitle());
        String issuerLine  = "Yayıncı         : "+ mCertificate.getIssuer().stringValue();  // .toTitle

        String statusLine  = "Sertifika Durumu: "+ toString();


        String selfChecksStartLine			= "Sertifika Kontrol Detayları: \n";
        String selfCheckLines				= selfChecksStartLine + checkResultsToString();

        String revocationCheckStartLine	= "İptal Durumu Kontrol Detayları: \n";
        String revocationCheckLines		= revocationCheckStartLine + revocationCheckResultsToString();

        String smCertStatusStartLine;
        String smCertStatusDetails;

        CertificateStatusInfo pSMCertStatus = getSigningCertficateInfo();

        if (pSMCertStatus!=null)
        {
            smCertStatusStartLine = "İmzalayan Sertifika Geçerlilik Detayları: \n";
            smCertStatusDetails = pSMCertStatus.printDetailedValidationReport();
        }
        else
        {
            smCertStatusStartLine	= "İmzalayan Sertifika Geçerlilik Detayları: \n";
            //smCertStatusDetails = MAKELINE(iLIndentation,iRIndentation,L("İmzalayan Sertifika Bulunamadı"));;
            smCertStatusDetails		= "GÜVENİLİR KÖK\n";
        }
        String smCertStatusLines = smCertStatusStartLine + smCertStatusDetails;

        String crlStatusStartLine = "SİL Detayları:\n";
        String crlStatusDetails = "";

        List<CRLStatusInfo> crlStatusInfos = getCRLInfoList();
        for (int i = 0 ; i < crlStatusInfos.size();i++)
        {
            crlStatusStartLine = "SİL Detayları:\n";
            crlStatusDetails += crlStatusInfos.get(i).printDetailedValidationReport();
        }
        if (crlStatusDetails==null || crlStatusDetails.trim().length()==0)
        {
            crlStatusStartLine	= "SİL Detayları:\n";
            crlStatusDetails	= "Yok\n";
        }

        String crlStatusLines = crlStatusStartLine+crlStatusDetails;

        report =	//REPORT_HEADER(iLIndentation,iRIndentation)+
                    subjectLine				+"\n"+
                    issuerLine				+"\n"+
                    statusLine				+
                    "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                    selfCheckLines			+
                    "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                    revocationCheckLines	+
                    "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                    smCertStatusLines		+
                    "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                    crlStatusLines			;
                    //+ REPORT_FOOTER(iLIndentation,iRIndentation)

        return report;

    }
}
