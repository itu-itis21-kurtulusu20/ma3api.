package tr.gov.tubitak.uekae.esya.api.smartcard.android.reader.atlantis;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

enum CardProtocol{
    T1,
    T0,
}

enum CommandStatus{
    COMMAND_NO_ERROR(0x00),
    COMMAND_FAILED(0x01),
    COMMAND_TIME_EXTENSION(0x02);

    private static final Map<Integer, CommandStatus> lookup = new HashMap<Integer, CommandStatus>();
    static {
        for (CommandStatus d : CommandStatus.values()) {
            lookup.put(d.code, d);
        }
    }
    private int code;
    CommandStatus(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public static CommandStatus get(int code) {
        return lookup.get(code);
    }
}


enum IccStatus{
    ICC_ACTIVE(0x00),
    ICC_INACTIVE(0x01),
    ICC_REMOVED(0x02);

    private static final Map<Integer, IccStatus> lookup = new HashMap<Integer, IccStatus>();
    static {
        for (IccStatus d : IccStatus.values()) {
            lookup.put(d.code, d);
        }
    }
    private int code;
    IccStatus(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public static IccStatus get(int code) {
        return lookup.get(code);
    }
}

enum T1Checksum{
    LRC,
    CRC;
    public int getChecksumLength(){
        if(this == LRC){
            return 1;
        }
        return 2;
    }
}

public class AtlantisReader {

    private static final String TAG = "AtlantisReader";
    private static final int RX_BUFFER_SIZE = 271;
    private static final int USB_TIMEOUT = 3000;
    private static final int CCID_RESPONSE_HEADER_LENGTH = 10;

    private static final int T1_HEADER_LENGTH = 3;
    private T1Checksum t1Checksum = T1Checksum.LRC;

    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbEndpoint outputEndpoint;
    private UsbEndpoint inputEndpoint;
    private UsbEndpoint interruptEndpoint;
    private UsbDeviceConnection usbConnection;
    private UsbInterface usbInterface;
    private CardProtocol cardProtocol = CardProtocol.T1;
    private byte[] rxBuffer;
    private byte[] atr;
    private boolean isOpened;
    private int ifsc = 0x20;
    private byte sequenceNumber = 0x40;

    public AtlantisReader(UsbManager usbManager){
        rxBuffer = new byte[RX_BUFFER_SIZE];
        this.usbManager = usbManager;
    }

    public void open(UsbDevice aUsbDevice){
        if(aUsbDevice == null){
            throw new IllegalArgumentException("The usbDevice parameter cannot be null.");
        }
        if(this.usbDevice != null){
            if(this.usbDevice.equals(aUsbDevice)){
                return;
            }
            this.close();
        }
        usbDevice = aUsbDevice;

        //ATR19 interface 0 is CCID interface
        usbInterface = usbDevice.getInterface(0);

        //Bulk Out
        outputEndpoint = usbInterface.getEndpoint(0);
        //Bulk In
        inputEndpoint = usbInterface.getEndpoint(1);
        //Interrupt In
        interruptEndpoint = usbInterface.getEndpoint(2);

        usbConnection = usbManager.openDevice(usbDevice);
        usbConnection.claimInterface(usbInterface, true);
        isOpened = true;
        Log.d(TAG,"Usb device opened");
    }

    public void close(){
        if(usbDevice == null){
            return;
        }

        this.usbConnection.releaseInterface(usbInterface);
        this.usbConnection.close();
        this.atr = null;
        this.usbDevice = null;
        this.outputEndpoint = null;
        this.inputEndpoint = null;
        this.interruptEndpoint = null;
        this.usbInterface = null;
        this.usbConnection = null;
        this.isOpened = false;
        this.sequenceNumber = 0x40;
        Log.d(TAG,"Usb device closed");
    }

    public static boolean isSupported(UsbDevice usbDevice){
        if((usbDevice.getVendorId() == 0x2406 && usbDevice.getProductId() == 0x6407)) {
            return true;
        }
        return false;
    }

    public boolean isOpened(){
        return isOpened;
    }

    public byte[] getAtr(){
        return atr;
    }

    private void getReaderParameters() throws AtlantisReaderException{
        byte[] data= new byte[]{
                (byte) 0x6C, //PC_TO_RDR_ICC_GET_PARAMETERS
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00, (byte) 0x00};
        usbConnection.bulkTransfer(outputEndpoint, data, data.length, USB_TIMEOUT);
        byte[] paramBuffer = new byte[30];
        int rxLen = usbConnection.bulkTransfer(inputEndpoint,paramBuffer,paramBuffer.length,USB_TIMEOUT);
        if(rxLen <= CCID_RESPONSE_HEADER_LENGTH){
            throw new AtlantisReaderException("Received incorrect number of bytes");
        }
        isCommandOk(rxBuffer);
        if(paramBuffer[9] == 0x00){
            this.cardProtocol = CardProtocol.T0;
        }
        else{
            this.cardProtocol = CardProtocol.T1;
            this.ifsc = (paramBuffer[15] & 0xFF);
        }
    }

