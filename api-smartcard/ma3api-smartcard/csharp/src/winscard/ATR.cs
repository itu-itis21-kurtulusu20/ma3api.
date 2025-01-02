using System;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.src.winscard
{
    public class ATR
    {
        private byte[] atr;

        private int startHistorical, nHistorical;

        /**
         * Constructs an ATR from a byte array.
         *
         * @param atr the byte array containing the answer-to-reset bytes
         * @throws NullPointerException if <code>atr</code> is null
         */
        public ATR(byte[] atr)
        {
            this.atr = (byte []) atr.Clone();
            parse();
        }

        private void parse()
        {
            if (atr.Length < 2)
            {
                return;
            }
            if ((atr[0] != 0x3b) && (atr[0] != 0x3f))
            {
                return;
            }
            int t0 = (atr[1] & 0xf0) >> 4;
            int n = atr[1] & 0xf;
            int i = 2;
            while ((t0 != 0) && (i < atr.Length))
            {
                if ((t0 & 1) != 0)
                {
                    i++;
                }
                if ((t0 & 2) != 0)
                {
                    i++;
                }
                if ((t0 & 4) != 0)
                {
                    i++;
                }
                if ((t0 & 8) != 0)
                {
                    if (i >= atr.Length)
                    {
                        return;
                    }
                    t0 = (atr[i++] & 0xf0) >> 4;
                }
                else
                {
                    t0 = 0;
                }
            }
            int k = i + n;
            if ((k == atr.Length) || (k == atr.Length - 1))
            {
                startHistorical = i;
                nHistorical = n;
            }
        }

        /**
         * Returns a copy of the bytes in this ATR.
         *
         * @return a copy of the bytes in this ATR.
         */
        public byte[] getBytes()
        {
            return (byte [])atr.Clone();
        }

        /**
         * Returns a copy of the historical bytes in this ATR.
         * If this ATR does not contain historical bytes, an array of length
         * zero is returned.
         *
         * @return a copy of the historical bytes in this ATR.
         */
        public byte[] getHistoricalBytes()
        {
            byte[] b = new byte[nHistorical];
            Array.Copy(atr,startHistorical, b, 0, nHistorical);
            return b;
        }

        /**
         * Returns a string representation of this ATR.
         *
         * @return a String representation of this ATR.
         */
        public String toString()
        {
            return "ATR: " + atr.Length + " bytes";
        }

        /**
         * Compares the specified object with this ATR for equality.
         * Returns true if the given object is also an ATR and its bytes are
         * identical to the bytes in this ATR.
         *
         * @param obj the object to be compared for equality with this ATR
         * @return true if the specified object is equal to this ATR
         */
        public bool equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj is ATR == false) {
                return false;
            }
            ATR other = (ATR)obj;
            return ArrayUtil.Equals(this.atr, other.atr);
        }
    }
}
