package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.BaglantiUtil;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds CRL for a given certificate from the given LDAP address 
 */
public class CRLFinderFromLDAPAddress extends CRLFinder
{
	private static Logger logger = LoggerFactory.getLogger(CRLFinderFromLDAPAddress.class);

    public static final String DIZIN_TIPI = "dizintipi";
    public static final String DIZIN_ADRESI = "dizinadresi";

    /**
     * Sertifikanın verilen LDAP adresindeki SİL ini bulur
     */
    protected List<ECRL> _findCRL(ECertificate aCertificate) throws ESYAException
    {
        String dizinTipi = mParameters.getParameterAsString(DIZIN_TIPI);
        String adres = mParameters.getParameterAsString(DIZIN_ADRESI);
        
        logger.debug("Getting CRL from address: " + adres);

        byte[] crlBytes = BaglantiUtil.dizindenVeriOku(adres, dizinTipi);

        ECRL crl = new ECRL(crlBytes);
        List<ECRL> crlList = new ArrayList<ECRL>(1);
        crlList.add(crl);

        return crlList;
    }

}
