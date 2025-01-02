package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate.CertificateMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl.CRLMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross.CrossCertificateMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl.DeltaCRLMatcher;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp.OCSPResponseMatcher;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for the matcher classes specified by MatchingPolicy
 */
public class MatchSystem {

	private static Logger logger = LoggerFactory.getLogger(MatchSystem.class);
	
    private List<CertificateMatcher> mCertificateMatchers = new ArrayList<CertificateMatcher>();
    private List<CRLMatcher> mCRLMatchers = new ArrayList<CRLMatcher>();
    private List<CrossCertificateMatcher> mCrossCertificateMatchers = new ArrayList<CrossCertificateMatcher>();
    private List<DeltaCRLMatcher> mDeltaCRLMatchers = new ArrayList<DeltaCRLMatcher>();
    private List<OCSPResponseMatcher> mOCSPResponseMatchers = new ArrayList<OCSPResponseMatcher>();

    public MatchSystem()
    {
    	try
    	{
    		LV.getInstance().checkLD(Urunler.SERTIFIKADOGRULAMA);
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
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
        mCertificateMatchers.add(aCertificateMatcher);
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
        mCRLMatchers.add(aCRLMatcher);
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
        mCrossCertificateMatchers.add(aCrossCertificateMatcher);
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
        mDeltaCRLMatchers.add(aDeltaCRLMatcher);
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
        mOCSPResponseMatchers.add(aOCSPResponseMatcher);
    }



    /**
     * İki sertifikayı SMSertifikası-Sertifika ilişkisi şeklinde eşleştirir.
     */
    public boolean matchCertificate(ECertificate aCertificate,
                                    ECertificate aIssuerCertificate)
    {
        for (CertificateMatcher se : mCertificateMatchers) {
            boolean match = se.matchCertificate(aCertificate, aIssuerCertificate);
            if (!match) 
            {
            	logger.trace(se.getClass().getName() + " failed");
                return false;
            }
        }
        return true;
    }

    public boolean matchCertificate(ECRL aCRL, ECertificate aIssuerCertificate)
    {
        for (CertificateMatcher se : mCertificateMatchers) {
            if (!se.matchCertificate(aCRL, aIssuerCertificate)) 
            {
            	logger.trace(se.getClass().getName() + " failed");
                return false;
            }
        }
        return true;
    }

    /**
     * Sertifika ile SİL'i eşleştirir
     */
    public boolean matchCRL(ECertificate aCertificate, ECRL aCRL)
    {
        for (CRLMatcher se : mCRLMatchers) 
        {
            if (!se.matchCRL(aCertificate, aCRL)) 
            {
            	logger.trace(se.getClass().getName() + " failed");
            	logger.trace("CRL:\n" + aCRL);
                return false;
            }
        }
        return true;
    }


    /**
     * Verilen Base SİL ile delta-SİL i eşleştirir
     */
    public boolean matchDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
    {
        for (DeltaCRLMatcher dse : mDeltaCRLMatchers)
        {
            if (!dse.deltaSilEslestir(aCRL, aDeltaCRL)) 
            {
            	logger.trace(dse.getClass().getName() + " failed");
                return false;
            }
        }
        return true;
    }

    public boolean matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer , EOCSPResponse aOCSPResponse)
    {
        for (OCSPResponseMatcher ose : mOCSPResponseMatchers)
        {
            if(!ose.matchOCSPResponse(aCertificate,aIssuer,aOCSPResponse))
            {
            	logger.trace(ose.getClass().getName() + " failed");
                return false;
            }
        }
        return true;
    }

    /**
     * Sertifika ile çapraz sertifika arasındaki eşleştirmeyi yapar
     */
    public boolean matchCrossCertificate(ECertificate aCertificate,
                                         ECertificate aCrossCertificate)
    {
        for (CrossCertificateMatcher cse : mCrossCertificateMatchers) {
            if (!cse.matchCrossCertificate(aCertificate, aCrossCertificate)) 
            {
            	logger.trace(cse.getClass().getName() + " failed");
                return false;
            }
        }
        return true;
    }
}
