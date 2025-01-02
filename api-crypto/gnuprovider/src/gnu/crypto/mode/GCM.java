package gnu.crypto.mode;

// ----------------------------------------------------------------------------
// $Id: CTR.java,v 1.1 2004/11/19 07:31:33 serdar Exp $
//
// Copyright (C) 2001, 2002, Free Software Foundation, Inc.
//
// This file is part of GNU Crypto.
//
// GNU Crypto is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2, or (at your option)
// any later version.
//
// GNU Crypto is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; see the file COPYING.  If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
//
// Linking this library statically or dynamically with other modules is
// making a combined work based on this library.  Thus, the terms and
// conditions of the GNU General Public License cover the whole
// combination.
//
// As a special exception, the copyright holders of this library give
// you permission to link this library with independent modules to
// produce an executable, regardless of the license terms of these
// independent modules, and to copy and distribute the resulting
// executable under terms of your choice, provided that you also meet,
// for each linked independent module, the terms and conditions of the
// license of that module.  An independent module is a module which is
// not derived from or based on this library.  If you modify this
// library, you may extend this exception to your version of the
// library, but you are not obligated to do so.  If you do not wish to
// do so, delete this exception statement from your version.
// ----------------------------------------------------------------------------

import gnu.crypto.Registry;
import gnu.crypto.cipher.IBlockCipher;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * RFC 5084
 * Using AES-CCM and AES-GCM Authenticated Encryption in the CMS
 *
 * NIST Special Publication 800-38D
 * Recommendation for Block Cipher Modes of Operation: Galois/Counter Mode (GCM) and GMAC
 *
 */
public class GCM extends BaseMode implements Cloneable {

    // Constants and variables
    // -------------------------------------------------------------------------

    /** The current counter. */

    private byte[] Tag = StringUtil.hexToByte("00000000000000000000000000000000");
    private BigInteger CB;
    private BigInteger InitialCB;

    private ParamsWithGCMSpec params;

    private byte [] encryptedBlock;
    private byte [] hSubKey;
    protected long aadLen = 0, cLen = 0;

    // Constructor(s)
    // -------------------------------------------------------------------------

    /**
     * <p>Trivial package-private constructor for use by the Factory class.</p>
     *
     * @param underlyingCipher the underlying cipher implementation.
     * @param cipherBlockSize the underlying cipher block size to use.
     */
    GCM(IBlockCipher underlyingCipher, int cipherBlockSize) {
        super(Registry.GCM_MODE, underlyingCipher, cipherBlockSize);
    }

    /**
     * <p>Private constructor for cloning purposes.</p>
     *
     * @param that the instance to clone.
     */
    private GCM(GCM that) {
        this((IBlockCipher) that.cipher.clone(), that.cipherBlockSize);
    }

    // Class methods
    // -------------------------------------------------------------------------

    // Cloneable interface implementation
    // -------------------------------------------------------------------------

    public Object clone() {
        return new GCM(this);
    }

    // Implementation of abstract methods in BaseMode
    // -------------------------------------------------------------------------

    public void setup() {

        //For the performace concern at blockMultiplication function, operations are made as if modeblocksize will be fix.
        if (modeBlockSize != 16) {
            throw new IllegalArgumentException("Mode block size must be 16 for GCM");
        }

        params = (ParamsWithGCMSpec) algorithmParams;

        byte [] j0 = null;
        hSubKey = getH();

        //Todo-GCM IV len !!!
        if(iv == null && iv.length <1){
            throw  new IllegalArgumentException("IV must at least 1 byte");
        } else if(iv.length == 12){
            byte [] pad = new byte[] {0,0,0,1};
            j0 = ByteUtil.concatAll(iv, pad);
        }else{
            j0 = StringUtil.hexToByte("00000000000000000000000000000000");
            int padLen = (16 * (int)Math.ceil((double) iv.length / 16) )  - iv.length;
            byte [] pad = new byte[padLen + 8];
            byte [] ivLen = ByteUtil.numberToByteArray(iv.length * 8,8);
            byte [] tempJ = ByteUtil.concatAll(iv, pad, ivLen);
            for(int i=0; i< tempJ.length/modeBlockSize;i++) {
                j0 = gHash(j0, tempJ, i * modeBlockSize, modeBlockSize);
            }
        }
        CB = new BigInteger(1, j0);
        InitialCB = CB;
        encryptedBlock =  new byte[modeBlockSize];
        initializeAAD();
    }


    private void initializeAAD() {
        try{
            if(params == null || params.getAAD() == null)
                return;

            InputStream aad = params.getAAD();
            byte [] buff = new byte[modeBlockSize];
            while(true) {
                int readLen = aad.read(buff, 0, modeBlockSize);
                if(readLen > 0)
                    aadLen = aadLen + readLen;
                else
                    break;

                if(readLen < modeBlockSize)
                {
                    for(int i = readLen; i<modeBlockSize; i++)
                        buff[i] = 0;
                }

                Tag = gHash(Tag, buff, 0, modeBlockSize);
            }
        }
        catch (IOException e){
            throw new IllegalArgumentException("Can not read additional authenticated data.",e);
        }
    }

    public void teardown() {
        CB = null;
    }

    public void encryptBlock(byte[] in, int i, byte[] out, int o) {
        CB = CB.add(BigInteger.ONE);
        gctr(CB, in, i, out, o);

        calculateTagForBlock(in.length-i, out, o);
    }

