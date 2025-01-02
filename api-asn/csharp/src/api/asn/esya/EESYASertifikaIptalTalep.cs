using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASertifikaIptalTalep : BaseASNWrapper<ESYASertifikaIptalTalep>
    {
        public EESYASertifikaIptalTalep(ESYASertifikaIptalTalep aObject) 
             : base(aObject) {}

        public EESYASertifikaIptalTalep(byte[] aBytes) 
            : base(aBytes, new ESYASertifikaIptalTalep()) {}

        public EESYASertifikaIptalTalep(    EESYASertifikaIptalTalep_kartSeriNo kartSeriNo,
                                            String kayitciAdi,
                                            EESYASertifikaIptalTalep_islemTipi islemTipi) 
            :base(new ESYASertifikaIptalTalep(
                    kartSeriNo.getObject(),
                    new Asn1UTF8String(kayitciAdi),
                    islemTipi.getObject() )){}

        public EESYASertifikaIptalTalep_kartSeriNo getSertifikaIptalTalep_kartSeriNo(){
            return new EESYASertifikaIptalTalep_kartSeriNo(mObject.kartSeriNo);
        }

        public String getKayitciAdi(){
            return mObject.kayitciAdi.mValue;
        }

        public EESYASertifikaIptalTalep_islemTipi getIslemTipi(){
            return new EESYASertifikaIptalTalep_islemTipi(mObject.islemTipi);
        }
    }
}
