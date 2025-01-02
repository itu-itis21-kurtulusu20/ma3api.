package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find;

import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.util.EBaglantiUtil;

/**
 * Created by IntelliJ IDEA.
 * User: int2
 * Date: 29.05.2012
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class KamuSmCertificateFinder implements ICertificateFinder {
    private static String ksmMobilWebAdres = "http://depo.kamusm.gov.tr/mobilcer/";

    public byte[] find(String certSerial){
        String httpPath = ksmMobilWebAdres+certSerial+".cer";
        byte[] bytes = EBaglantiUtil.urldenVeriOku(httpPath);
        return bytes;
    }
}
