package tr.gov.tubitak.uekae.esya.api.signature.certval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.CRLCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.CertificateCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.OCSPResponseCriteriaMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * resolvers in this class works find quick way. If initial resources have it
 * methods return it, if any resolver have what is searched, it doesn't seek more.
 *
 * @author ayetgin
 */
public class ValidationInfoResolver
{
    private static Logger logger = LoggerFactory.getLogger(ValidationInfoResolver.class);

    private List<ReferencedCertificateFinder> certResolvers = new ArrayList<ReferencedCertificateFinder>();
    private List<ReferencedCRLFinder> crlResolvers = new ArrayList<ReferencedCRLFinder>();
    private List<ReferencedOCSPResponseFinder> ocspResolvers = new ArrayList<ReferencedOCSPResponseFinder>();

    private List<ECertificate> initialCertificates = new ArrayList<ECertificate>();
    private List<ECRL> initialCRLs = new ArrayList<ECRL>();
    private List<EOCSPResponse> initialOCSPResponses = new ArrayList<EOCSPResponse>();

    private CertificateCriteriaMatcher certMatcher = new CertificateCriteriaMatcher();
    private CRLCriteriaMatcher crlMatcher = new CRLCriteriaMatcher();
    private OCSPResponseCriteriaMatcher ocspMatcher = new OCSPResponseCriteriaMatcher();



    public ValidationInfoResolver(){
    }

    public ValidationInfoResolver(List<ReferencedCertificateFinder> aCertResolvers, List<ReferencedCRLFinder> aCrlResolvers, List<ReferencedOCSPResponseFinder> aOcspResolvers)
    {
        certResolvers = aCertResolvers;
        crlResolvers = aCrlResolvers;
        ocspResolvers = aOcspResolvers;
    }

    public List<ECertificate> resolve(CertificateSearchCriteria aCriteria){
        List<ECertificate> certs = new ArrayList<ECertificate>();
        List<ECertificate> matchedCerts = new ArrayList<ECertificate>();

        if (initialCertificates !=null) {
            for (ECertificate cert : initialCertificates){
                if (certMatcher.match(aCriteria, cert)){
                    certs.add(cert);
                }
            }
        }
        if (certs.size()>0)
            return certs;

        if (certResolvers!=null)
        for (ReferencedCertificateFinder finder : certResolvers){
            try {
                certs = finder.find(aCriteria);
                if (certs!=null){
                    for (ECertificate cert : certs){
                        if (certMatcher.match(aCriteria, cert)){
                            matchedCerts.add(cert);
                        }
                    }
                     if (matchedCerts.size()>0)
                         return matchedCerts;

                }
            } catch (Exception x){
                logger.error("Error resolving cert reference "+aCriteria, x);
            }
        }

        return Collections.EMPTY_LIST;
    }

    public List<ECRL> resolve(CRLSearchCriteria aCriteria){
        List<ECRL> foundCRLs = new ArrayList<ECRL>();
        List<ECRL> matchedCRLs = new ArrayList<ECRL>();

        if (this.initialCRLs !=null) {
            for (ECRL cert : this.initialCRLs){
                if (crlMatcher.match(aCriteria, cert)){
                    foundCRLs.add(cert);
                }
            }
        }
        if (foundCRLs.size()>0)
            return foundCRLs;

        if (crlResolvers!=null)
        for (ReferencedCRLFinder finder : crlResolvers){
            try {
                foundCRLs = finder.find(aCriteria);
                if (foundCRLs!=null){
                    for (ECRL crl : foundCRLs){
                        if (crlMatcher.match(aCriteria, crl)){
                            matchedCRLs.add(crl);
                        }
                    }
                     if (matchedCRLs.size()>0)
                         return matchedCRLs;

                }
            } catch (Exception x){
                logger.error("Error resolving CRL reference "+aCriteria, x);
            }
        }

        return Collections.EMPTY_LIST;
    }


