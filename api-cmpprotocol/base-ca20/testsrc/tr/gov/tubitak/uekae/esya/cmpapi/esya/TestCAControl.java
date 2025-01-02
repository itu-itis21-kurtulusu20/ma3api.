package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.CmpHttpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionGenerator;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.ITrustedCertificateFinder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.ProtectionGeneratorWithSign;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 1, 2010
 * Time: 1:28:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCAControl {
    private static Logger logger = LoggerFactory.getLogger(TestCAControl.class);
    private static ECertificate sender;
    private static ECertificate cACertificate;

    public static void main(String[] args) {

        try {
            client_IR_CR_Example();
            client_IR_CR_Example();
            client_IR_CR_Example();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void client_IR_CR_Example() throws Exception {

        String url = "http://localhost:8085/control";
        //String url = "http://10.203.49.71:8085/ca/control";
        CmpHttpLayer connection = new CmpHttpLayer(url,6000*1000);
        String caCert = "C:\\PRJ\\ESYA_MA3API\\cmp-base20\\ESYA201DEV-CA.cer";
        cACertificate = ECertificate.readFromFile(caCert);
        EName recipient = cACertificate.getSubject();
        EName sender = cACertificate.getSubject();
        /*

        ICAControlHandler caControlHandler = new ICAControlHandler() {
           public String decryptDBPassword(String dbUserName, byte[] encryptedDBPass, byte[] registrarCertSerial) {
               return "123456";
           }

           public Pair<Long, String> getHSMPassword(String titleStr, List<Pair<Long, String>> slotInfoPairList) {
               return null;  //To change body of implemented methods use File | Settings | File Templates.
           }
       };

        CAControlProtocol caControlProtocol = new CAControlProtocol(connection, sender, recipient,protectionTrustProvider);

        CAServiceStatus serviceStatus = caControlProtocol.getServiceStatus();
        boolean initialized = serviceStatus.isInitialized();
        System.out.println("İlklendirilmiş mi ="+initialized);

        caControlProtocol.runInit("yon1", 1L, caControlHandler);



        /*
        EPKIHeader epkiHeader = CAControlUtil.createHeader(sender, recipient);

        //Yetkilendirme başlangıç mesajı gönderiliyor.
        ArrayList<Pair<String, byte[]>> initParams = new ArrayList<Pair<String, byte[]>>();
        initParams.add(new Pair<String, byte[]>(CAControlUtil.CA_CONTROL_PARAM_NAME_DB_USER_NAME,"ESYA201DEV".getBytes()));
        initParams.add(new Pair<String, byte[]>(CAControlUtil.CA_CONTROL_PARAM_NAME_DB_USER_PASSWORD,"123456".getBytes()));
        -*/

    };

    private static IProtectionGenerator createSigner() throws Exception {
        try {
            SmartCard card = new SmartCard(CardType.UTIMACO);
            long sessionID = card.openSession(16);
            card.login(sessionID,"123456");
            List<byte[]> yetkili_cer_imza_yon1 = card.readCertificate(sessionID, "yetkili_cer_imza_yon1");
            sender = new ECertificate(yetkili_cer_imza_yon1.get(0));
            return new ProtectionGeneratorWithSign(new SCSignerWithKeyLabel(card,sessionID,16,"yetkili_imza_yon1", SignatureAlg.RSA_SHA256.getName()), sender);
        } catch (Exception exc){
            exc.printStackTrace();
        }
        return null;
    }

    private static class MyITrustedCertificateFinder implements ITrustedCertificateFinder {
        private ECertificate trustedCertificate;

        public List<ECertificate> findTrustedCertificates(EPKIMessage incomingPkiMessage) {
            ArrayList<ECertificate> list = new ArrayList<ECertificate>();

            list.add(cACertificate);
            return list;
        }

        public void setProtectionCertificate(ECertificate trustedCertificate) {
            this.trustedCertificate = trustedCertificate;
        }
    }

    private static class MyICertificationAcceptanceStrategy implements ICertificationAcceptanceStrategy {
        public boolean handleAllCertificatesTogether() {
            return false;
        }

        public List<ECertStatus> acceptCertificates(List<ICertificationParam> certificationResults) {
            List<ECertStatus> certStatuses = new ArrayList<ECertStatus>();
            try {
                for (ICertificationParam certificationResult : certificationResults) {
                    byte[] certificateEncoded = certificationResult.getCertificateEncoded();
                    ECertificate cert = new ECertificate(certificateEncoded);
                    byte[] certHash = DigestUtil.digest(DigestAlg.SHA1, certificationResult.getCertificateEncoded());
                    long certReqId = certificationResult.getCertReqMsg().getCertRequest().getCertReqId();
                    certStatuses.add(new ECertStatus(new CertStatus(certHash, certReqId)));
                }
                return certStatuses;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return certStatuses;

            //return UtilCmp.createSuccesfullCertificationStatuses(certificationResults);
        }

        public void rollbackCertificates(List<ICertificationParam> certificationResult) {
            System.out.println("Rollback triggered...");
        }
    }
}


