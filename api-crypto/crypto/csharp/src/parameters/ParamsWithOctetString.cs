using System;
using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class ParamsWithOctetString : IAlgorithmParams
    {
         public static Asn1OpenType Asn1OctetStringNULL = new Asn1OpenType(new byte[]{0x04,0x00});

        private readonly string _strParam;

        public ParamsWithOctetString(String param)
        {
           _strParam = param;
        }

        public byte[] getEncoded()
        {
           Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
           Asn1OctetString octetString = new Asn1OctetString(_strParam);
           octetString.Encode(buff);
           return buff.MsgCopy;
        }
    }
}
