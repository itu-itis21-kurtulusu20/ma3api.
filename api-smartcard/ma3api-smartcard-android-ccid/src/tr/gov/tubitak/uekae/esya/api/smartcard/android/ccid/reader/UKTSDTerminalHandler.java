package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;

import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.TerminalHandler;

/**
 * Created by omer.dural on 05.06.2017.
 */

public class UKTSDTerminalHandler extends TerminalHandler {

    private static String TAG = "EsyaAndroidSignAPI-UKTSDTerminalHandler";
    Activity activity;
    UsbManager usbManager;
    PendingIntent permissionIntent;

    public UKTSDTerminalHandler(Activity activity, PendingIntent permissionIntent) {
        this.permissionIntent = permissionIntent;
        this.activity = activity;
        usbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
    }

    @Override
    public List<CardTerminal> listCardTerminals(CardTerminals.State state) throws CardException {

        Log.e(TAG, "listCardTerminal GIRIS.");
        List<CardTerminal> list = new ArrayList<CardTerminal>();
        try {

//            Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
//            File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
//            File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);
//            String extCardPath = externalSdCard.getAbsolutePath()+"/";

//            String extCardPath = "/mnt/extSdCard/";

            list.add(new UKTSDCardTerminal());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    @Override
    public ICommandTransmitter getTransmitter(CardTerminal cardTerminal) throws CardException {


        String strTmp;
        String appPath = "";
        File fDir = activity.getExternalFilesDir(null);
        appPath = fDir.getAbsolutePath() + "/";
        strTmp = Environment.getExternalStorageDirectory().getPath();
        // sdcard uzerinde o anda calisan uygulama dizininin yazma hakkı olan path bulunup akiscif transmitter constructor ina verilir

        UKTSDCommandTransmitter transmitter = new UKTSDCommandTransmitter(strTmp, appPath);

         //AbstractAkisCommands cmd = CIFFactory.getAkisCIFInstance(transmitter);

        return transmitter;


    }

}
