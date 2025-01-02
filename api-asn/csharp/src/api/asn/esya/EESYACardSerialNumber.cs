using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYACardSerialNumber : BaseASNWrapper<ESYACardSerialNumber>, ExtensionType
    {
        public EESYACardSerialNumber(byte[] aBytes)
            : base(aBytes, new ESYACardSerialNumber()){}

        public EESYACardSerialNumber(String value)
            : base(new ESYACardSerialNumber(value)){}

        public EESYACardSerialNumber(ESYACardSerialNumber aObject)
            : base(aObject){}

        public EExtension toExtension(bool aCritic) {           
             return new EExtension(EESYAOID.oid_ESYA_CardSerialNumber, aCritic, getBytes());
        }
    }
}