    public void powerOn() throws AtlantisReaderException{
        byte[] data= new byte[]{
                (byte) 0x62, //PC_TO_RDR_ICC_POWERON
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00, (byte) 0x00};
        usbConnection.bulkTransfer(outputEndpoint, data, data.length, USB_TIMEOUT);
        int rxLen = usbConnection.bulkTransfer(inputEndpoint,rxBuffer,rxBuffer.length,USB_TIMEOUT);
        if(rxLen != (CCID_RESPONSE_HEADER_LENGTH+getDwLength(rxBuffer))){
            throw new AtlantisReaderException("Received incorrect number of bytes");
        }
        isCommandOk(rxBuffer);
        atr = new byte[getDwLength(rxBuffer)];
        System.arraycopy(rxBuffer,CCID_RESPONSE_HEADER_LENGTH,atr,0,atr.length);
        getReaderParameters();
        if(cardProtocol == CardProtocol.T1){
            ifsdNegotiate((byte)ifsc,false,null);
        }
    }
    private byte[] intToByteArray(int value){
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    private int getDwLength(byte[] rxBuffer){
        return (rxBuffer[1] & 0xFF) | (rxBuffer[2] & 0xFF) << 8 | (rxBuffer[3] & 0xFF) << 16 | (rxBuffer[4] & 0xFF) << 24;
    }
    private IccStatus getIccStatus(byte[] rxBuffer){
        byte bStatus = rxBuffer[7];
        byte commandStatusMask = (byte)(0x3 << 6);
        byte iccStatus = (byte)(bStatus & ~commandStatusMask);
        return IccStatus.get(iccStatus);
    }
    private CommandStatus getCommandStatus(byte[] rxBuffer){
        byte bStatus = rxBuffer[7];
        byte commandStatusMask = (byte)(0x3 << 6);
        byte commandStatus = (byte)((bStatus & commandStatusMask) >> 6);
        return CommandStatus.get(commandStatus);
    }
    private boolean isCommandOk(byte[] rxBuffer) throws AtlantisReaderException{
        byte bError = rxBuffer[8];
        //IccStatus iccStauts =getIccStatus(rxBuffer);
        CommandStatus commandStatus =getCommandStatus(rxBuffer);
        if(bError != 0)
        {
            throw new AtlantisReaderException("CCID error flag is set with value: " + bError);
        }
        /*if( iccStauts!= IccStatus.ICC_ACTIVE){
            throw new AtlantisReaderException("Card not active, state: " + iccStauts.name());
        }*/
        if(commandStatus != CommandStatus.COMMAND_NO_ERROR){
            throw new AtlantisReaderException("Command error: " + commandStatus.name());
        }
        return true;
    }

    public void powerDown() throws AtlantisReaderException{
        byte[] data= new byte[]{
                (byte) 0x63, //PC_TO_RDR_ICC_POWEROFF
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00, (byte) 0x00};
        usbConnection.bulkTransfer(outputEndpoint, data, data.length, USB_TIMEOUT);
        int rxLen = usbConnection.bulkTransfer(inputEndpoint,rxBuffer,rxBuffer.length,USB_TIMEOUT);
        if(rxLen != CCID_RESPONSE_HEADER_LENGTH){
            throw new AtlantisReaderException("Received incorrect number of bytes");
        }
        isCommandOk(rxBuffer);
        atr = null;
    }

    public void warmReset() throws AtlantisReaderException{
        this.sequenceNumber = 0x40;
        powerOn();
    }

    public void coldReset() throws AtlantisReaderException{
        powerDown();
        this.sequenceNumber = 0x40;
        powerOn();
    }

    private void getSlotStatus() throws AtlantisReaderException{
        byte[] data= new byte[]{
                (byte) 0x65, //PC_TO_RDR_ICC_POWEROFF
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00, (byte) 0x00};
        usbConnection.bulkTransfer(outputEndpoint, data, data.length, USB_TIMEOUT);
        int rxLen = usbConnection.bulkTransfer(inputEndpoint,rxBuffer,rxBuffer.length,USB_TIMEOUT);
        if(rxLen != CCID_RESPONSE_HEADER_LENGTH){
            throw new AtlantisReaderException("Received incorrect number of bytes");
        }
        isCommandOk(rxBuffer);
    }

    public IccStatus getCardState() throws AtlantisReaderException{
        getSlotStatus();
        return getIccStatus(rxBuffer);
    }



    private byte getLRC(byte[] data,int length){
        byte lrc = 0x00;
        for(int i = 0; i < length; i++){
            lrc ^= data[i];
        }
        return  lrc;
    }

    private boolean isCheksumOk(byte[] t1Data,int t1Length) throws  AtlantisReaderException{
        if(t1Checksum == T1Checksum.LRC){
            return t1Data[t1Length-1] == getLRC(t1Data, t1Length-1);
        }
        throw new AtlantisReaderException("T1 CRC cheksums not supported.");
    }

    private byte[] createT1(byte nad, byte pcb, byte len ,byte[] inf) throws  AtlantisReaderException{
        byte[] t1 = new byte[3 + t1Checksum.getChecksumLength() + len];
        t1[0] = nad;
        t1[1] = pcb;
        t1[2] = (byte)len;
        if(inf != null && len > 0){
            System.arraycopy(inf,0,t1,3,len);
        }
        if(t1Checksum == T1Checksum.LRC){
            t1[t1.length-1] = getLRC(t1,t1.length-1);
            return t1;
        }
        else{
            throw new AtlantisReaderException("T1 CRC cheksums not supported.");
        }
    }


    private byte[] apduToT1Tpdu(byte[] apdu,byte apduLength) throws  AtlantisReaderException{
        sequenceNumber ^= 0x40;
        return createT1((byte)0,sequenceNumber,apduLength,apdu);
    }

    private int ifsdNegotiate(byte ifsc,boolean isResponse,byte[] xferResponse) throws AtlantisReaderException{
        byte[] ifsdR = createT1((byte)0,(byte)(isResponse ? 0xE1 : 0xC1),(byte)1,new byte[]{ifsc});
        byte[] resp = new byte[10];
        int rxLen = xferTpdu(ifsdR,ifsdR.length,xferResponse != null ? xferResponse : resp,0);
        if(!isResponse){
            if(resp[1] != (byte)0xE1
                    || resp[2] != (byte)1
                    || resp[3] != ifsc
                    || !isCheksumOk(resp,rxLen)
            )
            {
                throw new AtlantisReaderException("Invalid response to IFSD negotiation.");
            }
        }
        isCommandOk(rxBuffer);
        return rxLen;
    }

    private int handleWtxRequest(byte wti, byte[] xferResponse) throws AtlantisReaderException{
        byte nad,pcb,len,lrc;
        nad = 0;
        pcb = (byte)0xE3;
        len = 1;
        lrc = 0;
        lrc = (byte)(wti ^ lrc ^ nad ^ pcb ^ len);
        byte[] ifsdR = new byte[]{
                nad,pcb,len,wti,lrc
        };
        int rxLen = xferTpdu(ifsdR,ifsdR.length,xferResponse,wti);
        return rxLen;
    }

    public int xferApdu(byte[] apdu,int length, byte[] response) throws AtlantisReaderException{
        if(cardProtocol == CardProtocol.T1){
            byte[] t1Apdu = apduToT1Tpdu(apdu,(byte)length);
            int tpduLength = xferTpdu(t1Apdu,t1Apdu.length,response,0);
            if(!isCheksumOk(response,tpduLength)){
                throw new AtlantisReaderException("T1 checksum is incorrect.");
            }
            if (response[1] == (byte)0xC1){
                //IFSC Negotiation Request
                tpduLength = ifsdNegotiate(response[3],true,response);
            }
            else if(response[1] == (byte)0xC3){
                //WTX Request
                tpduLength = handleWtxRequest(response[3],response);
            }
            else if(response[1] != (byte)0x40 && response[1] != (byte)0){
                throw  new AtlantisReaderException("Unsupported T1 request. Request code:" + String.format("0x%20x",response[1]));
            }
            if(!isCheksumOk(response,tpduLength)){
                throw new AtlantisReaderException("T1 checksum is incorrect.");
            }
            System.arraycopy(response,3,response,0,tpduLength-4);
            return  tpduLength-4;
        }
        else{
            return  xferTpdu(apdu,length,response,0);
        }
    }
    public int xferTpdu(byte[] tpdu,int length, byte[] response,int wti) throws AtlantisReaderException{
        byte[] dwLen = intToByteArray(length);
        byte[] xferData = new byte[]{
                (byte)0x6F, //PC_TO_RDR_XFER
                (byte) dwLen[3], (byte) dwLen[2], (byte) dwLen[1], (byte) dwLen[0], //dwLENGTH
                (byte) 0x00, //Slot
                (byte) 0x00, //Seq
                (byte) wti, //Bwi
                (byte) 0x00, (byte) 0x00, //wLevelParameter 0=TPDU
        };

        byte tx[] = xferData;
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write(xferData);
            outputStream.write(tpdu);
            tx = outputStream.toByteArray();
        }catch (IOException ex){

        }
        usbConnection.bulkTransfer(outputEndpoint, tx, tx.length, USB_TIMEOUT);
        int rxLen = usbConnection.bulkTransfer(inputEndpoint,rxBuffer,rxBuffer.length,USB_TIMEOUT);
        int dataLen = getDwLength(rxBuffer);
        if(rxLen != (CCID_RESPONSE_HEADER_LENGTH+dataLen)){
            throw new AtlantisReaderException("Received incorrect number of bytes");
        }
        isCommandOk(rxBuffer);
        System.arraycopy(rxBuffer,CCID_RESPONSE_HEADER_LENGTH,response,0,dataLen);
        return dataLen;
    }

}
