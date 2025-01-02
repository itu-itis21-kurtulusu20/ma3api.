using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public class BlockNoBuffer : BlockBuffer
    {
        private readonly byte[] _emptyBytes = new byte[0];

        public BlockNoBuffer()
            : base(0) { }

        //@Override
        public override byte[] getRemaining()
        {
            return _emptyBytes;
        }

        //@Override
        public override byte[] update(byte[] aBytes)
        {
            return update(aBytes, 0, aBytes.Length);
        }

        //@Override
        public override byte[] update(byte[] aBytes, int aOffset, int aLength)
        {
            if (aBytes != null)
            {
                byte[] part = new byte[aLength];
                Array.Copy(aBytes, aOffset, part, 0, aLength);
                return part;
            }

            return _emptyBytes;
        }
    }
}
