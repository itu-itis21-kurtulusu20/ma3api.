package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import android.app.Activity;
import android.app.PendingIntent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scdroid.ccid.USBReader;
import com.scdroid.smartcard.Card;
import com.scdroid.smartcard.SCException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import tubitak.akis.cif.akisExceptions.AkisCardException;
import tubitak.akis.cif.akisExceptions.AkisException;
import tubitak.akis.cif.dataStructures.Version;
import tubitak.akis.cif.functions.ICommandTransmitter;

public class SCDCommandTransmitter implements ICommandTransmitter {

    protected static Logger logger = LoggerFactory.getLogger(SCDCommandTransmitter.class);
    private static String TAG = "EsyaAndroidSignAPI-SCDCommandTransmitter";
    private static final List<String> openedDevices = new ArrayList<String>();

    private boolean deviceOpened = false;

    public static int BUFF_SIZE = 16384;

    USBReader usbReader;
    SCDCardTerminal mCardTerminal;
    UsbManager mUsbManager;
    Activity mActivity;

    byte[] cardAtr;

    public SCDCommandTransmitter(USBReader usbReader, SCDCardTerminal aCardTerminal, UsbManager aUsbManager, Activity aActivity) {
        this.usbReader = usbReader;
        mCardTerminal = aCardTerminal;
        mUsbManager = aUsbManager;
        mActivity = aActivity;
        initialize();
    }

    public SCDCommandTransmitter(USBReader usbReader, Activity aActivity) {
        this.usbReader = usbReader;
        mActivity = aActivity;
        initialize();
    }

    PendingIntent permissionIntent;
    boolean permissionRequested = false;

    public void setPermissionIntent(PendingIntent permissionIntent) {
        this.permissionIntent = permissionIntent;
    }

    public void initialize() {
        try {
            while (!deviceOpened) {
                try {
                    Log.d(TAG, "Opening card reader.");
                    //usbReader.logLevel(USBReader.LOG_CCID);
                    usbReader.Open();
                    //usbReader.WaitCardEvent(USBReader.CARD_EVENT_DETECED);
                    Log.d(TAG, "Opened card reader.");
                    Thread.sleep(1000L);
                    Log.d(TAG, "Permission taken");
                    deviceOpened = true;
                } catch (Exception exc) {
                    Thread.sleep(1000L);
                    // exc.printStackTrace();
                    logger.error("Error in SCDCommandTransmitter", exc);
                }
            }
            Log.d(TAG, "Connecting to card.");
            cardAtr = usbReader.ConnectCard(Card.PROTOCOL_ANY);

            // Temporary solution for AKIS >=2.2.8 with OMNIKEY
            Version version = Version.getVersion(cardAtr);
            if (isAkisVersion2(version)) {
                UsbDevice usbDevice = mUsbManager.getDeviceList().values().iterator().next();
                Method getManufacturerName = UsbDevice.class.getMethod("getManufacturerName");
                String mfcName = (String) getManufacturerName.invoke(usbDevice, null);
                if (mfcName.toUpperCase().contains("OMNIKEY")) {
                    Log.d(TAG, "reset for OMNIKEY reader and AKISv2 card");
                    usbReader.PowerOff();
                    usbReader.PowerOn();
                }
            }

            Log.d(TAG, "Connected to card.");
        } catch (Exception e) {
            Log.e(TAG, "initialize ex", e);
        }
    }

    public ResponseAPDU transmit(CommandAPDU aCommand)  {
        byte[] response = null;
        byte[] bytes = aCommand.getBytes();
        //	String requestStr = StringUtil.toString(bytes);
        //Log.d("APDU","Send :"+requestStr);
        try {
            //usbReader.logLevel(USBReader.LOG_CCID);
            response = usbReader.Transmit(bytes);
        } catch (SCException e) {
            Log.e(TAG, "Tranmit Exception", e);
            e.printStackTrace();
        }
        //String responseStr ="";
        //if(response!=null)
        //	responseStr = StringUtil.toString(response);
        //Log.d("APDU","Receive :"+responseStr);
        return new ResponseAPDU(response);
    }

    public ATR atr() throws AkisCardException {
        return new ATR(cardAtr);
    }

    public void closeCardTerminal() {
        Log.d(TAG, "Before power cold reset");
        usbReader.Close();
        //mReader.power(mCardTerminal.getSlot(), Reader.CARD_COLD_RESET);
        Log.d(TAG, "After power cold reset");

        // Log.d(TAG,"Before disconnect card");
        // usbReader.DisConnectCard();
        // Log.d(TAG,"After disconnect card");
    }

    public int getCommandBufferSize() {
        return BUFF_SIZE;
    }

    @Override
    public void reset() {

    }

    @Override
    public void reset(String s) {

    }

    public byte[] control(byte[] bytes) {
        return bytes;
    }

    private static boolean isAkisVersion2(Version aVersion) {
        if (aVersion == Version.V10_UEKAE ||
                aVersion == Version.V11_UEKAE_INF ||
                aVersion == Version.V121_UEKAE_INF ||
                aVersion == Version.V121_UEKAE_NXP ||
                aVersion == Version.V121_UEKAE_UKiS ||
                aVersion == Version.V121_UEKAE_UKIS_HHNEC ||
                aVersion == Version.V121_UEKAE_UKiS_SMIC ||
                aVersion == Version.V122_UEKAE_INF ||
                aVersion == Version.V122_UEKAE_NXP ||
                aVersion == Version.V122_UEKAE_UKiS_HHNEC ||
                aVersion == Version.V122_UEKAE_UKiS_SMIC ||
                aVersion == Version.V12_UEKAE_INF ||
                aVersion == Version.V12_UEKAE_NXP ||
                aVersion == Version.V12_UEKAE_UKiS ||
                aVersion == Version.V13_UEKAE_INF ||
                aVersion == Version.V14_UEKAE_INF ||
                aVersion == Version.V14_UEKAE_NXP)
            return false;
        else
            return true;

    }
}
