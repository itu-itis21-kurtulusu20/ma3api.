package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityInfoAccessSyntax;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.BaglantiUtil;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

/**
 * Finds issuer certificate according to Authority Info Access (AIA) extension
 * information. It only searches in LDAP addresses 
 * @author IH
 */
public class CertificateFinderFromLDAP extends CertificateFinderFromAIA
{
	private static Logger logger = LoggerFactory.getLogger(CertificateFinderFromLDAP.class);

    private static final String DIZIN_TIPI = "dizintipi";

    /**
     * Sertifikanin LDAP uzerindeki SM Sertifikasi dagitici adreslerini doner
     */
    protected List<String> _getAddresses(ECertificate aSertifika)
    {
        EAuthorityInfoAccessSyntax aia = aSertifika.getExtensions().getAuthorityInfoAccessSyntax();
        if (aia != null) {
            return aia.getLdapAddresses();
        }
        return null;
    }

    /**
     * verilen LDAP adresinden Sertifika bilgisini okur
     */
    protected ECertificate _getCertificate(String aAdres)
    {
        String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
        byte [] certData =  BaglantiUtil.dizindenVeriOku(aAdres, dizinTipi);
        if(certData != null)
        {
        	ECertificate cert = null;
			try 
			{
				cert = new ECertificate(certData);
			} 
			catch (ESYAException e) 
			{
				logger.debug("Certificate Asn1 decode error", e);
			}
        	return cert;
        }
        else
        {
        	logger.debug("LDAP'tan veri okunamadı.");
        }
        return null;
    }

}
