using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Find CRL from ESYA Certificate Store
     * @author IH
     */
    public class CRLFinderFromECertStore : CRLFinder
    {
        private static readonly String GETACTIVECRL = "getactivecrl";
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected override List<ECRL> _findCRL(ECertificate aCertificate)
        {
            List<ECRL> crls = new List<ECRL>();
            CRLSearchTemplate crlSearchParams = new CRLSearchTemplate();
            crlSearchParams.setIssuer(aCertificate.getCRLIssuer()); // Butun CRL leri almamak için bir optimizasyon

            if (mParameters != null)
            {
                bool getActiveCrl = mParameters.getParameterBoolean(GETACTIVECRL);
                if (getActiveCrl == true)
                {
                    crlSearchParams.setValidAt(DateTime.UtcNow);//todo yanlış bu
                    logger.Debug("Active CRL is true. " + DateTime.UtcNow + " tarihinde geçerli bir sil arıyoruz");
                }
                else
                {

                    if (mParentSystem.isDoNotUsePastRevocationInfo())
                    {
                        DateTime? baseTime = mParentSystem.getBaseValidationTime();
                        crlSearchParams.setPublishedAfter(baseTime);
                    }
                    DateTime? lastRevocTime = (mParentSystem.getLastRevocationTime() == null) ? aCertificate.getNotAfter() : mParentSystem.getLastRevocationTime();
                    crlSearchParams.setPublishedBefore(lastRevocTime);
                }
            }


            ItemSource<DepoSIL> silItemSource = null;
            CertStore certStore = null;
            try
            {
                certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
                CertStoreCRLOps certStoreCrlOps = new CertStoreCRLOps(certStore);
                silItemSource = certStoreCrlOps.listStoreCRL(crlSearchParams, new CRLType[] { CRLType.BASE });
                DepoSIL depoSIL = silItemSource.nextItem();
                
                while (depoSIL != null)
                {
                    try
                    {
                        ECRL crl = new ECRL(depoSIL.getValue());
                        crls.Add(crl);
                    }
                    catch (ESYAException e)
                    {
                        logger.Warn("Depodan alinan sil olusturulurken hata olustu", e);
                    }
                    depoSIL = silItemSource.nextItem();
                }
            }
            catch (CertStoreException aEx)
            {
                logger.Error("Sil'ler listelenirken hata olustu", aEx);
                return null;
            }
            catch (ESYAException aEx)
            {
                logger.Error("Ilk depo sil nesnesi alinirken hata olustu", aEx);
                return null;
            }
            finally
            {
                try
                {
                    if (silItemSource != null) silItemSource.close();
                    if (certStore != null) certStore.closeConnection();
                }
                catch (CertStoreException e)
                {
                    logger.Error("Connection couldn't closed", e);
                }
            }
            logger.Debug("Depodan " + crls.Count + " tane sil bulundu.");
            return crls;
        }
    }
}
