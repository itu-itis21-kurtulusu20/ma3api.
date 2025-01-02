using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate
{
    /**
     * The data structure for storing the output of certificate validation
     *
     * CertificateStatusInfo stores the status of the certificate with the
     * validation details such as the status info of the issuer certificates and the
     * CRLs. If any OCSP query is performed the related information about that OCSP
     * query is also stored in this structure. 
     */
    [Serializable]
    public class CertificateStatusInfo : StatusInfo, ICloneable
    {
        private ECertificate mCertificate;
        private DateTime? mValidationTime;

        private CertificateStatus mCertificateStatus;
        private RevocationStatusInfo mRevocationInfo;
        private List<RevocationCheckResult> mRevocationCheckDetails = new List<RevocationCheckResult>(0);

        private List<CRLStatusInfo> mCRLInfoList = new List<CRLStatusInfo>();
        private List<CRLStatusInfo> mDeltaCRLInfoList = new List<CRLStatusInfo>(); // test

        private readonly List<OCSPResponseStatusInfo> mOCSPResponseInfos = new List<OCSPResponseStatusInfo>();

        private List<PathValidationRecord> mValidationHistory;

        private List<List<ECertificate>> mIncompletePathList;

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
         * <p/>
         * \param const ECertificate&
         * Sertifika
         */
        public CertificateStatusInfo(ECertificate aCertificate, DateTime? aValidationTime)
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

            if (mCertificateStatus == CertificateStatus.VALID)
            {
                sb.Append(Resource.message(Resource.CERTIFICATE_VALIDATION_SUCCESSFUL));
            }
            else
            {
                sb.Append(mCertificateStatus.textAl() + ". ");

                if (mCertificateStatus == CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE)
                {
                    List<CheckResult> checkerResults = getDetails();
                    foreach (CheckResult checkResult in checkerResults)
                    {
                        if (!checkResult.isSuccessful())
                            sb.Append(Resource.message(Resource.CERTIFICATE_CHECKER_FAIL, new String[] { checkResult.getCheckText() }));
                    }
                }

                if (mCertificateStatus == CertificateStatus.PATH_VALIDATION_FAILURE)
                {
                    List<PathValidationRecord> pathValidations = getValidationHistory();

                    if (pathValidations == null || pathValidations.Count == 0)
                        sb.Append(Resource.message(Resource.CERTIFICATE_NO_PATH_FOUND) + ". ");

                    if (pathValidations != null)
                    {
                        foreach (PathValidationRecord pathValidationRecord in pathValidations)
                            sb.Append(pathValidationRecord.getResultCode().getMessage() + ". ");
                        sb.Replace(".. ", ".");
                    }
                }
            }
            return sb.ToString();
        }

        /**
         * \brief
         * İptal Kontrol Sonucu Detay Bilgilerini döner.
         * <p/>
         * <p/>
         * /return const QList<RevocationCheckResult>&
         * İptal Kontrol Sonucu Detay Bilgileri
         */
        public List<RevocationCheckResult> getRevocationCheckDetails()
        {
            return mRevocationCheckDetails;
        }

        /**
         * \brief
         * İptal Kontrol Sonucu Detay Bilgilerini belirler.
         * <p/>
         * \param const QList<RevocationCheckResult> & iIptalKontrolDetaylari
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
            foreach (RevocationCheckResult checkResult in mRevocationCheckDetails)
            {
                if (checkResult.getCheckText().Equals(aKontrolText))
                    return checkResult;
            }
            return null;
        }


        /**
         * \brief
         * İptal Kontrol Sonucu Detay Bilgisi ekler.
         * <p/>
         * \param const RevocationCheckResult & iIKS
         * İptal Kontrol Sonucu
         */
        public void addRevocationCheckDetail(RevocationCheckResult aIKS)
        {
            mRevocationCheckDetails.Add(aIKS);
        }
        /**
         * Add revocation check detail 
         * @param aChecker RevocationChecker
         * @param aResult CheckStatus
         * @param aCheckResult RevokeCheckStatus
         */
        public void addRevocationCheckDetail(RevocationChecker aChecker,
                                     CheckStatus aResult,
                                     RevokeCheckStatus aCheckResult)
        {
            mRevocationCheckDetails.Add(new RevocationCheckResult(aChecker.getCheckText(),
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
            return mOCSPResponseInfos;
        }

        /**
         * Add OCSP response info to OCSP response info list
         * @param aStatusInfo
         */
        public void addOCSPResponseInfo(OCSPResponseStatusInfo ocspResponseStatusInfo)
        {
            if (!mOCSPResponseInfos.Contains(ocspResponseStatusInfo))
                mOCSPResponseInfos.Add(ocspResponseStatusInfo);
        }

        //public OCSPResponseStatusInfo getOCSPResponseInfo()
        //{
        //    return mOCSPResponseInfo;
        //}

        //public void setOCSPResponseInfo(OCSPResponseStatusInfo cevabiDurumu)
        //{
        //    mOCSPResponseInfo = cevabiDurumu;
        //}
        
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
        public void setCertificate(ECertificate sertifika)
        {
            mCertificate = sertifika;
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
        public DateTime? getValidationTime()
        {
            return mValidationTime;
        }

        /**
         * Set validation time which certificate is validated in this time
         * @param aValidationTime
         */
        public void setValidationTime(DateTime? aValidationTime)
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
            mCRLInfoList.Add(aCRLInfo);
        }

        //public CRLStatusInfo getCRLInfo()
        //{
        //    if (mCRLInfoList.Count == 0)
        //        return null;
        //    else
        //        return mCRLInfoList[0];
        //}

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

        //public CRLStatusInfo getDeltaCRLInfo()
        //{
        //    if (mDeltaCRLInfoList.Count == 0)
        //        return null;
        //    else
        //        return mDeltaCRLInfoList[0];
        //}

        /**
         * Add  delta CRL info to delta CRL info list
         * @param aDeltaSilDurumu
         */
        public void addDeltaCRLInfo(CRLStatusInfo aDeltaSilDurumu)
        {
            if(!mDeltaCRLInfoList.Contains(aDeltaSilDurumu))
                mDeltaCRLInfoList.Add(aDeltaSilDurumu);
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
        public void addToValidationHistory(PathValidationRecord aPathValidationRecord)
        {
            mValidationHistory.Add(aPathValidationRecord);
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
      * @param aCerts List<ECertificate>
      */
        public void setTrustedCertificates(List<ECertificate> aCerts)
        {
            mTrustedCertificates = aCerts;
        }

        /**
         * Checks whether certificate is revoked or not.
         * @return True if any path validation result is CERTIFICATE_REVOKED in validation history, false otherwise
         */ 
        public bool isRevoked()
        {
            for (int i = mValidationHistory.Count - 1; i >= 0; i--)
            {
                if (mValidationHistory[i].getResultCode() == PathValidationResult.CERTIFICATE_REVOKED)
                    return true;
            }
            return false;
        }

        /**
         * Convert validation history result to string
         */
        String validationHistory2String()
        {
            if (mValidationHistory.Count == 0)
                return Resource.PVR_NO_PATHFOUND;

            if (isRevoked())
                return Resource.PVR_CERTIFICATE_REVOKED;

            PathValidationResult res = mValidationHistory[0].getResultCode();
            int vhSize = mValidationHistory.Count;

            bool allSame = true;
            for (int i = 1; i < vhSize; i++)
            {
                if (mValidationHistory[i].getResultCode() != res)
                {
                    allSame = false;
                    break;
                }
            }

            if (!allSame)
                return Resource.ZINCIR_SORUNLU; // Different results found then return generic failure
            else
                return res.getMessage();// All results are the same then return the result

        }

        /**
        * Converts CertificateStatusInfo object to string
        */
        public String toString()
        {
            if (mCertificateStatus == CertificateStatus.VALID)
                return Resource.GECERLI;
            else if (mCertificateStatus == CertificateStatus.PATH_VALIDATION_FAILURE)
            {
                return validationHistory2String();
            }
            else if (mCertificateStatus == CertificateStatus.REVOCATION_CHECK_FAILURE)
            {
                return Resource.IPTAL_KONTROLU_SORUNLU;
            }
            else if (mCertificateStatus == CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE)
            {
                return Resource.SERTIFIKA_SORUNLU;
            }
            else if (mCertificateStatus == CertificateStatus.NO_TRUSTED_CERT_FOUND)
            {
                return Resource.NO_TRUSTED_CERT_FOUND;
            }
            else
            {
                return Resource.UNKNOWN;
            }

        }

        public Object Clone()
        {
            return base.MemberwiseClone();
        }

        public List<List<ECertificate>> getIncompletePathList()
        {
            return mIncompletePathList;
        }
        
        public void setIncompletePathList(List<List<ECertificate>> aIPL)
        {
            mIncompletePathList = aIPL;
        }

        /**
         * Convert self control check result to string
         */
        public String checkResultsToString()
        {
            String res = "";
            for (int i = 0; i < mDetails.Count; i++)
            {
                res += "  [-] " + mDetails[i].ToString() + "\n";
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
            for (int i = 0; i < mRevocationCheckDetails.Count; i++)
            {
                res += "  [-] " + mRevocationCheckDetails[i].ToString() + "\n";
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

            String subjectLine = "Sertifika Sahibi: " + mCertificate.getSubject().stringValue(); //.toTitle());
            String issuerLine = "Yayinci         : " + mCertificate.getIssuer().stringValue();  // .toTitle

            String statusLine = "Sertifika Durumu: " + toString();


            String selfChecksStartLine = "Sertifika Kontrol Detaylari: \n";
            String selfCheckLines = selfChecksStartLine + checkResultsToString();

            String revocationCheckStartLine = "Iptal Durumu Kontrol Detaylari: \n";
            String revocationCheckLines = revocationCheckStartLine + revocationCheckResultsToString();

            String smCertStatusStartLine;
            String smCertStatusDetails;

            CertificateStatusInfo pSMCertStatus = getSigningCertficateInfo();

            if (pSMCertStatus != null)
            {
                smCertStatusStartLine = "Imzalayan Sertifika Geçerlilik Detaylari: \n";
                smCertStatusDetails = pSMCertStatus.printDetailedValidationReport();
            }
            else
            {
                smCertStatusStartLine = "Imzalayan Sertifika Gecerlilik Detaylari: \n";
                //smCertStatusDetails = MAKELINE(iLIndentation,iRIndentation,L("İmzalayan Sertifika Bulunamadı"));;
                smCertStatusDetails = "GUVENILIR KOK\n";
            }
            String smCertStatusLines = smCertStatusStartLine + smCertStatusDetails;

            String crlStatusStartLine = "SIL Detaylari:\n";
            String crlStatusDetails = "";

            List<CRLStatusInfo> crlStatusInfos = getCRLInfoList();
            for (int i = 0; i < crlStatusInfos.Count; i++)
            {
                crlStatusStartLine = "SIL Detaylari:\n";
                crlStatusDetails += crlStatusInfos[i].printDetailedValidationReport();
            }
            if (crlStatusDetails == null || crlStatusDetails.Trim().Length == 0)
            {
                crlStatusStartLine = "SIL Detaylari:\n";
                crlStatusDetails = "Yok\n";
            }

            String crlStatusLines = crlStatusStartLine + crlStatusDetails;

            report =	//REPORT_HEADER(iLIndentation,iRIndentation)+
                        subjectLine + "\n" +
                        issuerLine + "\n" +
                        statusLine +
                        "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                        selfCheckLines +
                        "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                        revocationCheckLines +
                        "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                        smCertStatusLines +
                        "\n" + //EMPTYLINE(iLIndentation,iRIndentation)+
                        crlStatusLines;
            //+ REPORT_FOOTER(iLIndentation,iRIndentation)

            return report;

        }
    }
}
