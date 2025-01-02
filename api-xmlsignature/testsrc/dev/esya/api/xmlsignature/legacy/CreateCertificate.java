package dev.esya.api.xmlsignature.legacy;

/*import tr.gov.tubitak.uekae.esya.genel.araclar.PKCS10IstekUretici;
import tr.gov.tubitak.uekae.esya.genel.kripto.ozelanahtarla.AY_BasitImzaci;
import tr.gov.tubitak.uekae.esya.genel.kripto.ozelanahtarla.HafizadaImzaci;
import tr.gov.tubitak.uekae.esya.genel.kripto.Ozellikler;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.CertificationRequest;

import java.security.KeyPair;
import java.security.KeyPairGenerator;                           */

/**
 * @author ahmety
 * date: Sep 10, 2009
 */
public class CreateCertificate
{

  /*  public static void main(String[] args) throws Exception
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();

        AsnIO.dosyayaz(kp.getPrivate().getEncoded(), "C:\\private2.key");

        AY_BasitImzaci imzaci = new HafizadaImzaci(kp.getPrivate(), "RSA", Ozellikler.OZET_SHA256);

        PKCS10IstekUretici piu = new PKCS10IstekUretici(imzaci);
        CertificationRequest cr = piu.pkcs10IstegiOlustur(kp.getPublic(), UtilName.string2Name("CN=TR,O=Tübitak,OU=UEKAE", true));
        AsnIO.dosyaBase64yaz(cr, "C:\\istek2.txt");
   }*/
}
