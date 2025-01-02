using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted
{
    /**
     * <p>Base class for finding trusted certificates.</p>
     * @author IH
     */
    public abstract class TrustedCertificateFinder : Finder
    {
        public static readonly String PARAM_SECURITYLEVEL = "securitylevel";
        public static readonly String PARAM_ONLYSELFSIGNED = "onlyselfsigned";

        public static readonly String PARAM_PERSONAL = "personal";
        public static readonly String PARAM_ORGANIZATIONAL = "organizational";
        public static readonly String PARAM_LEGAL = "legal";

        protected abstract List<ECertificate> _findTrustedCertificate();

        public List<ECertificate> findTrustedCertificate()
        {
            List<ECertificate> eCertificates = _findTrustedCertificate();
            return filterBySelfSigningCertificate(eCertificates);
        }

        private List<ECertificate> filterBySelfSigningCertificate(List<ECertificate> eCertificates)
        {
            bool onlySelfSigned = mParameters.getParameterBoolean(PARAM_ONLYSELFSIGNED, true);

            // if not "only-self-signed": return all
            if (!onlySelfSigned)
            {
                return eCertificates;
            }
            // else

            // filter self-issued certificates
            List<ECertificate> certList = new List<ECertificate>();
            foreach (ECertificate cert in eCertificates)
            {
                if (cert.isSelfIssued())
                    certList.Add(cert);
            }
            return certList;
        }

        public List<SecurityLevel> getSecurityLevel()
        {
            String guvenlikSeviyesi = mParameters.getParameterAsString(PARAM_SECURITYLEVEL);
            if (guvenlikSeviyesi == null || guvenlikSeviyesi.Trim().Length == 0)
                guvenlikSeviyesi = "legal";

            List<SecurityLevel> result = new List<SecurityLevel>(3);
            try
            {
                String[] arr = guvenlikSeviyesi.Split(',');
                foreach (String levelStr in arr)
                    result.Add(SecurityLevel.getNesne(levelStr.Trim()));
                return result;// SecurityLevel.getNesne(guvenlikSeviyesi);
            }
            catch (CertStoreException e)
            {
                Console.WriteLine(e.StackTrace);
                return new List<SecurityLevel>(1);
            }
        }

    }
}
