package tr.gov.tubitak.uekae.esya.api.cmssignature;

import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.certval.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ValueFinderFromElsewhere implements ValueFinder {

    private ValidationInfoResolver resolver;

    private List<ECertificate> mCerts = new ArrayList<ECertificate>();
    private List<ECRL> mCRLs = new ArrayList<ECRL>();
    private List<EBasicOCSPResponse> mOCSPs = new ArrayList<EBasicOCSPResponse>();

    public ValueFinderFromElsewhere(List<ECertificate> initialCerts, ValidationInfoResolver resolver) {
        resolver.addCertificates(initialCerts);
        this.resolver = resolver;
    }

    public ValueFinderFromElsewhere(ValidationInfoResolver resolver) {
        this.resolver = resolver;
    }


    /*
    public void setInitialCertificatesForResolver(List<ECertificate> certs) {
    	resolver.addCertificates(certs);
    }

    public void setInitialCRLsForResolver(List<ECRL> crls) {
    	resolver.addCRLs(crls);
    }

    public void setInitialOCSPsForResolver(List<EOCSPResponse> ocsps) {
       resolver.addOCSPResponses(ocsps);
    }

     static CertStore mCS = null;
     static CertStoreCertificateOps mCSOps = null;
     static CertStoreCRLOps mCRLOps = null;
     static CertStoreOCSPOps mOCSPOps = null;
     static CertStoreRootCertificateOps mRootOps = null;

     static
     {
         try
         {
             mCS = new CertStore();
             mCSOps = new CertStoreCertificateOps(mCS);
             mCRLOps = new CertStoreCRLOps(mCS);
             mOCSPOps = new CertStoreOCSPOps(mCS);
             mRootOps = new CertStoreRootCertificateOps(mCS);
         }
         catch(Exception aEx)
         {
             //TODO
             aEx.printStackTrace();
         }
     }

     public ECertificateValues findCertValues(ECompleteCertificateReferences aRefs)
     throws CMSSignatureException
     {
         CertificateSearchTemplate template = new CertificateSearchTemplate();
         for(EOtherCertID oci:aRefs.getCertIDs())
         {

             template.setHash(oci.getDigestValue());
             try
             {
                 List<ECertificate> c = mCSOps.listCertificates(template);
                 if(c.isEmpty())
                 {
                     List<DepoKokSertifika> listStoreRootCertificates = mRootOps.listStoreRootCertificates(template, null, null);
                     if(listStoreRootCertificates==null || listStoreRootCertificates.isEmpty())
                     {
                         throw new CMSSignatureException("No hash found in certificate store for given reference:"+new BigInteger(template.getHash()).toString(16));
                     }
                     c = new ArrayList<ECertificate>();
                     for (DepoKokSertifika depoKokSertifika : listStoreRootCertificates) {
                         c.add(new ECertificate(depoKokSertifika.getValue()));
                     }
                 }
                 mCerts.add(c.get(0));

             }
             catch(Exception aEx)
             {
                 throw new CMSSignatureException("Error while finding certificate from certificate store",aEx);
             }
         }
         return new ECertificateValues(mCerts);

     } */


    public ECertificateValues findCertValues(ECompleteCertificateReferences aRefs)
            throws CMSSignatureException {
        CertificateSearchCriteria criteria = new CertificateSearchCriteria();
        for (EOtherCertID oci : aRefs.getCertIDs()) {

            criteria.setDigestValue(oci.getDigestValue());
            criteria.setDigestAlg(DigestAlg.fromOID(oci.getDigestAlg()));

            try {
                List<ECertificate> c = resolver.resolve(criteria);
                if (c.isEmpty())
                    throw new CMSSignatureException("No hash found in certificate store for given certificate reference:" + Arrays.toString(oci.getDigestValue()));                
                mCerts.add(c.get(0));

            } catch (Exception aEx) {
                // throw demesek de log bassak?
                throw new CMSSignatureException("Error while finding certificate from certificate store", aEx);
                //logger.warn("Error while finding certificate from certificate store", aEx);
            }
        }
        return new ECertificateValues(mCerts);
    }


    public ERevocationValues findRevocationValues(ECompleteRevocationReferences aRefs)
            throws CMSSignatureException {
        for (ECrlOcspRef ref : aRefs.getCrlOcspRefs()) {

            ECrlValidatedID[] elements = ref.getCRLIds();
            CRLSearchCriteria crlSearchCriteria = new CRLSearchCriteria();

            if(elements != null) {  // null check is required before for-each loop
                for (ECrlValidatedID cvi : elements) {
                    crlSearchCriteria.setDigestValue(cvi.getDigestValue());
                    crlSearchCriteria.setDigestAlg(DigestAlg.fromOID(cvi.getDigestAlg()));

                    try {
                        List<ECRL> c = resolver.resolve(crlSearchCriteria);
                        if (c.isEmpty())
                            throw new CMSSignatureException("No hash found in certificate store for given revocation reference:" + Arrays.toString(cvi.getDigestValue()));

                        mCRLs.add(c.get(0));

                    } catch (Exception aEx) {
                        throw new CMSSignatureException("Error while finding crl from certificate store", aEx);
                    }
                }
            }


            OCSPSearchCriteria ocspSearchCriteria = new OCSPSearchCriteria();
            EOcspResponsesID[] ids = ref.getOcspResponseIds();
            if(ids != null) {
                for (EOcspResponsesID id : ids) {
                	//TODO ocsp ref hash null olabilir
                    ocspSearchCriteria.setDigestValue(id.getDigestValue());
                    ocspSearchCriteria.setDigestAlg(DigestAlg.fromOID(id.getDigestAlg()));
                    try {
                        List<EOCSPResponse> list = resolver.resolve(ocspSearchCriteria);
                        if(list != null && list.size() != 0)
                        	mOCSPs.add(list.get(0).getBasicOCSPResponse());

                    } catch (Exception aEx) {
                        throw new CMSSignatureException("Error while finding ocsp from certificate store", aEx);
                    }
                }
            }
        }

        return new ERevocationValues(mCRLs, mOCSPs);
    }


    public List<ECertificate> getCertificates() {
        return new ArrayList<ECertificate>(mCerts);
    }

    public List<ECRL> getCRLs() {
        return new ArrayList<ECRL>(mCRLs);
    }

    public List<EBasicOCSPResponse> getOCSPs() {
        return new ArrayList<EBasicOCSPResponse>(mOCSPs);
    }

}
