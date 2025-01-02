package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.tools.IniFile;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.CmpTcpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

import java.io.File;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 1, 2010
 * Time: 1:28:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificationTest {
    //private static ILogger logger = LoggerFactory.getLogger(CertificationTest.class);

    public static void main(String[] args) {
        try {
            client_IR_CR_Example();
        } catch (Exception e) {
      //      logger.error(e, e);
        }
    }


    public static void client_IR_CR_Example() throws Exception {
        IniFile iniFile = new IniFile();
        iniFile.loadIni("test.ini");
        String ip = iniFile.getValue("CERTIFICATION","IP");
        int port = iniFile.getIntValue("CERTIFICATION","PORT");
        String caCert = iniFile.getValue("CERTIFICATION","CACERT");
        String senderCert = iniFile.getValue("CERTIFICATION","SENDERCERT");
        String authCode = iniFile.getValue("CERTIFICATION","AUTHCODE");
        String refNo = iniFile.getValue("CERTIFICATION","REFNO");
        long sertifikaTalepID = iniFile.getLongValue("CERTIFICATION","SERTIFIKATALEPID");
        String cardNo = iniFile.getValue("CERTIFICATION","CARDNO");
        long cardManufacturerNo = iniFile.getLongValue("CERTIFICATION","CARDMANUFACTURERNO");

        

        ECertificate cACertificate = new ECertificate(new File(caCert));
        ECertificate senderCertExample = new ECertificate(new File(senderCert));

        CmpTcpLayer cmpTcpLayer = new CmpTcpLayer(ip, port);


        PKIMessagePacket requestPacket;
        PBMParameterFinder pbmParameterFinder = new PBMParameterFinder(authCode);
        IProtectionTrustProvider protectionTrustProvider =new ProtectionTrustProvider(
                Arrays.asList(
                        (IProtectionController) new ProtectionControllerWithHMac(pbmParameterFinder)
                ),
                new ProtectionGeneratorWithHMac(pbmParameterFinder));


        EName sender = senderCertExample.getSubject();
        EName recipient = cACertificate.getSubject();
        String senderKID = refNo;  // personel refno
        ArrayList<ICertificationParam> certificationParams = new ArrayList<ICertificationParam>();

        KeyPair proKeyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.RSA_PKCS1);
        decryptor.init(proKeyPair.getPrivate(), new ParamsWithLength(1024));

        KeyGenerationOnServer generationOnServer = new KeyGenerationOnServer(
                sender, proKeyPair.getPublic(), sertifikaTalepID, cardNo, cardManufacturerNo, decryptor
        );
        
        certificationParams.add(generationOnServer);

        ICertificationAcceptanceStrategy acceptanceStrategy = new MyICertificationAcceptanceStrategy();

        InitializationProtocol certificationProtocol = new InitializationProtocol(
                cmpTcpLayer, sender, recipient, senderKID,
                certificationParams, protectionTrustProvider,
                acceptanceStrategy);

        certificationProtocol.runProtocol();

        long time = System.currentTimeMillis() % 10000000;
        AsnIO.dosyayaz(generationOnServer.getCertificate().getObject(), "NewCert" + time + ".cer");
        AsnIO.dosyayaz(generationOnServer.getPrivateKey().getEncoded(), "PrivKey" + time + ".bin");
/*        PfxOlusturucu pfxOlusturucu = new PfxOlusturucu("123456");
        byte[] pfxBytes = pfxOlusturucu.pfxOlustur(new ECertificate[]{generationOnServer.getCertificate()}, new PrivateKey[]{generationOnServer.getPrivateKey()});
        AsnIO.dosyayaz(pfxBytes,"NewPfx"+time+".pfx");*/
    }


/*    private static void Ini_Example() throws Exception{
        byte[] iv = DigestUtil.digest(DigestAlg.MD5, "DontTouchMyIV".getBytes());  // 16 byte  IV
        byte[] keyBytes = DigestUtil.digest(DigestAlg.MD5, "GizliAnahtarKendinYaz".getBytes());  // 16 byte  static key

        String dpPass = "test123456";
//        byte[] keyBytes = StringUtil.toByteArray("7611E097B0007403EAB6E47E38690A6A9770A1EAEB3B20FF");


        {   // encryption
            BufferedCipher encryptor = Crypto.getEncryptor(CipherAlg.AES128_CBC);
            encryptor.init(keyBytes, new ParamsWithIV(iv));
            byte[] keyEncrypted = encryptor.doFinal(dpPass.getBytes());
            IniFile iniFile = new IniFile();
            iniFile.setValue("GENERAL","PASSWORD", StringUtil.toString(keyEncrypted));
            iniFile.saveIni("iniexample.ini");
        }

        {   // Decryption
            IniFile iniFile = new IniFile();
            iniFile.loadIni("iniexample.ini");
            byte[] keyEncrypted = StringUtil.toByteArray(iniFile.getValue("GENERAL", "PASSWORD"));
            BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.AES128_CBC);
            decryptor.init(keyBytes, new ParamsWithIV(iv));
            byte[] keyDecrypted = decryptor.doFinal(keyEncrypted);

            System.out.println("KeyDecrypted:"+new String(keyDecrypted));

        }
    }*/


    private static class MyITrustedCertificateFinder implements ITrustedCertificateFinder {
        private ECertificate trustedCertificate;

        public List<ECertificate> findTrustedCertificates(EPKIMessage incomingPkiMessage) {
            ArrayList<ECertificate> list = new ArrayList<ECertificate>();
            ECertificate cACertificate = null;
            try {
                cACertificate = new ECertificate(new File("D:\\Projects\\ESYA_MA3API\\cmpapi\\headzeldalsm.cer"));
            } catch (Exception e) {
                throw new RuntimeException("Error while getting CA Certificate:" + e.getMessage(), e);
            }
            list.add(cACertificate);
            return list;
        }

        public void setProtectionCertificate(ECertificate trustedCertificate) {
            this.trustedCertificate = trustedCertificate;
        }
    }

    private static class MyICertificationAcceptanceStrategy implements ICertificationAcceptanceStrategy {
        public List<ECertStatus> acceptCertificates(List<ICertificationParam> certificationResults) {
/*            try {
                List<CertStatus> certStatuses = new ArrayList<CertStatus>();
                for (ICertificationParam certificationResult : certificationResults) {
                    System.out.println(certificationResult.getCertificate().toString());
                    byte[] certHash = DigestUtil.digest(DigestAlg.SHA1, certificationResult.getCertificate().getEncoded());
                    long certReqId = certificationResult.getCertReqMsg().certReq.certReqId.value;
                    certStatuses.add(new CertStatus(certHash, certReqId));
                }
                return certStatuses;
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }*/
            return UtilCmp.createSuccesfullCertificationStatuses(certificationResults);
        }

        public void rollbackCertificates(List<ICertificationParam> certificationResult) {
            System.out.println("Rollback triggered...");
        }
    }
}


