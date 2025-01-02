using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public class PBEKey: IPBEKey
    {
        private readonly byte[] _mKey;
        private readonly char[] _mPassword;
        private readonly byte[] _mSalt;
        private readonly int _mIterationCount;

        public PBEKey(byte[] aKey, char[] aPassword, byte[] aSalt, int aIterationCount)
        {
            _mKey = aKey;
            _mPassword = aPassword;
            _mSalt = aSalt;
            _mIterationCount = aIterationCount;
        }

        public char[] getPassword()
        {
            return _mPassword;
        }

        public byte[] getSalt()
        {
            return _mSalt;
        }

        public int getIterationCount()
        {
            return _mIterationCount;
        }

        public String getAlgorithm()
        {
            return "PBE";
        }

        public String getFormat()
        {
            return "RAW";
        }

        public byte[] getEncoded()
        {
            return _mKey;
        }
    }
}
