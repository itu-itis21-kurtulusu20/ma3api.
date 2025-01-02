package gnu.crypto.prng;

import gnu.crypto.Registry;

import java.security.SecureRandom;
import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BasePRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;

public class JavaRandomGenerator extends BasePRNG 
{
	public final String SEED = "seed";
	
	SecureRandom rand;
	protected long mSeed;
	
	
	public JavaRandomGenerator() 
	{
		super(Registry.Java_PRNG);
		rand = new SecureRandom();
	}

	public JavaRandomGenerator(JavaRandomGenerator aJRG)
	{
		super(Registry.Java_PRNG);
		this.buffer = aJRG.buffer.clone();
		this.ndx = aJRG.ndx;
		this.initialised = aJRG.initialised;
		this.mSeed = aJRG.mSeed;
	}

	@Override
	public Object clone() 
	{
		return new JavaRandomGenerator(this);
		
	}

	@Override
	public void setup(Map attributes) 
	{
		Object seed = attributes.get(SEED);
		if(seed != null)
		{
			mSeed = (Long) seed;
			rand.setSeed(mSeed);
		}
		
	}

	@Override
	public void fillBlock() throws LimitReachedException 
	{
		rand.nextBytes(buffer);
	}

}
