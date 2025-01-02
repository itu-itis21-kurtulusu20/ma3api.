using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EExtension : BaseASNWrapper<Extension>
    {
        public EExtension(byte[] aBytes) : base(aBytes, new Extension())
        {
        }

        public EExtension(Extension aObject)
            : base(aObject)
        {
        }

        //todo son parametre BaseAsnWrapper aValue iken byte yapmak zorunda kaldim 10.02.2011
        public EExtension(Asn1ObjectIdentifier aExtID, bool aCritic, byte[] aEncoded)
            : base(new Extension(aExtID.mValue, aCritic, aEncoded))
        {
            //super(new Extension(aExtID.value, aCritic, aValue.getEncoded()));
        }

        public EExtension(Asn1ObjectIdentifier aExtID, bool aCritic, Asn1Type asn1Type)
            : base(new Extension(aExtID.mValue, aCritic, null))
        {
            //super(new Extension(aExtID.value, aCritic, null));
            Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
            try
            {
                asn1Type.Encode(safeEncBuf);
            }
            catch (Exception ex)
            {
                throw new ESYAException("Extension islenirken hata: ExtID:" + aExtID, ex);
            }
            mObject.extnValue = new Asn1OctetString(safeEncBuf.MsgCopy);
        }

        public Asn1ObjectIdentifier getIdentifier()
        {
            return mObject.extnID;
        }

        public byte[] getValue()
        {
            return mObject.extnValue.mValue;
        }

        public Asn1DerDecodeBuffer getValueAsDecodeBuffer()
        {
            return new Asn1DerDecodeBuffer(mObject.extnValue.mValue);
        }

        public bool isCritical()
        {
            return mObject.critical.mValue;
        }
    }
}