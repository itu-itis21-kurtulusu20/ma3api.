using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.tools;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find
{
    public class KamuSmCertificateFinder:ICertificateFinder {
        private static String ksmMobilWebAdres = "http://depo.kamusm.gov.tr/mobilcer/";

        public byte[] find(String certSerial){
            String httpPath = ksmMobilWebAdres+certSerial+".cer";
            byte[] bytes = BaglantiUtil.urldenVeriOku(httpPath);
            return bytes;
        }
    }
}