package tr.gov.tubitak.uekae.esya.api.signature.certval.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SILTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CRLSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ReferencedCRLFinder;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class ReferencedCRLFinderFromCertStore implements ReferencedCRLFinder
{
    private static final Logger logger = LoggerFactory.getLogger(ReferencedCRLFinderFromCertStore.class);

    public CRLSearchTemplate createSearchTemplate(CRLSearchCriteria aCriteria)
    {
        CRLSearchTemplate crlSearchTemplate = new CRLSearchTemplate();

        if(aCriteria.getDigestAlg() != null) {
            crlSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
            crlSearchTemplate.setHash(aCriteria.getDigestValue());
        }
        else {
            String issuer = aCriteria.getIssuer();
            if(issuer != null && issuer.length() > 0) {
                try {
                    Name name = UtilName.string2Name(issuer,true);
                    crlSearchTemplate.setIssuer(new EName(name));
                } catch (Exception e) {
                    logger.error("Error decoding CRL issuer");
                }
            }

            BigInteger number = aCriteria.getNumber();
            if(number != null)
                crlSearchTemplate.setSILNumber(number.toByteArray());
        }
        return crlSearchTemplate;
    }

    public List<ECRL> find(CRLSearchCriteria aCriteria) throws ESYAException {
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
            logger.error("Sertifika deposuna ulaşılamadı", aEx);
            return null;
        }
        CertStoreCRLOps certStoreCRLOps = new CertStoreCRLOps(certStore);

        ItemSource<DepoSIL> silItemSource = null;
        DepoSIL depoSIL;

        List<ECRL> crls = new ArrayList<ECRL>();
        try {
            silItemSource = certStoreCRLOps.listStoreCRL(aSearchTemplate, new SILTipi[]{SILTipi.BASE});
            depoSIL = silItemSource.nextItem();

            while(depoSIL != null) {
                try {
                    ECRL crl = new ECRL(depoSIL.getValue());
                    crls.add(crl);
                } catch (Exception e) {
                    logger.warn("Depodan alinan sil olusturulurken hata olustu", e);
                }
                depoSIL = silItemSource.nextItem();
            }
        } catch (CertStoreException aEx) {
            logger.error("Sil'ler listelenirken hata olustu", aEx);
            return null;
        } catch (ESYAException aEx) {
            logger.error("Ilk depo sil nesnesi alinirken hata olustu", aEx);
            return null;
        } finally {
            try {
                if (silItemSource != null) silItemSource.close();
                if (certStore != null)  certStore.closeConnection();
            } catch (CertStoreException e) {
                logger.error("Connection couldn't closed", e);
            }
        }

        return crls;
    }
}
