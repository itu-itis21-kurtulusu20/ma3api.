package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores CRL Validation Results of previously validated CRLs for possible reuse 
 */
public class CRLValidationCache
{
    public static final int CAPACITY = 10000;

    protected List<CRLStatusInfo> mCheckResults = new ArrayList<CRLStatusInfo>();
    protected List<ECRL> mCheckedCRLs = new ArrayList<ECRL>();


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
        List<ECRL> crlList = new ArrayList<ECRL>();
        for (CRLStatusInfo crlStatusInfo : mCheckResults) {
            if ((crlStatusInfo != null) && (crlStatusInfo.getCRLStatus().equals(aCRLStatus)))
                crlList.add(crlStatusInfo.getCRL());
        }
        return crlList;
    }


    /**
     * Verilen SİL'in varsa doğrulama sonucunu döner
     */
    public CRLStatusInfo getCheckResult(ECRL aCRL)
    {
        for (CRLStatusInfo crlStatusInfo : mCheckResults) {
            if (crlStatusInfo.getCRL().equals(aCRL))
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

        if (mCheckResults.size() >= CAPACITY) {
            // Kapasite dolmussa en eski kontrol sonucu silinir.
            mCheckResults.remove(0);
        }

        if (mCheckedCRLs.size() >= CAPACITY) {
            // Kapasite dolmussa en eski kontrol sonucu silinir.
            mCheckedCRLs.remove(0);
        }

        mCheckResults.add(aCRLStatusInfo);
        mCheckedCRLs.add(aCRLStatusInfo.getCRL());
    }

    /**
     * KontrolSonuçları listesini temizler
     */
    public void clearCheckResults()
    {
        mCheckResults.clear();
        mCheckedCRLs.clear();
    }
}
