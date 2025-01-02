using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using SignatureValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{ 

/**
 * @author ayetgin
 */

    public class CertRevInfoExtractor
    {
        public IDictionary<String, Object> collectAllInitialValidationDataFromContextAsParams(Context context)
        {
            IDictionary<String, Object> parameters = new Dictionary<String, Object>();

            List<ECertificate> certificates = new List<ECertificate>();
            List<ECRL> crls = new List<ECRL>();
            List<EOCSPResponse> ocspResponses = new List<EOCSPResponse>();

            foreach (CertValidationData validationData in context.getAllValidationData())
            {
                certificates.AddRange(validationData.Certificates);
                crls.AddRange(validationData.Crls);
                ocspResponses.AddRange(validationData.OcspResponses);

                foreach (TimeStampValidationData vd in validationData.TSValidationData.Values)
                {
                    if (vd.CertificateValues != null)
                    {
                        certificates.AddRange(vd.CertificateValues.AllCertificates);
                    }
                    if (vd.RevocationValues != null)
                    {
                        crls.AddRange(vd.RevocationValues.AllCRLs);
                        ocspResponses.AddRange(vd.RevocationValues.AllOCSPResponses);
                    }
                }
            }

        parameters[AllEParameters.P_INITIAL_CERTIFICATES] = certificates;
        parameters[AllEParameters.P_INITIAL_CRLS] = crls;
        parameters[AllEParameters.P_INITIAL_OCSP_RESPONSES] = ocspResponses;

        return parameters;
    }

        public Pair<CertificateValues, RevocationValues> extractValidationDataCSI(Context mContext, CertificateStatusInfo csi)
        {
            CertificateValues certValues = new CertificateValues(mContext);
            RevocationValues revValues = new RevocationValues(mContext);
            //DigestMethod digestMethod = mContext.getConfig().getAlgorithmsConfig().getDigestMethod();

            UniqueCertRevInfo ucri = trace(csi);

            int counter = 0;
            foreach (ECertificate cert in ucri.getCertificates())
            {
                if (counter != 0)
                    certValues.addCertificate(cert);

                counter++;
            }

            foreach (ECRL crl in ucri.getCrls())
            {
                revValues.addCRL(crl);
            }

            foreach (EOCSPResponse ocsp in ucri.getOcspResponses())
            {
                revValues.addOCSPResponse(ocsp);
            }

            return new Pair<CertificateValues, RevocationValues>(certValues, revValues);
        }

        public Pair<CertificateValues, RevocationValues> removeDuplicateReferences(Context mContext, XMLSignature signature, CertificateValues certValues, RevocationValues revValues, ESignedData data)
        {
            CertificateValues resultingCerts = new CertificateValues(mContext);
            RevocationValues resultingRevs = new RevocationValues(mContext);

            IList<ECertificate> certsInTS = data.getCertificates();
            IList<ECertificate> certsExisting = mContext.getValidationData(signature).Certificates;

            RevocationValues revocationValues = signature.QualifyingProperties.createOrGetUnsignedProperties().UnsignedSignatureProperties.RevocationValues;
            CertificateValues certificateValues = signature.QualifyingProperties.createOrGetUnsignedProperties().UnsignedSignatureProperties.CertificateValues;
           
            for (int i = 0; i < certValues.CertificateCount; i++)
            {
                ECertificate cert = certValues.getCertificate(i);

                if ((certsInTS != null && certsInTS.Contains(cert)) || certsExisting.Contains(cert))
                {
                    continue;
                }

                IList<ECertificate> allCerts = new List<ECertificate>();
                if (certificateValues != null)
                {                
                    allCerts = certificateValues.AllCertificates;
                }

                bool exists = false;


                for (int j = 0; j < allCerts.Count; j++)
                {
                    if (allCerts[j].Equals(cert))
                    {
                        exists = true;
                        break;
                    }
                }
         
                if (!exists)
                {
                    resultingCerts.addCertificate(cert);
                }
            }

            IList<ECRL> crlsInTS = data.getCRLs();
            IList<ECRL> crlsExisting = mContext.getValidationData(signature).Crls;

            for (int i = 0; i < revValues.CRLValueCount; i++)
            {

                ECRL crl = revValues.getCRL(i);

                if ((crlsInTS != null && crlsInTS.Contains(crl)) || crlsExisting.Contains(crl))
                {
                    continue;
                }

                bool exists = false;

                IList<ECRL> allCRLs = new List<ECRL>();
                if (revocationValues != null)
                    allCRLs = revocationValues.AllCRLs;

                for (int j = 0; j < allCRLs.Count; j++)
                {
                    if (allCRLs[j].Equals(crl))
                    {
                        exists = true;
                        break;
                    }
                }

                if (!exists)
                {
                    resultingRevs.addCRL(crl);
                }
            }
            for (int i = 0; i < revValues.OCSPValueCount; i++)
            {
                EncapsulatedOCSPValue encapsulatedOCSPValue = revValues.getOCSPValue(i);
                resultingRevs.addOCSPValue(encapsulatedOCSPValue);
            }

            return new Pair<CertificateValues, RevocationValues>(resultingCerts, resultingRevs);
        }
        /**
     * @return first signatures info!
     */

        public UniqueCertRevInfo trace(SignedDataValidationResult aSDVR)
        {
            if (aSDVR == null)
                throw new XMLSignatureRuntimeException("CMS validation result is null");

            CertificateStatusInfo csi = null;

            foreach (cmssignature.validation.SignatureValidationResult result  in aSDVR.getSDValidationResults())
            {
                if (result.getCertStatusInfo() != null)
                {
                    csi = result.getCertStatusInfo();
                    return trace(csi);
                }
            }
            return null;
        }

        public UniqueCertRevInfo trace(CertificateStatusInfo csi)
        {
            UniqueCertRevInfo info = new UniqueCertRevInfo();
            _traceResources(csi, info);
            return info;
        }

        private void _traceResources(CertificateStatusInfo csi, UniqueCertRevInfo certRevInfo)
        {
            ECertificate cer = csi.getCertificate();

            List<CertificateStatusInfo> iptalZinciri = new List<CertificateStatusInfo>();

            CertificateStatusInfo cerStatus = csi;

            while (!certRevInfo.getCertificates().Contains(cer))
            {
                certRevInfo.add(cer);

                foreach (CRLStatusInfo crlStatus in cerStatus.getCRLInfoList())
                {
                    if (crlStatus.getCRLStatus() == CRLStatus.VALID)
                    {
                        certRevInfo.add(crlStatus.getCRL());
                        iptalZinciri.Add(crlStatus.getSigningCertficateInfo());
                    }
                }

                foreach (CRLStatusInfo deltaCrlStatus in cerStatus.getDeltaCRLInfoList())
                {
                    if (deltaCrlStatus.getCRLStatus() == CRLStatus.VALID)
                    {
                        certRevInfo.add(deltaCrlStatus.getCRL());
                        iptalZinciri.Add(deltaCrlStatus.getSigningCertficateInfo());
                    }
                }

                foreach(OCSPResponseStatusInfo ocspStatus in cerStatus.getOCSPResponseInfoList())
                {
                    if (ocspStatus.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
                    {
                        certRevInfo.add(ocspStatus.getOCSPResponse());
                        iptalZinciri.Add(ocspStatus.getSigningCertficateInfo());
                    }
                }

                cerStatus = cerStatus.getSigningCertficateInfo();
                if (cerStatus != null)
                    cer = cerStatus.getCertificate();
                else
                    break;
            }

            foreach (CertificateStatusInfo current in iptalZinciri)
            {
                _traceResources(current, certRevInfo);
            }

        }
    }
}