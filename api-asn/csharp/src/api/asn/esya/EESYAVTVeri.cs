using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYAVTVeri : BaseASNWrapper<ESYAVTVeri>
    {
        public EESYAVTVeri(byte[] aBytes)
            :base(aBytes, new ESYAVTVeri()){}

        public EESYAVTVeri(long anahtarNo, byte[] sifreli)
            :base(new ESYAVTVeri(anahtarNo,sifreli)){}

        public long getAnahtarNo()
        {
            return mObject.anahNo.mValue;
        }

        public byte[] getVeri()
        {
            return mObject.veri.mValue;
        }
    }
}
