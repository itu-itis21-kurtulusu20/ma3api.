package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityInfoAccessSyntax;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ConnectionUtil;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

/**
 * Finds issuer certificate according to Authority Info Access (AIA) extension
 * information. It only searches in HTTP addresses 
 */
public class CertificateFinderFromHTTP extends CertificateFinderFromAIA {

	private static Logger logger = LoggerFactory.getLogger(CertificateFinderFromHTTP.class);

    /**
     * Sertifikanin HTTP uzerindeki SM Sertifikasi dagitici adreslerini doner
     */
    protected List<String> _getAddresses(ECertificate aCertificate) {
        EAuthorityInfoAccessSyntax aia =
                    aCertificate.getExtensions().getAuthorityInfoAccessSyntax();

        return aia == null ? null : aia.getHttpAddresses();
    }

    /**
     * verilen HTTP adresinden Sertifika bilgisini okur
     */
    protected ECertificate _getCertificate(String aAddress) 
    {
    	try 
		{
    		String timeOut = mParameters.getParameterAsString(PARAM_TIMEOUT);
			byte [] certBytes = ConnectionUtil.urldenVeriOku(aAddress, timeOut);
			ECertificate certificate = new ECertificate(certBytes);
			return certificate;
		}
		catch (ESYAException e) 
		{
			logger.debug("ECertificate Asn1 decode error", e);
		}
		return null;
    }

}
