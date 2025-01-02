package tr.gov.tubitak.uekae.esya.api.smartcard.android.reader.atlantis;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import android.hardware.usb.UsbDevice;

public class AtlantisCardTerminal extends CardTerminal {

    AtlantisReader mReader;
    int mSlot;
    UsbDevice mUsbDevice;

    public AtlantisCardTerminal(AtlantisReader aReader, int aSlot, UsbDevice aUsbDevice)
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

        try{
            IccStatus cardStatus = mReader.getCardState();
            if(cardStatus != IccStatus.ICC_REMOVED){
                return true;
            }
            return false;
        }
        catch (AtlantisReaderException ex){
            throw new CardException(ex.getMessage());
        }

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
