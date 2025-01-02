using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASimetrikAnahtar : BaseASNWrapper<ESYASimetrikAnahtar>
    {
         public EESYASimetrikAnahtar(EAlgorithmIdentifier algorithm, byte[] simetrikAnahtar) 
                :base(new ESYASimetrikAnahtar(algorithm.getObject(), simetrikAnahtar)){}

        public EESYASimetrikAnahtar(byte[] aBytes)
            :base(aBytes, new ESYASimetrikAnahtar()){}

        public byte[] getSimetrikAnahtar(){
            return mObject.simetrikAnahtar.mValue;
        }

        public EAlgorithmIdentifier getAlgorithm(){
            return new EAlgorithmIdentifier(mObject.algorithm);
        }
    }
}
