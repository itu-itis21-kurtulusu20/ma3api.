package gnu.crypto.cipher;

import gnu.crypto.Registry;

import java.util.Collections;
import java.util.Iterator;
import java.security.InvalidKeyException;

/**
 * Bergama projesi temel alınarak gnu yapısına uygun hale getirilmiştir.
 * 
 * @author IH
 */
public class RC2 extends BaseCipher
{
	public static final int BLOCK_SIZE = 8;

	public static final int KEY_SIZE = 64;

	private static final int[] S_BOX = {0xD9, 0x78, 0xF9, 0xC4, 0x19, 0xDD, 0xB5, 0xED, 0x28, 0xE9, 0xFD, 0x79, 0x4A,
			0xA0, 0xD8, 0x9D, 0xC6, 0x7E, 0x37, 0x83, 0x2B, 0x76, 0x53, 0x8E, 0x62, 0x4C, 0x64, 0x88, 0x44, 0x8B, 0xFB,
			0xA2, 0x17, 0x9A, 0x59, 0xF5, 0x87, 0xB3, 0x4F, 0x13, 0x61, 0x45, 0x6D, 0x8D, 0x09, 0x81, 0x7D, 0x32, 0xBD,
			0x8F, 0x40, 0xEB, 0x86, 0xB7, 0x7B, 0x0B, 0xF0, 0x95, 0x21, 0x22, 0x5C, 0x6B, 0x4E, 0x82, 0x54, 0xD6, 0x65,
			0x93, 0xCE, 0x60, 0xB2, 0x1C, 0x73, 0x56, 0xC0, 0x14, 0xA7, 0x8C, 0xF1, 0xDC, 0x12, 0x75, 0xCA, 0x1F, 0x3B,
			0xBE, 0xE4, 0xD1, 0x42, 0x3D, 0xD4, 0x30, 0xA3, 0x3C, 0xB6, 0x26, 0x6F, 0xBF, 0x0E, 0xDA, 0x46, 0x69, 0x07,
			0x57, 0x27, 0xF2, 0x1D, 0x9B, 0xBC, 0x94, 0x43, 0x03, 0xF8, 0x11, 0xC7, 0xF6, 0x90, 0xEF, 0x3E, 0xE7, 0x06,
			0xC3, 0xD5, 0x2F, 0xC8, 0x66, 0x1E, 0xD7, 0x08, 0xE8, 0xEA, 0xDE, 0x80, 0x52, 0xEE, 0xF7, 0x84, 0xAA, 0x72,
			0xAC, 0x35, 0x4D, 0x6A, 0x2A, 0x96, 0x1A, 0xD2, 0x71, 0x5A, 0x15, 0x49, 0x74, 0x4B, 0x9F, 0xD0, 0x5E, 0x04,
			0x18, 0xA4, 0xEC, 0xC2, 0xE0, 0x41, 0x6E, 0x0F, 0x51, 0xCB, 0xCC, 0x24, 0x91, 0xAF, 0x50, 0xA1, 0xF4, 0x70,
			0x39, 0x99, 0x7C, 0x3A, 0x85, 0x23, 0xB8, 0xB4, 0x7A, 0xFC, 0x02, 0x36, 0x5B, 0x25, 0x55, 0x97, 0x31, 0x2D,
			0x5D, 0xFA, 0x98, 0xE3, 0x8A, 0x92, 0xAE, 0x05, 0xDF, 0x29, 0x10, 0x67, 0x6C, 0xBA, 0xC9, 0xD3, 0x00, 0xE6,
			0xCF, 0xE1, 0x9E, 0xA8, 0x2C, 0x63, 0x16, 0x01, 0x3F, 0x58, 0xE2, 0x89, 0xA9, 0x0D, 0x38, 0x34, 0x1B, 0xAB,
			0x33, 0xFF, 0xB0, 0xBB, 0x48, 0x0C, 0x5F, 0xB9, 0xB1, 0xCD, 0x2E, 0xC5, 0xF3, 0xDB, 0x47, 0xE5, 0xA5, 0x9C,
			0x77, 0x0A, 0xA6, 0x20, 0x68, 0xFE, 0x7F, 0xC1, 0xAD};

	// Constructors.
	// -----------------------------------------------------------------------

	/**
	 * Default 0-arguments constructor.
	 */
	public RC2()
	{
		super(Registry.RC2_CIPHER, BLOCK_SIZE, KEY_SIZE);
	}

	// Methods implementing BaseCipher.
	// -----------------------------------------------------------------------

	public Object clone()
	{
		return new RC2();
	}

	public Iterator blockSizes()
	{
		return Collections.singleton(new Integer(BLOCK_SIZE)).iterator();
	}

	public Iterator keySizes()
	{
		return Collections.singleton(new Integer(KEY_SIZE)).iterator();
	}

