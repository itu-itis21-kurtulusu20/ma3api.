using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    public class CertRevocationInfoFinder
    {
        private readonly List<ECertificate> mCertificates = new List<ECertificate>();
        readonly List<CertRevocationInfo> mCertRevList = new List<CertRevocationInfo>();
        List<ECertificate> mTrustedCerts = null;

        public CertRevocationInfoFinder(bool gelismis)
        {
            if(gelismis){
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.CMSIMZAGELISMIS);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message );
            }
            }
        }

        //@SuppressWarnings("unchecked")
        public CertificateStatusInfo validateCertificate(ECertificate aCer, Dictionary<String, Object> aParams, DateTime? aDate)
        {

            CheckerResult result = new CheckerResult();
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            Dictionary<String, Object>.KeyCollection keys = aParams.Keys;
            foreach (String key in keys)
            {
                params_[key] = aParams[key];
            }
            //params_.putAll(aParams);

            CertificateChecker certChecker = new CertificateChecker();
            certChecker.setParameters(params_);
            if (certChecker.checkCertificateAtTime(aCer, result, aDate, true, true, false) == false)
            {
                throw new CertificateValidationException(aCer, result);
            }

            CertificateStatusInfo csi = ((CertificateCheckerResultObject)result.getResultObject()).getCertStatusInfo();
            mTrustedCerts = csi.getTrustedCertificates();

            return csi;
        }

        public CertRevocationInfoFinder(CertificateStatusInfo aCSI)
        {
            _fillLists(aCSI);
        }


        private void _fillLists(CertificateStatusInfo aStatus)
        {
            ECertificate cer = aStatus.getCertificate();
            List<CertificateStatusInfo> iptalSMDurumlari = new List<CertificateStatusInfo>();

            CertificateStatusInfo cerStatus = aStatus;
            while (!mTrustedCerts.Contains(cer) && !mCertificates.Contains(cer))
            {
                mCertificates.Add(cer);
                List<CRLStatusInfo> crlStatusList = cerStatus.getCRLInfoList();
                List<CRLStatusInfo> deltaCrlStatusList = cerStatus.getDeltaCRLInfoList();
                List<OCSPResponseStatusInfo> ocspStatusList = cerStatus.getOCSPResponseInfoList();
               
                List<ECRL> crlList = new List<ECRL>();
                foreach (CRLStatusInfo crlStatus in crlStatusList)
                {
                    if(crlStatus.getCRLStatus() == CRLStatus.VALID)
                    {
                        crlList.Add(crlStatus.getCRL());
                        iptalSMDurumlari.Add(crlStatus.getSigningCertficateInfo());
                    }
                }

                foreach (CRLStatusInfo crlStatus in deltaCrlStatusList)
                {
                    if(crlStatus.getCRLStatus() == CRLStatus.VALID)
                    {
                        crlList.Add(crlStatus.getCRL());
                        iptalSMDurumlari.Add(crlStatus.getSigningCertficateInfo());
                    }
                }

                List<EBasicOCSPResponse> basicOCSPResponseList = new List<EBasicOCSPResponse>();
                foreach (OCSPResponseStatusInfo ocspStatus in ocspStatusList)
                {
                    if (ocspStatus.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
                    {
                        basicOCSPResponseList.Add(ocspStatus.getOCSPResponse().getBasicOCSPResponse());
                        iptalSMDurumlari.Add(ocspStatus.getSigningCertficateInfo());
                    }
                }
                
                //TODO sil de ocsp de null gelirse kontrol et
                CertRevocationInfo info = new CertRevocationInfo(cerStatus.getCertificate(), 
                    crlList.ToArray(), basicOCSPResponseList.ToArray());
                mCertRevList.Add(info);
                
                cerStatus = cerStatus.getSigningCertficateInfo();
                cer = cerStatus.getCertificate();
            }

            if (mTrustedCerts.Contains(cer) && !mCertificates.Contains(cer))
            {
                mCertificates.Add(cer);
                CertRevocationInfo info = new CertRevocationInfo(cer, null, null);
                mCertRevList.Add(info);
            }

            foreach (CertificateStatusInfo csi in iptalSMDurumlari)
            {
                _fillLists(csi);
            }
        }

        public List<CertRevocationInfo> getCertRevRefs(CertificateStatusInfo aCSI)
        {
            _fillLists(aCSI);
            return mCertRevList;
        }

        public List<ECertificate> getCertificates()
        {
            return mCertificates;
        }

        public List<EBasicOCSPResponse> getOCSPs()
        {
            List<EBasicOCSPResponse> all = new List<EBasicOCSPResponse>();
            foreach (CertRevocationInfo cri in mCertRevList)
            {
                EBasicOCSPResponse[] ocsps = cri.getOCSPResponses();
                if (ocsps != null)
                {
                    foreach (EBasicOCSPResponse ocsp in ocsps)
                    {
                        if (!all.Contains(ocsp))
                            all.Add(ocsp);
                    }
                }
            }

            return all;
        }

        public List<ECRL> getCRLs()
        {
            List<ECRL> all = new List<ECRL>();
            foreach (CertRevocationInfo cri in mCertRevList)
            {
                ECRL[] crls = cri.getCRLs();
                if (crls != null)
                {
                    foreach (ECRL crl in crls)
                    {
                        if (!all.Contains(crl))
                            all.Add(crl);
                    }
                }
            }

            return all;
        }


        public class CertRevocationInfo
        {
            readonly ECertificate mCertificate;
            readonly ECRL[] mCRLs;
            readonly EBasicOCSPResponse[] mOCSPResponse;

            public CertRevocationInfo(ECertificate aCert, ECRL[] aCRLs, EBasicOCSPResponse [] aOCSPResp)
            {
                mCertificate = aCert;
                mCRLs = aCRLs;
                mOCSPResponse = aOCSPResp;
            }

            public ECertificate getCertificate()
            {
                return mCertificate;
            }

            public ECRL[] getCRLs()
            {
                return mCRLs;
            }

            public EBasicOCSPResponse [] getOCSPResponses()
            {
                return mOCSPResponse;
            }
        }

    }
}
