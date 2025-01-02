using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl
{
    /**
     * The structure for CRL Validation Result
     * @author IH
     */
    [Serializable]
    public class CRLStatusInfo : StatusInfo, ICloneable
    {
        private ECRL mCRL;

        private CRLStatus mCRLStatus;

        public CRLStatusInfo()
        {
            mCRLStatus = CRLStatus.NOT_CHECKED;
        }

        public CRLStatusInfo(ECRL aCRL)
        {
            mCRL = aCRL;
            mCRLStatus = CRLStatus.NOT_CHECKED;
        }

        public CRLStatus getCRLStatus()
        {

            return mCRLStatus;
        }

        public void setCRLStatus(CRLStatus aCRLStatus)
        {
            mCRLStatus = aCRLStatus;
        }

        public ECRL getCRL()
        {
            return mCRL;
        }

        public void setCRL(ECRL aCRL)
        {
            mCRL = aCRL;
        }


        public Object /*CRLStatusInfo*/ Clone()
        {

            return (CRLStatusInfo)base.MemberwiseClone();

        }

        String checkResultsToString()
        {
            String res = "";
            for (int i = 0; i < mDetails.Count; i++)
            {
                res += "  [-] " + mDetails[i].ToString() + "\n";
            }
            //res.chop(1);
            return res;
        }

        public String printDetailedValidationReport()
        {
            String issuerLine = "SİL Yayıncısı : " + mCRL.getTBSCertList().getIssuer().getCommonNameAttribute() + "\n"; //.toTitle()));
            String crlNumberLine = "SİL Numarası  : " + mCRL.getCRLNumber() + "\n";
            String statusLine = "SİL Durumu    : " + mCRLStatus.ToString() + "\n";

            String selfChecksStartLine = "SİL Kontrol Detayları:\n";
            String selfCheckLines = selfChecksStartLine + checkResultsToString();

            String smCertStatusStartLine, smCertStatusDetails;

            CertificateStatusInfo pSMCertStatus = getSigningCertficateInfo();

            if (pSMCertStatus != null)
            {
                smCertStatusStartLine = "İmzalayan Sertifika Geçerlilik Detayları: \n";
                smCertStatusDetails = pSMCertStatus.printDetailedValidationReport();
            }
            else
            {
                smCertStatusStartLine = "İmzalayan Sertifika Geçerlilik Detayları: \n";
                //smCertStatusDetails = MAKELINE(iLIndentation,iRIndentation,L("İmzalayan Sertifika Bulunamadı"));;
                smCertStatusDetails = "GÜVENİLİR KÖK\n";
            }
            String smCertStatusLines = smCertStatusStartLine + smCertStatusDetails;


            String report =	//REPORT_HEADER(iLIndentation,iRIndentation)+
                                issuerLine +
                                crlNumberLine +
                                statusLine +
                                "\n" +
                                selfCheckLines +
                                "\n" +
                                smCertStatusLines;

            return report;
        }

    }
}
