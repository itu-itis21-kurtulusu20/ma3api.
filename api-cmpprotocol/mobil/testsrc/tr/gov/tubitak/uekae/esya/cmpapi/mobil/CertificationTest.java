package tr.gov.tubitak.uekae.esya.cmpapi.mobil;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.tools.IniFile;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;
import tr.gov.tubitak.uekae.esya.asn.crmf.ProofOfPossession;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.CertificateSign;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.POPSigner;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.CmpTcpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

import java.io.File;
import java.math.BigInteger;
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
    private static Logger logger = LoggerFactory.getLogger(CertificationTest.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            client_IR_CR_Example();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }


    public static void client_IR_CR_Example() throws Exception {
        IniFile iniFile = new IniFile();
        iniFile.loadIni("test.ini");
        String ip = iniFile.getValue("CERTIFICATION", "IP");
        int port = iniFile.getIntValue("CERTIFICATION", "PORT");
        String caCert = iniFile.getValue("CERTIFICATION", "CACERT");
        String raCert = iniFile.getValue("CERTIFICATION", "RACERT");
//        String raCertPrivate = iniFile.getValue("CERTIFICATION", "RACERTPRIVATE");
        long siparisDetayNo = iniFile.getLongValue("CERTIFICATION", "SIPARISDETAYNO");


        ECertificate cACertificate = new ECertificate(new File(caCert));
        ECertificate rACertificate = new ECertificate(new File(raCert));
        System.out.println("raCert seri:"+rACertificate.getSerialNumberHex());
        System.out.println("1-"+new BigInteger(String.valueOf(60L)).toString(16));
        System.out.println("2-"+new BigInteger(String.valueOf(150L)).toByteArray().length);
        CmpTcpLayer cmpTcpLayer = new CmpTcpLayer(ip, port);

/*        Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA1);
        byte[] privKeyBytes = AsnIO.dosyadanOKU(raCertPrivate);
        signer.init(KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, privKeyBytes));*/

        SmartCard smartCard = new SmartCard(CardType.UTIMACO);
        long sessionID = smartCard.openSession(25);
        smartCard.login(sessionID,"12345");
        BaseSigner signer = new SCSignerWithKeyLabel(smartCard,sessionID,25, "yetkili_imza_mobilkayitci", SignatureAlg.RSA_SHA1.getName(),null);


        IProtectionTrustProvider protectionTrustProvider = new ProtectionTrustProvider(
                Arrays.asList((IProtectionController)
                        new ProtectionControllerWithSign(new SingleTrustedCertificateFinder(cACertificate))),
                new ProtectionGeneratorWithSign(signer, rACertificate)
        );


        EName sender = rACertificate.getSubject();
        EName recipient = cACertificate.getSubject();
        ArrayList<ICertificationParam> certificationParams = new ArrayList<ICertificationParam>();

/*        KeyPair proKeyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.RSA_PKCS1);
        decryptor.init(proKeyPair.getPrivate(), new ParamsWithLength(1024));*/
        KeyPair keyOnClient = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);

        POPSigner popSigner = new POPSigner(null) {
            @Override
            public ProofOfPossession popOlustur(CertRequest certRequest) {
                ProofOfPossession pop = new ProofOfPossession();
                pop.set_raVerified();
                return pop;
            }
        };

        CertificationParam param = new CertificationParam(
                rACertificate.getSubject(),
                keyOnClient.getPublic(),
                siparisDetayNo,
                new CertificateSign(popSigner));
        certificationParams.add(param);

        ICertificationAcceptanceStrategy acceptanceStrategy = new MyICertificationAcceptanceStrategy();
        CertificationProtocol certificationProtocol = new CertificationProtocol(
                cmpTcpLayer, sender, recipient,
                certificationParams, protectionTrustProvider,
                acceptanceStrategy);
        certificationProtocol.runProtocol();
        long time = System.currentTimeMillis() % 10000000;
        AsnIO.dosyayaz(param.getCertificate().getEncoded(), "MobilCert" + time + ".cer");
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
            return UtilCmp.createSuccesfullCertificationStatuses(certificationResults);  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void rollbackCertificates(List<ICertificationParam> certificationResult) {
            System.out.println("Rollback triggered...");
        }
    }

}


