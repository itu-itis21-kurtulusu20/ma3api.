using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Logger = log4net.ILog;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval.impl
{
    public class ReferencedCRLFinderFromCertStore : ReferencedCRLFinder
    {
        private static Logger logger = log4net.LogManager.GetLogger(typeof(ReferencedCRLFinderFromCertStore));

        public CRLSearchTemplate createSearchTemplate(CRLSearchCriteria aCriteria)
        {
            CRLSearchTemplate crlSearchTemplate = new CRLSearchTemplate();

            if(aCriteria.getDigestAlg() != null) {
                crlSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
                crlSearchTemplate.setHash(aCriteria.getDigestValue());
            }
            else {
                String issuer = aCriteria.getIssuer();
                if(issuer != null && issuer.Length > 0) {
                    try {
                        Name name = UtilName.string2Name(issuer,true);
                        crlSearchTemplate.setIssuer(new EName(name));
                    } catch (Exception e) {
                        logger.Error("Error decoding CRL issuer");
                    }
                }

                BigInteger number = aCriteria.getNumber();
                if(number != null)
                    crlSearchTemplate.setSILNumber(number.GetData());
            }
            return crlSearchTemplate;
        }

        public List<ECRL> find(CRLSearchCriteria aCriteria)
        {
            if (aCriteria == null)
                throw new ArgErrorException("Expected search criteria");

            CRLSearchTemplate crlSearchTemplate = createSearchTemplate(aCriteria);

            return locate(crlSearchTemplate);
        }

        public List<ECRL> locate(CRLSearchTemplate aSearchTemplate) {
            CertStore certStore;
            try {
                certStore = new CertStore();
            }
            catch (CertStoreException aEx) {
                logger.Error("Sertifika deposuna ulaşılamadı", aEx);
                return null;
            }
            CertStoreCRLOps certStoreCRLOps = new CertStoreCRLOps(certStore);

            ItemSource<DepoSIL> silItemSource = null;
            DepoSIL depoSIL;

            List<ECRL> crls = new List<ECRL>();
            try {
                silItemSource = certStoreCRLOps.listStoreCRL(aSearchTemplate, new CRLType[]{CRLType.BASE});
                depoSIL = silItemSource.nextItem();

                while(depoSIL != null) {
                    try {
                        ECRL crl = new ECRL(depoSIL.getValue());
                        crls.Add(crl);
                    } catch (Exception e) {
                        logger.Warn("Depodan alinan sil olusturulurken hata olustu", e);
                    }
                    depoSIL = silItemSource.nextItem();
                }
            } catch (CertStoreException aEx) {
                logger.Error("Sil'ler listelenirken hata olustu", aEx);
                return null;
            } catch (ESYAException aEx) {
                logger.Error("Ilk depo sil nesnesi alinirken hata olustu", aEx);
                return null;
            } finally {
                try {
                    if (silItemSource != null) silItemSource.close();
                    if (certStore != null)  certStore.closeConnection();
                } catch (CertStoreException e) {
                    logger.Error("Connection couldn't closed", e);
                }
            }

            return crls;
        }
    }
}
