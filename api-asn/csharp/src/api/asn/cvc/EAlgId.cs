using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class EAlgId : BaseASNWrapper<AlgId>
    {
        public EAlgId(byte[] aEncoded)
            : base(aEncoded, new AlgId())
        {
            //super(aEncoded, new AlgId());
        }

        public EAlgId()
            : base(new AlgId())
        {
            //super(new AlgId());
        }

        public void setAlgId(byte[] aAldIdValue)
        {
            getObject().mValue = aAldIdValue;
        }

        public byte[] getByteValues()
        {
            return getObject().mValue;
        }

        public int getLength()
        {
            return getByteValues().Length;
        }

        public static EAlgId fromValue(byte[] aAlgIdValue)
        {
            EAlgId algId = new EAlgId();
            algId.setAlgId(aAlgIdValue);
            return algId;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static byte[] getTagLen(int aLen)
        {
            Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
            encodeBuffer.EncodeTagAndLength(Asn1Tag.UNIV, Asn1Tag.PRIM, 6, aLen);
            return encodeBuffer.MsgCopy;
        }

        public int[] toIntArray()
        {
            Asn1ObjectIdentifier objectIdentifier = new Asn1ObjectIdentifier();
            Asn1BerDecodeBuffer decodeBuffer = new Asn1BerDecodeBuffer(getBytes());
            try
            {
                objectIdentifier.Decode(decodeBuffer);
            }
            catch (Exception e)
            {
                //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                throw new ESYAException("Decode Error!", e);
            }
            return objectIdentifier.mValue;
        }
    }
}
