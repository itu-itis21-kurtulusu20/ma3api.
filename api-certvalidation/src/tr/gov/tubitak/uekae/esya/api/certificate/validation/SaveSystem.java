package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.CRLSaver;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.CertificateSaver;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.OCSPResponseSaver;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.MultiMap;

import java.util.ArrayList;
import java.util.List;

/**
 * The container class for certificate and CRL savers. 
 */
public class SaveSystem {

	private static Logger logger = LoggerFactory.getLogger(SaveSystem.class);
	
    private List<CertificateSaver> mCertificateSavers = new ArrayList<CertificateSaver>();
    private List<CRLSaver> mCRLSavers = new ArrayList<CRLSaver>();
    private List<OCSPResponseSaver> mOCSPResponseSavers = new ArrayList<OCSPResponseSaver>();

    private MultiMap<EIssuerAndSerialNumber, EOCSPResponse> mRegisteredOCSPs = new MultiMap<EIssuerAndSerialNumber, EOCSPResponse>();
    private MultiMap<EIssuerAndSerialNumber,ECRL> mRegisteredCRLs = new MultiMap<EIssuerAndSerialNumber, ECRL>();
    
    
    public SaveSystem()
    {
    	try
    	{
    		LV.getInstance().checkLD(Urunler.SERTIFIKADOGRULAMA);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
    }

    /**
     * Sertifika Kaydedicileri doner
     */
    public List<CertificateSaver> getCertificateSavers() {
        return mCertificateSavers;
    }

    /**
     * Sertifika Saver ekler
     */
    public void addCertificateSaver(CertificateSaver aCertificateSaver) {
        mCertificateSavers.add(aCertificateSaver);
    }

    /**
     * SİL Kaydedicileri döner
     */
    public List<CRLSaver> getCRLSavers() {
        return mCRLSavers;
    }

    /**
     * SİL Saver ekler
     */
    public void addCRLSaver(CRLSaver aCRLSaver) {
        mCRLSavers.add(aCRLSaver);
    }

    /**
     * OCSP kaydedicileri doner
     */
    public List<OCSPResponseSaver> getOCSPResponseSavers() {
        return mOCSPResponseSavers;
    }

    /**
     * OCSP Saver ekler
     */
    public void addOCSPResponseSaver(OCSPResponseSaver aOCSPResponseSaver) {
        mOCSPResponseSavers.add(aOCSPResponseSaver);
    }

    
    public void registerOCSP(ECertificate aCert, EOCSPResponse aOCSPResponse)
    {
    	if(aOCSPResponse != null)
    		mRegisteredOCSPs.insert(new EIssuerAndSerialNumber(aCert), aOCSPResponse);
    }

    public void registerCRL(ECertificate aCert, ECRL aCRL)
    {
    	if(aCRL != null)
    		mRegisteredCRLs.insert(new EIssuerAndSerialNumber(aCert), aCRL);
    }

    public void processOCSPs(ECertificate aCert)
    {
        List<EOCSPResponse> ocspList = mRegisteredOCSPs.get(new EIssuerAndSerialNumber(aCert));

        if (ocspList!=null)
        for (int o = 0 ; o<ocspList.size();o++)
        {
            for (int i = 0 ; i <mOCSPResponseSavers.size() ; i++)
            {
                if (mOCSPResponseSavers.get(i) != null) {
                    try {
                    mOCSPResponseSavers.get(i).addOCSP(ocspList.get(o), aCert);
                    } catch (Exception x){
                        logger.error("Error occurred saving OCSP Response", x);
                    }
                }
            }
        }
        cleanOCSPs(aCert);
    }

    void processCRLs(ECertificate aCert)
    {
        List<ECRL> crlList = mRegisteredCRLs.get(new EIssuerAndSerialNumber(aCert));

        if (crlList!=null)
        for (int c = 0 ; c<crlList.size();c++)
        {
            for (int i = 0 ; i <mCRLSavers.size() ; i++)
            {
                try {
                if (mCRLSavers.get(i)!=null)
                    mCRLSavers.get(i).addCRL(crlList.get(c));
                } catch (ESYAException x){
                    logger.error("Error occurred saving CRL", x);
                }
            }
        }
        cleanCRLs(aCert);
    }

    public void processRegisteredItems(ECertificate aCert)
    {
        processOCSPs(aCert);
        processCRLs(aCert);
    }
    
    public void processRegisteredItems(CertificateStatusInfo aCsi)
    {
    	if(aCsi == null)
    		return;
    	
  		processRegisteredItems(aCsi.getCertificate());
    	processRegisteredItems(aCsi.getSigningCertficateInfo());
    }

    public void cleanOCSPs(ECertificate aCert)
    {
        mRegisteredOCSPs.remove(new EIssuerAndSerialNumber(aCert));
    }

    public void cleanCRLs(ECertificate aCert)
    {
        mRegisteredCRLs.remove(new EIssuerAndSerialNumber(aCert));
    }

    public void cleanRegisteredItems(ECertificate aCert)
    {
        cleanOCSPs(aCert);
        cleanCRLs(aCert);
    }
    
    public void cleanRegisteredItems(CertificateStatusInfo aCsi)
    {
    	if(aCsi == null)
    		return;
    	
    	cleanRegisteredItems(aCsi.getCertificate());
    	cleanRegisteredItems(aCsi.getSigningCertficateInfo());
    }

}