    public List<EOCSPResponse> resolve(OCSPSearchCriteria aCriteria){
        List<EOCSPResponse> foundOCSPs = new ArrayList<EOCSPResponse>();
        List<EOCSPResponse> matchedOCSPs = new ArrayList<EOCSPResponse>();

        if (initialOCSPResponses !=null) {
            for (EOCSPResponse ocsp : initialOCSPResponses){
                if (ocspMatcher.match(aCriteria, ocsp)){
                    foundOCSPs.add(ocsp);
                }
            }
        }
        if (foundOCSPs.size()>0)
            return foundOCSPs;

        if (ocspResolvers!=null)
        for (ReferencedOCSPResponseFinder finder : ocspResolvers){
            try {
                foundOCSPs = finder.find(aCriteria);
                _extractCertsFromOCSP(foundOCSPs);
                if (foundOCSPs!=null){
                    for (EOCSPResponse ocsp : foundOCSPs){
                        if (ocspMatcher.match(aCriteria, ocsp)){
                            matchedOCSPs.add(ocsp);
                        }
                    }
                    if (matchedOCSPs.size()>0)
                        return matchedOCSPs;
                }
            } catch (Exception x){
                logger.error("Error resolving OCSP reference "+aCriteria, x);
            }
        }

        return Collections.EMPTY_LIST;
    }

    private void _extractCertsFromOCSP(List<EOCSPResponse> responses){
    	if(responses == null)
    		return;
        for (EOCSPResponse response : responses){
            EBasicOCSPResponse basicResponse = response.getBasicOCSPResponse();
            for (int i=0; i<basicResponse.getCertificateCount();i++){
                _addDifferent(basicResponse.getCertificate(i));
            }

        }

    }

    private void _addDifferent(ECertificate certificate){
        if ((certificate!=null) &&  (!initialCertificates.contains(certificate))){
            initialCertificates.add(certificate);
        }
    }


    public void addOCSPResponses(List<EOCSPResponse> aInitialOCSPResponses)
    {
        for(EOCSPResponse ocsp : aInitialOCSPResponses)
            addOCSPResponse(ocsp);
    }

    public void addOCSPResponse(EOCSPResponse ocsp)
    {
        if(!initialOCSPResponses.contains(ocsp))
            initialOCSPResponses.add(ocsp);
    }

    public void addCRLs(List<ECRL> aInitialCRLs)
    {
        for(ECRL crl : aInitialCRLs)
            addCRL(crl);
    }

    public void addCRL(ECRL crl)
    {
        if(!initialCRLs.contains(crl))
            initialCRLs.add(crl);
    }

    public void addCertificates(List<ECertificate> aInitialCertificates)
    {
        for(ECertificate cert : aInitialCertificates)
            addCertificate(cert);

        //initialCertificates = aInitialCertificates;
    }

    public void addCertificate(ECertificate cert)
    {
        if(!initialCertificates.contains(cert))
            initialCertificates.add(cert);
    }

    public List<ECertificate> getInitialCertificates() {
        return initialCertificates;
    }

    public List<ECRL> getInitialCRLs() {
        return initialCRLs;
    }

    public List<EOCSPResponse> getInitialOCSPResponses() {
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

    public void addCertificateResolvers(ReferencedCertificateFinder... aCertResolvers)
    {
        for (ReferencedCertificateFinder finder : aCertResolvers)
            if (!certResolvers.contains(finder))
                certResolvers.add(finder);
    }

    public void addCrlResolvers(ReferencedCRLFinder... aCrlResolvers)
    {
        for (ReferencedCRLFinder finder : aCrlResolvers)
            if (!crlResolvers.contains(finder))
                crlResolvers.add(finder);
    }

    public void addOcspResolvers(ReferencedOCSPResponseFinder... aOcspResolvers)
    {
        for (ReferencedOCSPResponseFinder finder : aOcspResolvers)
            if (!ocspResolvers.contains(finder))
                ocspResolvers.add(finder);
    }
}
