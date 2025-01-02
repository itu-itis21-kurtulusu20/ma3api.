
package gnu.crypto.prng;

import gnu.crypto.Registry;

import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BasePRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;

import com.sun.crypto.provider.UEKAECryptoCard;

public class UEKAECryptoCardGenerator extends BasePRNG 
{
    private static long msDeviceHandle = -1;
    private static final int KAYNAK_URETIM_UZUNLUK = 2048;
    
    protected UEKAECryptoCardGenerator() 
    {
	super(Registry.UEKAE_CRYPTO_CARD_PRNG);
	msDeviceHandle = UEKAECryptoCard.openInterface(0);
	buffer = new byte[KAYNAK_URETIM_UZUNLUK];
    }

    @Override
    public Object clone() 
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setup(Map attributes) 
    {
	
    }

    @Override
    public void fillBlock() throws LimitReachedException 
    {
	UEKAECryptoCard.getRandomBytes(msDeviceHandle, buffer, 0, buffer.length);
    }
   
}
