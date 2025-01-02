package dev.esya.api.cmssignature.certstore.update;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import org.junit.Ignore;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.asn.depo.*;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.IOException;

/**
 * Created by orcun.ertugrul on 12-Feb-18.
 */
@Ignore("Development tests")
public class CertStoreAddingCerts {

    /**
     *
     * İmzalanmış Asn yapısını svt dosyasına ekler.
     *
     * @throws Exception
     */

    @Test
    public void testAddingRootCert() throws Exception
    {
        String filePath = "C:\\a\\KokEkle\\kamusm_kok_s6_imzalanmis";

        CertStore cs = new CertStore();
        CertStoreRootCertificateOps ops = new CertStoreRootCertificateOps(cs);
        ops.addSignedRootCertificates(AsnIO.dosyadanOKU(filePath));

        /**
         * Run testConvertXmlDb function under ConvertToXmlDb for XML Store.
         * */
    }


    /**
     *
     * Sertifika deposuna eklenmek istenen sertifikayı KamuSM tarafından imzalanacak ASN formatına dönüştürür.
     * Sertifika tipi ve güvenlik seviyesi seçilmelidir.
     *
     * İmzalamayı yapan sınıfın adı CertStoreSigner. SmartCard modülü dependency'i olduğu için CMSSignature test kodları içerisinde.
     * */
    @Test
    public void convertACertToARecordThatWillBeSignedByKamuSM() throws ESYAException, CertStoreException, IOException
    {
        SertifikaTipi sertifikaTipi = SertifikaTipi.KOKSERTIFIKA;
        GuvenlikSeviyesi guvenlikSeviyesi = GuvenlikSeviyesi.LEGAL;
        ECertificate cert = ECertificate.readFromFile("C:\\a\\certToBeSignedByKamuSM.cer");

        DepoASNEklenecekKokSertifika eklenecekKokSertifika = CertStoreUtil.asnCertTOAsnEklenecek(
                cert.getObject(), sertifikaTipi, guvenlikSeviyesi);

        DepoASNKokSertifikalar kokler = new DepoASNKokSertifikalar();
        DepoASNKokSertifika kokSertifika = new DepoASNKokSertifika();
        kokSertifika.set_eklenecekSertifika(eklenecekKokSertifika);
        kokler.elements = new DepoASNKokSertifika[1];
        kokler.elements[0] = kokSertifika;

        String subject = cert.getSubject().getCommonNameAttribute();
        AsnIO.dosyayaz(kokler, "C:\\a\\KokEkle\\" + subject + "_asnStructureToBeSignedByKamuSM.dat");
    }


    /***
     *
     * KamuSM tarafından imzalanmış dosyayı okuyup içindeki sertifikaları konsol'a yazar.
     *
     * @throws Exception
     */
    @Test
    public void readSignedFile() throws Exception{

        byte[] imzaliKokSertifikalar = AsnIO.dosyadanOKU("C:\\a\\asnStructureSignedByKamuSM.dat");

        Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(imzaliKokSertifikalar);
        DepoASNImzalar imzalar = new DepoASNImzalar();
        imzalar.decode(dec);

        DepoASNImza[] imzaArray = imzalar.elements;

        for (DepoASNImza aRecord: imzaArray) {
            DepoASNEklenecekKokSertifika eklenecekKokSertifika = ((DepoASNEklenecekKokSertifika)aRecord.imzalanan.getElement());
            KOKGuvenSeviyesi kokGuvenSeviyesi = eklenecekKokSertifika.kokGuvenSeviyesi;
            ECertificate cert = new ECertificate(eklenecekKokSertifika.kokSertifikaValue.value);

            System.out.println(kokGuvenSeviyesi);
            System.out.println(cert);
        }
    }
}
