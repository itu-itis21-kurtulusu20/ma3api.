using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta
{
    /**
     * Finds delta-CRL from the local Certificate Store
     *
     * @author isilh
     */
    public class DeltaCRLFinderFromECertStore : DeltaCRLFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        protected override List<ECRL> _findDeltaCRL(ECRL aBaseCRL)
        {
            return _findDeltaCRL();
        }

        protected override List<ECRL> _findDeltaCRL(ECertificate aCertificate)
        {
            return _findDeltaCRL();
        }

        private List<ECRL> _findDeltaCRL()
        {
            List<ECRL> crls = new List<ECRL>();

            ItemSource<DepoSIL> silItemSource = null;
            DepoSIL depoSIL;
            CertStore certStore = null;
            try
            {
                certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
                CertStoreCRLOps si = new CertStoreCRLOps(certStore);
                silItemSource = si.listStoreCRL(new CRLSearchTemplate(), new CRLType[] { CRLType.DELTA });
                depoSIL = silItemSource.nextItem();
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
                logger.Error("Depodan siller alinirken hata olustu", aEx);
                return null;
            }
            catch (ESYAException aEx)
            {
                logger.Error("Ilk depo sil nesnesi alınırken hata olustu", aEx);
                return null;
            }
            finally
            {
                try
                {
                    if (silItemSource != null) silItemSource.close();
                    certStore.closeConnection();
                }
                catch (CertStoreException e)
                {
                    logger.Error("Connection couldn't closed", e);
                }
            }

            return crls;
        }
    }
}
