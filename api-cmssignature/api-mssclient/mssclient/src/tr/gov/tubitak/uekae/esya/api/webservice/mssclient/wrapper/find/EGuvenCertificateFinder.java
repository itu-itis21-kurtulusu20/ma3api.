package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.find;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.util.EBaglantiUtil;

/**
 * Created by IntelliJ IDEA.
 * User: int2
 * Date: 29.05.2012
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
public class EGuvenCertificateFinder implements ICertificateFinder{
    private Logger logger = LoggerFactory.getLogger(EGuvenCertificateFinder.class);
    public static String EGUVEN_LDAP_IP =  "85.235.93.62";
       public byte[] find(String certSerial){
        String dizinAdresi="ldap://"+EGUVEN_LDAP_IP+"/CERTIFICATESERIALNUMBER="+certSerial+",C=TR,dc=e-guven,dc=com?userCert?sub?(objectClass=*)";
           byte[] bytes = null;
           try
           {
                bytes = EBaglantiUtil.dizindenVeriOku(dizinAdresi);
           }
           catch(Exception exc)
           {
               logger.error("Error in EGuvenCertificateFinder:", exc);
           }
           if(bytes == null)
           {
               if(certSerial.startsWith("00")){
                   String serialOther = certSerial.substring(2);
                   dizinAdresi="ldap://"+EGUVEN_LDAP_IP+"/CERTIFICATESERIALNUMBER="+serialOther+",C=TR,dc=e-guven,dc=com?userCert?sub?(objectClass=*)";
                   bytes = EBaglantiUtil.dizindenVeriOku(dizinAdresi);
               }
           }
        return bytes;
    }
}
