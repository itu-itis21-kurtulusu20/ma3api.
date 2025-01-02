using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.policy
{
    /**
     * Specifies which Matcher classes will be used during validation.
     * 
     * @author IH
     */
    public class MatchingPolicy
    {
        private List<PolicyClassInfo> mCertificateMatchers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mCRLMatchers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mDeltaCRLMatchers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mCrossCertificateMatchers = new List<PolicyClassInfo>();
        private List<PolicyClassInfo> mOCSPResponseMatchers = new List<PolicyClassInfo>();


        public List<PolicyClassInfo> getCertificateMatchers()
        {
            return mCertificateMatchers;
        }

        public void setCertificateMatchers(List<PolicyClassInfo> aSertifikaEslestiriciler)
        {
            mCertificateMatchers = aSertifikaEslestiriciler;
        }

        public List<PolicyClassInfo> getCRLMatchers()
        {
            return mCRLMatchers;
        }

        public void setCRLMatchers(List<PolicyClassInfo> aSilEslestiriciler)
        {
            mCRLMatchers = aSilEslestiriciler;
        }

        public List<PolicyClassInfo> getCrossCertificateMatchers()
        {
            return mCrossCertificateMatchers;
        }

        public void setCrossCertificateMatchers(List<PolicyClassInfo> aCaprazSertifikaEslestiriciler)
        {
            mCrossCertificateMatchers = aCaprazSertifikaEslestiriciler;
        }

        public List<PolicyClassInfo> getDeltaCRLMatchers()
        {
            return mDeltaCRLMatchers;
        }

        public void setDeltaCRLMatchers(List<PolicyClassInfo> aDeltaSilEslestiriciler)
        {
            mDeltaCRLMatchers = aDeltaSilEslestiriciler;
        }

        public List<PolicyClassInfo> getOCSPResponseMatchers()
        {
            return mOCSPResponseMatchers;
        }

        public void setOCSPResponseMatchers(List<PolicyClassInfo> aOCSPResponseMatchers)
        {
            mOCSPResponseMatchers = aOCSPResponseMatchers;
        }

    }
}
