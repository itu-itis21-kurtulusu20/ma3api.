package gnu.crypto.sig.rsa;

import gnu.crypto.derivationFunctions.MGF1;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.Sha160;
import gnu.crypto.util.PRNG;

import java.security.interfaces.RSAKey;

import tr.gov.tubitak.uekae.esya.api.common.crypto.IRandom;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;

/*
 * 
 * @author orcun.ertugrul
 *
 * rfc 3447
 * 		 			 +----------+---------+-------+
 *				DB = |   lHash 	|    PS   |   M   |
 *		 			 +----------+---------+-------+
 *									|     	  
 *     	  +----------+ 				V
 *  	  |   seed 	 |--> MGF ---> xor
 *		  +----------+              |
 * 				|					|
 * 	   +--+     V 					|
 *     |00|    xor <--- MGF <-------|
 *	   +--+     | 					|
 *		 |      | 					|
 *		 V      V 					V
 *     +--+----------+----------------------------+
 *EM = |00|maskedSeed|          maskedDB		  |
 *	   +--+----------+----------------------------+
 *
 */

public class EME_OAEP  {

	/** The underlying hash function to use with this instance. */
	private IMessageDigest hash;
	private IRandom irnd;
	private int emLen;


	private EME_OAEP(IMessageDigest hash, int blockSize, IRandom irnd)
	{
		super();
		this.hash = hash;
		this.irnd = irnd;
		this.emLen = blockSize;
	}
	
	public static EME_OAEP getInstance(final RSAKey key) 
	{
		int blockSize = (key.getModulus().bitLength() + 7) / 8;
		return getInstance(blockSize);
	}
	
	public static EME_OAEP getInstance(int blockSize) 
	{
		return new EME_OAEP(new Sha160(), blockSize, null);
	}

	public static EME_OAEP getInstance(String mdName, int blockSize) 
	{
		IMessageDigest hash = HashFactory.getInstance(mdName);
		return new EME_OAEP(hash, blockSize, null);
	}

	public static EME_OAEP getInstance(String mdName, int blockSize, IRandom irnd) 
	{
		IMessageDigest hash = HashFactory.getInstance(mdName);
		return new EME_OAEP(hash, blockSize, irnd);
	}
	
	public byte [] encode(final byte [] M)
	{
		return encode(M, null);
	}

	public byte [] encode(final byte [] M, byte []label)
	{
		if(M.length > emLen - 2 * hash.hashSize() - 2)
			throw new IllegalArgumentException("message too long");
		
		byte[]  db = new byte[emLen-hash.hashSize()-1];

		//
		// copy in the message
		//
		System.arraycopy(M, 0, db, db.length - M.length, M.length);

		//
		// add sentinel
		//
		db[db.length - M.length - 1] = 0x01;

		//
		// as the block is already zeroed - there's no need to add PS (the >= 0 pad of 0)
		//

		//
		// add the hash of the encoding params.
		//
		if(label == null)
			hash.update(null,0,0);
		else
			hash.update(label, 0, label.length);
		byte []lhash = hash.digest();

		System.arraycopy(lhash, 0, db, 0, lhash.length);

		//
		// generate the seed.
		//
		byte[]  seed = new byte[hash.hashSize()];

		try {
			if(irnd != null)
				irnd.nextBytes(seed, 0, seed.length);
			else
				PRNG.nextBytes(seed, 0, seed.length);
		} catch (IllegalStateException e) {
			throw new RuntimeException("encode(): "+String.valueOf(e));
		} catch (LimitReachedException e) {
			throw new RuntimeException("encode(): "+String.valueOf(e));

		}

		byte [] dbMask = MGF1.generateBytes(seed, db.length, hash);

		for(int i = 0; i < dbMask.length ; i++)
			db[i] ^= dbMask[i];

		byte [] seedMask = MGF1.generateBytes(db, seed.length, hash);

		for(int i = 0; i < seedMask.length ; i++)
			seed[i] ^= seedMask[i];

		byte [] EM = new byte[emLen];

		EM[0] = 0;
		System.arraycopy(seed, 0, EM, 1, seed.length);
		System.arraycopy(db, 0, EM, 1 + seed.length, db.length);

		return EM;
	}

	public byte [] decode(final byte[] EM)
	{
		return decode(EM, null);
	}

	
	public byte [] decode(final byte[] EM, final byte []label)
	{		
		if(emLen != EM.length)
			throw new IllegalArgumentException("decryption error");
		if(emLen < 2 * hash.hashSize() + 2)
			throw new IllegalArgumentException("decryption error");
		
		byte []maskedSeed = new byte [hash.hashSize()];
		byte []maskedDB = new byte [emLen - hash.hashSize()-1];
		
		System.arraycopy(EM, 1, maskedSeed, 0, maskedSeed.length);
		System.arraycopy(EM, 1+maskedSeed.length, maskedDB, 0, maskedDB.length);
			
		byte [] seedMask = MGF1.generateBytes(maskedDB, hash.hashSize(), hash);
		//
        // unmask the seed.
        //
		byte []seed = maskedSeed;
		for(int i = 0; i < seedMask.length; i++)
			seed[i] ^= seedMask[i];
		
		byte []dbMask = MGF1.generateBytes(seed, emLen - hash.hashSize() - 1, hash );

		//
        // unmask the DB.
        //
		byte []db = maskedDB;
		for(int i = 0; i < dbMask.length; i++)
			db[i] ^= dbMask[i];
		
		
		if(label == null)
			hash.update(null,0,0);
		else
			hash.update(label, 0, label.length);
		byte [] lhash = hash.digest();
		
		//
		//check first byte is zero
		//
		if(EM[0] != 0x00)
			throw new IllegalArgumentException("decryption error");
		//
        // check the hash of label.
        //
		for(int i = 0; i < lhash.length; i++)
			if(lhash[i] != db[i])
				throw new IllegalArgumentException("decryption error");
		
		
		//
		//Iterate over PS block.
		//
		int i = hash.hashSize();
		while(i < db.length && db[i] == 0)
			++i;
		
		//
		//0x01 value must be placed at the end of PS block 
		//
		if(i >= db.length  || db[i] != 0x01)
			throw new IllegalArgumentException("decryption error");
		
		++i;
		byte []message = new byte[db.length - i];
		//
        // extract the message
        //
		System.arraycopy(db, db.length - message.length, message, 0, message.length);
					
		return message;
	}
	
	
	
	
}
