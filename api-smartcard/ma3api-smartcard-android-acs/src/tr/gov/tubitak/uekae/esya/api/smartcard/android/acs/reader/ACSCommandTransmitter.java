package tr.gov.tubitak.uekae.esya.api.smartcard.android.acs.reader;

import android.app.Activity;
import android.app.PendingIntent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import com.acs.smartcard.Reader;
import com.acs.smartcard.ReaderException;
import tubitak.akis.cif.akisExceptions.AkisCardException;
import tubitak.akis.cif.akisExceptions.AkisException;
import tubitak.akis.cif.functions.ICommandTransmitter;

import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.util.ArrayList;
import java.util.List;

public class ACSCommandTransmitter implements ICommandTransmitter {
    private static String TAG = "EsyaAndroidSignAPI-ACSCommandTransmitter";
    private static final List<String> openedDevices = new ArrayList<String>();

    private boolean deviceOpened = false;

    public static int BUFF_SIZE = 16384;

    Reader mReader;
    ACSCardTerminal mCardTerminal;
    UsbManager mUsbManager;
    Activity mActivity;
    PendingIntent mPermissionIntent;

    public ACSCommandTransmitter(Reader aReader, ACSCardTerminal aCardTerminal, UsbManager aUsbManager, Activity aActivity) {
        mReader = aReader;
        mCardTerminal = aCardTerminal;
        mUsbManager = aUsbManager;
        mActivity = aActivity;
        initialize();
    }

    PendingIntent permissionIntent;
    boolean permissionRequested = false;

    public ACSCommandTransmitter(Reader usbReader, Activity callerActivity) {
        this.mReader = usbReader;
        mActivity = callerActivity;
        initialize();
    }

    public void setPermissionIntent(PendingIntent permissionIntent) {
        this.permissionIntent = permissionIntent;
    }

    public void initialize() {
        try {
            UsbDevice usbDevice = mCardTerminal.getUsbDevice();
            while (!mUsbManager.hasPermission(usbDevice)) {
                try {
                    if (permissionRequested == false) {
                        Log.d(TAG, "Permission requested");
                        mUsbManager.requestPermission(usbDevice, permissionIntent);
                        permissionRequested = true;
                    }
                    Thread.sleep(1000L);
                } catch (InterruptedException localInterruptedException) {
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            try {
                if (!mReader.isOpened()) {
                    mReader.open(usbDevice);
                }
                Thread.sleep(1000L);
                Log.d(TAG, "Permission taken");
                deviceOpened = true;
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            Log.d(TAG, "power will be active");
            mReader.power(mCardTerminal.getSlot(), Reader.CARD_WARM_RESET);
            Log.d(TAG, "power activated");

            int preferredProtocols = Reader.PROTOCOL_UNDEFINED;
            preferredProtocols |= Reader.PROTOCOL_T0;
            preferredProtocols |= Reader.PROTOCOL_T1;
            Log.d(TAG, "Before set protocol");
            mReader.setProtocol(mCardTerminal.getSlot(), preferredProtocols);
            Log.d(TAG, "After set protocol");
        } catch (ReaderException e) {
            Log.e(TAG, "initialize ex", e);
        }
    }

    public ResponseAPDU transmit(CommandAPDU aCommand) throws AkisCardException {
        byte[] buff = new byte[BUFF_SIZE];
        int buffLen = buff.length;
        int responseLen = 0;
        try {
            Log.d(TAG, "Before transmit");
            responseLen = mReader.transmit(mCardTerminal.getSlot(), aCommand.getBytes(), aCommand.getBytes().length, buff, buffLen);
            Log.d(TAG, "After transmit.");
        } catch (ReaderException e) {
            Log.e(TAG, "Transmit Exception", e);
            throw new AkisCardException(new CardException(e));
        }

        byte[] response = new byte[responseLen];
        System.arraycopy(buff, 0, response, 0, responseLen);
        return new ResponseAPDU(response);
    }

    public ATR atr()  throws AkisCardException{
        ATR atr = new ATR(mReader.getAtr(mCardTerminal.getSlot()));
        return atr;
    }

    public void closeCardTerminal() {
        try {
            Log.d(TAG, "Before power cold reset");
            mReader.power(mCardTerminal.getSlot(), Reader.CARD_COLD_RESET);
            Log.d(TAG, "After power cold reset");

            Log.d(TAG, "Before reader close");
            mReader.close();
            Log.d(TAG, "After reader close");
        } catch (ReaderException e) {
            Log.e(TAG, "Error on reset", e);
        }
    }

    public int getCommandBufferSize() {
        return BUFF_SIZE;
    }

    @Override
    public void reset() throws AkisCardException {
        try {
            mReader.power(mCardTerminal.getSlot(), Reader.CARD_WARM_RESET);
        } catch (ReaderException e) {
            throw new AkisCardException(new CardException(e));
        }
    }

    @Override
    public void reset(String s) throws AkisCardException {
        try {
            mReader.power(mCardTerminal.getSlot(), Reader.CARD_WARM_RESET);
        } catch (ReaderException e) {
            throw new AkisCardException(new CardException(e));
        }
    }

    public byte[] control(byte[] bytes)  //todo pinped? card
    {
        return bytes;
    }
}
