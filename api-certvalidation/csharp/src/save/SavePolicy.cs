using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    /**
     * <p>Specifies which saver classes are used during validation </p>
     * @author IH
     */
    public class SavePolicy
    {
        private List<PolicyClassInfo> mCertificateSavers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mCRLSavers = new List<PolicyClassInfo>();
        // todo c++ da niye yok?
        private List<PolicyClassInfo> mOCSPResponseSavers = new List<PolicyClassInfo>();

        public SavePolicy()
        {

        }

        public List<PolicyClassInfo> getOCSPResponseSavers()
        {
            return mOCSPResponseSavers;
        }

        public void setOCSPResponseSavers(List<PolicyClassInfo> aOCSPResponseSavers)
        {
            mOCSPResponseSavers = aOCSPResponseSavers;
        }

        public List<PolicyClassInfo> getCRLSavers()
        {
            return mCRLSavers;
        }

        public void setCRLSavers(List<PolicyClassInfo> aCRLSavers)
        {
            mCRLSavers = aCRLSavers;
        }

        public List<PolicyClassInfo> getCertificateSavers()
        {
            return mCertificateSavers;
        }

        public void setCertificateSavers(List<PolicyClassInfo> aCertificateSavers)
        {
            mCertificateSavers = aCertificateSavers;
        }
    }
}
