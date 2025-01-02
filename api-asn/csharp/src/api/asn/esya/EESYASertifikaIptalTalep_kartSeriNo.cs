using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASertifikaIptalTalep_kartSeriNo : BaseASNWrapper<ESYASertifikaIptalTalep_kartSeriNo>
    {
        public EESYASertifikaIptalTalep_kartSeriNo(ESYASertifikaIptalTalep_kartSeriNo aObject): 
            base(aObject) {}

        public EESYASertifikaIptalTalep_kartSeriNo(byte[] aBytes) : 
            base(aBytes, new ESYASertifikaIptalTalep_kartSeriNo()){
            
        }

        public Asn1OctetString[] getKartSeriNolar() {
            return mObject.elements;
        }

        public void setKartSeriNolar(Asn1OctetString[] kartSeriNolar) {
            mObject.elements = kartSeriNolar;
        }
    }
}
