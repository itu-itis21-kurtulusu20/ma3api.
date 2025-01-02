using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    public class ValueFinderFromElsewhere : ValueFinder
    {
        private readonly ValidationInfoResolver resolver;

        private readonly List<ECertificate> mCerts = new List<ECertificate>();
        private readonly List<ECRL> mCRLs = new List<ECRL>();
        private readonly List<EBasicOCSPResponse> mOCSPs = new List<EBasicOCSPResponse>();

        public ValueFinderFromElsewhere(List<ECertificate> initialCerts, ValidationInfoResolver resolver)
        {
            resolver.addCertificates(initialCerts);
            this.resolver = resolver;
        }

        public ValueFinderFromElsewhere(ValidationInfoResolver resolver)
        {
            this.resolver = resolver;
        }
        public ECertificateValues findCertValues(ECompleteCertificateReferences aRefs)
        {
            CertificateSearchCriteria criteria = new CertificateSearchCriteria();
            foreach (EOtherCertID oci in aRefs.getCertIDs())
            {

                criteria.setDigestValue(oci.getDigestValue());
                criteria.setDigestAlg(DigestAlg.fromOID(oci.getDigestAlg()));

                try
                {
                    List<ECertificate> c = resolver.resolve(criteria);
                    if (c.Count == 0)
                        throw new CMSSignatureException("No hash found in certificate store for given certificate reference:" + System.Text.Encoding.UTF8.GetString(oci.getDigestValue()));
                    mCerts.Add(c[0]);

                }
                catch (Exception aEx)
                {
                    // throw demesek de log bassak?
                    throw new CMSSignatureException("Error while finding certificate from certificate store", aEx);
                    //logger.warn("Error while finding certificate from certificate store", aEx);
                }
            }
            return new ECertificateValues(mCerts);
        }


        public ERevocationValues findRevocationValues(ECompleteRevocationReferences aRefs)
             {
        foreach (ECrlOcspRef refer in aRefs.getCrlOcspRefs()) {

            ECrlValidatedID[] elements = refer.getCRLIds();
            CRLSearchCriteria crlSearchCriteria = new CRLSearchCriteria();

            if(elements != null) {  // null check is required before for-each loop
                foreach (ECrlValidatedID cvi in elements) {
                    crlSearchCriteria.setDigestValue(cvi.getDigestValue());
                    crlSearchCriteria.setDigestAlg(DigestAlg.fromOID(cvi.getDigestAlg()));

                    try {
                        List<ECRL> c = resolver.resolve(crlSearchCriteria);
                        if (c.Count==0)
                            throw new CMSSignatureException("No hash found in certificate store for given revocation reference:" + System.Text.Encoding.UTF8.GetString(cvi.getDigestValue()));

                        mCRLs.Add(c[0]);

                    } catch (Exception aEx) {
                        throw new CMSSignatureException("Error while finding crl from certificate store", aEx);
                    }
                }
            }


            OCSPSearchCriteria ocspSearchCriteria = new OCSPSearchCriteria();
            EOcspResponsesID[] ids = refer.getOcspResponseIds();
            if(ids != null) {
                foreach (EOcspResponsesID id in ids) {
                    ocspSearchCriteria.setDigestValue(id.getDigestValue());
                    ocspSearchCriteria.setDigestAlg(DigestAlg.fromOID(id.getDigestAlg()));
                    try {
                        List<EOCSPResponse> list = resolver.resolve(ocspSearchCriteria);
                        if (list != null && list.Count != 0)
                            mOCSPs.Add(list[0].getBasicOCSPResponse());

                    } catch (Exception aEx) {
                        throw new CMSSignatureException("Error while finding ocsp from certificate store", aEx);
                    }
                }
            }
        }

        return new ERevocationValues(mCRLs, mOCSPs);
    }


        public List<ECertificate> getCertificates()
        {
            return new List<ECertificate>(mCerts);
        }

        public List<ECRL> getCRLs()
        {
            return new List<ECRL>(mCRLs);
        }

        public List<EBasicOCSPResponse> getOCSPs()
        {
            return new List<EBasicOCSPResponse>(mOCSPs);
        }

    }
}
