package tr.gov.tubitak.uekae.esya.api.signature.certval.impl;

import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponderID;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreOCSPOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.OCSPSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ReferencedOCSPResponseFinder;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponderID;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class ReferencedOCSPResponseFinderFromCertStore implements ReferencedOCSPResponseFinder
{
    private static final Logger logger = LoggerFactory.getLogger(ReferencedOCSPResponseFinderFromCertStore.class);

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
                    responderID.set_byKey(new Asn1OctetString(aCriteria.getOCSPResponderIDByKey()));
                    rid = new EResponderID(responderID);
                    ocspSearchTemplate.setOCSPResponderID(rid.getEncoded());
                }
                else if(aCriteria.getOCSPResponderIDByName() != null){
                    Name name = UtilName.string2Name(aCriteria.getOCSPResponderIDByName(), true);
                    rid = new EResponderID(new EName(name));
                    ocspSearchTemplate.setOCSPResponderID(rid.getEncoded());
                }
            } catch (Exception x){
                logger.error("Error decoding responder ID ", x);
            }

            if(aCriteria.getProducedAt() != null)
                ocspSearchTemplate.setProducedAt(aCriteria.getProducedAt());
        }

        return ocspSearchTemplate;
    }

    public List<EOCSPResponse> find(OCSPSearchCriteria aCriteria) throws ESYAException
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
            logger.error("Sertifika deposuna ulaşılamadı", aEx);
            return null;
        }
        CertStoreOCSPOps certStoreOCSPOps = new CertStoreOCSPOps(certStore);

        List<EOCSPResponse> ocsps = new ArrayList<EOCSPResponse>();
        EOCSPResponse ocsp = null;

        //** orjinalinin kopyasi
        EBasicOCSPResponse bo = null;
        try {
            bo = certStoreOCSPOps.listOCSPResponses(aSearchTemplate);
        }
        catch (CertStoreException aEx) {
            logger.error("Depodan ocsp cevabi alinirken hata olustu", aEx);
        }
        finally {
            try {
                certStore.closeConnection();
            }
            catch (CertStoreException e) {
                logger.error("Connection could not closed", e);
            }
        }
        if(bo == null)
            return null;
        ocsp = EOCSPResponse.getEOCSPResponse(bo);
        ocsps.add(ocsp);
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
