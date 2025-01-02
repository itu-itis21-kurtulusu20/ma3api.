using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Logger = log4net.ILog;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval.impl
{
    public class ReferencedOCSPResponseFinderFromCertStore : ReferencedOCSPResponseFinder
    {
        private static Logger logger = log4net.LogManager.GetLogger(typeof(ReferencedOCSPResponseFinderFromCertStore));

        public OCSPSearchTemplate createSearchTemplate(OCSPSearchCriteria aCriteria)
        {
            OCSPSearchTemplate ocspSearchTemplate = new OCSPSearchTemplate();

            EResponderID rid = null;

            // if hash is present in search criteria, search according to it
            ocspSearchTemplate.setHash(aCriteria.getDigestValue());

            if(aCriteria.getDigestAlg() != null) {
                ocspSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
            }
            if (aCriteria.getDigestValue()==null)
            {  // otherwise use other search criteria
                try {
                    if(aCriteria.getOCSPResponderIDByKey() != null){
                        ResponderID responderID = new ResponderID();
                        responderID.Set_byKey(new Asn1OctetString(aCriteria.getOCSPResponderIDByKey()));
                        rid = new EResponderID(responderID);
                        ocspSearchTemplate.setOCSPResponderID(rid.getEncoded());
                    }
                    else if(aCriteria.getOCSPResponderIDByName() != null){
                        Name name = UtilName.string2Name(aCriteria.getOCSPResponderIDByName(), true);
                        rid = new EResponderID(new EName(name));
                        ocspSearchTemplate.setOCSPResponderID(rid.getEncoded());
                    }
                } catch (Exception x){
                    logger.Error("Error decoding responder ID ", x);
                }

                if(aCriteria.getProducedAt() != null)
                    ocspSearchTemplate.setProducedAt(aCriteria.getProducedAt());
            }

            return ocspSearchTemplate;
        }

        public List<EOCSPResponse> find(OCSPSearchCriteria aCriteria)
        {
            if (aCriteria == null)
                throw new ArgErrorException("Expected search criteria");

            OCSPSearchTemplate ocspSearchTemplate = createSearchTemplate(aCriteria);

            return locate(ocspSearchTemplate);
        }

        public List<EOCSPResponse> locate(OCSPSearchTemplate aSearchTemplate)
        {
            CertStore certStore;
            try {
                certStore = new CertStore();
            }
            catch (CertStoreException aEx) {
                logger.Error("Sertifika deposuna ulaşılamadı", aEx);
                return null;
            }
            CertStoreOCSPOps certStoreOCSPOps = new CertStoreOCSPOps(certStore);

            List<EOCSPResponse> ocsps = new List<EOCSPResponse>();
            EOCSPResponse ocsp = null;

            //** orjinalinin kopyasi
            EBasicOCSPResponse bo = null;
            try {
                bo = certStoreOCSPOps.listOCSPResponses(aSearchTemplate);
            }
            catch (CertStoreException aEx) {
                logger.Error("Depodan ocsp cevabi alinirken hata olustu", aEx);
            }
            finally {
                try {
                    certStore.closeConnection();
                }
                catch (CertStoreException e) {
                    logger.Error("Connection could not closed", e);
                }
            }
            if(bo == null)
                return null;
            ocsp = EOCSPResponse.getEOCSPResponse(bo);
            ocsps.Add(ocsp);
            return ocsps;
            //**

            /* depoya EOCSPResponse olarak kaydetmis olsaydik bu kodu kullanabilirdik
            try {
                ocspItemSource = certStoreOCSPOps.listStoreOCSPResponses(aSearchTemplate);
                depoOCSP = ocspItemSource.nextItem();

                while(depoOCSP != null) {
                    try {
                        EOCSPResponse ocsp = new EOCSPResponse(depoOCSP.getmOCSPValue());
                        ocsps.add(ocsp);
                    } catch (Exception e) {
                        logger.warn("Depodan alinan ocsp olusturulurken hata olustu", e);
                    }
                }
            } catch (CertStoreException aEx) {
                logger.error("OCSP'ler listelenirken hata olustu", aEx);
                return null;
            } catch (ESYAException aEx) {
                logger.error("Ilk depo ocsp nesnesi alinirken hata olustu", aEx);
                return null;
            } finally {
                try {
                    if (ocspItemSource != null) ocspItemSource.close();
                    if (certStore != null)  certStore.closeConnection();
                } catch (CertStoreException e) {
                    logger.error("Connection couldn't closed", e);
                }
            }

            return ocsps;
            //*/
        }
    }
}
