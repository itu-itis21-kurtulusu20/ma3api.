package tr.gov.tubitak.uekae.esya.api.smartcard.android.ccid.reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import com.phison.gti.Gti2Assd;
import com.phison.gti.Gti2Common;
import com.phison.gti.Gti2Exception;
import com.phison.gti.Utils;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tubitak.akis.cif.akisExceptions.AkisCIFException;
import tubitak.akis.cif.akisExceptions.AkisCardException;
import tubitak.akis.cif.akisExceptions.AkisException;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

import android.app.PendingIntent;

import android.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class GTICommandTransmitter implements ICommandTransmitter
{
	protected static Logger logger = LoggerFactory.getLogger(GTICommandTransmitter.class);
    private static String TAG="EsyaAndroidSignAPI-GTICommandTransmitter";
    private static final List<String> openedDevices = new ArrayList<String>();

    private boolean deviceOpened = false;

    public static int BUFF_SIZE = 16384;

    PendingIntent mPermissionIntent;
	Gti2Assd gti2Tunnel = null;
    

    public GTICommandTransmitter(GTICardTerminal aCardTerminal)
    {
        this.gti2Tunnel = aCardTerminal.getGTI2Tunnel();
    }

    PendingIntent permissionIntent;

    boolean permissionRequested=false;

    public void setPermissionIntent(PendingIntent permissionIntent){
        this.permissionIntent = permissionIntent;
    }

    public void initialize()
    {
        try
        {	        		
        	while(!deviceOpened){
	            try
	            {
	            	Log.d(TAG, "Opening card reader.");
	            	
	    			Log.d("tag", "ASSD_Connect �ncesi");
	            	gti2Tunnel.ASSD_Connect();
	    			Log.d("tag", "ASSD_Connect sonras�");
	    			gti2Tunnel.ASSD_ResetICC(true); //cold reset
	    			Log.d("tag", "ASSD_ResetICC sonras�");
	            	
	            	Log.d(TAG, "Opened card reader.");
	                Thread.sleep(1000L);
	                Log.d(TAG, "Permission taken");
	                deviceOpened = true;
	            }
	            catch(Exception exc)
	            {
	            	Thread.sleep(1000L);
	                // exc.printStackTrace();
					logger.error("Error in GTICommandTransmitter", exc);
	            }
        	}
            
        	Log.d(TAG, "Connected to card.");            
        }
        catch (Exception e) {
        	  Log.e(TAG, "initialize ex", e);
		}
    }

    public ResponseAPDU transmit(CommandAPDU aCommand)
    {	
        byte [] response = new byte[512];
        byte []trimmedResponse = null;
       	byte[] bytes = aCommand.getBytes();
       	String requestStr = StringUtil.toString(bytes); 
       	String responseStr ="";
       	int[] dwRLen = new int[]{Gti2Common.MAX_TRANSFER_BUFFER_SIZE, 0};
       	//Log.d("APDU","Send :"+requestStr);
		try {		
		
			Log.d("tag", "ASSD_SendAndReceiveAPDU oncesi");
			gti2Tunnel.ASSD_SendAndReceiveAPDU(bytes, bytes.length, response, dwRLen, 60);
			Log.d("tag", "ASSD_SendAndReceiveAPDU sonras�");

			trimmedResponse = new byte[dwRLen[0]];
			trimmedResponse =  Arrays.copyOf(response, dwRLen[0]);
			
			
			responseStr = Utils.strByteToHexString(response, dwRLen[0]);
			Log.d("tag", "ASSD_SendAndReceiveAPDU sonras� donen"+responseStr);
		} catch (Gti2Exception e) {
			Log.e(TAG, "Tranmit Exception",e);
			e.printStackTrace();
		}
		
		if(response!=null)
			responseStr = StringUtil.toString(trimmedResponse);
       	//Log.d("APDU","Receive :"+responseStr);
        return new ResponseAPDU(trimmedResponse);
    }

	public byte[] control(byte[] bytes)  {
		return new byte[0];

	}

	public ATR atr() throws AkisCardException
    {
        try {
			byte[] cardAtr = gti2Tunnel.ASSD_GetATR();
	    	return new ATR(cardAtr);
        } catch (Gti2Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error in GTICommandTransmitter", e);
	    	//throw new AkisCardException(-1023);// Bunu biz uydurduk
			throw new AkisCardException(new CardException(e));

        }
    }

    public void closeCardTerminal()
    {
		try {
			Log.d(TAG,"Before disconnect card");
			gti2Tunnel.ASSD_Disconnect();
			Log.d(TAG,"After disconnect card");
		} catch (Gti2Exception e) {
			Log.e(TAG,"ASSD_Disconnect exception, ErrCode: " + Integer.toHexString(e.getCode()) + ", Msg: " + e.getMessage(),e);
 
		}  
  	
    }

    public int getCommandBufferSize()
    {
        return BUFF_SIZE;
    }

	@Override
	public void reset() {

	}

	@Override
	public void reset(String s) {

	}
}
