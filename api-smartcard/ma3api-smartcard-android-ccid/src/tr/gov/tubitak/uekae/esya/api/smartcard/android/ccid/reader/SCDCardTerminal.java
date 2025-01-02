package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import android.hardware.usb.UsbDevice;

import com.scdroid.ccid.USBReader;
import com.scdroid.smartcard.SCException;

public class SCDCardTerminal extends CardTerminal
{
	USBReader usbReader;
    int slot;
    UsbDevice usbDevice;
    boolean isOpened=false;

    public SCDCardTerminal(USBReader usbReader, int slot, UsbDevice usbDevice)
    {
    	this.usbReader = usbReader; 
    	this.slot = slot;
    	this.usbDevice = usbDevice;        
    }

    @Override
    public Card connect(String arg0) throws CardException
    {
        throw new CardException("Unsupported operation on android for acs readers");
    }

    @Override
    public String getName()
    {    
    	return "";
    	//String deviceName = usbDevice.getDeviceName();
        //return deviceName;
    }

    @Override
    public boolean isCardPresent() throws CardException
    {  
    	try
    	{    		
    		return usbReader.isCardPresent();
    	}
    	catch (SCException e) {
    		throw new CardException(e);
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
        return slot;
    }

    public UsbDevice getUsbDevice()
    {
        return usbDevice;
    }

}
