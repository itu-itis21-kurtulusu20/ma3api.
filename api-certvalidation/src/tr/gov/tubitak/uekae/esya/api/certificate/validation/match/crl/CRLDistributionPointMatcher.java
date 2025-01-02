package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;

import tr.gov.tubitak.uekae.esya.api.asn.x509.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

/**
 * Matches a Certificate and a CRL according to the CRLDistributionPoint
 * extension information. 
 */
public class CRLDistributionPointMatcher extends CRLMatcher
{
    private static final Logger logger = LoggerFactory.getLogger(CRLDistributionPointMatcher.class);

    /**
     * Sertifika CDP eklentisi ile Sil Issuing Distribution Point eklentisini eşleştirir
     */
    protected boolean _matchCRL(ECertificate aCertificate, ECRL aCRL)
    {
        //String subject = aSertifika.getSubject().stringValue();
        //String crlIssuer = aCRL.getIssuer().stringValue();

        EIssuingDistributionPoint idp = aCRL.getCRLExtensions().getIssuingDistributionPoint();
        if (idp == null) {
            logger.debug("Silde Issuing Distribution Point uzantısı yok");
            return true;
        }

        ECRLDistributionPoints cdp = aCertificate.getExtensions().getCRLDistributionPoints();
        EDistributionPointName idpn = idp.getDistributionPoint();

        if (cdp == null) {
            logger.debug("Sertifikada CRL Distribution Point uzantısı yok");
            if (idpn != null) {
                if (idpn.getType() == DistributionPointName._FULLNAME)
                    return (EGeneralNames.hasMatch(idpn.getFullName(), aCRL.getIssuer()));
                else {
                    EName idpFullName = aCRL.getIssuer().clone();
                    idpFullName.appendRDN(idpn.getNameRelativeToCRLIssuer());
                    return idpFullName.equals(aCertificate.getIssuer());
                }
            }
            else return true;
        }

        if (idpn == null || (cdp.getCRLDistributionPointCount()==0) || (cdp.getCRLDistributionPoint(0).getDistributionPoint()==null))
            return true;

        boolean matchFound = false;

        EName cdpFullName, idpFullName;

        for (int i=0; i< cdp.getCRLDistributionPointCount(); i++) {

            ECRLDistributionPoint dp = cdp.getCRLDistributionPoint(i);

            EDistributionPointName dpn = dp.getDistributionPoint();

            if ((dpn.getType() == DistributionPointName._FULLNAME) && (idpn.getType() == DistributionPointName._FULLNAME)) {
                matchFound = (dpn.getFullName().equals(idpn.getFullName()));
            }
            else if (dpn.getType() == DistributionPointName._NAMERELATIVETOCRLISSUER) {
                if (dp.getCRLIssuer() != null) {
                    cdpFullName = dp.getCRLIssuer();
                }
                else {
                    cdpFullName = aCertificate.getIssuer().clone();
                }
                cdpFullName.appendRDN(dpn.getNameRelativeToCRLIssuer());

                if (idpn.getType() == DistributionPointName._NAMERELATIVETOCRLISSUER) {
                    idpFullName = aCRL.getIssuer().clone();
                    idpFullName.appendRDN(idpn.getNameRelativeToCRLIssuer());

                    matchFound = idpFullName.equals(cdpFullName);
                }
                else {
                    matchFound = EGeneralNames.hasMatch(idpn.getFullName(), cdpFullName);
                }
            }
            else {
                idpFullName = aCRL.getIssuer().clone();
                idpFullName.appendRDN(idpn.getNameRelativeToCRLIssuer());

                matchFound = (EGeneralNames.hasMatch(dpn.getFullName(), idpFullName));
            }
            if (matchFound) return true;
        }
        return matchFound;
    }
}
