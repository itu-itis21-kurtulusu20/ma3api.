using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.common.asn1
{
    public class TLVUtil

    {
        public static TLVInfo parseField(byte[] asn1Data, int startIndex) 
        {
            int tag = asn1Data[startIndex++];       
            return parseLen(tag, asn1Data, startIndex);
        }

        public static TLVInfo parseField(byte[] asn1Data, int startIndex, int fieldIndex) 
        {
            int index = startIndex;
            int tag = asn1Data[index++]; 
            index = parseLen(tag, asn1Data, index).getDataStartIndex();
            for(int i = 0; i<fieldIndex; i++){
                index = skipField(asn1Data, index);
            }

            return parseField(asn1Data, index);
        }

        public static TLVInfo parseFieldInsideTag(byte[] asn1Data, int startIndex, int fieldIndex) 
        {
            int index = startIndex;
            for(int i = 0; i < fieldIndex; i++){
                index = skipField(asn1Data, index);
            }
            return parseField(asn1Data, index);
        }

        private static TLVInfo parseLen(int tag, byte[] asn1Data, int index)
        {
            int len = 0;
            int firstLenByte = asn1Data[index++]; 
            if (firstLenByte < 0x80)
            {
                len = firstLenByte;
            }
            else
            {
                int lenLen = firstLenByte & 0x7F;
                for (int i = 0; i < lenLen; i++)
                {
                    len = len * 256;
                    len = len + (asn1Data[index++]);
                }
            }

            return new TLVInfo(tag, index, len);
        }


        /*
        * TLV info tag and encoding tag may differ. So tag is also taken. give -1 for tag in order to use tag inside TLVInfo.
        * */
        public static byte[] encode(int tag, byte[] asn1Data, TLVInfo tlvInfo)
        {
            byte[] encoded = new byte[tlvInfo.getDataEndIndex() - tlvInfo.getTagStartIndex()];   
            Array.Copy(asn1Data, tlvInfo.getTagStartIndex(), encoded, 0, tlvInfo.getDataEndIndex() - tlvInfo.getTagStartIndex());

            if (tag != -1)
                encoded[0] = (byte)tag;
            return encoded;
        }

        public static byte[] getData(byte[] asn1Data, TLVInfo tlvInfo)
        {
            byte[] data = new byte[tlvInfo.getDataLen()];
            Array.Copy(asn1Data, tlvInfo.getDataStartIndex(), data, 0, data.Length);
            return data; ;
        }

        private static int skipField(byte[] asn1Data, int index)
        {
            int tag = asn1Data[index++];
            return parseLen(tag, asn1Data, index).getDataEndIndex();
        }
    }
}
