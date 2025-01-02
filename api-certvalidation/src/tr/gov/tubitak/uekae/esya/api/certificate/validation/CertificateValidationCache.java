package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores Certificate Validation Results of previously validated Certificates
 * for possible reuse
 */
public class CertificateValidationCache
{
    private final static int CAPACITY = 100000;

    public List<CertificateStatusInfo> mCheckResults = new ArrayList<CertificateStatusInfo>();
    public List<ECertificate> mCheckedCertificates = new ArrayList<ECertificate>();


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
        for (CertificateStatusInfo pSDB : mCheckResults) {
            if (pSDB.getCertificate().equals(aCertificate))
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
    public void addCheckResult(CertificateStatusInfo aCSI, boolean aRecursive)
    {
        // Kontrol Sonucu NULL ise veya daha önce eklenmiş ise veya NOT_CHECKED ise bir şey yapmayalım.
        if ((aCSI == null) || (getCheckResult(aCSI.getCertificate()) != null) || (aCSI.getCertificateStatus() == CertificateStatus.NOT_CHECKED))
            return;

        if (mCheckResults.size() >= CAPACITY) {
            // Kapasite dolmussa en eski kontrol sonucu silinir.
            CertificateStatusInfo pSDB = mCheckResults.remove(0);
        }

        if (mCheckedCertificates.size()>= CAPACITY)
        {
            mCheckedCertificates.remove(0);
        }


        mCheckedCertificates.add(aCSI.getCertificate());

        if (aRecursive && aCSI.getSigningCertficateInfo()!=null && getCheckResult(aCSI.getSigningCertficateInfo().getCertificate())==null)
                addCheckResult(aCSI.getSigningCertficateInfo().clone(), aRecursive);

        mCheckResults.add(aCSI);
    }

    /**
     * \brief
     * Sertifika Dogrulama Sonuçları listesini temizler.
     */
    public void clearCheckresults()
    {
        mCheckResults.clear();
        mCheckedCertificates.clear();
    }
}
