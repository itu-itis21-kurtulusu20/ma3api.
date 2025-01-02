using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASertifikaIptalTalep_islemTipi : BaseASNWrapper<ESYASertifikaIptalTalep_islemTipi>
    {
        protected static EESYASertifikaIptalTalep_islemTipi _askiyaAl = new EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi.askiyaAl());
        protected static EESYASertifikaIptalTalep_islemTipi _askidanIndir = new EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi.askidanIndir());
        protected static EESYASertifikaIptalTalep_islemTipi _iptal = new EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi.iptal());

        public EESYASertifikaIptalTalep_islemTipi(ESYASertifikaIptalTalep_islemTipi aObject) 
            : base(aObject){}

        public int getValue()
        {
            return mObject.mValue;
        }
    }
}
