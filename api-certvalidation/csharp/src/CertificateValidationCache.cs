using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Stores Certificate Validation Results of previously validated Certificates
     * for possible reuse
     */
    public class CertificateValidationCache
    {
        private static readonly int CAPACITY = 100000;

        public List<CertificateStatusInfo> mCheckResults = new List<CertificateStatusInfo>();
        public List<ECertificate> mCheckedCertificates = new List<ECertificate>();


        /**
         * Hafızada bulunan sertifika doğrulama sonuçlarını döner
         */
        public List<CertificateStatusInfo> getCheckResults()
        {
            return mCheckResults;
        }

        /**
         * Hafazada doğrulama sonuçları bulunun sertifikaları döner
         */
        public List<ECertificate> getCheckedCertificates()
        {
            return mCheckedCertificates;
        }

        /**
         * Verilen sertifikanın varsa doğrulama sonucunu döner
         */
        public CertificateStatusInfo getCheckResult(ECertificate aCertificate)
        {
            foreach (CertificateStatusInfo pSDB in mCheckResults)
            {
                if (pSDB.getCertificate().Equals(aCertificate))
                    return pSDB;
            }
            return null;
        }

        /**
         * Adds the given Certificate Validation Result to the Certificate Validation Cache
         */
        public void addCheckResult(CertificateStatusInfo aCSI)
        {
            addCheckResult(aCSI, false);
        }

        /**
         * Adds the given Certificate Validation Result to the Certificate Validation Cache
         */
        public void addCheckResult(CertificateStatusInfo aCSI, bool aRecursive)
        {
            // Kontrol Sonucu NULL ise veya daha önce eklenmiş ise veya NOT_CHECKED ise bir şey yapmayalım.
            if ((aCSI == null) || (getCheckResult(aCSI.getCertificate()) != null) || (aCSI.getCertificateStatus() == CertificateStatus.NOT_CHECKED))
                return;

            if (mCheckResults.Count >= CAPACITY)
            {
                // Kapasite dolmussa en eski kontrol sonucu silinir.
                //CertificateStatusInfo pSDB = mCheckResults.remove(0);
                CertificateStatusInfo pSDB = mCheckResults[0];
                mCheckResults.RemoveAt(0);
            }

            if (mCheckedCertificates.Count >= CAPACITY)
            {
                mCheckedCertificates.RemoveAt(0);
            }

            mCheckedCertificates.Add(aCSI.getCertificate());

            if (aRecursive && aCSI.getSigningCertficateInfo() != null && getCheckResult(aCSI.getSigningCertficateInfo().getCertificate()) == null)
                addCheckResult((CertificateStatusInfo)aCSI.getSigningCertficateInfo().Clone(), aRecursive); //todo bu clone metodu dogru mu?!!! 11.02.2011

            mCheckResults.Add(aCSI);
        }

        /**
         * \brief
         * Sertifika Dogrulama Sonuçları listesini temizler.
         */
        public void clearCheckresults()
        {
            mCheckResults.Clear();
            mCheckedCertificates.Clear();
        }
    }
}
