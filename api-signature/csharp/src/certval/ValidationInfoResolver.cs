using System;
using System.Collections.Generic;
using Logger = log4net.ILog;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.signature.certval.match;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    /**
     * resolvers in this class works find quick way. If initial resources have it
     * methods return it, if any resolver have what is searched, it doesn't seek more.
     *
     * @author ayetgin
     */
    public class ValidationInfoResolver
    {
        private static Logger logger = log4net.LogManager.GetLogger(typeof(ValidationInfoResolver));

        private readonly List<ReferencedCertificateFinder> certResolvers = new List<ReferencedCertificateFinder>();
        private readonly List<ReferencedCRLFinder> crlResolvers = new List<ReferencedCRLFinder>();
        private readonly List<ReferencedOCSPResponseFinder> ocspResolvers = new List<ReferencedOCSPResponseFinder>();

        private readonly List<ECertificate> initialCertificates = new List<ECertificate>();
        private readonly List<ECRL> initialCRLs = new List<ECRL>();
        private readonly List<EOCSPResponse> initialOCSPResponses = new List<EOCSPResponse>();

        private readonly CertificateCriteriaMatcher certMatcher = new CertificateCriteriaMatcher();
        private readonly CRLCriteriaMatcher crlMatcher = new CRLCriteriaMatcher();
        private readonly OCSPResponseCriteriaMatcher ocspMatcher = new OCSPResponseCriteriaMatcher();



        public ValidationInfoResolver(){
        }

        public ValidationInfoResolver(List<ReferencedCertificateFinder> aCertResolvers, List<ReferencedCRLFinder> aCrlResolvers, List<ReferencedOCSPResponseFinder> aOcspResolvers)
        {
            certResolvers = aCertResolvers;
            crlResolvers = aCrlResolvers;
            ocspResolvers = aOcspResolvers;
        }

        public List<ECertificate> resolve(CertificateSearchCriteria aCriteria){
            List<ECertificate> certs = new List<ECertificate>();
            List<ECertificate> matchedCerts = new List<ECertificate>();

            if (initialCertificates !=null) {
                foreach (ECertificate cert in initialCertificates){
                    if (certMatcher.match(aCriteria, cert)){
                        certs.Add(cert);
                    }
                }
            }
            if (certs.Count > 0)
                return certs;

            if (certResolvers!=null)
            foreach (ReferencedCertificateFinder finder in certResolvers){
                try {
                    certs = finder.find(aCriteria);
                    if (certs!=null){
                        foreach (ECertificate cert in certs){
                            if (certMatcher.match(aCriteria, cert)){
                                matchedCerts.Add(cert);
                            }
                        }
                         if (matchedCerts.Count > 0)
                             return matchedCerts;

                    }
                } catch (Exception x){
                    logger.Error("Error resolving cert reference "+aCriteria, x);
                }
            }

            return /*Collections.EMPTY_LIST*/ new List<ECertificate>();
        }

        public List<ECRL> resolve(CRLSearchCriteria aCriteria)
        {
            List<ECRL> foundCRLs = new List<ECRL>();
            List<ECRL> matchedCRLs = new List<ECRL>();

            if (this.initialCRLs != null)
            {
                foreach (ECRL cert in this.initialCRLs)
                {
                    if (crlMatcher.match(aCriteria, cert))
                    {
                        foundCRLs.Add(cert);
                    }
                }
            }
            if (foundCRLs.Count > 0)
                return foundCRLs;

            if (crlResolvers != null)
                foreach (ReferencedCRLFinder finder in crlResolvers)
                {
                    try
                    {
                        foundCRLs = finder.find(aCriteria);
                        if (foundCRLs != null)
                        {
                            foreach (ECRL crl in foundCRLs)
                            {
                                if (crlMatcher.match(aCriteria, crl))
                                {
                                    matchedCRLs.Add(crl);
                                }
                            }
                            if (matchedCRLs.Count > 0)
                                return matchedCRLs;

                        }
                    }
                    catch (Exception x)
                    {
                        logger.Error("Error resolving CRL reference " + aCriteria, x);
                    }
                }

            return /*Collections.EMPTY_LIST*/ new List<ECRL>();
        }


        public List<EOCSPResponse> resolve(OCSPSearchCriteria aCriteria)
        {
            List<EOCSPResponse> foundOCSPs = new List<EOCSPResponse>();
            List<EOCSPResponse> matchedOCSPs = new List<EOCSPResponse>();

            if (initialOCSPResponses != null)
            {
                foreach (EOCSPResponse ocsp in initialOCSPResponses)
                {
                    if (ocspMatcher.match(aCriteria, ocsp))
                    {
                        foundOCSPs.Add(ocsp);
                    }
                }
            }
            if (foundOCSPs.Count > 0)
                return foundOCSPs;

            if (ocspResolvers != null)
                foreach (ReferencedOCSPResponseFinder finder in ocspResolvers)
                {
                    try
                    {
                        foundOCSPs = finder.find(aCriteria);
                        _extractCertsFromOCSP(foundOCSPs);
                        if (foundOCSPs != null)
                        {
                            foreach (EOCSPResponse ocsp in foundOCSPs)
                            {
                                if (ocspMatcher.match(aCriteria, ocsp))
                                {
                                    matchedOCSPs.Add(ocsp);
                                }
                            }
                            if (matchedOCSPs.Count > 0)
                                return matchedOCSPs;
                        }
                    }
                    catch (Exception x)
                    {
                        logger.Error("Error resolving OCSP reference " + aCriteria, x);
                    }
                }

            return /*Collections.EMPTY_LIST*/ new List<EOCSPResponse>();
        }

        private void _extractCertsFromOCSP(List<EOCSPResponse> responses){
            if (responses == null)
                return;
            foreach (EOCSPResponse response in responses){
                EBasicOCSPResponse basicResponse = response.getBasicOCSPResponse();
                for (int i=0; i<basicResponse.getCertificateCount();i++){
                    _addDifferent(basicResponse.getCertificate(i));
                }

            }

        }

        private void _addDifferent(ECertificate certificate){
            if ((certificate!=null) &&  (!initialCertificates.Contains(certificate))){
                initialCertificates.Add(certificate);
            }
        }


        public void addOCSPResponses(List<EOCSPResponse> aInitialOCSPResponses)
        {
            foreach (EOCSPResponse ocsp in aInitialOCSPResponses)
                if(!initialOCSPResponses.Contains(ocsp))
                    initialOCSPResponses.Add(ocsp);

            //initialOCSPResponses = aInitialOCSPResponses;
        }

        public void addCRLs(List<ECRL> aInitialCRLs)
        {
            foreach (ECRL crl in aInitialCRLs)
                if(!initialCRLs.Contains(crl))
                    initialCRLs.Add(crl);

            //initialCRLs = aInitialCRLs;
        }

        public void addCertificates(List<ECertificate> aInitialCertificates)
        {
            foreach (ECertificate cert in aInitialCertificates)
                if(!initialCertificates.Contains(cert))
                    initialCertificates.Add(cert);

            //initialCertificates = aInitialCertificates;
        }

        public List<ECertificate> getInitialCertificates()
        {
            return initialCertificates;
        }

        public List<ECRL> getInitialCRLs()
        {
            return initialCRLs;
        }

        public List<EOCSPResponse> getInitialOCSPResponses()
        {
            return initialOCSPResponses;
        }

        /*
        public void setCertificateResolvers(List<ReferencedCertificateFinder> aCertResolvers)
        {
            certResolvers = aCertResolvers;
        }

        public void setCrlResolvers(List<ReferencedCRLFinder> aCrlResolvers)
        {
            crlResolvers = aCrlResolvers;
        }

        public void setOcspResolvers(List<ReferencedOCSPResponseFinder> aOcspResolvers)
        {
            ocspResolvers = aOcspResolvers;
        }    */

        public void addCertificateResolvers(params ReferencedCertificateFinder[] aCertResolvers)
        {
            foreach (ReferencedCertificateFinder finder in aCertResolvers)
                if (!certResolvers.Contains(finder))
                    certResolvers.Add(finder);
        }

        public void addCrlResolvers(params ReferencedCRLFinder[] aCrlResolvers)
        {
            foreach (ReferencedCRLFinder finder in aCrlResolvers)
                if (!crlResolvers.Contains(finder))
                    crlResolvers.Add(finder);
        }

        public void addOcspResolvers(params ReferencedOCSPResponseFinder[] aOcspResolvers)
        {
            foreach (ReferencedOCSPResponseFinder finder in aOcspResolvers)
                if (!ocspResolvers.Contains(finder))
                    ocspResolvers.Add(finder);
        }
    }
}
