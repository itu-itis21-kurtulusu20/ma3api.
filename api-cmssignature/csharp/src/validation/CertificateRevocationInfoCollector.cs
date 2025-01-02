using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    public class CertificateRevocationInfoCollector
    {
        readonly List<ECertificate> mAllCerts = new List<ECertificate>();
        readonly List<ECRL> mAllCrls = new List<ECRL>();
        readonly List<EBasicOCSPResponse> mAllOcsps = new List<EBasicOCSPResponse>();

        /**
          * Default constructor
          */
        public CertificateRevocationInfoCollector()
        { }

        public void _extractAll(ESignedData aSD, Dictionary<string, object> aParams)
        {
            _extractFromSignedData(aSD);

            int count = aSD.getSignerInfoCount();
            for (int i = 0; i < count; i++)
            {
                ESignerInfo si = aSD.getSignerInfo(i);
                _extractFromSignerInfo(si, aParams);
            }


        }
        /**
        * Extract information from signed data and signerInfo
        */
        public void _extractFromSigner(ESignedData aSD, ESignerInfo aSI, Dictionary<string, object> aParams)
        {
            _extractFromSignedData(aSD);
            _extractFromSignerInfo(aSI, aParams);
        }

        private void _extractFromSignedData(ESignedData aSD)
        {
            //get certificates from signeddata
            List<ECertificate> certs = aSD.getCertificates();
            if (certs != null)
            {
                foreach (ECertificate cert in certs)
                    _addIfDifferent(cert);
            }

            //get crls from signeddata
            List<ECRL> crls = aSD.getCRLs();
            if (crls != null)
            {
                foreach (ECRL crl in crls)
                    _addIfDifferent(crl);
            }

            List<EOCSPResponse> ocsps = aSD.getOSCPResponses();
            _extractFromOCSP(ocsps);
            if (ocsps != null)
            {
                foreach (EOCSPResponse ocsp in ocsps)
                    _addIfDifferent(ocsp);
            }
        }


        private void _extractFromSignerInfo(ESignerInfo aSI, Dictionary<string, object> aParams)
        {
            _extractFromValuesAttributes(aSI);
            _extractFromCounterSignature(aSI, aParams);
           // _extractFromReferences(aSI, aParams);
            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_archiveTimestamp);
            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_archiveTimestampV2);
            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_archiveTimestampV3);

            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_certCRLTimestamp);
            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_escTimeStamp);
            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_signatureTimeStampToken);
            _extractFromTimeStamp(aSI, AttributeOIDs.id_aa_ets_contentTimestamp);
        }
        /*
        private void _extractFromReferences(ESignerInfo aSI, Dictionary<String, Object> aParams)
        {
            // bu fonksiyon çağrılamyabilir mi?Certificate checker'da referanslara karşılık gelenler bulunuyor zaten.

            // if type is ESC, ESX1 or ESX2; gather the values of references from certstore
            // DONE counter signaturelarda referans varsa,onlarin degerlerinide bul
            // her zaman aIsCounterSigner=true oluyor şimdi, ama önemli?
            ESignatureType type = SignatureParser.parse(aSI, true);
            if (type == ESignatureType.TYPE_ESC
                    || type == ESignatureType.TYPE_ESX_Type1
                    || type == ESignatureType.TYPE_ESX_Type2)
            {
                try
                {
                    if (aParams != null)
                    {
                        Object policyO = null;
                        aParams.TryGetValue(AllEParameters.P_CERT_VALIDATION_POLICIES, out policyO);
                        ValidationPolicy policy = null;
                        if (policyO != null)
                        {
                            CertValidationPolicies policies = (CertValidationPolicies)policyO;
                            // ValidationPolicy policy = (ValidationPolicy) policyO;
                            // TODO use most appropriate instead of default
                            policy = policies.getPolicyFor(CertValidationPolicies.CertificateType.DEFAULT);

                        }
                        else
                        {
                            policyO = aParams[AllEParameters.P_CERT_VALIDATION_POLICY];
                            if (policyO == null)
                            {
                                throw new Exception();
                            }
                            policy = (ValidationPolicy)policyO;
                        }

                        ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

                        vs.getFindSystem().findTrustedCertificates();

                        // DONE ValueFinderFromElsewhere -> ValueFinderFromElsewhere
                        // TODO EParametersda ValidationInfoFinder al...
                        // DONE ValueFinderFromElsewhere'a bilinen initial value ve
                        // trusted cert'ları ekle...

                        ValueFinderFromElsewhere vf = new ValueFinderFromElsewhere(vs.getFindSystem().getTrustedCertificates());
                        ECompleteCertificateReferences certRefs = new ECompleteCertificateReferences(aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs)[0].getValue(0));
                        ECompleteRevocationReferences revRefs = new ECompleteRevocationReferences(aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs)[0].getValue(0));
                        vf.findRevocationValues(revRefs);
                        vf.findCertValues(certRefs);

                        _addDifferentCerts(vf.getCertificates());
                        _addDifferentCRLs(vf.getCRLs());
                        _addDifferentOCSPs(vf.getOCSPs());
                        _extractFromBasicOCSP(vf.getOCSPs());
                    }
                }
                catch (Exception aEx)
                {
                    /*  Exception neden atalım ki?? tüm dataları hem bulmadık daha, hem vermedik
                    Object searchRevData = false;
                    aParams.TryGetValue(AllEParameters.P_FORCE_STRICT_REFERENCE_USE, out searchRevData);
                    if (true.Equals(searchRevData))
                        throw new CMSSignatureException(aEx.ToString(), aEx);
                     
                }
            }

        }*/

        private void _extractFromValuesAttributes(ESignerInfo aSI)
        {
            //SignerInfo icinde certvalues attribute u varsa,burdaki sertifikalari al. CertificateValues attribute u bir tane olabilir ve
            //bir degeri olabilir
            List<EAttribute> cvAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certValues);
            if (cvAttrs.Count != 0)
            {
                try
                {
                    ECertificateValues values = new ECertificateValues(cvAttrs[0].getValue(0));
                    List<ECertificate> certs = values.getCertificates();
                    if (certs != null)
                    {
                        _addDifferentCerts(certs);
                    }
                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("Error while extracting certificates from signerinfo", aEx);
                }
            }

            List<EAttribute> rvAttrs = aSI.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationValues);
            if (rvAttrs.Count != 0)
            {
                try
                {
                    ERevocationValues values = new ERevocationValues(rvAttrs[0].getValue(0));
                    List<ECRL> crls = values.getCRLs();
                    if (crls != null)
                    {
                        _addDifferentCRLs(crls);
                    }

                    List<EBasicOCSPResponse> ocsps = values.getBasicOCSPResponses();
                    if (ocsps != null)
                    {
                        _addDifferentOCSPs(ocsps);
                        _extractFromBasicOCSP(ocsps);
                    }

                }
                catch (Exception aEx)
                {
                    throw new CMSSignatureException("Error while extracting revocation values from signerinfo", aEx);
                }
            }


        }

        private void _extractFromTimeStamp(ESignerInfo aSI, Asn1ObjectIdentifier aTSOID)
        {
            //SignerInfo icinde verilen OID li timestamp attribute u varsa,bu attribute lari al.
            List<EAttribute> attrs = aSI.getUnsignedAttribute(aTSOID);
            if (attrs.Count == 0)
            {
                attrs = aSI.getSignedAttribute(aTSOID);
            }

            if (attrs.Count != 0)
            {
                foreach (EAttribute attr in attrs)
                {
                    ESignedData sd = null;
                    try
                    {
                        EContentInfo ci = new EContentInfo(attr.getValue(0));
                        sd = new ESignedData(ci.getContent());

                    }
                    catch (Exception aEx)
                    {
                        throw new CMSSignatureException("Error while decoding timestamp attribute", aEx);
                    }

                    _extractFromSignedData(sd);
                    _extractFromValuesAttributes(sd.getSignerInfo(0));
                }
            }
        }


        private void _extractFromCounterSignature(ESignerInfo aSI, Dictionary<String, Object> aParams)
        {
            List<EAttribute> attrs = aSI.getUnsignedAttribute(AttributeOIDs.id_countersignature);
            if (attrs.Count != 0)
            {
                ESignerInfo si = null;
                foreach (EAttribute attr in attrs)
                {
                    try
                    {
                        si = new ESignerInfo(attr.getValue(0));
                        _extractFromSignerInfo(si, aParams);
                    }
                    catch (Exception aEx)
                    {
                        throw new CMSSignatureException("Error while decoding countersignature attribute", aEx);
                    }
                }
            }
        }

        private void _extractFromBasicOCSP(List<EBasicOCSPResponse> responses)
        {
            foreach (EBasicOCSPResponse response in responses)
            {
                List<ECertificate> certs = new List<ECertificate>();
                for (int i = 0; i < response.getCertificateCount(); i++)
                {
                    certs.Add(response.getCertificate(i));
                }
                _addDifferentCerts(certs);
            }

        }
        private void _extractFromOCSP(List<EOCSPResponse> responses)
        {
            if (responses != null)
            {
                foreach (EOCSPResponse response in responses)
                {
                    List<ECertificate> certs = new List<ECertificate>();
                    for (int i = 0; i < response.getBasicOCSPResponse().getCertificateCount(); i++)
                    {
                        certs.Add(response.getBasicOCSPResponse().getCertificate(i));
                    }
                    _addDifferentCerts(certs);
                }
            }
        }
        protected void _addIfDifferent(ECertificate aCert)
        {
            if (!mAllCerts.Contains(aCert))
                mAllCerts.Add(aCert);
        }

        protected void _addIfDifferent(ECRL aCRL)
        {
            if (!mAllCrls.Contains(aCRL))
                mAllCrls.Add(aCRL);
        }
        protected void _addIfDifferent(EOCSPResponse aOCSPResponse)
        {
            if (!mAllOcsps.Contains(aOCSPResponse.getBasicOCSPResponse()))
                mAllOcsps.Add(aOCSPResponse.getBasicOCSPResponse());
        }
        /**
          * Returns all certificates
          * @return
          */
        public List<ECertificate> getAllCertificates()
        {
            return mAllCerts;
        }
        /**
          * Returns all CRLs
          * @return
          */
        public List<ECRL> getAllCRLs()
        {
            return mAllCrls;
        }
        /**
          * Returns OCSPs
          * @return
          */
        public List<EBasicOCSPResponse> getAllBasicOCSPResponses()
        {
            return mAllOcsps;
        }

        protected void _addDifferentCerts(List<ECertificate> aCerts)
        {
            foreach (ECertificate cert in aCerts)
            {
                if (!mAllCerts.Contains(cert))
                    mAllCerts.Add(cert);
            }
        }

        protected void _addDifferentCRLs(List<ECRL> aCRLs)
        {
            foreach (ECRL crl in aCRLs)
            {
                if (!mAllCrls.Contains(crl))
                    mAllCrls.Add(crl);
            }
        }

        protected void _addDifferentOCSPs(List<EBasicOCSPResponse> aOCSPs)
        {
            foreach (EBasicOCSPResponse ocsp in aOCSPs)
            {
                if (!mAllOcsps.Contains(ocsp))
                    mAllOcsps.Add(ocsp);
            }
        }

        public static void main(String[] args)
        {
            try
            {
                EContentInfo ci = new EContentInfo(AsnIO.dosyadanOKU("C://Signature-C-XL-4.p7s"));
                ESignedData sd = new ESignedData(ci.getContent());

                CertificateRevocationInfoCollector cc = new CertificateRevocationInfoCollector();
                cc._extractAll(sd, null);
                List<ECertificate> certs = cc.getAllCertificates();
                List<ECRL> crls = cc.getAllCRLs();
                List<EBasicOCSPResponse> ocsps = cc.getAllBasicOCSPResponses();

                foreach (ECertificate cer in certs)
                {
                    Console.WriteLine("cer:" + cer.getSerialNumberHex());
                }

                foreach (ECRL crl in crls)
                {
                    Console.WriteLine("crl:" + UtilName.name2String(crl.getIssuer().getObject()));
                }

                foreach (EBasicOCSPResponse ocsp in ocsps)
                {
                    Console.WriteLine("ocsp:" + ocsp.getTbsResponseData().getProducedAt());
                }

            }
            catch (Exception aEx)
            {
                Console.WriteLine(aEx.StackTrace);
            }
        }
    }
}
