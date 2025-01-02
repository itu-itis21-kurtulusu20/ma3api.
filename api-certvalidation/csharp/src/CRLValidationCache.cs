using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Stores CRL Validation Results of previously validated CRLs for possible reuse 
     */
    public class CRLValidationCache
    {
        public static readonly int CAPACITY = 10000;

        protected List<CRLStatusInfo> mCheckResults = new List<CRLStatusInfo>();
        protected List<ECRL> mCheckedCRLs = new List<ECRL>();


        /**
         * SİL Doğrulama Sonuçlarını liste olarak döner
         */
        public List<CRLStatusInfo> getCheckResults()
        {
            return mCheckResults;
        }

        /**
         * Doğrulama Sonucu bulunan SİL'leri liste olarak döner
         */
        public List<ECRL> getCheckedCRLs()
        {
            return mCheckedCRLs;
        }

        /**
         * Doğrulama Sonucu bulunan SİL'leri liste olarak döner
         */
        public List<ECRL> getCheckedCRLs(CRLStatus aCRLStatus)
        {
            List<ECRL> crlList = new List<ECRL>();
            foreach (CRLStatusInfo crlStatusInfo in mCheckResults)
            {
                if ((crlStatusInfo != null) && (crlStatusInfo.getCRLStatus().Equals(aCRLStatus)))
                    crlList.Add(crlStatusInfo.getCRL());
            }
            return crlList;
        }


        /**
         * Verilen SİL'in varsa doğrulama sonucunu döner
         */
        public CRLStatusInfo getCheckResult(ECRL aCRL)
        {
            foreach (CRLStatusInfo crlStatusInfo in mCheckResults)
            {
                if (crlStatusInfo.getCRL().Equals(aCRL))
                    return crlStatusInfo;
            }
            return null;
        }

        /**
         * Yeni bir SİL Doğrulama Sonucu ekler
         */
        public void addCheckResult(CRLStatusInfo aCRLStatusInfo)
        {
            // Kontrol Sonucu NULL ise veya daha önce eklenmiş ise bir şey yapmayalım.
            if ((aCRLStatusInfo == null) || (getCheckResult(aCRLStatusInfo.getCRL()) != null))
                return;

            if (mCheckResults.Count >= CAPACITY)
            {
                // Kapasite dolmussa en eski kontrol sonucu silinir.
                mCheckResults.RemoveAt(0);
            }

            if (mCheckedCRLs.Count >= CAPACITY)
            {
                // Kapasite dolmussa en eski kontrol sonucu silinir.
                mCheckedCRLs.RemoveAt(0);
            }

            mCheckResults.Add(aCRLStatusInfo);
            mCheckedCRLs.Add(aCRLStatusInfo.getCRL());
        }

        /**
         * KontrolSonuçları listesini temizler
         */
        public void clearCheckResults()
        {
            mCheckResults.Clear();
            mCheckedCRLs.Clear();
        }
    }
}
