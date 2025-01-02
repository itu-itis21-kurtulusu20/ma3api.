package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StoreFinder;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreOCSPOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.OCSPSearchTemplate;

import java.util.Calendar;
import java.util.Date;

// todo bu class Liste dönmeli, depo gelişmeli.

/**
 * Finds OCSP Response from the Local Certificate Store
 * @author IH
 */
public class OCSPResponseFinderFromECertStore extends OCSPResponseFinder
{
    private static Logger logger = LoggerFactory.getLogger(OCSPResponseFinderFromECertStore.class);

	public OCSPResponseFinderFromECertStore()
	{
		super();
	}

    protected EOCSPResponse _findOCSPResponse(ECertificate aCertificate, ECertificate aIssuerCert)
	{
    	CertStore certStore = null;
        EBasicOCSPResponse ocsp = null;
        try 
        {
        	 certStore = StoreFinder.createCertStore(getParameters(), mParentSystem.getDefaultStorePath());
             CertStoreOCSPOps certStoreOcspOps = new CertStoreOCSPOps(certStore);

             OCSPSearchTemplate ocspSearchParams = new OCSPSearchTemplate();

             //ocspSearchParams.setIssuer(aCertificate.getCRLIssuer()); // Butun CRL leri almamak için bir optimizasyon

             boolean historicValidation = (mParentSystem!=null) && (mParentSystem.getBaseValidationTime()!=null);

             if (historicValidation)
             {
                 if (mParentSystem==null) return null;// Parent yoksa çalışamam..

                 Calendar baseTime	= mParentSystem.getBaseValidationTime();
                 Calendar lastRevocTime = (mParentSystem.getLastRevocationTime()==null) ? aCertificate.getNotAfter() : mParentSystem.getLastRevocationTime();
                 ocspSearchParams.setProducedAtAfter(baseTime.getTime());
                 ocspSearchParams.setProducedAtBefore(lastRevocTime.getTime());
             }
             else
             {
                 ocspSearchParams.setProducedAtAfter(new Date());
             }
            ocsp = certStoreOcspOps.listOCSPResponses(ocspSearchParams);
        }
        catch (CertStoreException aEx)
        {
            logger.error("Depodan ocsp cevabi alinirken hata oluştu", aEx);
        }
        finally
        {
        	try
        	{
				certStore.closeConnection();
			} 
        	catch (CertStoreException e) 
        	{
        		logger.error("Connection couldn't closed", e);
			}
        }
        if(ocsp==null)
            return null;
        return EOCSPResponse.getEOCSPResponse(ocsp);
	}

}