	public Object makeKey(byte[] userkey, int ebits) throws InvalidKeyException
	{
		ebits = KEY_SIZE;
		int[] sKey = new int[64];
		int len = userkey.length;
		int ebytes = (ebits + 7) / 8;
		byte TM = (byte) (255 % (Math.pow(2, (8 + ebits - (8 * ebytes)))));

		// first work with a 128 byte array
		int[] sk = new int[128];
		for (int i = 0; i < len; i++)
		{
			sk[i] = userkey[i] & 0xFF;

		}
		for (int i = len; i < 128; i++)
		{
			sk[i] = S_BOX[(sk[i - len] + sk[i - 1]) & 0xFF];

			// The final phase of the key expansion involves replacing the
			// first byte of S with the entry selected from the S-box. In fact
			// this is a special case for tailoring the key to a given length.
			// sk[0] = S_BOX[sk[0]];

			// hmm.... key reduction to 'bits' bits
			// sk[128 - len] = S_BOX[sk[128 - len] & 0xFF];
		}
		sk[128 - ebytes] = S_BOX[sk[128 - ebytes] & TM];
		for (int i = 127 - ebytes; i >= 0; i--)
		{
			sk[i] = S_BOX[sk[i + ebytes] ^ sk[i + 1]];

			// now convert this byte array to the short array session key
			// schedule
		}
		for (int i = 63; i >= 0; i--)
		{
			sKey[i] = (sk[i * 2 + 1] << 8 | sk[i * 2]) & 0xFFFF;
		}
		return sKey;
	}

	public void encrypt(byte[] in, int i, byte[] out, int o, Object K, int bs)
	{
		int[] sKey = (int[]) K;
		int w0 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int w1 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int w2 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int w3 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int j = 0;
		for (int k = 0; k < 16; k++)
		{
			w0 = (w0 + (w1 & ~w3) + (w2 & w3) + sKey[j++]) & 0xFFFF;
			w0 = w0 << 1 | w0 >>> 15;
			w1 = (w1 + (w2 & ~w0) + (w3 & w0) + sKey[j++]) & 0xFFFF;
			w1 = w1 << 2 | w1 >>> 14;
			w2 = (w2 + (w3 & ~w1) + (w0 & w1) + sKey[j++]) & 0xFFFF;
			w2 = w2 << 3 | w2 >>> 13;
			w3 = (w3 + (w0 & ~w2) + (w1 & w2) + sKey[j++]) & 0xFFFF;
			w3 = w3 << 5 | w3 >>> 11;
			if ((k == 4) || (k == 10))
			{
				w0 += sKey[w3 & 0x3F];
				w1 += sKey[w0 & 0x3F];
				w2 += sKey[w1 & 0x3F];
				w3 += sKey[w2 & 0x3F];
			}
		}
		out[o++] = (byte) w0;
		out[o++] = (byte) (w0 >>> 8);
		out[o++] = (byte) w1;
		out[o++] = (byte) (w1 >>> 8);
		out[o++] = (byte) w2;
		out[o++] = (byte) (w2 >>> 8);
		out[o++] = (byte) w3;
		out[o++] = (byte) (w3 >>> 8);
	}

	public void decrypt(byte[] in, int i, byte[] out, int o, Object K, int bs)
	{
		int[] sKey = (int[]) K;
		int w0 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int w1 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int w2 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int w3 = (in[i++] & 0xFF) | (in[i++] & 0xFF) << 8;
		int j = 63;
		for (int k = 15; k >= 0; k--)
		{
			w3 = (w3 >>> 5 | w3 << 11) & 0xFFFF;
			w3 = (w3 - (w0 & ~w2) - (w1 & w2) - sKey[j--]) & 0xFFFF;
			w2 = (w2 >>> 3 | w2 << 13) & 0xFFFF;
			w2 = (w2 - (w3 & ~w1) - (w0 & w1) - sKey[j--]) & 0xFFFF;
			w1 = (w1 >>> 2 | w1 << 14) & 0xFFFF;
			w1 = (w1 - (w2 & ~w0) - (w3 & w0) - sKey[j--]) & 0xFFFF;
			w0 = (w0 >>> 1 | w0 << 15) & 0xFFFF;
			w0 = (w0 - (w1 & ~w3) - (w2 & w3) - sKey[j--]) & 0xFFFF;
			if ((k == 11) || (k == 5))
			{
				w3 = (w3 - sKey[w2 & 0x3F]) & 0xFFFF;
				w2 = (w2 - sKey[w1 & 0x3F]) & 0xFFFF;
				w1 = (w1 - sKey[w0 & 0x3F]) & 0xFFFF;
				w0 = (w0 - sKey[w3 & 0x3F]) & 0xFFFF;
			}
		}
		out[o++] = (byte) w0;
		out[o++] = (byte) (w0 >>> 8);
		out[o++] = (byte) w1;
		out[o++] = (byte) (w1 >>> 8);
		out[o++] = (byte) w2;
		out[o++] = (byte) (w2 >>> 8);
		out[o++] = (byte) w3;
		out[o++] = (byte) (w3 >>> 8);
	}
}
