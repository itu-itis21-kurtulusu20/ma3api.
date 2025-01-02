package tr.gov.tubitak.uekae.esya.api.smartcard.android.acs.reader;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals.State;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import android.util.Log;
import com.acs.smartcard.Reader;

import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.TerminalHandler;

public class ACSTerminalHandler extends TerminalHandler
{
    private static String TAG="EsyaAndroidSignAPI-ACSTerminalHandler";
    //private static Logger logger = Logger.getLogger(ACSTerminalHandler.class);
    Activity mActivity;
    Reader mReader;

    public ACSTerminalHandler(Activity callerActivity, PendingIntent permissionIntent) {
        this.permissionIntent = permissionIntent;
        this.mActivity = callerActivity;
        mUsbManager = (UsbManager) callerActivity.getSystemService(Context.USB_SERVICE);
        mReader = new Reader(mUsbManager);
    }

    public UsbManager getmUsbManager() {
        return mUsbManager;
    }

    public Reader getmReader() {
        return mReader;
    }

    PendingIntent permissionIntent;
    public void setPermissionIntent(PendingIntent permissionIntent){
        this.permissionIntent = permissionIntent;
    }

    UsbManager mUsbManager;
    public ACSTerminalHandler(Activity aActivity,UsbManager aUsbManager,Reader aReader)
    {
        mActivity = aActivity;
        mUsbManager = aUsbManager;
        mReader =aReader;
    }

    public ACSTerminalHandler(Activity aActivity)
    {
        mActivity = aActivity;
        mUsbManager = (UsbManager) mActivity.getSystemService(Context.USB_SERVICE);
        mReader = new Reader(mUsbManager);
    }

    @Override
    public List<CardTerminal> listCardTerminals(State aState)
            throws CardException
    {
        int slot = 0;
        List<CardTerminal> list = new ArrayList<CardTerminal>();
        for (UsbDevice device : mUsbManager.getDeviceList().values())
        {
            if (mReader.isSupported(device))
            {
                ACSCardTerminal cardTerminal = new ACSCardTerminal(mReader, slot, device);
                list.add(cardTerminal);
                slot++;
            }
        }
        return list;
    }

    public static void checkLicense()
    {
        try
        {
            boolean isTest = LV.getInstance().isTL(LV.Urunler.ANDROIDIMZA);
            if(isTest)
            {
                Log.d(TAG,"Test lisansı, android imza işlemlerinde 5 sn gecikme yaşanacak.");
                Thread.sleep(5000);
            }
        }
        catch(LE ex)
        {
            Log.e(TAG,"Lisans kontrolu basarisiz. ",ex);
            throw new RuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        catch (InterruptedException e)
        {
            Log.e(TAG,"Lisans kontrolu sirasinda interrup alindi. ",e);
            throw new RuntimeException("Lisans kontrolu sirasinda interrup alindi.");
        }

    }


    @Override
    public ICommandTransmitter getTransmitter(CardTerminal aCardTerminal)
            throws CardException
    {
        Log.d(TAG,"Before get transmitter");
        checkLicense();
        ACSCardTerminal acsTerminal = (ACSCardTerminal) aCardTerminal;
        ACSCommandTransmitter transmitter = new ACSCommandTransmitter(mReader, acsTerminal, mUsbManager, mActivity);
        transmitter.setPermissionIntent(permissionIntent);
        transmitter.initialize();
        Log.d(TAG,"After get transmitter");
        return transmitter;
    }
}
