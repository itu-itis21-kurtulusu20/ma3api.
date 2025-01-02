using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    //todo Java'daki LinkedHashMap benzeri bir veri yapısı kullanmak gerekiyor mu??
    public class EHeaderList : BaseASNWrapper<HeaderList>
    {
        public EHeaderList(HeaderList aObject)
            : base(aObject)
        {
        }
       
        public EHeaderList(byte[] aEncoded)
            : base(aEncoded, new HeaderList())
        {
        }

        public EHeaderList()
            : base(new HeaderList())
        {
        }


        public void setHeaderList(byte[] aHeaderListValue)
        {
            getObject().mValue = aHeaderListValue;
        }

        public static EHeaderList fromValue(byte[] aHeaderListValue)
        {
            EHeaderList headerList = new EHeaderList();
            headerList.setHeaderList(aHeaderListValue);
            return headerList;
        }

        public int getMessageLen()
        {
            int totalMessageSize = 0;
            Dictionary<Asn1Tag, int> headerListInfo = getListInfo();
            ICollection<int> values = headerListInfo.Values;
            foreach (int len in values)
            {
                totalMessageSize += len;
            }
            return totalMessageSize;
        }


        public Dictionary<Asn1Tag, int> getListInfo()
        {
            Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(getObject().mValue);
            Asn1Tag tag = new Asn1Tag();
            IntHolder parsedTypeLen = new IntHolder();
            //
            Dictionary<Asn1Tag, int> headerListInfo = new Dictionary<Asn1Tag, int>();

            while (decodeBuffer.ByteCount < getObject().mValue.Length)
            {
                Asn1Tag parsedTag = new Asn1Tag();
                if (!decodeBuffer.MatchTag(tag, parsedTag, parsedTypeLen))
                {
                    if (decodeBuffer.MatchTag(parsedTag, null, parsedTypeLen))
                    {
                        if (parsedTag.Equals(RsaPuK._TAG))
                            continue;
                        //Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
                        //encodeBuffer.encodeTag(parsedTag);
                        headerListInfo[parsedTag] = parsedTypeLen.mValue;
                    }
                }
            }
            return headerListInfo;
        }

        private int getAsn1TagLen(Asn1Tag aAsn1Tag)
        {
            String a = aAsn1Tag.ToString();
            int headerListSize = getObject().mValue.Length;
            Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(getObject().mValue);
            IntHolder typeLen = new IntHolder();

            while (decodeBuffer.ByteCount < headerListSize)
            {

                if (!decodeBuffer.MatchTag(aAsn1Tag, null, typeLen))
                {
                    decodeBuffer.ReadByte();
                }
                else
                {
                    return typeLen.mValue;
                }

                //            catch (Asn1Exception e) {
                //                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                //            } catch (IOException e) {
                //                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                //            }

            }
            return 0;
        }


    }
}
