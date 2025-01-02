package dev.esya.api.dist.manualExamples;

import org.junit.Test;
import tubitak.akis.cif.commands.AbstractAkisCommands;
import tubitak.akis.cif.commands.CIFFactory;
import tubitak.akis.cif.dataStructures.Version;
import tubitak.akis.cif.functions.CommandTransmitterPCSC;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import javax.smartcardio.ATR;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;
import java.util.List;

/**
 * Created by ramazan.girgin on 12/22/2015.
 */
public class AkisPerformanceTests {

    @Test
    public void testSignPerformance() throws Exception{
        CardType cardType = CardType.AKIS;
        long slotNo = 1;
        Chronometer totalCh = new Chronometer("TotalCh");
        Chronometer initCh = new Chronometer("InitCh");
        Chronometer sessionCh = new Chronometer("SessionCh");
        Chronometer loginCh = new Chronometer("loginCh");
        Chronometer logoutCh = new Chronometer("logoutCh");
        Chronometer readCertCh = new Chronometer("ReadCertCh");
        Chronometer signCh = new Chronometer("signCh");
        Chronometer closeSessionCh = new Chronometer("CloseSessionCh");

        byte[] dataForSign = ("Data For Sign - "+System.currentTimeMillis()).getBytes();

        int iterationCount = 1;
        int iterationOpCount = 5;
        long sessionId = -1;
        SmartCard  smartCard = null;
        String pinStr="12345";
        for (int i = 0; i < iterationCount; i++) {

            try{
                totalCh.start();

                initCh.start();
                smartCard = new SmartCard(cardType);
                initCh.stop();

                sessionCh.start();
                sessionId = smartCard.openSession(slotNo);
                sessionCh.stop();

                loginCh.start();
                smartCard.login(sessionId,pinStr);
                loginCh.stop();

                readCertCh.start();
                List<byte[]> signatureCertificates = smartCard.getSignatureCertificates(sessionId);
                readCertCh.stop();

                byte[] signCertByte = signatureCertificates.get(0);
                ECertificate eCertificate = new ECertificate(signCertByte);

                for (int j = 0; j < iterationOpCount; j++) {
                    signCh.start();
                    SmartOp.sign(smartCard, sessionId, slotNo, eCertificate.getSerialNumber().toByteArray(), dataForSign, SignatureAlg.RSA_SHA256.getName(), null);
                    signCh.stop();
                }
                totalCh.stop();

            }finally {
                if(sessionId!=-1){
                    logoutCh.start();
                    smartCard.logout(sessionId);
                    logoutCh.stop();

                    closeSessionCh.start();
                    smartCard.closeSession(sessionId);
                    closeSessionCh.stop();
                }
            }
        }

        TerminalFactory factory = TerminalFactory.getDefault();
        CardTerminal terminal = factory.terminals().list(CardTerminals.State.CARD_PRESENT).get(0);
        CommandTransmitterPCSC tranmitter = new CommandTransmitterPCSC(terminal, true);
        AbstractAkisCommands commands = CIFFactory.getAkisCIFInstance(tranmitter);
        Version version = commands.getVersion();
        ATR atr = tranmitter.atr();
        byte[] bytes = atr.getBytes();
        System.out.println("Kart Versiyonu      : "+version.toString());
        System.out.println("ATR                 : "+ StringUtil.toString(bytes));
        System.out.println("Toplam Iterasyon    : "+iterationCount);
        System.out.println("Imza Sayısı         : "+iterationCount*iterationOpCount);
        System.out.println("Toplam Süre         : "+totalCh.toTitle());
        System.out.println("pkcs11 ilklendirme  : "+initCh.toTitle());
        System.out.println("Open Session        : "+sessionCh.toTitle());
        System.out.println("Close Session       : "+sessionCh.toTitle());
        System.out.println("Login               : "+loginCh.toTitle());
        System.out.println("Logout              : "+logoutCh.toTitle());
        System.out.println("Read Certificate    :"+readCertCh.toTitle());
        System.out.println("Sign                :"+signCh.toTitle());

    }
}
