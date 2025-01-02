using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASifreliVeri : BaseASNWrapper<ESYASifreliVeri>
    {
         public EESYASifreliVeri(byte[] sifreliAnahtar, byte[] sifreliVeri)
            :base(new ESYASifreliVeri(sifreliAnahtar, sifreliVeri)){}

        public EESYASifreliVeri(byte[] aBytes)
            :base(aBytes, new ESYASifreliVeri()){}

        public byte[] getSifreliVeri(){
            return mObject.sifreliVeri.mValue;
        }

        public byte[] getSifreliAnahtar(){
            return mObject.sifreliAnahtar.mValue;
        }
    }
}
