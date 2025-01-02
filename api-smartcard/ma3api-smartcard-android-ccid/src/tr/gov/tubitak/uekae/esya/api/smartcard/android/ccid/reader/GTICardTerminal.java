package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import android.util.Log;

import com.phison.gti.Gti2Assd;
import com.phison.gti.Gti2Exception;

public class GTICardTerminal extends CardTerminal
{
	String extCardPath = null;
	Gti2Assd gti2Tunnel = new Gti2Assd();
	
 //   boolean isOpened=false;

    public GTICardTerminal(String extCardPath) throws CardException
    {
    	this.extCardPath = extCardPath;
    	try
    	{    		
    		//return usbReader.isCardPresent();
			Log.d("tag", "ASSD_DetectAssdDevices oncesi");
			gti2Tunnel.ASSD_DetectAssdDevices(extCardPath);
			Log.d("tag", "ASSD_DetectAssdDevices sonras�");
			
			return ;
    	}
    	catch (Gti2Exception e) {
    		throw new CardException(e);
		}
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
    		//return usbReader.isCardPresent();
			Log.d("tag", "ASSD_DetectAssdDevices oncesi");
			gti2Tunnel.ASSD_DetectAssdDevices(extCardPath);
			Log.d("tag", "ASSD_DetectAssdDevices sonras�");
			return true;
    	}
    	catch (Gti2Exception e) {
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

    public Gti2Assd getGTI2Tunnel()
    {
        return gti2Tunnel;
    }

}
