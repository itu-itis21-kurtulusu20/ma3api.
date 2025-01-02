using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.save;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.infra.util;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * The container class for certificate and CRL savers. 
     */
    public class SaveSystem
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private readonly List<CertificateSaver> mCertificateSavers = new List<CertificateSaver>();
        private readonly List<CRLSaver> mCRLSavers = new List<CRLSaver>();
        private readonly List<OCSPResponseSaver> mOCSPResponseSavers = new List<OCSPResponseSaver>();

        private readonly HashMultiMap<EIssuerAndSerialNumber, EOCSPResponse> mRegisteredOCSPs = new HashMultiMap<EIssuerAndSerialNumber, EOCSPResponse>();
        private readonly HashMultiMap<EIssuerAndSerialNumber, ECRL> mRegisteredCRLs = new HashMultiMap<EIssuerAndSerialNumber, ECRL>();



        public SaveSystem()
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

        /**
         * Sertifika Kaydedicileri doner
         */
        public List<CertificateSaver> getCertificateSavers()
        {
            return mCertificateSavers;
        }

        /**
         * Sertifika Saver ekler
         */
        public void addCertificateSaver(CertificateSaver aCertificateSaver)
        {
            mCertificateSavers.Add(aCertificateSaver);
        }

        /**
         * SİL Kaydedicileri döner
         */
        public List<CRLSaver> getCRLSavers()
        {
            return mCRLSavers;
        }

        /**
         * SiL Saver ekler
         */
        public void addCRLSaver(CRLSaver aCRLSaver)
        {
            mCRLSavers.Add(aCRLSaver);
        }

        /**
         * OCSP kaydedicileri doner
         */
        public List<OCSPResponseSaver> getOCSPResponseSavers()
        {
            return mOCSPResponseSavers;
        }

        /**
         * OCSP Saver ekler
         */
        public void addOCSPResponseSaver(OCSPResponseSaver aOCSPResponseSaver)
        {
            mOCSPResponseSavers.Add(aOCSPResponseSaver);
        }

        public void registerOCSP(ECertificate aCert, EOCSPResponse aOCSPResponse)
        {
            if (aOCSPResponse != null)
                mRegisteredOCSPs.put(new EIssuerAndSerialNumber(aCert), aOCSPResponse);
        }

        public void registerCRL(ECertificate aCert, ECRL aCRL)
        {
            if (aCRL != null)
                mRegisteredCRLs.put(new EIssuerAndSerialNumber(aCert), aCRL);
        }

        public void processOCSPs(ECertificate aCert)
        {
            List<EOCSPResponse> ocspList = mRegisteredOCSPs.get(new EIssuerAndSerialNumber(aCert));

            if (ocspList != null)
                for (int o = 0; o < ocspList.Count; o++)
                {
                    for (int i = 0; i < mOCSPResponseSavers.Count; i++)
                    {
                        if (mOCSPResponseSavers[i] != null)
                        {
                            try
                            {
                                mOCSPResponseSavers[i].addOCSP(ocspList[o], aCert);
                            }
                            catch (Exception x)
                            {
                                logger.Error("Error occurred saving OCSP Response", x);
                            }
                        }
                    }
                }

            cleanOCSPs(aCert);
        }

        void processCRLs(ECertificate aCert)
        {
            List<ECRL> crlList = mRegisteredCRLs.get(new EIssuerAndSerialNumber(aCert));

            if (crlList != null)
                for (int c = 0; c < crlList.Count; c++)
                {
                    for (int i = 0; i < mCRLSavers.Count; i++)
                    {
                        try
                        {
                            if (mCRLSavers[i] != null)
                                mCRLSavers[i].addCRL(crlList[c]);
                        }
                        catch (ESYAException x)
                        {
                            logger.Error("Error occurred saving CRL", x);
                        }
                    }
                }

            cleanCRLs(aCert);
        }

        public void processRegisteredItems(ECertificate aCert)
        {
            processOCSPs(aCert);
            processCRLs(aCert);
        }

        public void processRegisteredItems(CertificateStatusInfo aCsi)
        {
            if (aCsi == null)
                return;

            processRegisteredItems(aCsi.getCertificate());
            processRegisteredItems(aCsi.getSigningCertficateInfo());
        }


        public void cleanOCSPs(ECertificate aCert)
        {
            mRegisteredOCSPs.remove(new EIssuerAndSerialNumber(aCert));
        }

        public void cleanCRLs(ECertificate aCert)
        {
            mRegisteredCRLs.remove(new EIssuerAndSerialNumber(aCert));
        }

        public void cleanRegisteredItems(ECertificate aCert)
        {
            cleanOCSPs(aCert);
            cleanCRLs(aCert);
        }

        public void cleanRegisteredItems(CertificateStatusInfo aCsi)
        {
            if (aCsi == null)
                return;

            cleanRegisteredItems(aCsi.getCertificate());
            cleanRegisteredItems(aCsi.getSigningCertficateInfo());
        }
    }
}
