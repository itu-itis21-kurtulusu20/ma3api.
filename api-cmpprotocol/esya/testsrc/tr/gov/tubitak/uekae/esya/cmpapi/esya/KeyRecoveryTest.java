package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IKeyRecoveryParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.CmpTcpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.ProtectionTrustProvider;

import java.io.File;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 13, 2010
 * Time: 10:37:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class KeyRecoveryTest {
    private static Logger logger = LoggerFactory.getLogger(KeyRecoveryTest.class);

        public static void main(String[] args) {
        //BasicConfigurator.configure();
        try {
            client_KR_Example();
        } catch (Exception e) {
            //logger.error(e, e);
        }
    }
    private static void client_KR_Example() throws Exception {
        ECertificate cACertificate = new ECertificate(new File("D:\\Projects\\ESYA_MA3API\\cmpapi\\headzeldalsm.cer"));
        ECertificate rACertificate = new ECertificate(new File("D:\\Projects\\ESYA_MA3API\\cmpapi\\kayitcizeldal.cer"));

        CmpTcpLayer cmpTcpLayer = new CmpTcpLayer("127.0.0.1", 829, 120);

        EName sender = rACertificate.getSubject();
        EName recipient = cACertificate.getSubject();
        String senderKID = "7b57faa4";  // personel refno

        PBMParameterFinder pbmParameterFinder = new PBMParameterFinder("bda8a52");
        IProtectionTrustProvider protectionTrustProvider = new ProtectionTrustProvider(
                Arrays.asList((IProtectionController) new ProtectionControllerWithHMac(pbmParameterFinder)),
                new ProtectionGeneratorWithHMac(pbmParameterFinder));

        List<KeyRecoveryParam> keyRecoveryParams = new ArrayList<KeyRecoveryParam>();
        KeyRecoveryParam recoveryParam = new KeyRecoveryParam(
                Arrays.asList(41L),
                KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024));
        keyRecoveryParams.add(recoveryParam);
        
        KeyRecoveryProtocol keyRecoveryProtocol = new KeyRecoveryProtocol(
                cmpTcpLayer, sender, recipient, senderKID,
                keyRecoveryParams, protectionTrustProvider);
        keyRecoveryProtocol.runProtocol();

        long time = System.currentTimeMillis() % 10000000;
        Pair<ECertificate, PrivateKey> pair = recoveryParam.getCertAndPrivKeys().get(0);
        AsnIO.dosyayaz(pair.getObject1().getObject(), "NewCert_KR_" + time + ".cer");
        AsnIO.dosyayaz(pair.getObject2().getEncoded(), "PrivKey_KR_" + time + ".bin");
    }
}
