using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.common.asn1
{
    public class TLVInfo
    {

        private int tag;
        private int dataStartIndex;
        private int dataLen;

        public TLVInfo(int tag, int dataStartIndex, int dataLen)
        {
            this.tag = tag;
            this.dataStartIndex = dataStartIndex;
            this.dataLen = dataLen;
        }

        public int getTag()
        {
            return tag;
        }

        public int getDataStartIndex()
        {
            return dataStartIndex;
        }

        public int getDataLen()
        {
            return dataLen;
        }

        public int getDataEndIndex()
        {
            return dataStartIndex + dataLen;
        }

        public int getTagStartIndex()
        {
            int lenLen = getLenLen();
            return getDataStartIndex() - lenLen - 1;
        }

        public int getLenFromTag()
        {
            return getDataLen() + getLenLen() + 1;
        }

        private int getLenLen()
        {
            if (dataLen < 0x80)
                return 1;
            else if (dataLen > 0x7F && dataLen <= 0xFF)
                return 2;
            else if (dataLen > 0xFF && dataLen <= 0xFFFF)
                return 3;
            else if (dataLen > 0xFFFF && dataLen <= 0xFFFFFF)
                return 4;
            else if (dataLen > 0xFFFFFF && dataLen <= 0xFFFFFFFF)
                return 5;
            else
                throw new ESYARuntimeException("Too large asn1 len for this class!");
        }
    }
}
    