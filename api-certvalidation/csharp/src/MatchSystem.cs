using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.tools;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Container class for the matcher classes specified by MatchingPolicy
     */
    public class MatchSystem
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private List<CertificateMatcher> mCertificateMatchers = new List<CertificateMatcher>();
        private List<CRLMatcher> mCRLMatchers = new List<CRLMatcher>();
        private List<CrossCertificateMatcher> mCrossCertificateMatchers = new List<CrossCertificateMatcher>();
        private List<DeltaCRLMatcher> mDeltaCRLMatchers = new List<DeltaCRLMatcher>();
        private List<OCSPResponseMatcher> mOCSPResponseMatchers = new List<OCSPResponseMatcher>();

        public MatchSystem()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.SERTIFIKADOGRULAMA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }

        public List<CertificateMatcher> getCertificateMatchers()
        {
            return mCertificateMatchers;
        }

        public void setCertificateMatchers(List<CertificateMatcher> aCertificateMatchers)
        {
            mCertificateMatchers = aCertificateMatchers;
        }

        public void addCertificateMatcher(CertificateMatcher aCertificateMatcher)
        {
            mCertificateMatchers.Add(aCertificateMatcher);
        }

        public List<CRLMatcher> getCRLMatchers()
        {
            return mCRLMatchers;
        }

        public void setCRLMatchers(List<CRLMatcher> aCRLMatchers)
        {
            mCRLMatchers = aCRLMatchers;
        }

        public void addCRLMatcher(CRLMatcher aCRLMatcher)
        {
            mCRLMatchers.Add(aCRLMatcher);
        }

        public List<CrossCertificateMatcher> getCrossCertificateMatchers()
        {
            return mCrossCertificateMatchers;
        }

        public void setCrossCertificateMatchers(List<CrossCertificateMatcher> aCrossCertificateMatchers)
        {
            mCrossCertificateMatchers = aCrossCertificateMatchers;
        }

        public void addCrossCertificateMatcher(CrossCertificateMatcher aCrossCertificateMatcher)
        {
            mCrossCertificateMatchers.Add(aCrossCertificateMatcher);
        }

        public List<DeltaCRLMatcher> getDeltaCRLMatchers()
        {
            return mDeltaCRLMatchers;
        }

        public void setDeltaCRLMatchers(List<DeltaCRLMatcher> aDeltaCRLMatchers)
        {
            mDeltaCRLMatchers = aDeltaCRLMatchers;
        }

        public void addDeltaCRLMatcher(DeltaCRLMatcher aDeltaCRLMatcher)
        {
            mDeltaCRLMatchers.Add(aDeltaCRLMatcher);
        }
        public List<OCSPResponseMatcher> getOCSPResponseMatchers()
        {
            return mOCSPResponseMatchers;
        }

        public void setOCSPResponseMatchers(List<OCSPResponseMatcher> aOCSPResponseMatchers)
        {
            mOCSPResponseMatchers = aOCSPResponseMatchers;
        }

        public void addOCSPResponseMatcher(OCSPResponseMatcher aOCSPResponseMatcher)
        {
            mOCSPResponseMatchers.Add(aOCSPResponseMatcher);
        }


        /**
         * İki sertifikayı SMSertifikası-Sertifika ilişkisi şeklinde eşleştirir.
         */
        public bool matchCertificate(ECertificate aCertificate,
                                        ECertificate aIssuerCertificate)
        {
            foreach (CertificateMatcher se in mCertificateMatchers)
            {
                bool match = se.matchCertificate(aCertificate, aIssuerCertificate);
                if (!match)
                {
                    logger.Trace(se.GetType().FullName + " failed");
                    return false;
                }
            }
            return true;
        }

        public bool matchCertificate(ECRL aCRL, ECertificate aIssuerCertificate)
        {
            foreach (CertificateMatcher se in mCertificateMatchers)
            {
                if (!se.matchCertificate(aCRL, aIssuerCertificate))
                {
                    logger.Trace(se.GetType().FullName + " failed");
                    return false;
                }
            }
            return true;
        }

        /**
         * Sertifika ile SİL'i eşleştirir
         */
        public bool matchCRL(ECertificate aCertificate, ECRL aCRL)
        {
            foreach (CRLMatcher se in mCRLMatchers)
            {
                if (!se.matchCRL(aCertificate, aCRL))
                {
                    logger.Trace(se.GetType().FullName + " failed");
                    logger.Trace("CRL:\n" + aCRL);
                    return false;
                }
            }
            return true;
        }


        /**
         * Verilen Base SİL ile delta-SİL i eşleştirir
         */
        public bool matchDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
        {
            foreach (DeltaCRLMatcher dse in mDeltaCRLMatchers)
            {
                if (!dse.deltaSilEslestir(aCRL, aDeltaCRL))
                {
                    logger.Trace(dse.GetType().FullName + " failed");
                    return false;
                }
            }
            return true;
        }

        public bool matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse)
        {
            foreach (OCSPResponseMatcher ose in mOCSPResponseMatchers)
            {
                if (!ose.matchOCSPResponse(aCertificate, aIssuer, aOCSPResponse))
                {
                    logger.Trace(ose.GetType().FullName + " failed");
                    return false;
                }
            }
            return true;
        }


        /**
         * Sertifika ile çapraz sertifika arasındaki eşleştirmeyi yapar
         */
        public bool matchCrossCertificate(ECertificate aCertificate,
                                             ECertificate aCrossCertificate)
        {
            foreach (CrossCertificateMatcher cse in mCrossCertificateMatchers)
            {
                if (!cse.matchCrossCertificate(aCertificate, aCrossCertificate))
                {
                    logger.Trace(cse.GetType().FullName + " failed");
                    return false;
                }
            }
            return true;
        }
    }
}
