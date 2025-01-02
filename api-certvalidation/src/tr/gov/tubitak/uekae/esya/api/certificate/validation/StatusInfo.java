package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Doğrulama işlemlerinin sonuç bilgilerini tutan veri yapısı
 * Farklı nesneler için doğrulama işlemleri sonuçları için bu classtan extend eden
 * classlar yazılmalıdır.</p>
 * @author IH
 */
public class StatusInfo implements Serializable
{
    // Her kontrolcu için kontrolcu durumunu tutan yapı
    protected List<CheckResult> mDetails = new ArrayList<CheckResult>();

    // Duruma sahip nesneyi imzalayan sertifikanın durumu
    private CertificateStatusInfo mSigningCertficateInfo;

    // kontrolün yapıldığı tarih bilgisi
    private Date mCheckDate;

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
        mDetails.add(aCheckResult);
    }

    public void addDetail(Checker aChecker, CheckStatus aCheckStatus, boolean aSuccessful)
    {
        mDetails.add(new CheckResult(aChecker.getCheckText(), aCheckStatus.getText(), aCheckStatus, aSuccessful));
    }

    public CheckResult getDetail(Checker aChecker)
    {
        for (CheckResult sonuc : mDetails) {
            if (sonuc.getChecker().getClass().equals(aChecker.getClass())) {
                return sonuc;
            }
        }
        return null;
    }

    public Date getCheckDate()
    {
        return mCheckDate;
    }

    public void setCheckDate(Date aKontrolTarihi)
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

        while (pSDB.getSigningCertficateInfo()!=null) {
            pSDB = pSDB.getSigningCertficateInfo();
        }

        return pSDB.getCertificate();
    }
}
