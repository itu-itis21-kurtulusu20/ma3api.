using log4net;
using System.Reflection;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl
{
    /**
     * Matches a Certificate and a CRL according to the CRLDistributionPoint
     * extension information. 
     */
    public class CRLDistributionPointMatcher : CRLMatcher
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Sertifika CDP eklentisi ile Sil Issuing Distribution Point eklentisini eşleştirir
         */
        protected override bool _matchCRL(ECertificate aCertificate, ECRL aCRL)
        {
            //String subject = aSertifika.getSubject().stringValue();
            //String crlIssuer = aCRL.getIssuer().stringValue();

            EIssuingDistributionPoint idp = aCRL.getCRLExtensions().getIssuingDistributionPoint();
            if (idp == null)
            {
                logger.Debug("Silde Issuing Distribution Point uzantısı yok");
                return true;
            }

            ECRLDistributionPoints cdp = aCertificate.getExtensions().getCRLDistributionPoints();
            EDistributionPointName idpn = idp.getDistributionPoint();

            if (cdp == null)
            {
                logger.Debug("Sertifikada CRL Distribution Point uzantısı yok");
                if (idpn != null)
                {
                    if (idpn.getType() == DistributionPointName._FULLNAME)
                        return (EGeneralNames.hasMatch(idpn.getFullName(), aCRL.getIssuer()));
                    else
                    {
                        EName idpFullName_ = aCRL.getIssuer().Clone();
                        idpFullName_.appendRDN(idpn.getNameRelativeToCRLIssuer());
                        return idpFullName_.Equals(aCertificate.getIssuer());
                    }
                }
                else return true;
            }

            if (idpn == null || (cdp.getCRLDistributionPointCount() == 0) || (cdp.getCRLDistributionPoint(0).getDistributionPoint() == null))
                return true;

            bool matchFound = false;

            EName cdpFullName, idpFullName;

            for (int i = 0; i < cdp.getCRLDistributionPointCount(); i++)
            {

                ECRLDistributionPoint dp = cdp.getCRLDistributionPoint(i);

                EDistributionPointName dpn = dp.getDistributionPoint();

                if ((dpn.getType() == DistributionPointName._FULLNAME) && (idpn.getType() == DistributionPointName._FULLNAME))
                {
                    matchFound = (dpn.getFullName().Equals(idpn.getFullName()));
                }
                else if (dpn.getType() == DistributionPointName._NAMERELATIVETOCRLISSUER)
                {
                    if (dp.getCRLIssuer() != null)
                    {
                        cdpFullName = dp.getCRLIssuer();
                    }
                    else
                    {
                        cdpFullName = aCertificate.getIssuer().Clone();
                    }
                    cdpFullName.appendRDN(dpn.getNameRelativeToCRLIssuer());

                    if (idpn.getType() == DistributionPointName._NAMERELATIVETOCRLISSUER)
                    {
                        idpFullName = aCRL.getIssuer().Clone();
                        idpFullName.appendRDN(idpn.getNameRelativeToCRLIssuer());

                        matchFound = idpFullName.Equals(cdpFullName);
                    }
                    else
                    {
                        matchFound = EGeneralNames.hasMatch(idpn.getFullName(), cdpFullName);
                    }
                }
                else
                {
                    idpFullName = aCRL.getIssuer().Clone();
                    idpFullName.appendRDN(idpn.getNameRelativeToCRLIssuer());

                    matchFound = (EGeneralNames.hasMatch(dpn.getFullName(), idpFullName));
                }
                if (matchFound) return true;
            }
            return matchFound;
        }
    }
}
