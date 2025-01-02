package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLReason;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.tools.IniFile;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IRevocationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.RevocationParamWithSerial;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.CmpTcpLayer;

import java.io.File;
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
public class RevocationTest {
    private static Logger logger = LoggerFactory.getLogger(RevocationTest.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            client_RR_Example();
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    private static void client_RR_Example() throws Exception {
        IniFile iniFile = new IniFile();
        iniFile.loadIni("test.ini");
        String ip = iniFile.getValue("REVOCATION", "IP");
        int port = iniFile.getIntValue("REVOCATION", "PORT");
        String caCert = iniFile.getValue("REVOCATION", "CACERT");
        String raCert = iniFile.getValue("REVOCATION", "RACERT");
        String raCertPrivate = iniFile.getValue("REVOCATION", "RACERTPRIVATE");
        String certToRevoke = iniFile.getValue("REVOCATION", "CERTTOREVOKE");
        int crlReasonNo =  iniFile.getIntValue("REVOCATION", "CRLREASON");
        ECRLReason crlReason = ECRLReason.UNSPECIFIED;


        ECertificate cACertificate = new ECertificate(new File(caCert));
        ECertificate rACertificate = new ECertificate(new File(raCert));
        ECertificate certificateToRevoke = new ECertificate(new File(certToRevoke));

        CmpTcpLayer cmpTcpLayer = new CmpTcpLayer(ip, port);

        EName sender = rACertificate.getSubject();
        EName recipient = cACertificate.getSubject();
        String senderKID = "";  // personel refno


        Signer signer = Crypto.getSigner(SignatureAlg.RSA_SHA1);
        byte[] privKeyBytes = AsnIO.dosyadanOKU(raCertPrivate);
        signer.init(KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, privKeyBytes));

        try {
            byte[] imzalanacak = "12345678901234567890".getBytes();
            byte[] signed = signer.sign(imzalanacak);

            if (!SignUtil.verify(SignatureAlg.RSA_SHA1, imzalanacak, signed, rACertificate))
                throw new RuntimeException("Atılan imza doğrulanamadı!!!");
        } catch (Exception e) {
            throw new RuntimeException("Atılan imza doğrulanamadı!!!:" + e.getMessage(), e);
        }

        IProtectionTrustProvider protectionTrustProvider = new ProtectionTrustProvider(
                Arrays.asList((IProtectionController) new ProtectionControllerWithSign(new MyTrustedCertificateFinder(cACertificate))),
                new ProtectionGeneratorWithSign(signer, rACertificate));


        RevocationParamWithSerial revocationParamWithSerial = new RevocationParamWithSerial(
                new Asn1BigInteger(certificateToRevoke.getSerialNumber()), crlReason);

        ArrayList<IRevocationParam> revocationParams = new ArrayList<IRevocationParam>();
        revocationParams.add(revocationParamWithSerial);

        RevocationProtocol revocationProtocol = new RevocationProtocol(
                cmpTcpLayer, sender, recipient, senderKID,
                revocationParams, protectionTrustProvider);

        revocationProtocol.runProtocol();

        System.out.println("revocationParamWithSerial.getPkiStatusInfo():" + AsnIO.getFormattedAsn(revocationParamWithSerial.getPkiStatusInfo()));

    }

    /**
     * Created by IntelliJ IDEA.
     * User: zeldal.ozdemir
     * Date: Dec 3, 2010
     * Time: 10:53:43 AM
     * To change this template use File | Settings | File Templates.
     */
    public static class MyTrustedCertificateFinder implements ITrustedCertificateFinder {
        private ECertificate trustedCertificate;
        private ECertificate cACertificate;

        public MyTrustedCertificateFinder(ECertificate cACertificate) {

            this.cACertificate = cACertificate;
        }

        public List<ECertificate> findTrustedCertificates(EPKIMessage incomingPkiMessage) {
            ArrayList<ECertificate> list = new ArrayList<ECertificate>();
            list.add(cACertificate);
            return list;
        }


        public void setProtectionCertificate(ECertificate trustedCertificate) {
            this.trustedCertificate = trustedCertificate;
        }
    }
}
