package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader_pvno;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlReqMsg;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYACAControlReqMsg_islemtipi;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAParametreleri;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.CmpHttpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.UtilCmp;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 1, 2010
 * Time: 1:28:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificationTest {
    private static Logger logger = LoggerFactory.getLogger(CertificationTest.class);
    private static ECertificate sender;
    private static ECertificate cACertificate;

    public static void main(String[] args) {

        try {
            client_IR_CR_Example();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    private static EPKIHeader createPkiHeaderTemplate() throws ESYAException {
        PKIHeader pkiHeader = new PKIHeader();
        pkiHeader.pvno = new PKIHeader_pvno(2);
        pkiHeader.sender = new GeneralName();
        pkiHeader.recipient = new GeneralName();
        //mesaj zamani
        pkiHeader.messageTime = new Asn1GeneralizedTime();
        try {
            pkiHeader.messageTime.setTime(Calendar.getInstance());
        } catch (Asn1Exception aEx) {
            throw new RuntimeException("Message Time Couldnt Created", aEx);
        }

        byte[] senderNonceBytes = new byte[16];

        //generate 128 bits pseudorandom number
        try {
            java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(senderNonceBytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error while generating transactionID-senderNonce" + ex.getMessage(), ex);
        }
        pkiHeader.senderNonce = new Asn1OctetString(senderNonceBytes);
        EPKIHeader epkiHeader = new EPKIHeader(pkiHeader);
        return epkiHeader;
    }



    public static void client_IR_CR_Example() throws Exception {

        String url = "http://localhost:8085/cmp";
        String caCert = "C:\\PRJ\\ESYA_MA3API\\cmp-base20\\ESYA201DEV-CA.cer";

        String cardNo = "3";
        long cardManufacturerNo = 3;
        cACertificate = ECertificate.readFromFile(caCert);

        CmpHttpLayer connection = new CmpHttpLayer(url,6000*1000);

        IProtectionGenerator signer = createSigner();
        /*String macCode = "23232232323223322325453456132AB565454CD54534EF6565656656";
        PBMParameterFinder pbmParameterFinder = new PBMParameterFinder(macCode);
        ProtectionControllerWithHMac protectionControllerWithHMac = new ProtectionControllerWithHMac(pbmParameterFinder);
        IProtectionTrustProvider protectionTrustProvider = new ProtectionTrustProvider(Arrays.asList(protectionControllerWithHMac),new ProtectionGeneratorWithHMac(pbmParameterFinder));*/

        IProtectionTrustProvider protectionTrustProvider = new ProtectionTrustProvider(
                Arrays.<IProtectionController>asList(new ProtectionControllerWithSign(new MyITrustedCertificateFinder())),signer
        );



        EName recipient = cACertificate.getSubject();
        ArrayList<ICertificationParam> certificationParams = new ArrayList<ICertificationParam>();

        KeyPair proKeyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.RSA_PKCS1);
        decryptor.init(proKeyPair.getPrivate(), new ParamsWithLength(1024));

        KeyGenerationOnServer generationOnServer = new KeyGenerationOnServer(sender.getSubject(),proKeyPair.getPublic(), 728L, cardNo, cardManufacturerNo, decryptor);

        certificationParams.add(generationOnServer);


        ICertificationAcceptanceStrategy acceptanceStrategy = new MyICertificationAcceptanceStrategy();

        String senderKID = StringUtil.toString(DigestUtil.digest(DigestAlg.SHA1, sender.getSubjectPublicKeyInfo().getSubjectPublicKey()));

        InitializationProtocol certificationProtocol = new InitializationProtocol(
                connection, sender.getSubject(), recipient, senderKID, // empty
                certificationParams, protectionTrustProvider,
                acceptanceStrategy);

        try{
            certificationProtocol.setLocaleTag("en-US");
            certificationProtocol.runProtocol();
        }catch (Exception exc){
            exc.printStackTrace();
        }

        long time = System.currentTimeMillis() % 10000000;
      //  AsnIO.dosyayaz(generationOnServer.getCertificate().getObject(), "NewCert" + time + ".cer");
      //  AsnIO.dosyayaz(generationOnServer.getPrivateKey().getEncoded(), "PrivKey" + time + ".bin");

        ICertificationParam iCertificationParam = certificationParams.get(0);
        ECertResponse certResponse = iCertificationParam.getCertResponse();
        if(!certResponse.isAccepted()){
            certResponse.getPkiStatusInfo().getErrorString(",");
        }
        /*        PfxOlusturucu pfxOlusturucu = new PfxOlusturucu("123456");
        byte[] pfxBytes = pfxOlusturucu.pfxOlustur(new ECertificate[]{generationOnServer.getCertificate()}, new PrivateKey[]{generationOnServer.getPrivateKey()});
        AsnIO.dosyayaz(pfxBytes,"NewPfx"+time+".pfx");*/
    }

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