    public void decryptBlock(byte[] in, int i, byte[] out, int o) {

        CB = CB.add(BigInteger.ONE);
        gctr(CB, in, i, out, o);

        calculateTagForBlock(in.length-i, in, i);
    }

    private void calculateTagForBlock(int length, byte[] cipher, int cipherIndex) {
        if(length < modeBlockSize)
        {
            byte []XBuf = new byte[modeBlockSize];
            System.arraycopy(cipher, cipherIndex, XBuf, 0, length);
            Tag = gHash(Tag, XBuf, 0, modeBlockSize);
        }
        else
        {
            Tag = gHash(Tag, cipher, cipherIndex, modeBlockSize);
        }
    }


    private void gctr(BigInteger cbParam, byte[] in, int inOffset, byte[] out, int outOffset) {
        byte[] cbBytes = bigIntToByte(cbParam);
        cipher.encryptBlock(cbBytes, 0, encryptedBlock, 0);

        int len = modeBlockSize;
        if((in.length - inOffset) < modeBlockSize)
            len = in.length - inOffset;

        for (int i = 0; i < len; i++) {
            out[outOffset++] = (byte)(in[inOffset++] ^ encryptedBlock[i]);
        }

        cLen += len;
    }

    private byte[] bigIntToByte(BigInteger cbParam){
        byte[] cb = cbParam.toByteArray();
        byte[] cbBytes =  new byte[modeBlockSize];

        if(cb.length==16){
          return cb;
        }else if(cb.length==17 && cb[0]==(byte)0x00){
            System.arraycopy(cb,1,cbBytes,0,cbBytes.length);
        }else if(cb.length < modeBlockSize){
            byte [] leadingZero = new byte[modeBlockSize-cb.length];
            cbBytes = ByteUtil.concatAll(leadingZero,cb);
        }else
            throw new IllegalArgumentException("Mode block size is unexpected. Size: " + cb.length);

          return cbBytes;
    }

    private byte [] gHash(byte [] Y, byte [] X, int index, int len){
        byte [] temp = ByteUtil.xor(Y, 0, X, index, len);
        return blockMultiplication(temp, hSubKey);
    }

    private byte[] getH(){
        hSubKey =  new byte[modeBlockSize];
        byte [] zeros = new byte[16];
        cipher.encryptBlock(zeros, 0, hSubKey,0);

        return hSubKey;
    }

    private boolean getBit(int[] b, int pos){
        int p = pos / 32;
        pos %= 32;
        long i = (b[p] >>> (31 - pos)) & 1;

        return i != 0;
    }

    private static boolean getBit(byte[] b, int pos){
        int p = pos / 8;
        pos %= 8;
        int i = (b[p] >>> (7 - pos)) & 1;

        return i != 0;
    }


    private void shift(int[] b) {
        int temp, temp2;
        temp2 = 0;
        for (int i = 0; i < b.length; i++) {
            temp =  (b[i] << 31);
            b[i] =  (b[i] >>> 1);
            b[i] =  (b[i] | temp2);
            temp2 = temp;
        }
    }

    // Given block X and Y, returns the multiplication of X * Y
    private byte[] blockMultiplication(byte[] x, byte[] y) {

        int P128 =  0xe1000000;

        if (x.length != modeBlockSize  || y.length != modeBlockSize) {
            throw new RuntimeException("illegal input sizes");
        }

        int[] z = new int[4]; //modeblocksize(16)/sizeofInteger(4) = 4
        int[] v = new int[4];

        v[0] = ByteBuffer.wrap(y,0,4).getInt();
        v[1] = ByteBuffer.wrap(y,4,4).getInt();
        v[2] = ByteBuffer.wrap(y,8,4).getInt();
        v[3] = ByteBuffer.wrap(y,12,4).getInt();


        // calculate Z1-Z127 and V1-V127
        for (int i = 0; i < 127; i++) {
            // Zi+1 = Zi if bit i of x is 0
            if (getBit(x, i)) {
                z[0] ^= v[0];
                z[1] ^= v[1];
                z[2] ^= v[2];
                z[3] ^= v[3];
            }

            boolean lastBitOfV = getBit(v, 127);
            shift(v);
            if (lastBitOfV) v[0] ^= P128;
        }

        // calculate Z128
        if (getBit(x, 127)){
            z[0] ^= v[0];
            z[1] ^= v[1];
            z[2] ^= v[2];
            z[3] ^= v[3];
        }
        return ByteBuffer.allocate(16).putInt(z[0]).putInt(z[1]).putInt(z[2]).putInt(z[3]).array();
    }

    public byte [] doFinal(){

        int tagLen = 16;

        if(params.getTag() != null && params.getTag().length != 0)
            tagLen = params.getTag().length;

        byte [] aLenBytes = ByteUtil.numberToByteArray(aadLen*8,8);
        byte [] cLenBytes = ByteUtil.numberToByteArray(cLen*8,8);
        byte [] totalLen = ByteUtil.concatAll(aLenBytes, cLenBytes);

        byte [] T = new byte[tagLen];

        Tag = gHash(Tag, totalLen, 0, totalLen.length);
        byte [] partialTag = Arrays.copyOfRange(Tag, 0, tagLen);
        gctr(InitialCB,partialTag,0,T,0);

        return T;
    }
}
