package dev.esya.api.smartcard.akiscif;

import org.junit.Test;
import tubitak.akis.cif.commands.AbstractAkisCommands;
import tubitak.akis.cif.commands.CIFFactory;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.APDUSmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.PCTerminalHandler;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import java.util.List;

public class AkisCifTest {

    @Test
    public void apduSmartCardOpenSession() throws Exception{
        APDUSmartCard smartCard = new APDUSmartCard();
        smartCard.openSession(1);
    }

    @Test
    public void listSerialNumbers() throws Exception {
        APDUSmartCard sc = new APDUSmartCard();
        long[] slotList = sc.getSlotList();
        sc.openSession(slotList[0]);

        String serials = sc.getSerial();

        if(serials.length() == 0)
        {
            throw new Exception("Serial numbers could not be read!");
        }

    }

    @Test
    public void listKeys() throws Exception {
        APDUSmartCard sc = new APDUSmartCard();
        long[] slotList = sc.getSlotList();
        sc.openSession(slotList[0]);

        List<byte[]> signatureCertificates = sc.getSignatureCertificates();

        if(signatureCertificates.size() <= 0)
        {
            throw new Exception("Certificate could not be read!");
        }
    }

    @Test
    public void selectPKCS15() throws Exception{
        PCTerminalHandler terminalHandler = new PCTerminalHandler();
        List<CardTerminal> terminals = terminalHandler.listCardTerminals(CardTerminals.State.CARD_PRESENT);
        ICommandTransmitter pcsc = terminalHandler.getTransmitter(terminals.get(0));

        AbstractAkisCommands commands = CIFFactory.getAkisCIFInstance(pcsc);

        commands.selectMF();
        commands.selectDFByName("PKCS-15".getBytes("ASCII"));
    }


    @Test
    public void readFile()throws Exception{
        PCTerminalHandler terminalHandler = new PCTerminalHandler();
        List<CardTerminal> terminals = terminalHandler.listCardTerminals(CardTerminals.State.CARD_PRESENT);
        ICommandTransmitter pcsc = terminalHandler.getTransmitter(terminals.get(0));

        AbstractAkisCommands commands = CIFFactory.getAkisCIFInstance(pcsc);

        commands.selectMF();
        commands.selectDFByName("PKCS-15".getBytes("ASCII"));
        commands.verify((byte)1,"12345".getBytes(), false);
        commands.selectFileUnderMF(new byte[]{0x2A, 0x14});



        byte[] bytes7 = commands.readRecordFile((byte)7);
        System.out.println(StringUtil.toHexString(bytes7));

        byte[] bytes9 = commands.readRecordFile((byte)9);
        System.out.println(StringUtil.toHexString(bytes9));
    }
}
