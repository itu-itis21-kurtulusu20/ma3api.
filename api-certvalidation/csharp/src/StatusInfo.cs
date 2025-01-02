using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * <p>Doğrulama işlemlerinin sonuç bilgilerini tutan veri yapısı
     * Farklı nesneler için doğrulama işlemleri sonuçları için bu classtan extend eden
     * classlar yazılmalıdır.</p>
     * <p>Bu yapıda doğrulama işlemine giren tüm {@link dogrulama.inspect.Checker} lerin sonuçları
     * {@link dogrulama.inspect.CheckStatus} yapısında tutulmaktadır.
     * Ayrıca kontrolün yapıldığı tarih ve kontrolü yapılan nesneyi
     * imzalayan sertifikanın durumuna da bu yapı içinden ulaşılabilir.
     * </p>
     * @author IH
     */
    [Serializable]
    public class StatusInfo
    {
        // Her kontrolcu için kontrolcu durumunu tutan yapı
        protected List<CheckResult> mDetails = new List<CheckResult>();

        // Duruma sahip nesneyi imzalayan sertifikanın durumu
        private CertificateStatusInfo mSigningCertficateInfo;

        // kontrolün yapıldığı tarih bilgisi
        private DateTime? mCheckDate;

        public List<CheckResult> getDetails()
        {
            return mDetails;
        }

        public void setDetails(List<CheckResult> aDurumAyrintilari)
        {
            mDetails = aDurumAyrintilari;
        }

        public void addDetail(CheckResult aCheckResult)
        {
            mDetails.Add(aCheckResult);
        }

        public void addDetail(Checker aChecker, CheckStatus aCheckStatus, bool aSuccessful)
        {
            mDetails.Add(new CheckResult(aChecker.getCheckText(), aCheckStatus.getText(), aCheckStatus, aSuccessful));
        }

        /*public CheckResult getDetail(Checker aChecker)
        {
            foreach (CheckResult sonuc in mDetails)
            {
                //if (sonuc.getChecker().getClass().equals(aChecker.getClass())) {
                if (sonuc.getChecker().GetType().Equals(aChecker.GetType()))
                {
                    return sonuc;
                }
            }
            return null;
        }*/

        public DateTime? getCheckDate()
        {
            return mCheckDate;
        }

        public void setCheckDate(DateTime aKontrolTarihi)
        {
            mCheckDate = aKontrolTarihi;
        }

        public CertificateStatusInfo getSigningCertficateInfo()
        {
            return mSigningCertficateInfo;
        }

        public void setSigningCertficateInfo(CertificateStatusInfo aImzalayanSertifikaDurumu)
        {
            mSigningCertficateInfo = aImzalayanSertifikaDurumu;
        }

        public ECertificate getTrustCertificate()
        {
            CertificateStatusInfo pSDB = mSigningCertficateInfo;

            while (pSDB.getSigningCertficateInfo() != null)
            {
                pSDB = pSDB.getSigningCertficateInfo();
            }

            return pSDB.getCertificate();
        }
    }
}
