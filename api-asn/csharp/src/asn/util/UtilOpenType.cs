using System;
using System.IO;
using System.Runtime.CompilerServices;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilOpenType
    {
        public static readonly Asn1OpenType Asn1NULL = new Asn1OpenType(new byte[] { 5, 0 });

        [MethodImpl(MethodImplOptions.Synchronized)]
        public static Asn1OpenType toOpenType<T>(BaseASNWrapper<T> asnWrapper) where T : Asn1Type
        {
            return toOpenType(asnWrapper.getObject());
        }
        [MethodImpl(MethodImplOptions.Synchronized)]
        public static Asn1OpenType toOpenType(Asn1Type aVal)
        {
            Asn1OpenType openT = null;
            Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
            aVal.Encode(enbuf);
            openT = new Asn1OpenType();

            openT.Decode(new Asn1DerDecodeBuffer(enbuf.GetInputStream()));

            return openT;
        }
        [MethodImpl(MethodImplOptions.Synchronized)]
        public static void fromOpenType(Asn1OpenType aOpenType, Asn1Type aToType)
        {
            if (aToType == null)
                throw new IOException("null");
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aOpenType.mValue);
            aToType.Decode(decBuf);
        }
    }
}
