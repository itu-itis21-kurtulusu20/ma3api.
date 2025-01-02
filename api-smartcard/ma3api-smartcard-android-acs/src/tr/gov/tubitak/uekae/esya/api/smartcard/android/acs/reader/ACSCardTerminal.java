package tr.gov.tubitak.uekae.esya.api.smartcard.android.acs.reader;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import android.hardware.usb.UsbDevice;

import com.acs.smartcard.Reader;

public class ACSCardTerminal extends CardTerminal
{
    Reader mReader;
    int mSlot;
    UsbDevice mUsbDevice;

    public ACSCardTerminal(Reader aReader, int aSlot, UsbDevice aUsbDevice)
    {
        mReader = aReader;
        mSlot = aSlot;
        mUsbDevice = aUsbDevice;
    }

    @Override
    public Card connect(String arg0) throws CardException
    {
        throw new CardException("Unsupported operation on android for acs readers");
    }

    @Override
    public String getName()
    {
        return mUsbDevice.getDeviceName();
    }

    @Override
    public boolean isCardPresent() throws CardException
    {
        int state = mReader.getState(mSlot);
        if((state & Reader.CARD_PRESENT) == Reader.CARD_PRESENT)
            return true;

        return false;
    }

    @Override
    public boolean waitForCardAbsent(long arg0) throws CardException
    {
        throw new CardException("Unsupported operation on android for acs readers");
    }

    @Override
    public boolean waitForCardPresent(long arg0) throws CardException
    {
        throw new CardException("Unsupported operation on android for acs readers");
    }

    public int getSlot()
    {
        return mSlot;
    }

    public UsbDevice getUsbDevice()
    {
        return mUsbDevice;
    }

}
