package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLDistributionPoints;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.BaglantiUtil;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds CRL for a given certificate according to the CRL Distribution Points
 * extension information in the certificate.It only searches in LDAP addresses
 * specified in the CDP extension.
 *
 * @author IH
 */
public class CRLFinderfromLDAP extends CRLFinderFromCDP
{
	private static Logger logger = LoggerFactory.getLogger(CRLFinderfromLDAP.class);
    private static String DIZIN_TIPI = "dizintipi";

    /**
     * Sertifikanın LDAP üzerindeki SİL dağıtıcı adreslerini döner
     */
    protected List<String> _getAddresses(ECertificate aCertificate)
    {
    	ECRLDistributionPoints crlDistributionPoints = aCertificate.getExtensions().getCRLDistributionPoints(); 
    	if(crlDistributionPoints == null)
    		return new ArrayList<String>();
        return crlDistributionPoints.getLdapAddresses();
    }

    /**
     * Verilen LDAP adresinden SİL bilgisini okur
     */
    ECRL _getCRL(String aAddress)
    {
    	logger.debug("Getting CRL from address: " + aAddress);
        String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
        byte [] crlData = BaglantiUtil.dizindenVeriOku(aAddress, dizinTipi);
        if(crlData != null)
        {
        	ECRL crl = null;
			try 
			{
				crl = new ECRL(crlData);
			} 
			catch (ESYAException e) 
			{
				logger.error("CRL Asn1 decode error", e);
			}
        	return crl;
        }
        else
        {
        	logger.error("CRL can not be read from LDAP");
        }
        return null;
    }

    /**
     * Sertifikanın LDAP üzerindeki SİL dağıtıcısını döner
     */
    protected EName _getCRLIssuer(ECertificate aCertificate)
    {
        List<EName> issuers = aCertificate.getExtensions().getCRLDistributionPoints().getLdapIssuers();
        if (issuers.size() > 0) {
            return issuers.get(0);
        }
        return null;
    }

}
