package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.OCSPClient;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;

import java.util.List;

/**
 * Finds OCSP Response from the OCSP Servers specified in the
 * AuthorityInfoAccess extension information in the certificate
 *
 * @author IH
 */
public class OCSPResponseFinderFromAIA extends OCSPResponseFinder
{
    private static Logger logger = LoggerFactory.getLogger(OCSPResponseFinderFromAIA.class);
    private static DigestAlg digestAlgForOcspFinder;
    
    /**
    * Verilen Sertifika için AuthorityInfoAccess eklentisinden OCSP Cevabı bulur
    */
    protected EOCSPResponse _findOCSPResponse(ECertificate aCertificate, ECertificate aIssuerCert)
    {
        if (logger.isDebugEnabled()){
            logger.debug("Find ocsp response from AIA for : '"+aCertificate.getSubject().stringValue() + "'");
        }
    	List<String> addresses = aCertificate.getOCSPAdresses();
    	if(addresses!=null && !addresses.isEmpty() )
    	{
	    	String address = aCertificate.getOCSPAdresses().get(0);
            if (logger.isDebugEnabled()) {
                logger.debug("found address : " + address);
            }
	        try
	        {
                Chronometer c = new Chronometer("Find OCSP Response");
                c.start();

	        	OCSPClient ocspClient = new OCSPClient(address);
	        	if(digestAlgForOcspFinder != null)
	        		ocspClient.setDigestAlgForOcspRequest(digestAlgForOcspFinder);
	        	
	            ocspClient.openConnection(mParameters.getParameterAsString(PARAM_TIMEOUT));
	            ocspClient.queryCertificate(aCertificate, aIssuerCert);
	            EOCSPResponse response = ocspClient.getOCSPResponse();

                ocspClient.closeConnection();

                logger.info(c.stopSingleRun());

                if (response.getResponseStatus() == OCSPResponseStatus._SUCCESSFUL)
                {
                    if (mParentSystem!=null)
                        mParentSystem.getSaveSystem().registerOCSP(aCertificate,response);
                        //Find::saveOCSPResponse(mParentSystem->getSaveSystem(),response);
                    return response;
                }
                else
                {
                	logger.error("OCSPResponseStatus is not _SUCCESSFUL. It is " + response.getResponseStatus());
                }
	        }
	        catch(Exception x)
	        {
	            logger.error("OCSP indirmede hata oluştu. Address: " + address, x);
	        }
    	}
        else
        {
            logger.debug("No OCSP addresses found in AIA");
        }
        return null;
    }
    
	public void setDigestAlgForOcspFinder(DigestAlg digestAlg){
		digestAlgForOcspFinder = digestAlg;
	}
}
