package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Base class for finding trusted certificates.</p>
*
 * @author IH
 */
public abstract class TrustedCertificateFinder extends Finder
{
    protected static Logger logger = LoggerFactory.getLogger(TrustedCertificateFinder.class);

	public static final String PARAM_SECURITYLEVEL = "securitylevel";
    public static final String PARAM_ONLYSELFSIGNED = "onlyselfsigned";

	public static final String PARAM_PERSONAL = "personal";
	public static final String PARAM_ORGANIZATIONAL = "organizational";
	public static final String PARAM_LEGAL = "legal";

    protected abstract List<ECertificate> _findTrustedCertificate();

    public List<ECertificate> findTrustedCertificate() {
        List<ECertificate> eCertificates = _findTrustedCertificate();
        return filterBySelfSigningCertificate(eCertificates);
    }

    /**
     * @param eCertificates Default list of certificates in case the flag "onlyselfsigned" is set to `false` in the configuration file.
     * @return Inferred list of certificates.
     */
    private List<ECertificate> filterBySelfSigningCertificate(List<ECertificate> eCertificates) {
        boolean onlySelfSigned = mParameters.getParameterBoolean(PARAM_ONLYSELFSIGNED, true);

        // if not "only-self-signed": return all
        if (!onlySelfSigned) {
            return eCertificates;
        }
        // else

        // filter self-issued certificates
        List<ECertificate> certList = new ArrayList<ECertificate>();
        for (ECertificate cert : eCertificates) {
            if (cert.isSelfIssued())
                certList.add(cert);
        }
        return certList;
    }

    public List<GuvenlikSeviyesi> getGuvenlikSeviyesi()
    {
    	String guvenlikSeviyesi = mParameters.getParameterAsString(PARAM_SECURITYLEVEL);
        if (guvenlikSeviyesi==null || guvenlikSeviyesi.trim().length()==0)
            guvenlikSeviyesi = "legal";
        List<GuvenlikSeviyesi> result = new ArrayList<GuvenlikSeviyesi>(3);
    	try
    	{
            String[] arr = guvenlikSeviyesi.split(",");
            for (String levelStr : arr)
			    result.add(GuvenlikSeviyesi.getNesne(levelStr.trim()));
            return result;
		}
    	catch (Exception e)
		{
            logger.error("Error in TrustedCertificateFinder", e);
    		return Arrays.asList(GuvenlikSeviyesi.LEGAL);
		}
    }
}
