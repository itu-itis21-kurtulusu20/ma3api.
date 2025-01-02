package dev.esya.api.infra.certore;

import org.junit.Ignore;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XMLStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orcun.ertugrul on 12-Feb-18.
 */
@Ignore("Development tests")
public class CertStoreListCerts {

    @Test
    public void testListRootCerts() throws Exception{
        CertStore store = new CertStore();
        CertStoreRootCertificateOps rootOps = new CertStoreRootCertificateOps(store);
        List<DepoKokSertifika> rootCertificates = rootOps.listStoreRootCertificates();

        List<DepoKokSertifika> legalCerts = new ArrayList<DepoKokSertifika>();
        for (DepoKokSertifika aRootCert : rootCertificates){
            if(aRootCert.getKokGuvenSeviyesi() == GuvenlikSeviyesi.LEGAL)
                legalCerts.add(aRootCert);
        }

        for (DepoKokSertifika aLegalCert : legalCerts){
            ECertificate cert = new ECertificate(aLegalCert.getValue());
            System.out.println(cert.toString());
        }

        System.out.println(legalCerts.size() + " adet Kök bulundu.");
    }

    @Test
    public void testListXMLRootCerts()throws  Exception{
        XMLStore store = new XMLStore();
        List<DepoKokSertifika> trustedCertificates = store.getTrustedCertificates();

        List<DepoKokSertifika> legalCerts = new ArrayList<DepoKokSertifika>();
        for (DepoKokSertifika aRootCert : trustedCertificates){
            if(aRootCert.getKokGuvenSeviyesi() == GuvenlikSeviyesi.LEGAL)
                legalCerts.add(aRootCert);
        }

        for (DepoKokSertifika aCert : legalCerts){
            ECertificate cert = new ECertificate(aCert.getValue());
            System.out.println(cert.toString());
        }

        System.out.println(legalCerts.size() + " adet Kök bulundu.");
    }


    @Test
    public void testListOrganizationalRootCerts() throws Exception{
        CertStore store = new CertStore();
        CertStoreRootCertificateOps rootOps = new CertStoreRootCertificateOps(store);
        List<DepoKokSertifika> rootCertificates = rootOps.listStoreRootCertificates();

        List<DepoKokSertifika> legalCerts = new ArrayList<DepoKokSertifika>();
        for (DepoKokSertifika aRootCert : rootCertificates){
            if(aRootCert.getKokGuvenSeviyesi() == GuvenlikSeviyesi.ORGANIZATIONAL)
                legalCerts.add(aRootCert);
        }

        for (DepoKokSertifika aLegalCert : legalCerts){
            ECertificate cert = new ECertificate(aLegalCert.getValue());
            System.out.println(cert.toString());
        }
    }


    @Test
    public void testListNONRootCerts() throws Exception{
        CertStore store = new CertStore();
        CertStoreCertificateOps certificateOps = new CertStoreCertificateOps(store);
        List<ECertificate> certificates = certificateOps.listCertificates();

        for (ECertificate cert:certificates) {
            System.out.println(cert.toString());

        }
    }



}
